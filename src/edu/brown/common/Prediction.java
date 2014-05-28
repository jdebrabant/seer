
package edu.brown.common; 

import java.util.*; 
import java.io.*; 


public class Prediction
{
	TemplatePrediction t_pred; 
	ParameterPrediction p_pred; 
	
	double confidence; 
	
	public Prediction(TemplatePrediction t, ParameterPrediction p)
	{
		p_pred = p; 
		t_pred = t; 
		
		confidence = p.confidence * t.confidence; 
	}
		
	public String getPredictionBlockID()
	{
		return (t_pred.template_id + "-" + p_pred.partition_id);  		
	}
	
	public int getPredictedTemplate()
	{
		return t_pred.template_id; 
	}
	
	public int getPredictedParameters()
	{
		
		return 0; 
	}
}