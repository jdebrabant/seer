
package edu.brown.test; 

import java.util.*; 
import java.io.*; 

import edu.brown.common.*; 
import edu.brown.cache.*; 


public class CacheTester extends Tester
{
	private PredictiveCache pred_cache; 
	private LRUCache lru_cache; 
	private RandomCache rand_cache; 
	
	private static int CACHE_SIZE = 0;
	private static int MAX_PREDICT_DEPTH = 3; 
	
	private static int NUM_BLOCKS_TO_PREFETCH = 0; 
	
	public LinkedList<String> unique_cache_blocks; 
	
	public static void main(String [] args)
	{
		double hierarchical_cache_hit = 0; 
		double aggressive_cache_hit = 0; 
		double flat_cache_hit = 0; 
		double lru_cache_hit = 0; 
		double random_cache_hit = 0; 
		
		double average_predictive = 0; 
		double average_lru = 0; 
		int num_experiments = 0; 
		
		int delta; 
		
		if(args.length != 3)
		{
			System.out.println("Usage: java CacheTester <template training file> <parameter training file> <query logs>"); 
			System.exit(1); 
		}
		
		
		for(int i = 1; i <= 135; i += 1)
		{
			CACHE_SIZE = i; 
		
			CacheTester tester = new CacheTester(); 		
			
			tester.trainModels(args[0], args[1], 10); 
			tester.readQueryLogs(args[2]);
			
			MAX_PREDICT_DEPTH = 0; 
			hierarchical_cache_hit = tester.computePredictiveCacheHitRate(0);
			MAX_PREDICT_DEPTH = 5; 
			aggressive_cache_hit =  tester.computePredictiveCacheHitRate(0);
			//flat_cache_hit = tester.computePredictiveCacheHitRate(1); 
			lru_cache_hit = tester.computeLRUCacheHitRate(); 
			//random_cache_hit = tester.computeRandomCacheHitRate(); 
			
			/*
			System.out.println("max cache size: " + i);
			
			System.out.println("predictive hit rate: " + predictive_cache_hit);
			//System.out.println("predictive cache size: " + tester.pred_cache.getCacheSize()); 
			//System.out.print("predictive cache contents: "); 
			//tester.pred_cache.printCache();
			
			
			System.out.println("lru hit rate: " + lru_cache_hit); 
			//System.out.print("lru cache contents: "); 
			//tester.lru_cache.printCache();
			
			//System.out.println("random hit rate: " + random_cache_hit); 
			//System.out.println(); 
			
			System.out.println("cache size as a percent of query workload size: " + (CACHE_SIZE / (double)tester.unique_cache_blocks.size()));
			System.out.println("\n"); 
			
			//System.out.println("encountered " + tester.unique_cache_blocks.size() + " unique cache blocks"); 
			*/
			
			System.out.println("cache size: " + CACHE_SIZE / (double)tester.unique_cache_blocks.size()); 
			System.out.println("hierarcical cache hit rate: " + hierarchical_cache_hit); 
			System.out.println("aggressive cache hit rate: " + aggressive_cache_hit);
			System.out.println("lru cache hit rate: " + lru_cache_hit); 
			//System.out.println("flat cache hit rate: " + flat_cache_hit + "\n\n"); 			
			/*
			average_predictive += predictive_cache_hit; 
			average_lru += lru_cache_hit; 
			num_experiments++; 
			*/
		}
		
		
		//System.out.println("average hit rate of predictive cache: " + (average_predictive/num_experiments)); 
		//System.out.println("average hit rate of lru cache: " + (average_lru/num_experiments)); 
	}	

	public CacheTester()
	{
		super(); 
		
		pred_cache = new PredictiveCache(CACHE_SIZE); 
		lru_cache = new LRUCache(CACHE_SIZE); 
		rand_cache = new RandomCache(CACHE_SIZE); 
		
		unique_cache_blocks = new LinkedList<String>(); 
		
	}
	
	public double computeRandomCacheHitRate()
	{
		int current; 
		
		LinkedList<Integer> current_parameters; 
		LinkedList<Integer> parameter_prediction; 
		
		String current_template; 
		String template_history = ""; 
		
		int num_cache_hits = 0; 
		int num_cache_misses = 0; 
		
		String block_id; 
		
		for(int current_query = 0; current_query < template_sequence.size(); current_query++)
		{
			current_template = template_sequence.get(current_query).toString(); 
			current_parameters = parameter_sequence.get(current_query); 
			
			// add paritions in current query to cache block
			for(int i = 0; i < current_parameters.size(); i++)
			{
				block_id = new String(current_template + "-" + current_parameters.get(i).intValue()); 
				CacheBlock block = new CacheBlock(block_id); 
				
				// add each partition to the query cache
				if(rand_cache.addToCache(block))
					num_cache_hits++; 
				else
					num_cache_misses++; 
			}
		}
		
		return (num_cache_hits/(double)(num_cache_misses+num_cache_hits));  
	}
	
	public double computeLRUCacheHitRate()
	{
		int current; 
				
		LinkedList<Integer> current_parameters; 
		LinkedList<Integer> parameter_prediction; 
		
		String current_template; 
		String template_history = ""; 
		
		int num_cache_hits = 0; 
		int num_cache_misses = 0; 
		
		LinkedList<String> predicted_block_ids; 
		
		String block_id; 
		
		// warm up cache
		for(int current_query = 0; current_query < template_sequence.size(); current_query++)
		{
			current_template = template_sequence.get(current_query).toString(); 
			current_parameters = parameter_sequence.get(current_query); 
						
			// add paritions in current query to cache block
			for(int i = 0; i < current_parameters.size(); i++)
			{
				block_id = new String(current_template + "-" + current_parameters.get(i).intValue()); 
				CacheBlock block = new CacheBlock(block_id); 
				
				// add each partition to the query cache
				lru_cache.addToCache(block); 
			}
		}	
		
		for(int current_query = 0; current_query < template_sequence.size(); current_query++)
		{
			current_template = template_sequence.get(current_query).toString(); 
			current_parameters = parameter_sequence.get(current_query); 
			
			// add paritions in current query to cache block
			for(int i = 0; i < current_parameters.size(); i++)
			{
				block_id = new String(current_template + "-" + current_parameters.get(i).intValue()); 
				CacheBlock block = new CacheBlock(block_id); 
				
				// add each partition to the query cache
				if(lru_cache.addToCache(block))
					num_cache_hits++; 
				else
					num_cache_misses++; 
			}
			
			predicted_block_ids = pred_cache.predictBlocksRecursively(0, 1, predictor, template_sequence.get(current_query), 
																      template_history, current_parameters);
			
			// prefetch blocks
			for(int i = 0; i < NUM_BLOCKS_TO_PREFETCH; i++)
			{
				
				if(i >= predicted_block_ids.size())
					break; 
							
				CacheBlock block = new CacheBlock(predicted_block_ids.get(i)); 
				
				lru_cache.addToCache(block); 
			}
			
		}	
		
		return (num_cache_hits/(double)(num_cache_misses+num_cache_hits));  
	}
	
	public double computePredictiveCacheHitRate(int prediction_type)
	{
		int current; 
		
		int template_prediction; 
		
		LinkedList<Integer> current_parameters; 
		LinkedList<Integer> parameter_prediction; 
		
		
		String current_template; 
		String template_history = ""; 
		
		int num_cache_hits = 0; 
		int num_cache_misses = 0; 
		
		String block_id; 
		
		LinkedList<String> predicted_block_ids; 
		
		double prediction_cost_avg = 0; 
		long start_time, stop_time, total_time = 0;  
		
		// warm up cache
		for(int current_query = 0; current_query < template_sequence.size() - 1; current_query++)
		{
			current_template = template_sequence.get(current_query).toString(); 
			current_parameters = parameter_sequence.get(current_query); 
			
			predicted_block_ids = new LinkedList<String>(); 
			
			template_history += current_template; 
			
			predicted_block_ids = pred_cache.predictBlocksRecursively(prediction_type, MAX_PREDICT_DEPTH, predictor, template_sequence.get(current_query), 
																      template_history, current_parameters); 
			
			// add paritions in current query to cache block
			for(int i = 0; i < current_parameters.size(); i++)
			{
				block_id = new String(current_template + "-" + current_parameters.get(i).intValue()); 
				CacheBlock block = new CacheBlock(block_id);  
				
				if(!unique_cache_blocks.contains(block_id))
					unique_cache_blocks.add(block_id); 
				
				pred_cache.addToCache(block, predicted_block_ids); 
			}
		}
		
		for(int current_query = 0; current_query < template_sequence.size() - 1; current_query++)
		{
			current_template = template_sequence.get(current_query).toString(); 
			current_parameters = parameter_sequence.get(current_query); 
			
			predicted_block_ids = new LinkedList<String>(); 
			
			template_history += current_template; 
			
			
			start_time = System.currentTimeMillis(); 
			predicted_block_ids = pred_cache.predictBlocksRecursively(prediction_type, 1, predictor, template_sequence.get(current_query), 
																	  template_history, current_parameters); 
			stop_time = System.currentTimeMillis(); 
			
			total_time += (stop_time - start_time); 
																	  					
			// add paritions in current query to cache block
			for(int i = 0; i < current_parameters.size(); i++)
			{
				block_id = new String(current_template + "-" + current_parameters.get(i).intValue()); 
				CacheBlock block = new CacheBlock(block_id);  
								
				// add each partition to the query cache
				if(pred_cache.addToCache(block, predicted_block_ids))
					num_cache_hits++; 
				else
					num_cache_misses++; 
			}
			
			// prefetch blocks
			for(int i = 0; i < NUM_BLOCKS_TO_PREFETCH; i++)
			{
				
				if(i >= predicted_block_ids.size())
					break; 
				
				//System.out.println("predicted block id: " + predicted_block_ids.get(i));  
			
				CacheBlock block = new CacheBlock(predicted_block_ids.get(i)); 
				//System.out.println("adding block " + predicted_block_ids.get(i) + " to cache."); 
				pred_cache.addToCache(block, predicted_block_ids); 
			}
		}
		
		if(prediction_type == 0)
		{
			System.out.println("average hierarchical prediction time (milliseconds): " + (total_time / (double)(template_sequence.size()-1)) );
		}
		else if(prediction_type == 1)
		{
			System.out.println("average flat prediction time (milliseconds): " + (total_time / (double)(template_sequence.size()-1)));
		} 	
		
		return (num_cache_hits/(double)(num_cache_misses+num_cache_hits)); 
	}
}














