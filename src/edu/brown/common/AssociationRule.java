

package edu.brown.common; 

import java.util.*; 

public class AssociationRule
{
	public LinkedList<Integer> lhs; 
	public LinkedList<Integer> rhs; 
	public double support; 
	public boolean removed; 
	
	public int lhs_template_id; 
	public int rhs_template_id; 
	
	public AssociationRule(LinkedList<Integer> l, LinkedList<Integer> r, double supp)
	{
		lhs = l; 
		rhs = r; 
		support = supp; 
		removed = false;
		
		lhs_template_id = -1; 
		rhs_template_id = -1; 
	}
	
	public AssociationRule(LinkedList<Integer> l, LinkedList<Integer> r, double supp, int lhs_template, int rhs_template)
	{
		lhs = l; 
		rhs = r; 
		support = supp; 
		removed = false;
		
		lhs_template_id = lhs_template; 
		rhs_template_id = rhs_template; 
	}
	
	/*
		Creates an association rule from a String in the following format: 
	 
			lhs ==> rhs , support
	 
		where lhs and rhs are a list of integer item id's and support is a double. 
	 */
	public AssociationRule(String rule)
	{
		StringTokenizer tokenizer; 
		String token; 
		
		Integer partition; 

		try 
		{
			tokenizer = new StringTokenizer(rule); 
			lhs = new LinkedList<Integer>(); 
			rhs = new LinkedList<Integer>(); 
			
			// read in LHS
			while(tokenizer.hasMoreTokens())
			{
				token = tokenizer.nextToken(); 
				
				if(token.equals("==>"))
					break; 
				
				partition = new Integer(Integer.parseInt(token)); 
				
				if(partition < WorkloadGenerator2D.MAX_QUERY_TEMPLATES)
				{
					lhs_template_id = partition.intValue(); 
				}
				else
				{
					lhs.add(partition);
				}
			}
			
			// read in RHS
			while(tokenizer.hasMoreTokens())
			{
				token = tokenizer.nextToken(); 
				
				if(token.equals(","))
					break; 
				
				partition = new Integer(Integer.parseInt(token)); 
				
				if(partition < WorkloadGenerator2D.MAX_QUERY_TEMPLATES)
				{
					rhs_template_id = partition.intValue(); 
				}
				else
				{
					rhs.add(partition);
				}
			}
			
			support = Double.parseDouble(tokenizer.nextToken());
			
			removed = false; 		
		}
		catch(Exception e)
		{
			System.out.println(e.getMessage()); 
		}
	}
	
	public String toString()
	{
		String rule = lhs_template_id + " "; 
		
		for(int i = 0; i < lhs.size(); i++)
		{
			rule += lhs.get(i).intValue() + " "; 
		}
		
		rule += " ==> " + rhs_template_id + " "; 
		
		for(int i = 0; i < rhs.size(); i++)
		{
			rule += rhs.get(i).intValue() + " "; 
		}
		
		rule += ", " + support + "\n";
		
		return rule; 
	}
}