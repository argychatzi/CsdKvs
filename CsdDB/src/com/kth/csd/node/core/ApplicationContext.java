package com.kth.csd.node.core;

import java.util.ArrayList;

import com.kth.csd.networking.ConnectionMetaData;

public class ApplicationContext {

	private static boolean isMaster = false;

	private static NodeFarm mNodeFarm;
	private static ConnectionMetaData mMasterNode;
	private static KeyValueStore mKeyValueStore;

	public static boolean isMaster() {
		return isMaster;
	}

	public NodeFarm getNodes() {
		return mNodeFarm;
	}
	
	public static void setMasterNode(ConnectionMetaData connectionMetaData){
		mMasterNode = connectionMetaData;
	}

	public static ConnectionMetaData getMasterNode() {
		return new ConnectionMetaData("127.1.2.3", 11222);
	}

	public static KeyValueStore getKeyValueStore() {
		return KeyValueStore.getInstance();
	}

	public static void generatNodeFarm(ArrayList<ConnectionMetaData> nodeIps) {
		mNodeFarm = new NodeFarm(nodeIps);
	}
	
	public static void updateMaster(ConnectionMetaData masterConnectionMetadata) {
		mMasterNode = masterConnectionMetadata;
	}
}
