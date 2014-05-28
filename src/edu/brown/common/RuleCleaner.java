
package edu.brown.common; 

import java.util.*; 
import java.io.*; 

public class RuleCleaner 
{
	public static final int MIN_PARTITIONS_IN_VALID_RULE = 6;
	
	public static void main(String [] args)
	{
		BufferedReader in; 
		BufferedWriter out; 
		
		String line; 
		String token; 
		
		int support; 
		
		StringTokenizer tokenizer; 
		
		AssociationRule rule; 
		LinkedList<AssociationRule> valid_rules; 
		
		LinkedList<Integer> lhs;
		LinkedList<Integer> rhs;
		
		boolean skip_rule; 
		
		int header_count = 0; 
		
		int lhs_template; 
		int rhs_template; 
		
		int rule_count = 0; 
        
        int num_templates; 
		
		if(args.length != 3)
		{
			System.out.println("usage: java RuleCleaner <input rule file> <output rule file> <num templates>"); 
		}
	
		try
		{
			in = new BufferedReader(new FileReader(args[0])); 
			out = new BufferedWriter(new FileWriter(args[1])); 
            
            num_templates = Integer.parseInt(args[2]); 
			
			valid_rules = new LinkedList<AssociationRule>(); 
			
			// advance file past header
			while((line = in.readLine()) != null)
			{
				tokenizer = new StringTokenizer(line); 

				token = tokenizer.nextToken(); 
				if(token.equals("MINSUPPORT"))
					break; 
			}
			
			// read in the rule file line by line
			while((line = in.readLine()) != null)
			{
				rule_count++; 
				
				lhs = new LinkedList<Integer>(); 
				rhs = new LinkedList<Integer>(); 
				
				lhs_template = rhs_template = -1; 
				
				tokenizer = new StringTokenizer(line); 
				
				skip_rule = false; 
				
				// read in left hand side of rule
				while(tokenizer.hasMoreTokens())
				{
					token = tokenizer.nextToken(); 
					
					if(token.equals("--") || token.equals("PROCESS"))
					{
						//System.out.println("skipping here 1: " + line); 
						skip_rule = true; 
						break; 
					}
					
					if(token.equals("->"))
						break; 
					
					if(Integer.parseInt(token) <=  num_templates)
					//if(Integer.parseInt(token) == 1)
					{
						lhs_template = Integer.parseInt(token); 
						continue; 
					}
					
					lhs.add(new Integer(Integer.parseInt(token))); 
				}
				
				if(skip_rule)  // skip the rest of this rule
					continue; 
		
				// read in right hand side of rule
				while(tokenizer.hasMoreTokens())
				{
					token = tokenizer.nextToken(); 
					
					if(token.equals("->"))
					{
						//System.out.println("skipping here 2: " + line); 

						skip_rule = true; 
						break; 
					}
					
					if(token.equals("--"))
						break; 
					
					if(Integer.parseInt(token) <=  num_templates)
					//if(Integer.parseInt(token) == 1)
					{
						rhs_template = Integer.parseInt(token); 
						continue; 
					}
					
					rhs.add(new Integer(Integer.parseInt(token))); 
				}
				
				if(skip_rule)  // skip the rest of this rule
					continue; 
				
				// read in rule support 
				support = Integer.parseInt(tokenizer.nextToken()); 

				rule = new AssociationRule(lhs, rhs, support, lhs_template, rhs_template); 
				
				// check if rule is a valid rule, otherwise discard
				if(isRuleValid(rule))  
				{
					valid_rules.add(rule); 
				}
			}
			
			System.out.println(rule_count + " rules read"); 
			System.out.println(valid_rules.size() + " rules are valid"); 
			
			cleanRules(valid_rules); 
			
			System.out.println(valid_rules.size() + " cleaned rules"); 
			
			for(AssociationRule ar: valid_rules)
			{
				
				out.write(ar.toString()); 
			}
			
			in.close(); 
			out.close(); 
		}
		catch(Exception e)
		{
			System.out.println(e.getMessage()); 
			e.printStackTrace(); 
		}
		
	}
					   
	private static boolean isRuleValid(AssociationRule rule)
	{
		if(rule.lhs_template_id > 0 && rule.rhs_template_id > 0)
		{
			if(rule.lhs.size() >= MIN_PARTITIONS_IN_VALID_RULE && rule.rhs.size() >= MIN_PARTITIONS_IN_VALID_RULE)  // only produce rules of a certain size
				return true; 
		}
				
		return false; 
	}
	
	private static void cleanRules(LinkedList<AssociationRule> rules)
	{
		int num_rules_start; 
		int num_rules_end; 
		
		num_rules_start = rules.size(); 
		
		int rules_cleaned = 0; 
		int rules_removed = 0; 
		
		for(int i = 0; i < rules.size(); i++)
		{
			for(int j = 0; j < rules.size(); j++)
			{
				if(i == j)
					continue; 
					
				if(rules.get(i).lhs_template_id != rules.get(j).lhs_template_id || rules.get(i).rhs_template_id != rules.get(j).rhs_template_id)
					continue; 
					
				if(rules.get(i).lhs.size() != rules.get(j).lhs.size())
					continue;
					
				if(rules.get(i).rhs.size() < rules.get(j).rhs.size())
					continue; 
				
				// rule1.lhs == rule2.lhs
				if(listContains(rules.get(i).lhs, rules.get(j).lhs) && listContains(rules.get(j).lhs, rules.get(i).lhs))
				{
					// rule2.rhs is a subset of rule1.rhs
					if(listContains(rules.get(i).rhs, rules.get(j).rhs))
					{
						if(rules.get(i).support >= rules.get(j).support)
						{
							rules_removed++; 
							rules.remove(j); 
							//j--; 
						}
					}
				}
			}
			
			rules_cleaned++; 
			if(rules_cleaned % 10 == 0)
				System.out.println(rules_removed + " out of " + rules_cleaned + " rules removed."); 
		}
		
		num_rules_end = rules.size(); 
		
		System.out.println("removed " + (num_rules_start - num_rules_end) + " out of " + num_rules_start + " rules"); 
	}
	
	// returns true if list_1 contains list_2, false otherwise
	private static boolean listContains(LinkedList<Integer> list_1, LinkedList<Integer> list_2)
	{
		for(int i = 0; i < list_2.size(); i++)
		{
			if(!list_1.contains(list_2.get(i)))
				return false; 
		}
		return true; 
	}

}


































