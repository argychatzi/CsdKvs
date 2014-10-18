package com.yahoo.ycsb.db;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Properties;
import java.util.Set;
import java.util.Vector;

import com.google.gson.Gson;
import com.kth.csd.networking.RequestSender;
import com.kth.csd.node.Constants;
import com.kth.csd.node.operation.KeyValueEntry;
import com.kth.csd.node.operation.KvsOperation;
import com.kth.csd.node.operation.KvsOperation.YCSB_OPERATION;
import com.kth.csd.utils.Logger;
import com.yahoo.ycsb.ByteIterator;
import com.yahoo.ycsb.DB;
import com.yahoo.ycsb.DBException;
import com.yahoo.ycsb.StringByteIterator;


public class ElasticSearchClient extends DB{

	private static final String TAG = ElasticSearchClient.class.getCanonicalName();
	private static final String PROPERTY_KEY_SERVER_IP = "serverIp";

	private RequestSender mRequestSender;
	private Gson mGson;
	
	public void init() throws DBException{
		Logger.d(TAG, "init Called!");
		Properties properties = getProperties();
		properties.list(System.out);
		
		String serverIp = properties.getProperty(PROPERTY_KEY_SERVER_IP);
		mRequestSender = new RequestSender(serverIp);
		mGson = new Gson();
	}
	
	@Override
	public Properties getProperties() {
		String serverIp = Constants.DEFAULT_HOST;
		
		try{
			FileReader fileReader = new FileReader( System.getProperty("user.dir") + "/properties/server_ip.txt" );
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			serverIp = bufferedReader.readLine();
			bufferedReader.close();			
		} catch(IOException e){
			Logger.d(TAG, "Read from file" + e.toString());
			e.printStackTrace();
		}
		Properties properties = new Properties();
		properties.setProperty(PROPERTY_KEY_SERVER_IP, serverIp);
		
		return properties;
	}
	
	 @Override
	    public void cleanup() throws DBException {
		 Logger.d(TAG, "cleanup Called!");
	    }
	 
	 /**
	 * Delete a record from the database. 
	 *
	 * @param table The name of the table
	 * @param key The record key of the record to delete.
	 * @return Zero on success, a non-zero error code on error
	 */
	@Override
	public int delete(String arg0, String arg1) {
		Logger.d(TAG, "performing delete");
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
	@Override
	public int insert(String table, String key, HashMap<String, ByteIterator> values) {
		
		HashMap<String, String> stringHashMap = StringByteIterator.getStringMap(values);
		KeyValueEntry keyValueEntry = new KeyValueEntry( key, stringHashMap);
		KvsOperation operation = new KvsOperation(YCSB_OPERATION.READ,keyValueEntry);

		String operationAsJson = mGson.toJson(operation);
		return mRequestSender.sendRequest(operationAsJson);
	}


    /**
     * Read a record from the database. Each field/value pair from the result will be stored in a HashMap.
     *
     * @param table The name of the table
     * @param key The record key of the record to read.
     * @param fields The list of fields to read, or null for all of them
     * @param result A HashMap of field/value pairs for the result
     * @return Zero on success, a non-zero error code on error or "not found".
     */
	@Override
	public int read(String table, String key, Set<String> fields, HashMap<String, ByteIterator> result) {
		
		HashMap<String, String> stringHashMap = StringByteIterator.getStringMap(result);
		KeyValueEntry keyValueEntry = new KeyValueEntry( key, stringHashMap);
		KvsOperation operation = new KvsOperation(YCSB_OPERATION.READ,keyValueEntry);
		
		String operationAsJson = mGson.toJson(operation);
		return mRequestSender.sendRequest(operationAsJson);
	}

	 /**
     * Perform a range scan for a set of records in the database. Each field/value pair from the result will be stored in a HashMap.
     *
     * @param table The name of the table
     * @param startkey The record key of the first record to read.
     * @param recordcount The number of records to read
     * @param fields The list of fields to read, or null for all of them
     * @param result A Vector of HashMaps, where each HashMap is a set field/value pairs for one record
     * @return Zero on success, a non-zero error code on error. See this class's description for a discussion of error codes.
     */
	@Override
	public int scan(String table, String startkey, int recordcount, Set<String> fields,
			Vector<HashMap<String, ByteIterator>> result) {
		Logger.d(TAG, "performing scan");
		return 0;
	}

	/**
     * Update a record in the database. Any field/value pairs in the specified values HashMap will be written into the record with the specified
     * record key, overwriting any existing values with the same field name.
     *
     * @param table The name of the table
     * @param key The record key of the record to write.
     * @param values A HashMap of field/value pairs to update in the record
     * @return Zero on success, a non-zero error code on error. See this class's description for a discussion of error codes.
     */
	@Override
	public int update(String table, String key, HashMap<String, ByteIterator> values) {
		Logger.d(TAG, "performing update");
		return 0;
	}

}
