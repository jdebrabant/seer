
package edu.brown.common; 

import java.util.*;
import java.io.*;

public class RangeQuery3D extends Query
{
	public int x_min, x_max;
	public int y_min, y_max;
	public int z_min, z_max; 


    public RangeQuery3D(int template, int x1, int x2,  int y1 ,int y2, int z1, int z2, int sequence, int event, int task)
	{
	    super(template, sequence, event, task); 
	    
	    x_min = x1;
		x_max = x2;
		
		y_min = y1;
		y_max = y2;
        
        z_min = z1; 
        z_max = z2; 
	}
}