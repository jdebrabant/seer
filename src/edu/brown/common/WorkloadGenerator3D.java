package edu.brown.common; 

import java.util.*;
import java.io.*; 

public class WorkloadGenerator3D extends WorkloadGenerator
{
    public final int x_min_window;
    public final int x_max_window;
    public final int y_min_window;
    public final int y_max_window;
    public final int z_min_window; 
    public final int z_max_window;  
            
    public static final int MAX_QUERY_TEMPLATES = 10; 
    
    public WorkloadGenerator3D(int minimum_x, int maximum_x, 
                               int minimum_y, int maximum_y, 
                               int minimum_z, int maximum_z, 
                               int x_partitions, int y_partitions, int z_partitions, 
                               int num_sequences, String output_dir)
    {
    	super(num_sequences, output_dir); 
    
        x_min_window = minimum_x;
        x_max_window = maximum_x;
        y_min_window = minimum_y;
        y_max_window = maximum_y;
        z_min_window = minimum_z; 
        z_max_window = maximum_z; 
        
		// create prediction space
		p_space = new PredictionSpace3D(x_partitions, y_partitions, z_partitions); 
		((PredictionSpace3D)p_space).mapToDataSpace(minimum_x, maximum_x, minimum_y, maximum_y, minimum_z, maximum_z); 
		p_space.writeMetaData(output_dir + "meta.txt"); 
    }
}