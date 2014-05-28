
package edu.brown.common; 

import java.util.*; 
import java.io.*; 



public class TemplatePrediction
{
	public int template_id; 
	public double confidence; 
	
	public TemplatePrediction(int id, double conf)
	{
		template_id = id; 
		confidence = conf; 
	}

}