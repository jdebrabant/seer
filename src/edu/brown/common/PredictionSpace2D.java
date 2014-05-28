
package edu.brown.common; 

import java.util.*;

import java.awt.Rectangle; 

public class PredictionSpace2D extends PredictionSpace
{
	private int num_x_partitions; 
	private int num_y_partitions; 
		
	
	public PredictionSpace2D(int num_x, int num_y)
	{
		grid = new LinkedList<Partition>(); 
		num_x_partitions = num_x; 
		num_y_partitions = num_y; 
	}
	
	public void mapToDataSpace(int x_min, int x_max, int y_min, int y_max)
	{
		
		int curr_x = x_min; 
		int curr_y = y_min; 
		int total_partitions = 10;  
		
		Partition2D p; 
		
		// determine partition size for x and y direction
		int partition_size_x = (x_max - x_min) / num_x_partitions;
        int partition_size_y = (y_max - y_min) / num_y_partitions;
		
		try 
		{
			for(int i = 0; i < num_x_partitions; i++)
			{
				curr_y = y_min;
				for(int j = 0; j < num_y_partitions; j++)
				{
					p = new Partition2D(curr_x, curr_x + partition_size_x, 
									  curr_y, curr_y + partition_size_y, 
									  total_partitions + "");
					
					grid.add(p); 
					
					curr_y += partition_size_y; 
					
					if(curr_y > y_max)
						curr_y = y_max; 
					
					total_partitions++; 
				}
				curr_x += partition_size_x; 
				
				if(curr_x > x_max)
					curr_x = x_max; 
			}
		}
		catch(Exception e)
		{
			System.out.println(e.getMessage()); 
		}
	}
	
	public LinkedList<String> mapRangeQueryToPredictionSpace(Query q)
	{
		RangeQuery2D query = (RangeQuery2D)q; 
	
		LinkedList<String> intersecting_partitions = new LinkedList<String>(); 
				
		int partition_count = 0; 
		
		try 
		{
			for(Partition p: grid)
			{
                Partition2D p2d = (Partition2D)p; 
                
				Rectangle r1 = new Rectangle(p2d.x_min, p2d.y_min, (p2d.x_max-p2d.x_min), (p2d.y_max-p2d.y_min)); 
				Rectangle r2 = new Rectangle(query.x_min, query.y_min, (query.x_max-query.x_min), (query.y_max-query.y_min));
				
				if(r1.intersects(r2))
				{
					partition_count++; 
					intersecting_partitions.add(p.partition_id);
				}
			}
		}
		catch(Exception e)
		{
			System.out.println(e.getMessage()); 
		}
				
		return intersecting_partitions; 
	}
}





