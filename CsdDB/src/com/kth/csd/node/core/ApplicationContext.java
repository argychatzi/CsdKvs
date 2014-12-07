package com.kth.csd.node.core;

import java.util.ArrayList;

import com.kth.csd.networking.ConnectionMetaData;

public class ApplicationContext {

	private static boolean isMaster = false;
	private static boolean isUpdate = false;
	

	private static NodeFarm mNodeFarm;
	private static ConnectionMetaData mMasterNode;
	private static KeyValueStore mKeyValueStore;
	
	// update for slave case
	public static boolean isUpdate(){
		return isUpdate;
	}
	// update for isMaster
	public static void setIsMasterTrue(){
		isMaster = true;
	}
	
	public static void setUpdateTrue(){
		isUpdate = true;
	}

	public static boolean isMaster() {
		return isMaster;
	}

	public static NodeFarm getNodes() {
		return mNodeFarm;
	}
	
	public static void setMasterNode(ConnectionMetaData connectionMetaData){
		mMasterNode = connectionMetaData;
	}

	public static ConnectionMetaData getMasterNode() {
		// for now it is hard coded
		return new ConnectionMetaData("10.0.8.4", 47448);
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
