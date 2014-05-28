

package edu.brown.common; 

import java.util.*;
import java.io.*;

public class Query 
{
    public int task_id; 
	public int sequence_id; 
	public int event_id; 
    public int template_id; 
    
	public Query(int template, int sequence, int event, int task)
	{
		task_id = task; 
		template_id = template; 
		sequence_id = sequence; 
		event_id = event; 
	}
	
	public String convertToSQL()
	{			
		return ("convertToSQL() not implemented"); 
	}
	

}