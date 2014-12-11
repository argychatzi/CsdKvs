package com.kth.csd.node.core;

import java.util.ArrayList;

import com.kth.csd.networking.ConnectionMetaData;

public class ApplicationContext {

	private static boolean isMaster = false;
	private static boolean isUpdate = false;
	

	private static NodeFarm mNodeFarm;
	private static KeyValueStore mKeyValueStore;
	
	private static ConnectionMetaData mInternalConnection;
	private static ConnectionMetaData mExternalConnection;
	
	private static ConnectionMetaData mMasterExternalConnection;
	private static ConnectionMetaData mMasterInternalConnection;
	
	// update for if write is from master
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
		return mMasterExternalConnection.equals(mExternalConnection) && mMasterInternalConnection.equals(mInternalConnection);
	}

	public static NodeFarm getNodes() {
		return mNodeFarm;
	}
	
	public static KeyValueStore getKeyValueStore() {
		return KeyValueStore.getInstance();
	}

	public static void generateNodeFarm(ArrayList<ConnectionMetaData> nodeIps) {
		mNodeFarm = new NodeFarm(nodeIps);
	}
	
	public static ConnectionMetaData getInternalConnection() {
		return mInternalConnection;
	}
	
	public static ConnectionMetaData getExternalConnection() {
		return mExternalConnection;
	}
	
	public static ConnectionMetaData getMasterExternalConnection() {
		return mMasterExternalConnection;
	}
	
	public static ConnectionMetaData getMasterInternalConnection() {
		return mMasterInternalConnection;
	}
	
	public static void setOwnInternalConnection(ConnectionMetaData internalConnection) {
		mInternalConnection = internalConnection;
	}
	
	public static void setOwnExternalConnection(ConnectionMetaData externalConnection) {
		mExternalConnection = externalConnection;
	}
	
	public static void setMasterInternalConnection(ConnectionMetaData internalConnection) {
		mMasterInternalConnection = internalConnection;
	}
	
	public static void setMasterExternalConnection(ConnectionMetaData externalConnection) {
		mMasterExternalConnection = externalConnection;
	}
	
}
