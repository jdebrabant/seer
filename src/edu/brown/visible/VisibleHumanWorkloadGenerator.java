
package edu.brown.visible; 

import java.util.*; 
import java.io.*; 

import edu.brown.common.*; 

public class VisibleHumanWorkloadGenerator extends WorkloadGenerator3D
{
	private static final int NUM_WORKLOADS = 4; 
	private static final int NUM_WORKLOADS_TO_GENERATE = 20; 
	
	private static double MAX_QUERY_ERROR = 0; 
	private static final int NUM_QUERY_TEMPLATES = 10; 
    
    private static String OUTPUT_DIRECTORY = "../data/visible/"; 
    
	private static final int MIN_X = 0; 
	private static final int MAX_X = 2048; 
	
	private static final int MIN_Y = 0; 
	private static final int MAX_Y = 1216; 
	
	private static final int MIN_Z = 0; 
	private static final int MAX_Z = 1024; 

/*
	private static final int NUM_X_PARTITIONS = 10;  
	private static final int NUM_Y_PARTITIONS = 10; 
	private static final int NUM_Z_PARTITIONS = 10;
*/	
	private static final int NUM_X_PARTITIONS = 16;  
	private static final int NUM_Y_PARTITIONS = 16; 
	private static final int NUM_Z_PARTITIONS = 8; 
	

	
	public enum Interactivity {HIGH, MEDIUM, LOW}; 

    public VisibleHumanWorkloadGenerator(int minimum_x, int maximum_x, 
    									 int minimum_y, int maximum_y, 
    									 int minimum_z, int maximum_z, 
							 			 int x_partitions, 
							 			 int y_partitions, 
							 			 int z_partitions)
    {
        super(minimum_x, maximum_x, minimum_y, maximum_y, minimum_z, maximum_z,
        	  x_partitions, y_partitions, z_partitions,
        	  NUM_WORKLOADS, OUTPUT_DIRECTORY);
    }
         
       	
    public static void main(String args[])
    {
        try 
		{
			// workload id represents which workload to run, or 5 for all workloads
			if(args.length != 1)
			{
				System.out.println("usage: java SequenceGenerator <workload id>"); 
				System.exit(1); 
			}
			
			//MAX_QUERY_ERROR = Double.parseDouble(args[0]); 
			int workload = Integer.parseInt(args[0]); 
			
			Random rand = new Random();

			VisibleHumanWorkloadGenerator task_simulator = 
				new VisibleHumanWorkloadGenerator(MIN_X, MAX_X, MIN_Y, MAX_Y, MIN_Z, MAX_Z, 
												  NUM_X_PARTITIONS, NUM_Y_PARTITIONS, NUM_Z_PARTITIONS);
			int random_sequence; 
			int random_interactivity; 
			
			int sequence_id = 0; 
			int workload_id = 0; 
			
			/*
			if(workload == 0)
			{
				OUTPUT_DIRECTORY += 0;
			}
			else if(workload == 1)
			{
				OUTPUT_DIRECTORY += 1;
			}
			else if(workload == 2)
			{
				OUTPUT_DIRECTORY += 2; 
			}
			else if(workload == 3)
			{
				OUTPUT_DIRECTORY += 3; 
			}
			*/
			
			if(workload == 0 || workload == 5)
			{
				System.out.print("Generating workload 0..."); 
				
				for(int i = 0; i < NUM_WORKLOADS_TO_GENERATE; i++)
				{
					random_interactivity = rand.nextInt(3); 
					
					if(random_interactivity == 0)
					{
						sequence_id = task_simulator.simulateWorkloadOne(sequence_id, workload_id, Interactivity.LOW); 
					}
					else if(random_interactivity == 1)
					{
						sequence_id = task_simulator.simulateWorkloadOne(sequence_id, workload_id, Interactivity.MEDIUM); 
					}
					else
					{
						sequence_id = task_simulator.simulateWorkloadOne(sequence_id, workload_id, Interactivity.HIGH);
					}
					
					sequence_id++;
				}
				 
				System.out.println("done."); 
			}	
			
			if(workload == 1 || workload == 5)
			{
				System.out.print("Generating workload 1..."); 
				workload_id = 1; 			
				
				for(int i = 0; i < NUM_WORKLOADS_TO_GENERATE; i++)
				{
					random_interactivity = rand.nextInt(3); 
					
					if(random_interactivity == 0)
					{
						sequence_id = task_simulator.simulateWorkloadTwo(sequence_id, workload_id, Interactivity.LOW);
					}
					else if(random_interactivity == 1)
					{
						sequence_id = task_simulator.simulateWorkloadTwo(sequence_id, workload_id, Interactivity.MEDIUM);
					}
					else
					{
						sequence_id = task_simulator.simulateWorkloadTwo(sequence_id, workload_id, Interactivity.HIGH);
					}
					
					sequence_id++; 
				}

				System.out.println("done."); 
			}
			
			if(workload == 2 || workload == 5)
			{
				System.out.print("Generating workload 2..."); 
				
				workload_id = 2; 
				for(int i = 0; i < NUM_WORKLOADS_TO_GENERATE; i++)
				{
					random_interactivity = rand.nextInt(3); 
					
					if(random_interactivity == 0)
					{
						sequence_id = task_simulator.simulateWorkloadThree(sequence_id, workload_id, Interactivity.LOW);
					}
					else if(random_interactivity == 1)
					{
						sequence_id = task_simulator.simulateWorkloadThree(sequence_id, workload_id, Interactivity.MEDIUM);
					}
					else
					{
						sequence_id = task_simulator.simulateWorkloadThree(sequence_id, workload_id, Interactivity.HIGH);
					}
					sequence_id++; 
				}

				System.out.println("done."); 
			}
			
			if(workload == 3 || workload == 5)
			{
				System.out.println("Generating mixed workload...");
				
				for(int i = 0; i < NUM_WORKLOADS_TO_GENERATE; i++)
				{
					sequence_id = task_simulator.simulateWorkloadFour(sequence_id); 
					sequence_id++; 
				}
			}
 
 			System.out.println("Generated " + (sequence_id+1) + " unique sequences (support " + (1/(double)sequence_id) + ")");
			
            task_simulator.close();
        }
        catch(NumberFormatException e)
        {
            System.out.println("Invalid Input");  
            e.printStackTrace();
        }
    }
    
    public int simulateWorkloadOne(int sequence_id, int workload_id, Interactivity inter) // whole body scan
    {
        LinkedList<Query> query_sequence = new LinkedList <Query>();
		
		// initial query positions
		int x_min = 0; 
		int x_max = 100; 
		int y_min = 0; 
		int y_max = 100; 
		int z_min = 0; 
		int z_max = 100;
		
		int delta = 0; 
				
		if(inter == Interactivity.HIGH)
			delta = 128; 
		else if(inter == Interactivity.MEDIUM)
			delta = 256; 
		else
			delta = 384;
			
			delta = 128; 		
		
		int template_id = 1; 
		int event_id = 0; 
		
		x_min = y_min = 0; 
		x_max = y_max = 510; 
		while(z_max <= 1024)
		{
			
			VisibleHumanQuery query = new VisibleHumanQuery(template_id, 
															x_min, x_max, 
															y_min, y_max, 
															z_min, z_max, 
															sequence_id, event_id+1, workload_id);
															
			query.randomize(MAX_QUERY_ERROR); 

			query_sequence.add(query); 

			event_id++;
			
			z_min += delta; 
			z_max += delta; 
			
			x_min = y_min = 513; 
			x_max = y_max = 1023;
			
			query = new VisibleHumanQuery(template_id, 
															x_min, x_max, 
															y_min, y_max, 
															z_min, z_max, 
															sequence_id, event_id+1, workload_id);
															
														
															
			query.randomize(MAX_QUERY_ERROR); 

			query_sequence.add(query); 

			event_id++;
			
			z_min += delta; 
			z_max += delta;
		}
		
        writeRFormat(query_sequence);
        
        return sequence_id; 
    }
    
    public int simulateWorkloadTwo(int sequence_id, int workload_id, Interactivity inter)  // scan of the heart
    {
        LinkedList<Query> query_sequence = new LinkedList <Query>();
		
		// initial query positions
		int x_min = 1024; 
		int x_max = 1536; 
		
		int y_min = 513; 
		int y_max = 639; 
		
		int z_min = 513; 
		int z_max = 639; 
		
		int delta = 0; 
		
		if(inter == Interactivity.HIGH)
			delta = 100; 
		else if(inter == Interactivity.MEDIUM)
			delta = 150; 
		else
			delta = 250;
		
		int template_id = 1; 
		int event_id = 0; 
		
		VisibleHumanQuery query; 
		
		// reset x and y min/max
		x_min = 1024; 
		x_max = 1250; 
		y_min = 513; 
		y_max = 639;
		
		for(int i = 1; i <= 5; i++)
		{
			delta = 0; 
			
			x_min = 1000; 
			x_max = 1200; 
			y_min = 500; 
			y_max = 600;
			
			template_id = i; 
			
			query = new VisibleHumanQuery(template_id, 
											x_min, x_max, 
											y_min, y_max, 
											z_min, z_max, 
											sequence_id, event_id+1, workload_id);
																														
			query_sequence.add(query); 
			
			x_min += delta; 
			x_max += delta; 
			
			y_min += delta; 
			y_max += delta;
															
			event_id++; 	
			
			/*
			query = new VisibleHumanQuery(template_id, 
											x_min, x_max, 
											y_min, y_max, 
											z_min, z_max, 
											sequence_id, event_id+1, workload_id);
			
			query_sequence.add(query); 
			
			event_id++; 
			*/															
		}
		
        writeRFormat(query_sequence);
        
        return sequence_id;
    }
    
    public int simulateWorkloadThree(int sequence_id, int workload_id, Interactivity inter)  // multi-axis scan of brain
    {
        LinkedList<Query> query_sequence = new LinkedList <Query>();
		
		// initial query positions
		int x_min = 0; 
		int x_max = 100; 
		int y_min = 0; 
		int y_max = 100; 
		int z_min = 0; 
		int z_max = 100;
		
		int delta = 0; 
				
		if(inter == Interactivity.HIGH)
			delta = 64; 
		else if(inter == Interactivity.MEDIUM)
			delta = 128; 
		else
			delta = 256;
			
		VisibleHumanQuery query;
		
		
		int template_id = 1; 
		int event_id = 0; 
		
		int num_restarts = 0; 

		delta = 128; 
		
		Random rand_gen = new Random(); 
		
		while(z_max <= 640)
		{
			x_min = y_min = 0; 
			x_max = y_max = 512;
			
			query = new VisibleHumanQuery(template_id, 
															x_min, x_max, 
															y_min, y_max, 
															z_min, z_max, 
															sequence_id, event_id+1, workload_id);
															
			query.randomize(MAX_QUERY_ERROR); 

			query_sequence.add(query); 

			event_id++;
			
			x_min = y_min = 513; 
			x_max = y_max = 1024;
			
			query = new VisibleHumanQuery(template_id, 
															x_min, x_max, 
															y_min, y_max, 
															z_min, z_max, 
															sequence_id, event_id+1, workload_id);
															
														
															
			query.randomize(MAX_QUERY_ERROR); 

			query_sequence.add(query); 

			event_id++;
			
			z_min += delta; 
			z_max += delta; 
			
			if(rand_gen.nextInt(3) == 0)
			{
				z_min = 0; 
				z_max = 128; 
				num_restarts++; 
			}
			
			if(num_restarts == 4)
			{
				break; 
			}
		}
		
		/*
		z_min += delta; 
		z_max += delta;
		
		while(z_max <= 640)
		{
			x_min = y_min = 0; 
			x_max = y_max = 512;
			
			query = new VisibleHumanQuery(template_id, 
															x_min, x_max, 
															y_min, y_max, 
															z_min, z_max, 
															sequence_id, event_id+1, workload_id);
															
			query.randomize(MAX_QUERY_ERROR); 

			query_sequence.add(query); 

			event_id++;
			
			x_min = y_min = 513; 
			x_max = y_max = 1024;
			
			query = new VisibleHumanQuery(template_id, 
															x_min, x_max, 
															y_min, y_max, 
															z_min, z_max, 
															sequence_id, event_id+1, workload_id);
															
														
															
			query.randomize(MAX_QUERY_ERROR); 

			query_sequence.add(query); 

			event_id++;
			
			z_min += delta; 
			z_max += delta;
		}
		*/
		
		/*
		while(z_max <= 640)
		{
			while(x_max <= 1216)
			{	
				while(y_max <= 1216)
				{
					 query = new VisibleHumanQuery(template_id, 
																	x_min, x_max, 
																	y_min, y_max, 
																	z_min, z_max, 
																	sequence_id, event_id+1, workload_id);
																	
					query.randomize(MAX_QUERY_ERROR); 

					query_sequence.add(query); 

					event_id++; 
					y_min += delta; 
					y_max += delta;
					
				}
				x_min += delta; 
				x_max += delta;
				
			}	
			
			z_min += delta; 
			z_max += delta;			
		}
	
		x_min = 0; 
		x_max = 100; 
		y_min = 0; 
		y_max = 100; 
		z_min = 0; 
		z_max = 100;
		
		while(z_max <= 640)
		{
			while(x_max <= 1216)
			{	
				while(y_max <= 1216)
				{
					 query = new VisibleHumanQuery(template_id, 
																	x_min, x_max, 
																	y_min, y_max, 
																	z_min, z_max, 
																	sequence_id, event_id+1, workload_id);
																	
					query.randomize(MAX_QUERY_ERROR); 

					query_sequence.add(query); 

					event_id++; 
					y_min += delta; 
					y_max += delta;
					
				}
				x_min += delta; 
				x_max += delta;
				
			}	
			
			z_min += delta; 
			z_max += delta;			
		}
		*/
		
		/*
		while(x_max <= 1216)
		{
			while(y_max <= 1216)
			{	
				while(z_max <= 640)
				{
					query = new VisibleHumanQuery(template_id, 
													x_min, x_max, 
													y_min, y_max, 
													z_min, z_max, 
													sequence_id, event_id+1, workload_id); 
			
					query_sequence.add(query); 
				
					event_id++; 
				
					z_min += delta; 
					z_max += delta;	
				}
				
				y_min += delta; 
				y_max += delta;				
			}
			x_min += delta; 
			x_max += delta; 
		}
		*/
		
        writeRFormat(query_sequence);
        
    	return sequence_id; 
    }
    
    public int simulateWorkloadFour(int sequence_id) // mixed workload
    {
		Random rand = new Random(); 
		int random_workload = 0; 
		int random_interactivity = 0; 
		int workload_id = 3; 
		
		Interactivity inter; 
		
        for(int i = 0; i < 1; i++)		
		{
			random_workload = rand.nextInt(3); 
			random_interactivity = rand.nextInt(3); 
			
			if(random_interactivity == 0)
			{
				inter = Interactivity.HIGH; 
			}
			else if(random_interactivity == 1)
			{
				inter = Interactivity.MEDIUM; 
			}
			else
			{
				inter = Interactivity.LOW; 
			}
			
			if(random_workload == 0)
			{
				sequence_id = simulateWorkloadOne(sequence_id, workload_id, inter); 
			}
			else if(random_workload == 1)
			{
				sequence_id = simulateWorkloadTwo(sequence_id, workload_id, inter); 
			}
			else
			{
				sequence_id = simulateWorkloadThree(sequence_id, workload_id, inter); 
			}
			sequence_id++; 
		}		
		return sequence_id; 
    }
}






