
package edu.brown.cache; 

import java.util.*; 
import java.io.*; 

import edu.brown.common.*; 

public class Cache 
{
	public int MAX_CACHE_SIZE; 
	
	public LinkedList<CacheBlock> blocks; 
	
	public int current_time;
	
	
	public Cache(int cache_size)
	{
		MAX_CACHE_SIZE = cache_size;
		
		blocks = new LinkedList<CacheBlock>(); 
		current_time = 0; 
	}
	
	
	public int containsBlock(String id)
	{
		for(int i = 0; i < blocks.size(); i++)
		{
			//System.out.println(blocks.get(i).block_id + " " + id); 
			if(blocks.get(i).block_id.equals(id))
				return i; 
		}
		
		return -1; 
	}
	
	public boolean updateTimestamp(String id, int new_time)
	{
		int location; 
		
		location = containsBlock(id); 
		
		if(location > 0)
		{
			blocks.get(location).timestamp = new_time; 
			return true; 
		}
		
		return false;   // cache does not contain this block
	}
	
	public int getCacheSize()
	{
		return blocks.size(); 
	}
	
	public void printCache()
	{
		for(int i = 0; i < blocks.size(); i++)
			System.out.print(blocks.get(i).block_id + " "); 
		System.out.println(); 
			
	}

}




