package com.kth.csd.node.executors;

import java.util.HashMap;

import com.kth.csd.networking.ConnectionMetaData;
import com.kth.csd.node.operation.KeyValueEntry;

public class OperationAnalyzer {

	private static final String MASTER_MOVED_KEY = "MASTER_MOVED_KEY";
	private static final String MASTER_MOVED_HOST_IP_KEY = "MASTER_MOVED_HOST_IP_KEY";
	private static final String MASTER_MOVED_HOST_PORT_KEY = "MASTER_MOVED_HOST_PORT_KEY";
	
	public static boolean hasMasterMoved(KeyValueEntry entry){
		return Boolean.parseBoolean(entry.getValues().get(MASTER_MOVED_KEY));
	}
	
	public static ConnectionMetaData extractMasterConnection(KeyValueEntry entry) {
		String masterIp   = entry.getValues().get(MASTER_MOVED_HOST_IP_KEY);
		String masterPort = entry.getValues().get(MASTER_MOVED_HOST_PORT_KEY);
		return new ConnectionMetaData (masterIp, Integer.parseInt(masterPort));
	}

	public static HashMap generateMessageMoved(ConnectionMetaData masterNodeConnectionMetaData) {
		HashMap<String,String> values = new HashMap<String, String>();
		values.put(MASTER_MOVED_KEY, "true");
		values.put(MASTER_MOVED_HOST_IP_KEY, masterNodeConnectionMetaData.getHost());
		values.put(MASTER_MOVED_HOST_PORT_KEY, String.valueOf(masterNodeConnectionMetaData.getPort()));
		return values;
	}
}
