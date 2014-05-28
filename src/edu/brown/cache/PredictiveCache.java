
package edu.brown.cache; 

import java.util.*; 
import java.io.*; 

import edu.brown.common.*; 

public class PredictiveCache extends Cache
{
	
	public PredictiveCache(int cache_size)
	{
		super(cache_size); 
	}
	
	public LinkedList<String> predictBlocksRecursively(int prediction_type, int depth, Predictor predictor, int current_template, 
													   String template_history, LinkedList<Integer> current_parameters)
	{
		LinkedList<String> predicted_blocks = new LinkedList<String>(); 
		
		LinkedList<Integer> parameter_prediction; 
		int template_prediction; 
		
		TemplatePrediction tp; 
		
		// make copy of template history 
		String templates = new String(template_history); 
		LinkedList<Integer> parameters =  new LinkedList<Integer>(current_parameters); 
		
		for(int i = 0; i < depth; i++)
		{
			// predict next template and parameters
			//template_prediction = predictor.predictTemplate(templates);
			//parameter_prediction = predictor.predictParameters(current_template, template_prediction, parameters); 
					
			tp = null; 
			parameter_prediction = null; 
			if(prediction_type == 0) // hierarchical
			{
				tp = predictor.predictTemplateWithConfidence(templates); 
				parameter_prediction = predictor.predictParameters(current_template, tp.template_id, parameters); 
			}
			else if(prediction_type == 1) // flat
			{
				AssociationRule rule_prediction = predictor.predictFlat(current_template, parameters); 
				
				tp =  new TemplatePrediction(rule_prediction.rhs_template_id, 0);  
				parameter_prediction = rule_prediction.rhs; 				
			}
			else if(prediction_type == 2)
			{
			}
			else if(prediction_type == 3)
			{
			}
						
			if(depth == 1 && tp.confidence < .4)
				break; 
			
			// convert the template and parameter predictions into block ids
			template_prediction = tp.template_id; 
			for(int j = 0; j < parameter_prediction.size(); j++)
			{
				predicted_blocks.add(new String(template_prediction + "-" + parameter_prediction.get(j))); 
			}
			
			// update the predictions for the next round of prediction
			current_template = template_prediction; 
			templates += template_prediction;
			parameters = parameter_prediction; 
			
		}
		
		return predicted_blocks; 
	}
	
	public boolean addToCache(CacheBlock block, LinkedList<String> predicted_blocks)
	{
		int location_in_cache; 
		String block_evicted; 
		
		location_in_cache = containsBlock(block.block_id);
		
		boolean cache_hit = false; 
		
		if(location_in_cache < 0) // block is not in cache, add it
		{
			cache_hit = false; 
			block.timestamp = current_time; 
			if(blocks.size() >= MAX_CACHE_SIZE)
			{
				block_evicted = evictCacheBlock(predicted_blocks); 
				
				if(block_evicted != null)  // update timestamp and add block to cache
				{
					blocks.add(block); 
				}
				else
				{
					System.out.println("ERROR: no block found for eviction"); 
					System.exit(1); 
				}
			}
			else  // there is space, so just add block to cache
			{
				blocks.add(block); 
			}			
		}
		else  // block is in cache at location_in_cache, so just update timestamp
		{
			cache_hit = true; 
			blocks.get(location_in_cache).timestamp = current_time; 
		}
		
		current_time++; 
		
		return cache_hit; 
	}
	
	// returns the id of the block chosen for replacement
	public String evictCacheBlock(LinkedList<String> predicted_block_ids)
	{
		int min_time = Integer.MAX_VALUE;
		int min_location = -1; 
		
		int min_time_predicted = Integer.MAX_VALUE; 
		int min_location_predicted = -1; 
		
		String id_of_evicted_block = ""; 
		int location_of_evicted_block = -1; 
		
		for(int i = 0; i < blocks.size(); i++)
		{
			// we only consider a block for replacement if it is not predicted
			if(!predicted_block_ids.contains(blocks.get(i).block_id))
			{
				//System.out.println("block " + blocks.get(i).block_id + " was not predicted"); 
				if(blocks.get(i).timestamp < min_time)
				{
					//System.out.println("block " + blocks.get(i).block_id + " timestamp " + blocks.get(i).timestamp); 
					min_time = blocks.get(i).timestamp; 
					min_location = i; 
				}
			}
			else
			{
				//System.out.println("PREDICTED"); 
			}
		}
		
		if(min_location != -1)  // we have a block to remove
		{
			id_of_evicted_block = blocks.get(min_location).block_id; 
			blocks.remove(min_location); 
		}
		else // all the blocks in the cache were predicted, so evict a predicted block
		{
			//System.out.println("all blocks in cache were predicted...removing a predicted block"); 
			//System.out.println("num predicted blocks: " + predicted_block_ids.size()); 			
			
			for(int i = predicted_block_ids.size() - 1; i >= 0; i--)
			{
				id_of_evicted_block = predicted_block_ids.get(i);
				location_of_evicted_block = containsBlock(id_of_evicted_block); 
				if(location_of_evicted_block >= 0)
					break; 
			}
			
			if(location_of_evicted_block >= 0)
			{
				blocks.remove(location_of_evicted_block);
			}
			else
			{
				System.out.println("ERROR: predicted block is no longer in the cache"); 
				System.exit(1); 
			}
		}
		
		return id_of_evicted_block;
	}
}




