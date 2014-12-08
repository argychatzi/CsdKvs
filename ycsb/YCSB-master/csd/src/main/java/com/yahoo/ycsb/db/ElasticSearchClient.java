package com.yahoo.ycsb.db;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Properties;
import java.util.Set;
import java.util.Vector;

import com.kth.csd.networking.ConnectionMetaData;
import com.kth.csd.networking.KvsClient;
import com.kth.csd.networking.KvsClient.YcsbTrafficInputInteraceHolder;
import com.kth.csd.networking.interfaces.external.ClientExternalInputInterface;
import com.kth.csd.node.Constants;
import com.kth.csd.node.operation.KeyValueEntry;
import com.kth.csd.utils.Logger;
import com.yahoo.ycsb.ByteIterator;
import com.yahoo.ycsb.DB;
import com.yahoo.ycsb.DBException;
import com.yahoo.ycsb.StringByteIterator;

public class ElasticSearchClient extends DB implements YcsbTrafficInputInteraceHolder{

	private static final String TAG = ElasticSearchClient.class.getCanonicalName();
	private static final String PROPERTY_KEY_SERVER_IP = "serverIp";

	private KvsClient mKvsClient;

	public void init() throws DBException {
		Logger.d(TAG, "init Called!");
		Properties properties = getProperties();
		properties.list(System.out);

		String serverIp = properties.getProperty(PROPERTY_KEY_SERVER_IP);
//		String serverIp = "10.0.0.2";
		createRequestSenderFromConnectionMetadata(new ConnectionMetaData(serverIp, Constants.DEFAULT_PORT));
	}

	private void createRequestSenderFromConnectionMetadata(ConnectionMetaData connectionMetaData) {
		mKvsClient = new KvsClient(new ClientExternalInputInterface(this), connectionMetaData);
	}

	@Override
	public Properties getProperties() {
		String serverIp = Constants.DEFAULT_HOST;
//		String serverIp = "10.0.0.2";
		try {
			FileReader fileReader = new FileReader(System.getProperty("user.dir") + "/properties/server_ip.txt");
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			serverIp = bufferedReader.readLine();
			bufferedReader.close();
		} catch (IOException e) {
			Logger.d(TAG, "Read from file" + e.toString());
			e.printStackTrace();
		}
		Properties properties = new Properties();
		properties.setProperty(PROPERTY_KEY_SERVER_IP, serverIp);

		return properties;
	}

	@Override
	public void cleanup() throws DBException {
	}

	/**
	 * Delete a record from the database.
	 *
	 * @param table
	 *            The name of the table
	 * @param key
	 *            The record key of the record to delete.
	 * @return Zero on success, a non-zero error code on error
	 */
	@Override
	public int delete(String arg0, String arg1) {
		return 0;
	}

	/**
	 * Insert a record in the database. Any field/value pairs in the specified
	 * values HashMap will be written into the record with the specified record
	 * key.
	 *
	 * @param table
	 *            The name of the table
	 * @param key
	 *            The record key of the record to insert.
	 * @param values
	 *            A HashMap of field/value pairs to insert in the record
	 * @return Zero on success, a non-zero error code on error
	 */
	@Override
	public int insert(String table, String key, HashMap<String, ByteIterator> values) {
		HashMap<String, String> stringHashMap = StringByteIterator.getStringMap(values);
		KeyValueEntry keyValueEntry = new KeyValueEntry(key, stringHashMap);
		return mKvsClient.write(keyValueEntry);
	}

	/**
	 * Read a record from the database. Each field/value pair from the result
	 * will be stored in a HashMap.
	 *
	 * @param table
	 *            The name of the table
	 * @param key
	 *            The record key of the record to read.
	 * @param fields
	 *            The list of fields to read, or null for all of them
	 * @param result
	 *            A HashMap of field/value pairs for the result
	 * @return Zero on success, a non-zero error code on error or "not found".
	 */
	@Override
	public int read(String table, String key, Set<String> fields, HashMap<String, ByteIterator> result) {
		HashMap<String, String> stringHashMap = StringByteIterator.getStringMap(result);
		KeyValueEntry keyValueEntry = new KeyValueEntry(key, stringHashMap);
		return mKvsClient.read(keyValueEntry);
	}

	/**
	 * Perform a range scan for a set of records in the database. Each
	 * field/value pair from the result will be stored in a HashMap.
	 *
	 * @param table
	 *            The name of the table
	 * @param startkey
	 *            The record key of the first record to read.
	 * @param recordcount
	 *            The number of records to read
	 * @param fields
	 *            The list of fields to read, or null for all of them
	 * @param result
	 *            A Vector of HashMaps, where each HashMap is a set field/value
	 *            pairs for one record
	 * @return Zero on success, a non-zero error code on error. See this class's
	 *         description for a discussion of error codes.
	 */
	@Override
	public int scan(String table, String startkey, int recordcount, Set<String> fields, Vector<HashMap<String, ByteIterator>> result) {
		Logger.d(TAG, "performing scan");
		return 0;
	}

	/**
	 * Update a record in the database. Any field/value pairs in the specified
	 * values HashMap will be written into the record with the specified record
	 * key, overwriting any existing values with the same field name.
	 *
	 * @param table
	 *            The name of the table
	 * @param key
	 *            The record key of the record to write.
	 * @param values
	 *            A HashMap of field/value pairs to update in the record
	 * @return Zero on success, a non-zero error code on error. See this class's
	 *         description for a discussion of error codes.
	 */
	@Override
	public int update(String table, String key, HashMap<String, ByteIterator> values) {
		return 0;
	}

	@Override
	public void onMasterNodeMoved(ConnectionMetaData master) {
		createRequestSenderFromConnectionMetadata(master);
	}

}
