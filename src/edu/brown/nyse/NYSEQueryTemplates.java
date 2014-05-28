
package edu.brown.nyse; 

import java.util.*;
import java.io.*; 

import edu.brown.common.*; 

public class NYSEQueryTemplates
{
	
	public static final String A = ""; 
	public static final String B = "";
	public static final String C = "";	
	public static final String D = "";
	public static final String E = "";

	private static final String TIME_GREATER = " WHERE quote_time > "; 
	private static final String TIME_LESS = " AND quote_time < "; 
	private static final String PRICE_GREATER = " AND offer_price > "; 
	private static final String PRICE_LESS = " AND offer_price < "; 
	
	private static final int NUM_QUERY_TEMPLATES = 10; 
	
	//private Random rand; 

	//private LinkedList<String> query_templates; 
	
	public NYSEQueryTemplates()
	{
		
	}

	public static String instantiateQueryTemplate(int template_id, int time_min, int time_max, int price_min, int price_max)
	{
		String query = null;  
				
		if(template_id == 0)
		{
			query = "SELECT * from quote " + 
					TIME_GREATER + time_min + TIME_LESS + time_max + 
					PRICE_GREATER + price_min + PRICE_LESS + price_max; 
			
		}
		else if(template_id == 1)
		{
			query = "SELECT avg(offer_price) from quote " + 
					TIME_GREATER + time_min + TIME_LESS + time_max + 
					PRICE_GREATER + price_min + PRICE_LESS + price_max + 
					" GROUP BY(symbol)"; 
		}
		else if(template_id == 2)
		{
			query = "SELECT * from quote " + 
					TIME_GREATER + time_min + TIME_LESS + time_max + 
					PRICE_GREATER + price_min + PRICE_LESS + price_max;
		}
		else if(template_id == 3)
		{
			query = "SELECT * from quote " + 
					TIME_GREATER + time_min + TIME_LESS + time_max + 
					PRICE_GREATER + price_min + PRICE_LESS + price_max; 
		}
		else if(template_id == 4)
		{
			query = "SELECT * from quote " + 
					TIME_GREATER + time_min + TIME_LESS + time_max + 
					PRICE_GREATER + price_min + PRICE_LESS + price_max; 
		}
		else if(template_id == 5)
		{
			query = "SELECT * from quote " + 
					TIME_GREATER + time_min + TIME_LESS + time_max + 
					PRICE_GREATER + price_min + PRICE_LESS + price_max; 
		}
		else if(template_id == 6)
		{
			query = "SELECT * from quote " + 
			TIME_GREATER + time_min + TIME_LESS + time_max + 
			PRICE_GREATER + price_min + PRICE_LESS + price_max; 
		}
		
		
		return query; 
		
	}

}