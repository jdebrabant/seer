
package edu.brown.test; 

import java.util.*; 
import java.io.*; 

import vmm.algs.*;
import edu.brown.common.*; 
import edu.brown.cache.*; 

public class PredictionModelTester extends Tester
{	
	
	double precision; 
	double recall; 
	double f_measure; 
	
	public static void main(String [] args)
	{
		if(args.length != 3)
		{
			System.out.println("Usage: java PredictionModelTester <template training file> <parameter training file> <query logs>"); 
		}
		
		PredictionModelTester tester = new PredictionModelTester(); 		
		
		tester.trainModels(args[0], args[1], 10); 
		tester.readQueryLogs(args[2]); 
		
		tester.runHierarchicalModel(); 
		tester.printStats(); 
		
		tester.runFlatModel(); 
		tester.printStats(); 
		

	}
		
	public PredictionModelTester()
	{
		super(); 
		
		precision = recall = f_measure = -1; 
	}
	
	public void printStats()
	{
		System.out.println("precision: " + precision); 
		System.out.println("recall: " + recall); 
		System.out.println("f-measure: " + f_measure);
	}
	
	public void runFlatModel()
	{
		double precision_sum = 0; 
		double current_precision = 0; 
		
		double current_recall = 0; 
		double recall_sum = 0; 
		
		int current; 
				
		LinkedList<Integer> parameter_prediction; 
		
		String current_template; 
				
		int num_correct_predictions = 0; 
		
		AssociationRule prediction; 
		
		
		for(int current_query = 0; current_query < template_sequence.size() - 1; current_query++)
		{
			current_template = template_sequence.get(current_query).toString(); 
								
			prediction = predictor.predictFlat(template_sequence.get(current_query), parameter_sequence.get(current_query)); 
	
			if(prediction.rhs_template_id != template_sequence.get(current_query).intValue())
			{
				current_precision = 0; 
				current_recall = 0; 
				
			}
			else
			{
				current_precision = intersectCount(prediction.rhs, parameter_sequence.get(current_query+1)) / 
									(double)prediction.rhs.size(); 
				
				current_recall = intersectCount(prediction.rhs, parameter_sequence.get(current_query+1)) / 
									(double)parameter_sequence.get(current_query+1).size(); 
				
				num_correct_predictions++; 
			}
			
			//System.out.println("query " + (current_query + 1) + " precision: " + current_precision);
			//System.out.println("query " + (current_query + 1) + " recall: " + current_recall + "\n"); 
			
			precision_sum += current_precision; 
			recall_sum += current_recall; 
		}
		
		System.out.println("correctly predicted " + num_correct_predictions + 
						   " out of " + (template_sequence.size() - 1) + 
						   " templates (" + (num_correct_predictions / (double)(template_sequence.size() - 1)) + ")"); 
		
		precision = precision_sum/(template_sequence.size() - 1); 
		recall = recall_sum/(template_sequence.size() - 1); 
		f_measure = (2 * ((precision*recall)/(precision+recall))); 
	}
	
	
	public void runHierarchicalModel()
	{
		double precision_sum = 0; 
		double current_precision = 0; 
		
		double current_recall = 0; 
		double recall_sum = 0; 
		
		int current; 
		
		int template_prediction; 
		
		LinkedList<Integer> parameter_prediction; 
		
		String current_template; 
		
		String template_history = ""; 
		
		int num_correct_predictions = 0; 
		
		
		for(int current_query = 0; current_query < template_sequence.size() - 1; current_query++)
		{
			current_template = template_sequence.get(current_query).toString(); 
			
			template_history += current_template; 
			
			template_prediction = predictor.predictTemplate(template_history);
			
			System.out.print(template_prediction + " : " + template_sequence.get(current_query + 1));
			if(template_prediction == template_sequence.get(current_query + 1))
			{
				//System.out.println(", CORRECT"); 
				num_correct_predictions++; 
			}
			else
			{
				//System.out.println(", INCORRECT"); 
			}
			
			parameter_prediction = predictor.predictParameters(template_sequence.get(current_query), template_prediction, 
															   parameter_sequence.get(current_query)); 
			
			
			System.out.println("current query parameters: " + parameter_sequence.get(current_query)); 
			System.out.println("predicted parameters: " + parameter_prediction); 
			System.out.println("actual parameters: " + parameter_sequence.get(current_query+1)); 
			 
			
			if(parameter_sequence.get(current_query+1).size() == 0)
			{
				System.out.println("ERROR: next query parameter set is empty");
				
				current_precision = current_recall = 0; 
			}
			else
			{
				current_precision = intersectCount(parameter_prediction, parameter_sequence.get(current_query+1)) / 
									(double)parameter_prediction.size(); 
				
				current_recall = intersectCount(parameter_prediction, parameter_sequence.get(current_query+1)) / 
											 (double)parameter_sequence.get(current_query+1).size(); 
			}
			
			//System.out.println("query " + (current_query + 1) + " precision: " + current_precision);
			//System.out.println("query " + (current_query + 1) + " recall: " + current_recall + "\n"); 
			
			precision_sum += current_precision; 
			recall_sum += current_recall; 
		}
		
		System.out.println("correctly predicted " + num_correct_predictions + 
						   " out of " + (template_sequence.size() - 1) + 
						   " templates (" + (num_correct_predictions / (double)(template_sequence.size() - 1)) + ")"); 
		
		precision = precision_sum/(template_sequence.size() - 1); 
		recall = recall_sum/(template_sequence.size() - 1); 
		f_measure = (2 * ((precision*recall)/(precision+recall))); 
	}
	
	
	public double calculateFMeasure(double precision, double recall)
	{
		f_measure = (2 * ((precision*recall)/(precision+recall))); 
		return f_measure; 
	}
	
	public int intersectCount(LinkedList<Integer> list_1, LinkedList<Integer> list_2)
	{
		int count = 0; 
		
		for(int i = 0; i < list_1.size(); i++)
		{
			if(list_2.contains(list_1.get(i)))
				count++; 
		}
		
		return count; 
	}
}











