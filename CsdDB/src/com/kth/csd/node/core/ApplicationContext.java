package com.kth.csd.node.core;

import java.util.ArrayList;

import com.kth.csd.networking.ConnectionMetaData;

public class ApplicationContext {

	private static boolean isMaster = false;

	private static ArrayList<ConnectionMetaData> mNodes;
	private static ConnectionMetaData mMasterNode;
	private static KeyValueStore mKeyValueStore;

	public static boolean isMaster() {
		return isMaster;
	}

	public static ArrayList<ConnectionMetaData> getNodes() {
		return mNodes;
	}

	public static ConnectionMetaData getMasterNodeConnectionMetaData() {
//		return mMasterNode;
		return new ConnectionMetaData("127.1.2.3", 11222);
	}

	public static KeyValueStore getKeyValueStore() {
		return KeyValueStore.getInstance();
	}
}
