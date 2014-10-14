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
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Properties;
import java.util.Set;
import java.util.Vector;

import com.google.gson.Gson;


/**
 * Basic DB that just prints out the requested operations, instead of doing them against a database.
 */
public class CsdDB extends DB{
	
	public static final int RESULT_CODE_SUCCESS = 0;
	public static final int RESULT_CODE_FAILURE = -1;
	private static final String DATABASE_FILE = "db.txt";
	
	private File databaseFile;
	private Gson gson;
	
	/**
	 * Initialize any state for this DB.
	 * Called once per DB instance; there is one DB instance per client thread.
	 * @throws FileNotFoundException 
	 */
	/*The init() method should be used to set up the connection to the database 
	and do any other initialization.
	These properties will be passed to the DB instance after the constructor, 
	so it is important to retrieve them only in the init() method and not the constructor
	 */
	@Override
	public void init() throws FileNotFoundException{
		databaseFile = new File( DATABASE_FILE );
		gson = new Gson();
		
		System.out.println("***************** Properties *****************");

		Properties p = getProperties();
		if( p != null ){
			for( Enumeration e = p.propertyNames(); e.hasMoreElements(); ){
				String k=(String)e.nextElement();
				System.out.println("\""+k+"\"=\""+p.getProperty(k)+"\"");
			}
		}
		System.out.println("**********************************************");
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
	@Override
	public int read(String table, String key, Set<String> fields, HashMap<String,ByteIterator> result) throws IOException{
		int resultCode = RESULT_CODE_FAILURE;
		
		if(key!=null){
			FileReader fileReader = new FileReader( databaseFile );
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			
			System.out.println("READ: " + key);
			
			for( String line = bufferedReader.readLine(); line != null; line = bufferedReader.readLine() ){
				KeyValueEntry entry = gson.fromJson(line, KeyValueEntry.class);

				if(entry.getKey().equals(key)){
					System.out.println("Found key:" + key + " value:" + entry.getValues());
					resultCode = RESULT_CODE_SUCCESS;
					break;
				}
			}
			
			bufferedReader.close();
			fileReader.close();
		}
	
		return resultCode;
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
	@Override
	public int insert(String table, String key, HashMap<String,ByteIterator> values) throws IOException{
		int resultCode = RESULT_CODE_FAILURE;
		
		if(key!=null){
//		if(key!=null && values != null){
			
			System.out.println("INSERT key = "+key);

			FileWriter fileWriter = new FileWriter(databaseFile, true);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

            KeyValueEntry entry = new KeyValueEntry(key, values);

            bufferedWriter.write( gson.toJson(entry) );
            bufferedWriter.newLine();

            bufferedWriter.close();
            fileWriter.close();
			
			resultCode = RESULT_CODE_SUCCESS;
		}
		
		return resultCode;
	}


	@Override
	public int scan(String table, String startkey, int recordcount, Set<String> fields, Vector<HashMap<String, ByteIterator>> result) {
		// Do nothing
		return RESULT_CODE_FAILURE;
	}
	
	
	@Override
	public int update(String table, String key, HashMap<String, ByteIterator> values) {
		// Do nothing
		return RESULT_CODE_FAILURE;
	}
	
	
	@Override
	public int delete(String table, String key) {
		// Do nothing
		return RESULT_CODE_FAILURE;
	}
	
	
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
