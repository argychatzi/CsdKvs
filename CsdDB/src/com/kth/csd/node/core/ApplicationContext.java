package com.kth.csd.node.core;

import java.util.ArrayList;

import com.kth.csd.networking.ConnectionMetaData;
import com.kth.csd.networking.messages.AbstractNetworkMessage;
import com.kth.csd.node.Constants;

public class ApplicationContext {
	//private static boolean isMaster = true;
	private static boolean isMaster = false;
	private static boolean isUpdate = false;
	

	private static NodeFarm mNodeFarm;
	private static ConnectionMetaData mMasterNode;
	private static KeyValueStore mKeyValueStore;
	private static AbstractNetworkMessage slaveNodeStatistics;
	private static boolean isFirstTimeMeasuringRTT=true;
	private static ArrayList<String> ycsbIPs;
	
	
	
	public static ArrayList<String> getYcsbIPs() {
		return ycsbIPs;
	}

	public static void setYcsbIPs(ArrayList<String> ycsbIPs) {
		ApplicationContext.ycsbIPs = ycsbIPs;
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
		return new ConnectionMetaData("192.168.0.5", Constants.INTERNAL_PORT);
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
