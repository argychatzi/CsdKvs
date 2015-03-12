package com.kth.csd.node.core;

import java.util.ArrayList;
import java.util.HashMap;

import com.codahale.metrics.EWMA;
import com.kth.csd.networking.ConnectionMetaData;
import com.kth.csd.networking.messages.AbstractNetworkMessage;
import com.kth.csd.networking.messages.MasterMovedMessage;
import com.kth.csd.networking.messages.StatisticsRequestMessage;
import com.kth.csd.utils.Logger;

public class ApplicationContext {

	protected static final String TAG = ApplicationContext.class.getCanonicalName();

	private static NodeFarm mNodeFarm;
	private static HashMap<String, Double>mNodeWithDelayCostMap;
	private static ArrayList<String> ipsOfWritingYcsbClients;

	private static ConnectionMetaData mInternalConnection;
	private static ConnectionMetaData mExternalConnection;

	private static ConnectionMetaData mMasterExternalConnection;
	private static ConnectionMetaData mMasterInternalConnection;
	
	private static HashMap <String, Integer> mYcsbClientsStatisticsMapSoFar = new HashMap<String, Integer>();
	public static HashMap <String, Integer> mYcsbClientsStatisticsMapPerSecond = new HashMap<String, Integer>();
	public static HashMap <String, Double> mYcsbClientsStatisticsMapPerSecondWithEma = new HashMap <String, Double>();
	
	public static HashMap <String, EWMA> ewmaKeeper;
	public static HashMap<String,Double> masterOwnDelay;
	
	//TODO Ahmed remove
	private static boolean isFirstTimeMeasuringRTT;

	public static ArrayList<String> getYcsbWritingIPs() {
		if (ipsOfWritingYcsbClients == null){
			ipsOfWritingYcsbClients = new ArrayList<String>();
		}
		return ipsOfWritingYcsbClients;
	}

	public static void addIpToYcsbWritingIPs(String oneYcsbIP) {
		
		if(ipsOfWritingYcsbClients == null){
			ipsOfWritingYcsbClients = new ArrayList<String> ();
		}
		if(!ipsOfWritingYcsbClients.contains(oneYcsbIP)){
			Logger.d(TAG, "addIpToYcsbWritingIPs"+oneYcsbIP);
			ipsOfWritingYcsbClients.add(oneYcsbIP);	
		}
	}
	
	public static void updateNodeWithDelayCostMap(String nodeIp, double nodeDelayCost){
		if (mNodeWithDelayCostMap == null){
			mNodeWithDelayCostMap = new HashMap<String, Double>();
		}
		mNodeWithDelayCostMap.put(nodeIp, nodeDelayCost);
	}
	
	public static HashMap<String, Double> getNodeWithDelayCostMap(){
		return mNodeWithDelayCostMap;
	}

	//TODO Ahmed remove
	public static boolean getIsFirstTimeMeasuringRTT() {
		return isFirstTimeMeasuringRTT;
	}

	public static void setFirstTimeMeasuringRTT(boolean isFirstTimeMeasuringRTT) {
		ApplicationContext.isFirstTimeMeasuringRTT =isFirstTimeMeasuringRTT;
	}

//	public static AbstractNetworkMessage  statisticsResultstoMaster(AbstractNetworkMessage statisticsResults) {
//		return slaveNodeStatistics = statisticsResults;
//	}
       
	public static HashMap<String, Integer> getmYcsbClientsStatisticsMapSoFar() {
//		if(mYcsbClientsStatisticsMapPerSecond == null){
//			mYcsbClientsStatisticsMapPerSecond = new HashMap<String, Integer>();
//		}
		return mYcsbClientsStatisticsMapSoFar;
	}

	public static void updatemYcsbClientsStatisticsMapSoFar(String clientIP, int writesSoFar){
//		if (mYcsbClientsStatisticsMapSoFar == null){
//			mYcsbClientsStatisticsMapSoFar = new HashMap<String, Integer>();
//		}
		mYcsbClientsStatisticsMapSoFar.put(clientIP, writesSoFar);
	}

	public static HashMap<String, Integer> getmYcsbClientsStatisticsMapPerSecond() {
		return mYcsbClientsStatisticsMapPerSecond;
	}
	
	public static void updatemYcsbClientsStatisticsMapPerSecond(String clientIP, int writesPerSec){
		if (mYcsbClientsStatisticsMapPerSecond == null){
			mYcsbClientsStatisticsMapPerSecond = new HashMap<String, Integer>();
		}
		mYcsbClientsStatisticsMapPerSecond.put(clientIP, writesPerSec);
	}

	public static HashMap<String, Double> getmYcsbClientsStatisticsMapPerSecondWithEma() {
		return mYcsbClientsStatisticsMapPerSecondWithEma;
	}

	public static void updatemYcsbClientsStatisticsMapPerSecondWithEma(String clientIP, double writesPerSecWithEma){
		if (mYcsbClientsStatisticsMapPerSecondWithEma == null){
			mYcsbClientsStatisticsMapPerSecondWithEma = new HashMap<String, Double>();
		}
		mYcsbClientsStatisticsMapPerSecondWithEma.put(clientIP, writesPerSecWithEma);
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
	
	/*public static boolean connectionMetadatBelongsToMasterExternal(ConnectionMetaData connectionMetaData){
		//return connectionMetaData.equals(mExternalConnection);
		return connectionMetaData.equals(mMasterExternalConnection);
	}

	public static boolean connectionMetadatBelongsToMasterInternal(ConnectionMetaData connectionMetaData){
		//return connectionMetaData.equals(mInternalConnection);
		return connectionMetaData.equals(mMasterInternalConnection);
	}*/
	public static boolean connectionMetadataIPBelongsToMasterInternal(ConnectionMetaData connectionMetaData){
		return connectionMetaData.getHost().equals(mMasterInternalConnection.getHost());
	}

	public static void generateNodeFarm(ArrayList<ConnectionMetaData> nodeIps) {
		Logger.d(TAG, "generateNodeFarm " + nodeIps.toString());
		mNodeFarm = NodeFarm.getInstance(nodeIps);
	}
	
	public static void assignNewMaster(String newIp){
		mMasterExternalConnection.setHost(newIp);
		mMasterInternalConnection.setHost(newIp);
		
		MasterMovedMessage masterMovedMessage = new MasterMovedMessage(mMasterInternalConnection, mMasterExternalConnection);
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

	public synchronized static void setMasterInternalConnection(ConnectionMetaData internalConnection) {
		Logger.d(TAG, "assignNewMasterInternal " + internalConnection);
		mMasterInternalConnection = internalConnection;
	}

	public synchronized static void setMasterExternalConnection(ConnectionMetaData externalConnection) {
		Logger.d(TAG, "assignNewMasterInternal " + externalConnection);
		mMasterExternalConnection = externalConnection;
	}
}
