/***************************************************************************************************
 * File: SequenceGenerator.java
 * Authors: Justin A. DeBrabant (debrabant@cs.brown.edu)
 * Description: 
 
 ****************************************************************************************************/

import java.util.*;
import java.io.*; 


public class DataGenerator
{
	public static final int MIN_X = 10000; 
	public static final int MAX_X = 70000; 
	
	public static final int MIN_Y = 25; 
	public static final int MAX_Y = 160; 
	
	
	public static void main(String args[])
	{
		try 
		{
			int num_tuples = Integer.parseInt(args[0]); 
			String filename = args[1]; 
			
			Random rand = new Random(); 
			
			BufferedWriter out = new BufferedWriter(new FileWriter(filename)); 
			
			int min_x, min_y, max_x, max_y; 
			int temp; 
			
			int x, y; 
			
			System.out.print("generating " + num_tuples + " tuple dataset..."); 
			
			for(int i = 0; i < num_tuples; i++)
			{
				x = rand.nextInt(MAX_X - MIN_X) + MIN_X;
				y = rand.nextInt(MAX_Y - MIN_Y) + MIN_Y; 
				
				out.write(i + "," + x + "," + y + "," +
						  0 + "," + 0 + "," + 0 + "," + 0 + "," + 0 + "," +
						  0 + "," + 0 + "," + 0 + "," + 0 + "," + 0 + "\n"); 
				
			}
			
			System.out.println("done."); 
			
			out.close(); 
			
		}
		catch(Exception e)
		{
			System.out.println(e.getMessage()); 
		}
	}

}