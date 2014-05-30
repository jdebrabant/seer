
package edu.brown.common; 

import java.util.*; 
import java.io.*; 

public class ParameterPredictor 
{
	LinkedList<AssociationRule> rules; 
	
	public ParameterPredictor()
	{
		rules = new LinkedList<AssociationRule>(); 
	}
	
	public void addAssociationRule(AssociationRule rule)
	{
		rules.add(rule); 
	}
	
	public LinkedList<Integer> predict(int current_template, int predicted_template, LinkedList<Integer> current_query_partitions)
	{
		LinkedList<Integer> prediction = new LinkedList<Integer>(); 
		
		double match_percent; 
		double max = -1; 
		int max_location = 0; 
		
		AssociationRule ar; 
		
		
		ar = findBestRule(current_template, predicted_template, current_query_partitions); 
		
		//System.out.println("rule has support: " + ar.support); 
	 	//System.out.println(ar.rhs);	
		return ar.rhs; 
	}
	
	public AssociationRule predictAll(int current_template, LinkedList<Integer> current_query_partitions)
	{
		LinkedList<Integer> prediction = new LinkedList<Integer>(); 
		
		AssociationRule ar; 
		
		ar = findBestRule(current_template, current_query_partitions);
		
		return ar; 
	}
	
	public AssociationRule findBestRule(int current_template, LinkedList<Integer> current_query_partitions)
	{
		int intersect_count = 0;
		int max_count = -1; 
		int max_rule = 0; 
		
		for(int i = 0; i < rules.size(); i++)
		{	
			if(current_template == rules.get(i).lhs_template_id)
			{
				intersect_count = computeIntersectCount(current_query_partitions, rules.get(i).lhs); 
			}
			else
			{
				intersect_count = 0; 
			}
			
			if(intersect_count > max_count)
			{
				max_count = intersect_count; 
				max_rule = i; 
			}
		}
		
		return rules.get(max_rule);
	}
	
	
	public AssociationRule findBestRule(int current_template, int predicted_template, LinkedList<Integer> current_query_partitions)
	{
		int intersect_count = 0;
		int max_count = -1; 
		int max_rule = 0; 
		
		for(int i = 0; i < rules.size(); i++)
		{	
			if(current_template == rules.get(i).lhs_template_id && predicted_template == rules.get(i).rhs_template_id)
			{
				intersect_count = computeIntersectCount(current_query_partitions, rules.get(i).lhs); 
			}
			else
			{
				intersect_count = 0; 
			}
			
			if(intersect_count > max_count)
			{
				max_count = intersect_count; 
				max_rule = i; 
			}
		}
		System.out.println(rules.get(max_rule));
		return rules.get(max_rule); 
	}
	
	public int computeIntersectCount(LinkedList<Integer> set_1, LinkedList<Integer> set_2)
	{
		int intersect_count = 0; 
		
		for(int i = 0; i < set_1.size(); i++)
		{				
			
			if(set_2.contains(set_1.get(i)))
				intersect_count++; 
		}
		
	//	System.out.println("interect count = " + intersect_count); 
		
		return intersect_count; 
	}
}









