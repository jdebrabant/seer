
package edu.brown.cache;

import java.util.*; 
import java.io.*; 

import edu.brown.common.*; 

public class RandomCache extends Cache
{
	Random rand; 
	
	public RandomCache(int cache_size)
	{
		super(cache_size);
		
		rand = new Random(); 
	}
	
	public boolean addToCache(CacheBlock block)
	{
		int location_in_cache; 
		String block_evicted; 
		
		location_in_cache = containsBlock(block.block_id);
		
		boolean cache_hit = false; 
		
		//System.out.println("adding cache block " + block.block_id + " to cache"); 
		
		if(location_in_cache < 0) // block is not in cache, add it
		{
			cache_hit = false; 
			block.timestamp = current_time; 
			if(blocks.size() >= MAX_CACHE_SIZE)
			{
				block_evicted = evictCacheBlock(); 
				
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
			//System.out.println("...already in cache"); 
			cache_hit = true; 
			blocks.get(location_in_cache).timestamp = current_time; 
		}
		
		current_time++; 
		
		return cache_hit; 
	}
	
	// implements random eviction strategy
	public String evictCacheBlock()
	{
		int location_of_evicted_block = rand.nextInt(blocks.size()); 
		
		String id = blocks.get(location_of_evicted_block).block_id; 
		
		blocks.remove(location_of_evicted_block); 
		
		return id;
	}
}
