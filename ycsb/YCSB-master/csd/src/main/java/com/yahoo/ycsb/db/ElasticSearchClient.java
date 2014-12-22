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

		createKvsClientFromConnectionMetadata(new ConnectionMetaData(serverIp, Constants.DEFAULT_EXTERNAL_PORT));
	}

	private void createKvsClientFromConnectionMetadata(ConnectionMetaData connectionMetaData) {
		mKvsClient = new KvsClient(new ClientExternalInputInterface(this), connectionMetaData);
	}

	@Override
	public Properties getProperties() {
//		String serverIp = Constants.MASTER_IP;
//		String serverIp = "10.0.0.2";
//		try {
//			FileReader fileReader = new FileReader(System.getProperty("user.dir") + "/properties/server_ip.txt");
//			BufferedReader bufferedReader = new BufferedReader(fileReader);
//			serverIp = bufferedReader.readLine();
//			bufferedReader.close();
//		} catch (IOException e) {
//			Logger.d(TAG, "Read from file" + e.toString());
//			e.printStackTrace();
//		}

		String serverIp = Constants.DEFAULT_HOST;
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
	public int scan(String arg0, String arg1, int arg2, Set<String> arg3, Vector<HashMap<String, ByteIterator>> arg4) {
		throw new RuntimeException("scan operation is not supported");
	}

	public int delete(String arg0, String arg1) {
		throw new RuntimeException("delete operation is not supported");
	}

	@Override
	public int insert(String table, String key, HashMap<String, ByteIterator> values) {
		HashMap<String, String> stringHashMap = StringByteIterator.getStringMap(values);
		KeyValueEntry keyValueEntry = new KeyValueEntry(key, stringHashMap);
		return mKvsClient.write(keyValueEntry);
	}

	@Override
	public int read(String table, String key, Set<String> fields, HashMap<String, ByteIterator> result) {
		HashMap<String, String> stringHashMap = StringByteIterator.getStringMap(result);
		KeyValueEntry keyValueEntry = new KeyValueEntry(key, stringHashMap);
		return mKvsClient.read(keyValueEntry);
	}

	@Override
	public int update(String table, String key, HashMap<String, ByteIterator> values) {
		HashMap<String, String> stringHashMap = StringByteIterator.getStringMap(values);
		KeyValueEntry keyValueEntry = new KeyValueEntry(key, stringHashMap);
		return mKvsClient.write(keyValueEntry);
	}

	@Override
	public void onMasterNodeMoved(ConnectionMetaData master) {
		createKvsClientFromConnectionMetadata(master);
	}

}
