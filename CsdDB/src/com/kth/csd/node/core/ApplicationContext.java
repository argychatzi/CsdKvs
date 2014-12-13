package com.kth.csd.node.core;

import java.util.ArrayList;
import java.util.HashMap;

import com.kth.csd.networking.ConnectionMetaData;
import com.kth.csd.networking.messages.AbstractNetworkMessage;
import com.kth.csd.networking.messages.MasterMovedMessage;
import com.kth.csd.utils.Logger;

public class ApplicationContext {

	protected static final String TAG = KvsNode.class.getCanonicalName();

	//private static boolean isMaster = true;
	private static boolean isMaster = false;
	private static boolean isUpdate = false;


	private static NodeFarm mNodeFarm;
	private static KeyValueStore mKeyValueStore;
	private static AbstractNetworkMessage slaveNodeStatistics;
	private static HashMap<String, Double>mNodeWithDelayCostMap;
	private static boolean isFirstTimeMeasuringRTT=true;
	private static ArrayList<String> ycsbIPs;

	private static ConnectionMetaData mInternalConnection;
	private static ConnectionMetaData mExternalConnection;

	private static ConnectionMetaData mMasterExternalConnection;
	private static ConnectionMetaData mMasterInternalConnection;

	public static ArrayList<String> getYcsbIPs() {
		return ycsbIPs;
	}

	public static void addIpToYcsbIPs(String oneYcsbIP) {
		if(ycsbIPs==null){
			ycsbIPs = new ArrayList<String> ();
		}
		if(!ApplicationContext.ycsbIPs.contains(oneYcsbIP)){
			Logger.d(TAG, "adding IP"+oneYcsbIP);
			ApplicationContext.ycsbIPs.add(oneYcsbIP);	
		}
		
	}
	public static void updateNodeWithDelayCostMap(String nodeIp, double nodeDelayCost){
		mNodeWithDelayCostMap.put(nodeIp, nodeDelayCost);
	}
	public static HashMap<String, Double> getNodeWithDelayCostMap(){
		return mNodeWithDelayCostMap;
	}

	public static boolean getIsFirstTimeMeasuringRTT() {
		return isFirstTimeMeasuringRTT;
	}

	public static void setFirstTimeMeasuringRTT(boolean isFirstTimeMeasuringRTT) {
		ApplicationContext.isFirstTimeMeasuringRTT =isFirstTimeMeasuringRTT;
	}

	public static AbstractNetworkMessage  statisticsResultstoMaster(AbstractNetworkMessage statisticsResults) {
		return slaveNodeStatistics = statisticsResults;
	}

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

	public static NodeFarm getNodeFarm() {
		return mNodeFarm;
	}

	public static KeyValueStore getKeyValueStore() {
		return KeyValueStore.getInstance();
	}

	public static boolean connectionMetadatBelongsToMasterExternal(ConnectionMetaData connectionMetaData){
		return connectionMetaData.equals(mExternalConnection);
	}

	public static boolean connectionMetadatBelongsToMasterInternal(ConnectionMetaData connectionMetaData){
		return connectionMetaData.equals(mInternalConnection);
	}

	public static void generateNodeFarm(ArrayList<ConnectionMetaData> nodeIps) {
		Logger.d(TAG, "generateNodeFarm " + nodeIps.toString());
		mNodeFarm = new NodeFarm(nodeIps);
	}
	
	public static void assignNewMaster(ConnectionMetaData internal, ConnectionMetaData external){
		setMasterInternalConnection(internal);
		setMasterExternalConnection(external);
		
		MasterMovedMessage masterMovedMessage = new MasterMovedMessage(internal, external);
		ApplicationContext.getNodeFarm().broadCast(masterMovedMessage);
	}
	

	public static ConnectionMetaData getOwnInternalConnection() {
		return mInternalConnection;
	}

	public static ConnectionMetaData getOwnExternalConnection() {
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
