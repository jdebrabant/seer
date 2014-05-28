
package edu.brown.common; 

import java.util.*;
import java.io.*; 

public class PredictionSpace
{
    public LinkedList<Partition> grid;
	
	public PredictionSpace()
	{
	}
	
	public void writeMetaData(String file)
	{
		FileWriter grid_meta_out; 
		
		try 
		{
			grid_meta_out = new FileWriter(file); 
            
			for(int i = 0; i < grid.size(); i++)
			{
				grid.get(i).toString(); 
				grid_meta_out.write(grid.get(i).toString()); 
			}
			
			grid_meta_out.close(); 
		}
		catch(Exception e)
		{
			System.out.println(e.getMessage()); 
		}
	}
	
	public LinkedList<String> mapRangeQueryToPredictionSpace(Query query)
	{
		return null; 
	}

}





