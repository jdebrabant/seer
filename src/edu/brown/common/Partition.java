/***************************************************************************************************
 * File: Partition.java
 * Authors: Justin A. DeBrabant (debrabant@cs.brown.edu)
 * Description: 

 ****************************************************************************************************/

package edu.brown.common; 

import java.util.*; 

public class Partition
{
	public String partition_id; 
	
	public Partition()
	{
		
		partition_id = ""; 
	}
	
	public Partition(String id)
	{
		partition_id = id;  
	}
	
	public String toString()
	{
		return ("toString() not implemented."); 
	}
	
	public String convertToSQL()
	{
		return ("convertToSQL() not implemented.");
	}
}