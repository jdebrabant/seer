
package edu.brown.cache; 

import java.util.*; 
import java.io.*; 

import edu.brown.common.*; 


public class LRUCache extends Cache
{

	public LRUCache(int cache_size)
	{
		super(cache_size); 
	}
	
	public boolean addToCache(CacheBlock block)
	{
		int location_in_cache; 
		String block_evicted; 
		
		location_in_cache = containsBlock(block.block_id);
		
		boolean cache_hit = false; 
		
		
		//System.out.println("adding cache block " + block.block_id + " to cache"); 
		//System.out.println("cache size: " + blocks.size()); 
		
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
	
	// implements LRU eviction strategy
	public String evictCacheBlock()
	{
		int min_time = Integer.MAX_VALUE;
		int min_location = -1; 
		
		String id_of_evicted_block = null; 
		
		for(int i = 0; i < blocks.size(); i++)
		{
			if(blocks.get(i).timestamp < min_time)
			{
				min_time = blocks.get(i).timestamp; 
				min_location = i; 
			}
		}
		
		id_of_evicted_block = blocks.get(min_location).block_id; 
		
		blocks.remove(min_location); 
		
		return id_of_evicted_block;
	}
}
