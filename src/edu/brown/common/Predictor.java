
package edu.brown.common; 

import java.util.*; 
import java.io.*; 

import vmm.algs.*;

import be.ac.ulg.montefiore.run.jahmm.*;
import be.ac.ulg.montefiore.run.jahmm.learn.*;
import be.ac.ulg.montefiore.run.jahmm.io.*;

public class Predictor 
{
	public DCTWPredictor template_predictor; 
	public ParameterPredictor parameter_predictor; 
	
	public Hmm<ObservationInteger> hmm;
    
    public int num_query_templates; 
	
	public static final double MIN_CONFIDENCE_THRESHOLD = .6; 
	
	public Predictor(int num_templates)
	{
        num_query_templates = num_templates; 
        
		template_predictor = new DCTWPredictor(); 
		
		parameter_predictor = new ParameterPredictor(); 
		
		//Hmm<ObservationInteger> hmm = new Hmm<ObservationInteger>(SequenceGenerator.NUM_QUERY_TEMPLATES, 
		//														  new OpdfIntegerFactory(SequenceGenerator.NUM_QUERY_TEMPLATES));
	}
	
	public Prediction predictHierarchical(int current_template, LinkedList<Integer> parameters)
	{
		return null; 
	}
	
	public AssociationRule predictFlat(int current_template, LinkedList<Integer> parameters)
	{
		return parameter_predictor.predictAll(current_template, parameters); 
	}
	
	
	public LinkedList<Integer> predictParameters(int current_template, int predicted_template, LinkedList<Integer> parameters)
	{
		return parameter_predictor.predict(current_template, predicted_template, parameters); 
	}
	
	public int predictTemplate(String previous_templates)
	{
		int most_likely_template = 0;
		double max_likelihood = -1; 
		double current_template_likelihood; 
				
		for(int i = 0; i < num_query_templates; i++)
		{			
			current_template_likelihood = template_predictor.predict(Character.forDigit(i, 10), previous_templates); 
			
			//System.out.println("P(" + i + " | " + previous_templates + "): " + current_template_likelihood); 
			
			if(current_template_likelihood > max_likelihood)
			{
				max_likelihood = current_template_likelihood; 
				most_likely_template = i; 
			}
		}
			
		return most_likely_template; 
	}
	
	public TemplatePrediction predictTemplateWithConfidence(String previous_templates)
	{
		int most_likely_template = 0;
		double max_likelihood = -1; 
		double current_template_likelihood; 
		
		for(int i = 0; i < num_query_templates; i++)
		{			
			current_template_likelihood = template_predictor.predict(Character.forDigit(i, 10), previous_templates); 
			
			//System.out.println("P(" + i + " | " + previous_templates + "): " + current_template_likelihood); 
			
			if(current_template_likelihood > max_likelihood)
			{
				max_likelihood = current_template_likelihood; 
				most_likely_template = i; 
			}
		}
		
		//System.out.println("template has confidence: " + max_likelihood); 
		
		return new TemplatePrediction(most_likely_template, max_likelihood); 
	}
	
	public void trainParameterModel(String rule_file)
	{
		BufferedReader in; 
		String line; 

		try
		{
			in = new BufferedReader(new FileReader(rule_file)); 
			
			while((line = in.readLine()) != null)
			{
				System.out.println(line);
				parameter_predictor.addAssociationRule(new AssociationRule(line)); 
			}
		}
		catch(Exception e)
		{
			System.out.println(e.getMessage()); 
			e.printStackTrace(); 
		}
	}
	
	public void trainTemplateModel(String training_file)
	{
		BufferedReader in; 
		String training_sequence; 
				
		try 
		{
			in = new BufferedReader(new FileReader(training_file));  
			
			//List<List<ObservationVector>> seqs = ObservationSequencesReader.readSequences(new ObservationVectorReader(), in);
			//in.close();
			
			//KMeansLearner<ObservationVector> kml = new KMeansLearner<ObservationVector>(3, new OpdfMultiGaussianFactory(2), seqs);
			//Hmm<ObservationVector> hmm = kml.learn();
			
			training_sequence = in.readLine(); 
						
			//System.out.println("template training sequence: " + training_sequence); 
						
			//template_predictor.init(256, 0.001, 0.0, 0.0001, 1.05, 20);
			template_predictor.init(256, 5); 
			template_predictor.learn(training_sequence); 
			//template_predictor.learn("0123"); 
			
			//System.out.println("P(d | c): " + template_predictor.predict('1', "010")); 
			
		}
		catch (Exception e) 
		{
			System.out.println("ERROR: " +  e.getMessage()); 
		}
	}
}







