
package edu.brown.test; 

import java.util.*; 
import java.lang.*; 
import java.io.*; 
import java.sql.*; 

import edu.brown.common.*; 
import edu.brown.cache.*; 

public class RuntimeTester extends Tester
{
	private PredictiveCache pred_cache;
	private LRUCache lru_cache; 
	
	private static int MAX_PREDICT_DEPTH = 1; 
	private static int MAX_BLOCKS_TO_PREFETCH = 4; 
	private static int CACHE_SIZE = 50;
	private static int PROCESSING_TIME_IN_MS = 10000; 
	
	private static final double MIN_CONFIDENCE_THRESHOLD = .6; 
	
	protected Connection conn;
	protected Statement stmt;
	
	private boolean SIMULATE = true; 
	private boolean TESTING = false; 
	
	public LinkedList<String> unique_cache_blocks; 
	
	public static void main(String [] args)
	{
		if(args.length != 4)
		{
			System.out.println("Usage: java RuntimeTester <template training file> <parameter training file> <query logs> <mapping file>"); 
			System.exit(1); 
		}
		
		for(int i = 1; i <= 300; i+=1)
		//for(int i = 50; i <= 50; i++)
		{
			CACHE_SIZE = i; 
			
			RuntimeTester tester = new RuntimeTester(); 
			
			tester.trainModels(args[0], args[1], 10); 
			tester.readQueryLogs(args[2]);
			//tester.readPartitionMapping2D(args[3]); 
			
			tester.runLRU(false); 
			tester.runLRU(true); 

			tester.runPredictive(false); 
			tester.runPredictive(true); 
			
			System.out.println("unique cache blocks: " + tester.unique_cache_blocks.size()); 
			System.out.println("cache size as a percent of query workload size: " + (CACHE_SIZE / (double)tester.unique_cache_blocks.size()));
			System.out.println("\n"); 
		}
	}
	
	public void runLRU(boolean prefetch)
	{
		long query_start_time, query_end_time; 
		long total_runtime = 0; 
		
		String current_template; 	
		LinkedList<Integer> current_parameters;
		
		int total_queries = 0; 
		int cache_hits = 0; 
		int cache_misses = 0; 
		
		String template_history = "";
		
		try 
		{
			// warm up cache
			for(int current_query = 0; current_query < template_sequence.size(); current_query++)
			{
				current_template = template_sequence.get(current_query).toString(); 
				current_parameters = parameter_sequence.get(current_query); 
				
				// add paritions in current query to cache block
				for(int i = 0; i < current_parameters.size(); i++)
				{
					String block_id = new String(current_template + "-" + current_parameters.get(i).intValue()); 
					CacheBlock block = new CacheBlock(block_id); 
					
					// add each partition to the query cache
					lru_cache.addToCache(block); 
				}
			}
			
			for(int i = 0; i < template_sequence.size(); i++)
			//for(int i = 0; i < 100; i++)
			{
				query_start_time = System.currentTimeMillis();  // start query timer
				
				template_history += template_sequence.get(i).toString(); 
				current_template = template_sequence.get(i).toString(); 
				current_parameters = parameter_sequence.get(i); 
				
				// execute current query
				for(int j = 0; j < current_parameters.size(); j++)
				{
					String block_id = new String(current_template + "-" + current_parameters.get(j).intValue()); 
					CacheBlock block = new CacheBlock(block_id); 
					
					if(lru_cache.containsBlock(block_id) < 0) // block is not in cache, so execute query and add block to cache
					{
						cache_misses++; 
						
						Partition p = partition_mapping.get(current_parameters.get(j).intValue()); 
						
						if(!SIMULATE)
							executeQuery(p.convertToSQL()); 
												
						lru_cache.addToCache(block);
					}
					else // block is already in cache, so just update timestamp
					{
						lru_cache.addToCache(block); 
						cache_hits++; 
					}
					
					if(!unique_cache_blocks.contains(block_id))
						unique_cache_blocks.add(block_id); 
				}
				
				query_end_time = System.currentTimeMillis(); // stop query timer
				
				total_runtime += (query_end_time - query_start_time);
				
				// instead of sleeping, use time to prefetch data
				if(prefetch)
				{
					prefetchData(i, template_history, false);
				}
				else 
				{
					if(!SIMULATE)
						Thread.sleep(PROCESSING_TIME_IN_MS);
				}
			}
			
			if(prefetch)
			{
				System.out.println("LRU: executed " + cache_misses + " queries");
			}
			else 
			{
				System.out.println("LRU (w/o prefetch): executed " + cache_misses + " queries");
			}
		 	
			System.out.println("\tcache hit rate: " + (cache_hits/(double)(cache_hits+cache_misses))); 
			//System.out.println("naive runtime: " + total_runtime); 
			
		}
		catch(Exception e)
		{
			System.out.println(e.getMessage()); 
		}
	}
		
	public void runPredictive(boolean prefetch)
	{
		String template_history = "";
				
		String current_template; 	
		LinkedList<Integer> current_parameters; 
		
		LinkedList<String> predicted_block_ids; 
		
		long query_start_time, query_end_time; 
		long total_runtime = 0; 
		
		int cache_hits = 0; 
		int cache_misses = 0; 
		int total_queries = 0; 
		
		boolean warmup = true; 
		
		TemplatePrediction tp; 

		try 
		{
			// warm up cache
			for(int i = 0; i < template_sequence.size(); i++)
			{
				current_template = template_sequence.get(i).toString(); 
				current_parameters = parameter_sequence.get(i); 
				
				predicted_block_ids = pred_cache.predictBlocksRecursively(0, MAX_PREDICT_DEPTH, predictor, template_sequence.get(i), 
																	 template_history, current_parameters); 
				// execute current query 
				for(int j = 0; j < parameter_sequence.get(i).size(); j++)
				{
					String block_id = new String(current_template + "-" + current_parameters.get(j).intValue()); 
					CacheBlock block = new CacheBlock(block_id); 
					
					if(pred_cache.containsBlock(block_id) < 0) // block is not in cache, so execute query and add block to cache
					{
						Partition p = partition_mapping.get(current_parameters.get(j)); 
						
						pred_cache.addToCache(block, predicted_block_ids);
					}
				}
							
				template_history += template_sequence.get(i); 
			}
			
			for(int i = 0; i < template_sequence.size(); i++)
			{
				template_history += template_sequence.get(i).toString(); 
				current_template = template_sequence.get(i).toString(); 
				current_parameters = parameter_sequence.get(i); 
				
				predicted_block_ids = pred_cache.predictBlocksRecursively(0, MAX_PREDICT_DEPTH, predictor, template_sequence.get(i), 
																		  template_history, current_parameters); 
				
				query_start_time = System.currentTimeMillis(); // start query timer
				
				// execute current query 
				
				//System.out.print("executing: "); 
				for(int j = 0; j < parameter_sequence.get(i).size(); j++)
				{
					String block_id = new String(current_template + "-" + current_parameters.get(j).intValue()); 
					CacheBlock block = new CacheBlock(block_id); 
					
					//System.out.print(block_id + " "); 

					if(pred_cache.containsBlock(block_id) < 0) // block is not in cache, so execute query and add block to cache
					{
						cache_misses++; 
						
						Partition p = partition_mapping.get(current_parameters.get(j)); 
						
						if(!SIMULATE)
							executeQuery(p.convertToSQL()); 
						
						pred_cache.addToCache(block, predicted_block_ids);
					}
					else 
					{
						cache_hits++; 
						pred_cache.addToCache(block, predicted_block_ids);
					}
				}
				
				query_end_time = System.currentTimeMillis(); // stop query timer
				
				total_runtime += (query_end_time - query_start_time); 
							
				// instead of sleeping, use time to prefetch data
				if(prefetch)
				{
					prefetchData(i, template_history, true);
					
					if(TESTING)
					{
						// FOR TESTING
						System.out.print("\nnext query: "); 
						if(i != template_sequence.size())
						{
							for(int k = 0; k < parameter_sequence.get(i+1).size(); k++)
							{
								System.out.print(template_sequence.get(i+1) + "-" + parameter_sequence.get(i+1).get(k) + " "); 
							}
						}
						System.out.println("\n"); 
					}
					
				}
				else 
				{
					if(!SIMULATE)
						Thread.sleep(PROCESSING_TIME_IN_MS);
				}
			}
			
			if(prefetch)
			{
				System.out.println("SeerDB: executed " + cache_misses + " queries"); 
			}
			else 
			{
				System.out.println("SeerDB (w/o prefetch): executed " + cache_misses + " queries"); 
			}
			
			System.out.println("\tcache hit rate: " + (cache_hits/(double)(cache_hits+cache_misses))); 
			//System.out.println("optimized runtime: " + total_runtime); 
			
		}
		catch(Exception e)
		{
			e.getMessage(); 
		}
	}
	
	public void prefetchData(int current_query, String template_history, boolean predictive)
	{
		String current_template; 	
		LinkedList<Integer> current_parameters; 
		LinkedList<Integer> parameter_prediction; 
		
		LinkedList<String> predicted_block_ids; 
		
		LinkedList<String> blocks_to_prefetch; 
		
		long prefetch_start_time; 
		
		Integer partition_id; 
		
		prefetch_start_time = System.currentTimeMillis();	
	
		// determine current query template and parameter
		current_template = template_sequence.get(current_query).toString(); 
		current_parameters = parameter_sequence.get(current_query); 
		
		predicted_block_ids = new LinkedList<String>(); 
		
		template_history += current_template; 
		
		Cache cache; 
		
		predicted_block_ids = pred_cache.predictBlocksRecursively(0, MAX_PREDICT_DEPTH, predictor, template_sequence.get(current_query), 
																  template_history, current_parameters); 
		
		blocks_to_prefetch = pred_cache.predictBlocksRecursively(0, 1, predictor, template_sequence.get(current_query), 
															template_history, current_parameters); 
		
		/*
		System.out.println(predicted_block_ids); 
		
		if(current_query > 0)
			return; 
		 */
		
		if(predictive)
			cache = pred_cache; 
		else
			cache = lru_cache; 
		
		//System.out.println(predicted_block_ids.size() + " blocks were predicted" ); 
		
		if(TESTING)
			System.out.print("prefetching blocks: "); 
		
		for(int i = 0; i < blocks_to_prefetch.size(); i++)
		{
			String block_id = new String(blocks_to_prefetch.get(i)); 
			CacheBlock block = new CacheBlock(block_id); 
			
			if(cache.containsBlock(block_id) < 0) // block is not in cache, so execute query and add block to cache
			{
				StringTokenizer tokenizer = new StringTokenizer(blocks_to_prefetch.get(i), "-"); 
				tokenizer.nextToken(); 
				
				String token = tokenizer.nextToken(); 
				
				partition_id = new Integer(Integer.parseInt(token)); 
				
				Partition p = partition_mapping.get(partition_id); 
				
				if(!SIMULATE)
					executeQuery(p.convertToSQL()); 
				
				if(predictive)
				{
					pred_cache.addToCache(block, predicted_block_ids);
				}
				else 
				{
					lru_cache.addToCache(block); 
				}
			}
			else // block is already in cache, so just update timestamp
			{
				if(predictive)
				{
					pred_cache.addToCache(block, predicted_block_ids);
				}
				else 
				{
					lru_cache.addToCache(block); 
				}
			}
			
			if(TESTING)
				System.out.print(block_id + " "); 
			
			if(SIMULATE)
			{
				if(i >= MAX_BLOCKS_TO_PREFETCH)
					break;
			}
			
			// processing time expired, stop prefetching
			if((System.currentTimeMillis() - prefetch_start_time) > PROCESSING_TIME_IN_MS)
				break; 
		}
	}
	
	public void executeQuery(String query)
	{
		ResultSet result; 

		try 
		{
			//System.out.println(query); 
			
			if(!SIMULATE)
			{
				stmt = conn.createStatement(); 
				result = stmt.executeQuery(query); 
				result.close(); 
			}
		}
		catch(Exception e)
		{
			System.out.println(e.getMessage()); 
		}
	}

	public RuntimeTester()
	{
		pred_cache = new PredictiveCache(CACHE_SIZE); 
		lru_cache = new LRUCache(CACHE_SIZE); 
		
		unique_cache_blocks = new LinkedList<String>(); 
		
		// connection strings
		String db_url, user, password;  
		
		db_url = "jdbc:postgresql://";
		db_url += "localhost";
		db_url += "/";
		db_url += "nyse_uniform"; 
		
		user = "justin";
		password = "";
		
		try
		{
			if(!SIMULATE)
			{
				conn = DriverManager.getConnection(db_url, user, password);
				stmt = conn.createStatement();
			}
		}
		catch(Exception e)
		{
			System.out.println(e.getMessage()); 
		}
		
	}
}






