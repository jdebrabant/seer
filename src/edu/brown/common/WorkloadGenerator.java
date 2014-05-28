
package edu.brown.common; 

import java.util.*;
import java.io.*; 

public class WorkloadGenerator
{
    protected FileWriter arules_out;
	protected FileWriter sql_out; 
	protected FileWriter vmm_out; 
	protected FileWriter hmm_out; 
    
    protected FileWriter [] sequence_files;   
    
    public static final int MAX_QUERY_TEMPLATES = 10; 
	public static final int MAX_TILES = 8; 
    
    protected PredictionSpace p_space; 
    
    public WorkloadGenerator(int num_sequences, String output_dir)
    {
		sequence_files = new FileWriter[num_sequences]; 
		
        try
        {
			// open output files
			arules_out = new FileWriter(output_dir + "sequence-all.asc");
			sql_out = new FileWriter(output_dir + "sql.txt"); 
			vmm_out = new FileWriter(output_dir + "vmm_train.seq"); 
			hmm_out = new FileWriter(output_dir + "hmm_train.seq"); 
			
			for(int i = 0; i < num_sequences; i++)
			{
				sequence_files[i] = new FileWriter(output_dir + "sequence-" + i + "/sequence-" + i + ".asc"); 
			}
        }
        catch(Exception e)
        {
            System.out.println(e.getMessage()); 
        }
    }
    
    public void writeRFormat(List<Query> query_sequence)
	{		
		LinkedList<String> intersecting_partitions; 
		
		try 
		{
			for(Query query: query_sequence)
			{				
				sql_out.write(query.convertToSQL() + "\n"); 
				vmm_out.write("" + query.template_id); 
				hmm_out.write(query.template_id + " ; "); 
				
				intersecting_partitions = p_space.mapRangeQueryToPredictionSpace(query); 
				
				//System.out.println("intersecting partitions = " + intersecting_partitions.size()); 
				if(intersecting_partitions.size() == 0)
				{
					System.out.println(query.convertToSQL()); 
				}
				
				//arules_out.write(query.sequence_id + " "); 
				//arules_out.write(query.event_id + " ");
				//arules_out.write((intersecting_partitions.size()+1) + " "); 
				arules_out.write(query.template_id + " "); 
				
				sequence_files[query.task_id].write(query.sequence_id + " "); 
				sequence_files[query.task_id].write(query.event_id + " "); 
				
				if(intersecting_partitions.size() < MAX_TILES)
				{
					sequence_files[query.task_id].write((intersecting_partitions.size()+1) + " "); 
				}
				else
				{
					sequence_files[query.task_id].write((MAX_TILES + 1) + " ");
				}
				sequence_files[query.task_id].write(query.template_id + " "); 
				
				int num_partitions_written = 0; 
				for(String partition_id: intersecting_partitions)
				{
					if(num_partitions_written == MAX_TILES)
						break; 
					
					arules_out.write(partition_id + " "); 
					sequence_files[query.task_id].write(partition_id + " "); 
					
					num_partitions_written++; 
				}
				
				arules_out.write("\n"); 
				
				sequence_files[query.task_id].write("\n"); 
			}
			
			hmm_out.write("\n"); 
		}
		catch(Exception e)
		{
			System.out.println(e.getMessage());
		}
	}
    
    public void close()
    {
        try
        {
			arules_out.close(); 
			sql_out.close(); 
			
			vmm_out.write("\n"); 
			vmm_out.close(); 
			
			hmm_out.close(); 
			
			for(int i = 0; i < sequence_files.length; i++)
			{
				sequence_files[i].close(); 
			}
		}
        catch(Exception e)
        {
            System.out.println(e.getMessage()); 
        }
    }   
}
