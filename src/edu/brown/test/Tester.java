
package edu.brown.test; 

import java.util.*; 
import java.io.*; 

import vmm.algs.*;

import edu.brown.common.*; 
import edu.brown.cache.*; 


public class Tester 
{
	public Predictor predictor; 
	
	public LinkedList<Integer> template_sequence; 
	public LinkedList<LinkedList<Integer>> parameter_sequence; 
	public HashMap<Integer, Partition> partition_mapping; 
	
	public Tester()
	{
		template_sequence = new LinkedList<Integer>(); 
		parameter_sequence = new LinkedList<LinkedList<Integer>>(); 
		partition_mapping = new HashMap<Integer, Partition>();
	}
	
	public void trainModels(String template_train_file, String parameter_train_file, int num_templates)
	{
        predictor = new Predictor(num_templates); 
        
		predictor.trainTemplateModel(template_train_file); 
		predictor.trainParameterModel(parameter_train_file); 
	}
	
	public void readQueryLogs(String filename)
	{		
		BufferedReader in; 
		
		String line; 
        StringTokenizer tokenizer;
        int queries_read = 0;
        int num_partitions = 0;
		int sum_partitions = 0;
		
		String token; 
		
		LinkedList<Integer> partitions; 
		
		try 
        {
            in = new BufferedReader(new FileReader(filename)); 
			
			while((line = in.readLine()) != null)
            {
				partitions = new LinkedList<Integer>(); 
				
				tokenizer = new StringTokenizer(line, " ");
				
				template_sequence.add(new Integer(Integer.parseInt(tokenizer.nextToken()))); 
				
				while(tokenizer.hasMoreTokens())
				{
					partitions.add(new Integer(Integer.parseInt(tokenizer.nextToken()))); 
				}
				
				parameter_sequence.add(partitions); 
            }
			
			in.close();
			
        }
        catch (Exception e) 
        {
            System.out.println(e.getMessage()); 
        }
	}
	
	public void readPartitionMapping2D(String filename)
	{
		BufferedReader in; 
		
		String line; 
        StringTokenizer tokenizer;

		Integer id;
		String mapping; 
		
		int x_min, x_max, y_min, y_max; 
		Partition p; 
		
		try 
        {
            in = new BufferedReader(new FileReader(filename)); 
			
			while((line = in.readLine()) != null)
            {
				tokenizer = new StringTokenizer(line, "-"); 
				
				
				// parse partition id
				id = new Integer(Integer.parseInt(tokenizer.nextToken())); 
				
				//System.out.println("adding partition " + id); 
				
				// parse dimensions
				mapping = tokenizer.nextToken(); 
				tokenizer = new StringTokenizer(mapping); 
				x_min = Integer.parseInt(tokenizer.nextToken()); 
				x_max = Integer.parseInt(tokenizer.nextToken());
				y_min = Integer.parseInt(tokenizer.nextToken());
				y_max = Integer.parseInt(tokenizer.nextToken());
				
				p = new Partition2D(x_min, x_max, y_min, y_max, id.toString()); 
				
				partition_mapping.put(id, p); 
				
            }
			
			in.close();
        }
        catch (Exception e) 
        {
            System.out.println(e.getMessage()); 
        }
	}
	

}
											
											
											
											
											
											
											
											
