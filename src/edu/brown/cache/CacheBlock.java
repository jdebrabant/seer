
package edu.brown.cache; 

import java.util.*; 
import java.io.*; 

import edu.brown.common.*; 

public class CacheBlock 
{
	//public int block_id; 
	public String block_id; 
	public int timestamp; 
	
	public LinkedList<String> data; 
	
	public CacheBlock(String id)
	{
		block_id = id; 
		data = new LinkedList<String>(); 
		
		timestamp = -1; // timestamp will be managed by cache
	}
	
	public void addToData(String record)
	{
		data.add(record); 
	}
	
}
