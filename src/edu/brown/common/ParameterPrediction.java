
package edu.brown.common; 

import java.util.*; 
import java.io.*; 



public class ParameterPrediction
{
	public Integer partition_id; 
	public double confidence; 
	
	public ParameterPrediction(Integer id, double conf)
	{
		partition_id = id; 
		confidence = conf; 
	}
	
}