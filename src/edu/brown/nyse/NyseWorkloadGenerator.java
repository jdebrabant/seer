/***************************************************************************************************
 * File: NyseWorkloadGenerator.java
 * Authors: Justin A. DeBrabant (debrabant@cs.brown.edu)
 * Description: 
	Used for artificial dataset generation. Generates sequences of timebox queries and 
	partitions the queries into uniform partitions. 
****************************************************************************************************/

package edu.brown.nyse; 

import java.util.*;
import java.io.*; 

import edu.brown.common.*; 

public class NyseWorkloadGenerator extends WorkloadGenerator2D
{
	public static final int NUM_SEQUENCES = 3; 
	public static final int NUM_SEQUENCES_TO_SIMULATE = NUM_SEQUENCES * 20; 
	public static double MAX_QUERY_ERROR = 0; 
	public static final int NUM_QUERY_TEMPLATES = 10; 
    
    public static final String OUTPUT_DIRECTORY = "../data/nyse/"; 
    
    public static final int MIN_TIME = 10000; 
    public static final int MAX_TIME = 70000; 
    public static final int MIN_PRICE = 25; 
    public static final int MAX_PRICE = 160; 
    
    public static final int NUM_X_PARTITIONS = 20; 
    public static final int NUM_Y_PARTITIONS = 20; 
    
       	
    public static void main(String args[])
    {
        try 
		{
			if(args.length != 1)
			{
				System.out.println("usage: java SequenceGenerator <max query error>"); 
				System.exit(1); 
			}
			
			MAX_QUERY_ERROR = Double.parseDouble(args[0]); 
			
			Random rand = new Random();

			NyseWorkloadGenerator task_simulator = new NyseWorkloadGenerator(MIN_TIME, MAX_TIME, 
																			 MIN_PRICE, MAX_PRICE, 
																			 NUM_X_PARTITIONS, NUM_Y_PARTITIONS);
			int random_sequence; 
			
			for(int i = 0; i < NUM_SEQUENCES_TO_SIMULATE; i++)
			{
				random_sequence = rand.nextInt(NUM_SEQUENCES) + 1; 
								
				if(random_sequence == 1)
					task_simulator.simulateTaskOne(i); 
				else if(random_sequence == 2)
					task_simulator.simulateTaskTwo(i); 
				else if(random_sequence == 3)
					task_simulator.simulateTaskThree(i); 
				else if(random_sequence == 4)
					task_simulator.simulateTaskFour(i); 
				else if(random_sequence == 5)
					task_simulator.simulateTaskFive(i); 
			}
			
			
            task_simulator.close();
        }
        catch(NumberFormatException e)
        {
            System.out.println("Invalid Input");  
            e.printStackTrace();
        }
    }

    public NyseWorkloadGenerator(int minimum_x, int maximum_x, int minimum_y, int maximum_y, 
							 int number_of_partitions_x, int number_of_partitions_y)
    {
        super(minimum_x, maximum_x, minimum_y, maximum_y, 
        	  number_of_partitions_x, number_of_partitions_y, 
        	  NUM_SEQUENCES, OUTPUT_DIRECTORY); 
    }
    
    public void simulateTaskOne(int sequence_id) //3 non-contiguous boxes, upward trend
    {
        LinkedList<Query> query_sequence = new LinkedList <Query>();
		
		// initial query positions
		int query_min_x = 35000; 
		int query_max_x = 40000; 
		int query_min_y = 30; 
		int query_max_y = 40; 
		
		int delta_x = 5000; 
		int delta_y = 10; 
		
		int template_id = 0; 
		
		for(int i = 0; i < 4; i++)
		{
			if(i % 2 == 0)
				template_id = 1; 
			else 
				template_id = 1; 
			
			NYSEQuery query = new NYSEQuery(template_id, query_min_x, query_max_x, query_min_y, query_max_y, sequence_id, i+1, 0); 
			query.randomize(MAX_QUERY_ERROR); 
			
			query_sequence.add(query); 
			
			query_min_x += delta_x;
			query_max_x += delta_x; 
			
			query_min_y += delta_y; 
			query_max_y += delta_y; 
		}
		
        writeRFormat(query_sequence);
    }

    public void simulateTaskTwo(int sequence_id) // 2 levels of 6 contiguous boxes, linearly arranged
    {
		LinkedList<Query> query_sequence = new LinkedList <Query>();
		
		// initial query positions
		int query_min_x = 25000; 
		int query_max_x = 28000; 
		int query_min_y = 100; 
		int query_max_y = 110; 
		
		int delta_x = 3000; 
		
		int template_id = 2; 
		
		for(int i = 0; i < 6; i++)
		{
			NYSEQuery query = new NYSEQuery(template_id, query_min_x, query_max_x, query_min_y, query_max_y, sequence_id, i+1, 1); 
			query.randomize(MAX_QUERY_ERROR); 
			
			query_sequence.add(query); 
			
			query_min_x += delta_x;
			query_max_x += delta_x; 
		}
		
		query_min_x = 25000; 
		query_max_x = 28000; 
		query_min_y = 50; 
		query_max_y = 60; 
				
		template_id = 3; 
		
		for(int i = 0; i < 6; i++)
		{
			NYSEQuery query = new NYSEQuery(template_id, query_min_x, query_max_x, query_min_y, query_max_y, sequence_id, i+7, 1); 
			query.randomize(MAX_QUERY_ERROR); 
			
			query_sequence.add(query); 
			
			query_min_x += delta_x;
			query_max_x += delta_x; 
		}
		
        writeRFormat(query_sequence);
    }

    public void simulateTaskThree(int sequence_id) // 4 boxes, split into 2 pairs of upward trends (1 before noon, 1 after)
    {		
		LinkedList<Query> query_sequence = new LinkedList <Query>();
		
		// initial query positions
		int query_min_x = 18000; 
		int query_max_x = 22000; 
		int query_min_y = 30; 
		int query_max_y = 40; 
		
		int delta_x = 1000; 
		int delta_y = 5; 
		
		int template_id = 0; 
		
		for(int i = 0; i < 4; i++)
		{
			switch(i)
			{
				case 0: 
					template_id = 4; 
					break; 
				case 1: 
					template_id = 5; 
					break; 
				case 2: 
					template_id = 4; 
					break; 
				case 3: 
					template_id = 5; 
					break; 
			}
			
			
			NYSEQuery query = new NYSEQuery(template_id, query_min_x, query_max_x, query_min_y, query_max_y, sequence_id, i+1, 2); 
			query.randomize(MAX_QUERY_ERROR); 
			
			query_sequence.add(query); 
			
			query_min_x += delta_x;
			query_max_x += delta_x; 
			
			query_min_y += delta_y; 
			query_max_y += delta_y; 
		}
		
		query_min_x = 38000; 
		query_max_x = 42000; 
		query_min_y = 30; 
		query_max_y = 40; 
		
		for(int i = 0; i < 4; i++)
		{
			switch(i)
			{
				case 0: 
					template_id = 4; 
					break; 
				case 1: 
					template_id = 5; 
					break; 
				case 2: 
					template_id = 4; 
					break; 
				case 3: 
					template_id = 5; 
					break; 
			}
			
			NYSEQuery query = new NYSEQuery(template_id, query_min_x, query_max_x, query_min_y, query_max_y, sequence_id, i+5, 2); 
			query.randomize(MAX_QUERY_ERROR); 
			
			query_sequence.add(query); 
			
			query_min_x += delta_x;
			query_max_x += delta_x; 
			
			query_min_y += delta_y; 
			query_max_y += delta_y; 
		}
		
        writeRFormat(query_sequence);
    }
	
	public void simulateTaskFour(int sequence_id)
	{
		LinkedList<Query> query_sequence = new LinkedList <Query>();
		
		// initial query positions
		int query_min_x = 48000; 
		int query_max_x = 50000; 
		int query_min_y = 60; 
		int query_max_y = 70; 
		
		int delta_y = 10; 
		
		int template_id = 0; 
		
		for(int i = 0; i < 6; i++)
		{
			if(i % 2 == 0)
				template_id = 8; 
			else 
				template_id = 8; 
			
			NYSEQuery query = new NYSEQuery(template_id, query_min_x, query_max_x, query_min_y, query_max_y, sequence_id, i+1, 3); 
			query.randomize(MAX_QUERY_ERROR); 
			
			query_sequence.add(query); 
			
			query_min_y += delta_y; 
			query_max_y += delta_y; 
		}
		
        writeRFormat(query_sequence);
		
	}
	
	public void simulateTaskFive(int sequence_id)
	{
		LinkedList<Query> query_sequence = new LinkedList <Query>();
		
		// initial query positions
		int query_min_x = 20000; 
		int query_max_x = 24000; 
		int query_min_y = 25; 
		int query_max_y = 30; 
		
		int delta_x = 5000; 
		int delta_y = 10; 
		
		int template_id = 0; 
		
		for(int i = 0; i < 8; i++)
		{
			if(i % 2 == 0)
				template_id = 6; 
			else 
				template_id = 7; 
			
			NYSEQuery query = new NYSEQuery(template_id, query_min_x, query_max_x, query_min_y, query_max_y, sequence_id, i+1, 4); 
			query.randomize(MAX_QUERY_ERROR); 
			
			query_sequence.add(query); 
			
			query_min_x += delta_x;
			query_max_x += delta_x; 
			
			query_min_y += delta_y; 
			query_max_y += delta_y; 
		}
		
        writeRFormat(query_sequence);
		
	}
	
 
}





