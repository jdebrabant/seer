
package edu.brown.visible; 

import java.util.*;
import java.io.*;

import edu.brown.common.*; 

public class VisibleHumanQuery extends RangeQuery3D 
{
	protected Random rand; 

	public VisibleHumanQuery(int template, int x1, int x2,  int y1 ,int y2, int z1, int z2, int sequence, int event, int task)
	{
        super(template, x1, x2, y1, y2, z1, z2, sequence, event, task); 
        	
        rand = new Random();
	}
	
	public String convertToSQL()
	{			
		return ("subarray(body, " + x_min + ", " + y_min + ", " + z_min + ", " + x_max + ", " + y_max + ", " + z_max + ")"); 
	}
	
	public void randomize(double max_error)
	{
		int x_diff = x_max - x_min; 
		double y_diff = y_max - y_min;
		double z_diff = z_max - z_min;
						
		x_min += (x_diff * max_error * rand.nextDouble()); 
		x_max += (x_diff * max_error * rand.nextDouble());
		y_min += (y_diff * max_error * rand.nextDouble()); 
		y_max += (y_diff * max_error * rand.nextDouble()); 
        z_min += (z_diff * max_error * rand.nextDouble()); 
        z_max += (z_diff * max_error * rand.nextDouble()); 		
	}
}





