/**                                                                                                                                                                                
 * Copyright (c) 2010 Yahoo! Inc. All rights reserved.                                                                                                                             
 *                                                                                                                                                                                 
 * Licensed under the Apache License, Version 2.0 (the "License"); you                                                                                                             
 * may not use this file except in compliance with the License. You                                                                                                                
 * may obtain a copy of the License at                                                                                                                                             
 *                                                                                                                                                                                 
 * http://www.apache.org/licenses/LICENSE-2.0                                                                                                                                      
 *                                                                                                                                                                                 
 * Unless required by applicable law or agreed to in writing, software                                                                                                             
 * distributed under the License is distributed on an "AS IS" BASIS,                                                                                                               
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or                                                                                                                 
 * implied. See the License for the specific language governing                                                                                                                    
 * permissions and limitations under the License. See accompanying                                                                                                                 
 * LICENSE file.                                                                                                                                                                   
 */

package com.yahoo.ycsb;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.Enumeration;
import java.util.Vector;

import com.google.gson.Gson;
import com.google.gson.JsonObject;


/**
 * Basic DB that just prints out the requested operations, instead of doing them against a database.
 */
public class CSDDB extends DB
{
	public static final String VERBOSE="basicdb.verbose";
	public static final String VERBOSE_DEFAULT="true";

	public static final String SIMULATE_DELAY="basicdb.simulatedelay";
	public static final String SIMULATE_DELAY_DEFAULT="0";
	public static PrintWriter usertable;


	boolean verbose;
	int todelay;

	//Your class must have a public no-argument constructor,
	//because the instances will be constructed inside a factory which will use the no-argument constructor.
	public CSDDB()
	{
		todelay=0;
	}


	void delay()
	{
		if (todelay>0)
		{
			try
			{
				Thread.sleep((long)Utils.random().nextInt(todelay));
			}
			catch (InterruptedException e)
			{
				//do nothing
			}
		}
	}

	/**
	 * Initialize any state for this DB.
	 * Called once per DB instance; there is one DB instance per client thread.
	 * @throws FileNotFoundException 
	 */
	@SuppressWarnings("unchecked")
	/*The init() method should be used to set up the connection to the database 
	and do any other initialization.
	These properties will be passed to the DB instance after the constructor, 
	so it is important to retrieve them only in the init() method and not the constructor
	 */

	public void init() throws FileNotFoundException
	{
		verbose=Boolean.parseBoolean(getProperties().getProperty(VERBOSE, VERBOSE_DEFAULT));
		todelay=Integer.parseInt(getProperties().getProperty(SIMULATE_DELAY, SIMULATE_DELAY_DEFAULT));

		if (verbose)
		{
			usertable = new PrintWriter("usertable.txt");
			System.out.println("***************** properties *****************");
			Properties p=getProperties();
			if (p!=null)
			{
				for (Enumeration e=p.propertyNames(); e.hasMoreElements(); )
				{
					String k=(String)e.nextElement();
					System.out.println("\""+k+"\"=\""+p.getProperty(k)+"\"");
				}
			}
			System.out.println("**********************************************");
		}
	}

	/**
	 * Read a record from the database. Each field/value pair from the result will be stored in a HashMap.
	 *
	 * @param table The name of the table
	 * @param key The record key of the record to read.
	 * @param fields The list of fields to read, or null for all of them
	 * @param result A HashMap of field/value pairs for the result
	 * @return Zero on success, a non-zero error code on error
	 * @throws IOException 
	 */
	public int read(String table, String key, Set<String> fields, HashMap<String,ByteIterator> result) throws IOException
	{
		delay();

		if (verbose)
		{					
			FileReader fr;BufferedReader br;
			System.out.print("READ "+table+" "+key+" [ ");
			if(key!=null){
				
				fr = new FileReader("usertable");
				br = new BufferedReader(fr);
				String line=br.readLine();//Read one line from the file
				search:while(line !=null){
					if(line.contains("{\""+key+"\"")){  
						
						
						System.out.println("key found");
						if (fields!=null)
						{
							for (String f : fields)
							{
								if(line.contains(f))
								System.out.print("filed found: "+f+" ");
							}
						}
						else
						{
							System.out.print("<all fields>");
						}
						
						
						break search;
					}
					line = br.readLine();
				}			




				br.close();
				fr.close();

			}
			System.out.println("]");
		}

		return 0;
	}




	/**
	 * Insert a record in the database. Any field/value pairs in the specified values HashMap will be written into the record with the specified
	 * record key.
	 *
	 * @param table The name of the table
	 * @param key The record key of the record to insert.
	 * @param values A HashMap of field/value pairs to insert in the record
	 * @return Zero on success, a non-zero error code on error
	 */
	public int insert(String table, String key, HashMap<String,ByteIterator> values)
	{
		delay();

		if (verbose)
		{
			JsonObject json = null; 
			if(key!=null){
				System.out.print("INSERT "+table+"key= "+key);
				if (values!=null)
				{	
					json = new JsonObject();
					for (String k : values.keySet())
						json.addProperty(k, values.get(k).toString());
				}
			}
			
			Gson gson = new Gson();
			gson.toJson(json);
			json.toString();
			usertable.println(json);
		}

	return 0;
}


@Override
public int scan(String table, String startkey, int recordcount,
		Set<String> fields, Vector<HashMap<String, ByteIterator>> result) {
	// TODO Auto-generated method stub
	return 0;
}


@Override
public int update(String table, String key,
		HashMap<String, ByteIterator> values) {
	// TODO Auto-generated method stub
	return 0;
}


@Override
public int delete(String table, String key) {
	// TODO Auto-generated method stub
	return 0;
}


/**
 * Short test of BasicDB
 */
/*
	public static void main(String[] args)
	{
		BasicDB bdb=new BasicDB();

		Properties p=new Properties();
		p.setProperty("Sky","Blue");
		p.setProperty("Ocean","Wet");

		bdb.setProperties(p);

		bdb.init();

		HashMap<String,String> fields=new HashMap<String,String>();
		fields.put("A","X");
		fields.put("B","Y");

		bdb.read("table","key",null,null);
		bdb.insert("table","key",fields);

		fields=new HashMap<String,String>();
		fields.put("C","Z");

		bdb.update("table","key",fields);

		bdb.delete("table","key");
	}*/
}
