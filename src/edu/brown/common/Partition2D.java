/***************************************************************************************************
 * File: Partition.java
 * Authors: Justin A. DeBrabant (debrabant@cs.brown.edu)
 * Description: 

 ****************************************************************************************************/

package edu.brown.common; 

import java.util.*; 

public class Partition2D extends Partition
{
	public int x_min; 
	public int x_max; 
	public int y_min; 
	public int y_max; 
		
	public Partition2D()
	{
		x_min = x_max = -1; 
		y_min = y_max = -1;  
		
		partition_id = ""; 
	}
	
	public Partition2D(int xmin, int xmax, int ymin, int ymax, String id)
	{
		x_min = xmin; 
		x_max = xmax; 
		y_min = ymin; 
		y_max = ymax; 
		
		partition_id = id;  
	}
	
	public String toString()
	{
		return (new String(partition_id + "-" + x_min + " " + x_max + " " + y_min + " " + y_max + "\n")); 
	}
	
	public String convertToSQL()
	{
		return ("SELECT c1 FROM quotes " + 
				"WHERE x > " + x_min+ " AND x < " + x_max + 
				" AND y > " + y_min + " AND y < " + y_max + "\n");
	}
}