
package edu.brown.common; 

import java.util.*;
import java.io.*;

public class RangeQuery2D extends Query
{
	public int x_min, x_max;
	public int y_min, y_max;

    public RangeQuery2D(int template, int x1, int x2,  int y1 ,int y2, int sequence, int event, int task)
	{
	    super(template, sequence, event, task); 
	    
	    x_min = x1;
		x_max = x2;
		
		y_min = y1;
		y_max = y2;
	}
}