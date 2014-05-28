


import java.util.*;
import java.sql.*;
import java.io.*;
//import util.*;

public class DataLoader
{
	protected Connection conn;
	protected Statement stmt;
	
	protected BufferedReader data_in;
	protected BufferedReader file_in;
	
	private final int BATCH_SIZE = 100; 
	
	public static void main(String args[])
	{
		DataLoader loader = new DataLoader(); 
		
		loader.load(args[0]); 
	}
	
	public DataLoader()
	{
		// connection strings
		String db_url, user, password;  
		

				
		// parsing vars
		String filename;
		String line;
		Scanner in = new Scanner(System.in);
		
		db_url = "jdbc:postgresql://";
		System.out.println("Enter db server: ");
		db_url += in.next();
		db_url += "/";
		
		System.out.println("Enter database: ");
		db_url += in.next();
		
		System.out.println("Enter user: ");
		user = in.next();
		
		//password = PasswordField.readPassword("Enter password: ");
		
		password = "";
		
		try
		{
			conn = DriverManager.getConnection(db_url, user, password);
			stmt = conn.createStatement();
				
			//data_file = args[0]; 
			//data_file = file_in.readLine();
		}
		catch(Exception e)
		{
		    System.out.println(e.getMessage()); 
		}
	}
	
	
	public int load(String data_file)
	{

		// query vars
		String query;
		String new_query;
		
		StringTokenizer tokenizer;
		
		int batch_counter = 0, total_read = 1; 
		
		try   // establish database connection and open data file
		{			
			data_in = new BufferedReader(new FileReader("10G.data"));
			
			// initialize counters/query
			query = "INSERT INTO quotes VALUES ";
			
			String id; 
			String x_min, x_max; 
			String y_min, y_max;
			
			String x, y; 
			
			String c1, c2, c3, c4, c5, c6, c7, c8, c9, c10; 
			
			tokenizer = new StringTokenizer(data_in.readLine());
 
			while(data_in.ready())
			{
				id = tokenizer.nextToken(); 
				x = tokenizer.nextToken(); 
				y = tokenizer.nextToken(); 
				
				c1 = tokenizer.nextToken(); 
				c2 = tokenizer.nextToken(); 
				c3 = tokenizer.nextToken(); 
				c4 = tokenizer.nextToken(); 
				c5 = tokenizer.nextToken(); 
				c6 = tokenizer.nextToken(); 
				c7 = tokenizer.nextToken(); 
				c8 = tokenizer.nextToken(); 
				c9 = tokenizer.nextToken(); 
				c10 = tokenizer.nextToken(); 
								
				query += "(" + id.trim() + ", " + x.trim() + ", " + y.trim() + ", " + c1.trim() + ", " + c2.trim() + ", "
						+ c3.trim() + ", " + c4.trim() + ", " + c5.trim() + ", " + c6.trim() 
						+ ", " + c7.trim() + ", " + c8.trim() + ", " + c9.trim() + ", " + c10.trim() + ")";
				
				
				if(batch_counter == BATCH_SIZE || !data_in.ready())  // last iteration of batch or file is done, so execute query
				{
					batch_counter = 0; 
					query += ";";
					
					try 
					{
						stmt.executeUpdate(query);
					}
					catch(SQLException e)
					{
						System.out.println(e.getMessage());
					}
					query = "INSERT INTO quotes VALUES ";  // reset query string
				}
				else
				{
					query += ", ";
				}

				
				batch_counter++;
				total_read++;
				
				tokenizer = new StringTokenizer(data_in.readLine()); 
			}
			
			System.out.println("read " + total_read + " from " + data_file);
		}
		catch (Exception e) 
		{
			System.out.println(e.getMessage());
		}
		
		return total_read; 
		
	}
	
	
}