
package edu.brown.common; 

import java.util.*;
import java.io.*; 

import javax.media.j3d.*;
import javax.vecmath.*; 

public class PredictionSpace3D extends PredictionSpace
{
	private int num_x_partitions; 
	private int num_y_partitions; 
    private int num_z_partitions; 

			
	public PredictionSpace3D(int num_x, int num_y, int num_z)
	{
		grid = new LinkedList<Partition>(); 
		num_x_partitions = num_x; 
		num_y_partitions = num_y; 
        num_z_partitions = num_z; 
	}
	
	public void writeMetaData(String file)
	{
		FileWriter grid_meta_out; 
		
		try 
		{
			grid_meta_out = new FileWriter(file); 
            
			for(int i = 0; i < grid.size(); i++)
			{
				Partition3D p3d = (Partition3D)grid.get(i);
				grid_meta_out.write(p3d.toString()); 
			}
			
			grid_meta_out.close(); 
		}
		catch(Exception e)
		{
			System.out.println(e.getMessage()); 
		}
	}
	
	public void mapToDataSpace(int x_min, int x_max, int y_min, int y_max, int z_min, int z_max)
	{
		
		int curr_x = x_min; 
		int curr_y = y_min; 
        int curr_z = z_min; 
		int total_partitions = 10;  
		
		Partition3D p; 
		
		// determine partition size for x and y direction
		int partition_size_x = (x_max - x_min) / num_x_partitions;
        int partition_size_y = (y_max - y_min) / num_y_partitions;
        int partition_size_z = (z_max - z_min) / num_z_partitions; 

		System.out.println("partiton sizes: " + partition_size_x + " " + partition_size_y + " " + partition_size_z); 
		System.out.flush(); 
		
		try 
		{
			for(int i = 0; i < num_x_partitions; i++)
			{
				curr_y = y_min;
				for(int j = 0; j < num_y_partitions; j++)
				{
                    curr_z = z_min; 
                    for(int k = 0; k < num_z_partitions; k++)
                    {
                        p = new Partition3D(curr_x, curr_x + partition_size_x, 
                                            curr_y, curr_y + partition_size_y, 
                                            curr_z, curr_z + partition_size_z, 
                                            total_partitions + "");
                        
                        grid.add(p); 
                        
                        curr_z += partition_size_z; 
                        
                        if(curr_z > z_max)
                            curr_z = z_max; 
                        
                        total_partitions++; 
                    }
                    
                    curr_y += partition_size_y; 
                    
                    if(curr_y > y_max)
                        curr_y = y_max; 
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
		RangeQuery3D query = (RangeQuery3D)q; 
	
		LinkedList<String> intersecting_partitions = new LinkedList<String>(); 
				
		int partition_count = 0; 
        

        /*
        Point3d min, max; 
        BoundingBox box_1, box_2;
        
        // create a bounding box for the  query 
        min = new Point3d(query.x_min, query.x_min, query.z_min); 
        max = new Point3d(query.x_max, query.y_max, query.z_max);
        box_1 = new BoundingBox(min, max); 
        */
		
		try 
		{
			for(Partition p: grid)
			{
                Partition3D p3d = (Partition3D)p; 
                
                /*
                // create a bounding box for the current partition
                min = new Point3d(p3d.x_min, p3d.y_min, p3d.z_min); 
                max = new Point3d(p3d.x_max, p3d.y_max, p3d.z_max);
                box_2 = new BoundingBox(min, max); 
                */
                
            	if((query.x_max >= p3d.x_min && query.x_min <= p3d.x_max) && 
           			(query.y_max >= p3d.y_min && query.y_min <= p3d.y_max) &&
           			(query.z_max >= p3d.z_min && query.z_min <= p3d.z_max))
        		{
        			partition_count++; 
					intersecting_partitions.add(p.partition_id);
        		}
                
                /*
                System.out.println("box1: " + query.x_min + " " + query.y_min + " " + query.z_min); 
				System.out.println("box2: " + p3d.x_min + " " + p3d.y_min + " " + p3d.z_min);  
				                
				if(box_1.intersect(box_2)) // if the 2 boxes intersect, add to list of intersecting partitions
				{
					partition_count++; 
					intersecting_partitions.add(p.partition_id);
				}
				*/
			}
		}
		catch(Exception e)
		{
			System.out.println(e.getMessage()); 
		}
				
		return intersecting_partitions; 
	}
}





