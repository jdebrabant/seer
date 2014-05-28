
package edu.brown.nyse; 

import java.util.*; 

import edu.brown.common.*; 

public class NYSEQuery extends RangeQuery2D
{
	protected Random rand; 

    public NYSEQuery(int template, int x1, int x2,  int y1 ,int y2, int sequence, int event, int task)
	{
        super(template, x1, x2, y1, y2, sequence, event, task); 
        	
        rand = new Random();
	}
    
    public String convertToSQL()
    {	        
        return ("SELECT avg(c1) FROM quote " + 
                "WHERE x > " + x_min + " AND x < " + x_max + 
                " AND y > " + y_min + " AND y < " + y_max + ";");         
    }
    
    public void randomize(double max_error)
	{
		int x_diff = x_max - x_min; 
		double y_diff = y_max - y_min;
        
		x_min += (x_diff * max_error * rand.nextDouble()); 
		x_max += (x_diff * max_error * rand.nextDouble());
		y_min += (y_diff * max_error * rand.nextDouble()); 
		y_max += (y_diff * max_error * rand.nextDouble()); 
		
	}
}