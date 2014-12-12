package com.kth.csd.networking.interfaces.internal;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;

import com.kth.csd.node.Constants;
import com.kth.csd.node.core.DelayMeasurement;
import com.kth.csd.networking.ConnectionMetaData;
import com.kth.csd.networking.KvsClient;
import com.kth.csd.networking.messages.MasterMovedMessage;
import com.kth.csd.networking.messages.AbstractNetworkMessage;
import com.kth.csd.networking.messages.OperationReadMessage;
import com.kth.csd.networking.messages.OperationWriteMessage;
import com.kth.csd.networking.messages.StatisticsRequestMessage;
import com.kth.csd.networking.messages.StatisticsResultMessage;
import com.kth.csd.node.core.ApplicationContext;
import com.kth.csd.node.core.ExponentialMovingAverageExpanded;
import com.kth.csd.node.core.NodeFarm;
import com.kth.csd.node.executors.KvsReader;
import com.kth.csd.node.executors.KvsWriter;
import com.kth.csd.node.operation.KeyValueEntry;
import com.kth.csd.node.operation.KvsOperation;
import com.kth.csd.utils.Logger;

public class ServerInternalInputInterface extends IoHandlerAdapter{
	
	private static final String TAG = "ServerConnectionHandler";
	private final static double alpha = 0.9;
	private  ExponentialMovingAverageExpanded emaStateObj;
	private boolean isFirstTime  = true;
	private static HashMap<String, Double> EMAResults;
	private HashMap<KvsOperation, IoSession> mSessionVault;
	
	@Override
	public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
		cause.printStackTrace();
	}

	@Override
	public void messageReceived(IoSession session, Object message) throws Exception {
		System.out.println("ServerInternal msg received!!!");
		AbstractNetworkMessage response = (AbstractNetworkMessage) message;
		
		switch(response.getType()){
		/*
		+ * In case of statistics req the list of client IPs is captured and then only one time we create EMA object to keep the state
		+ * of EMA, we couldn't initialize this object above as it needs the list of clients IPs.
		+ * Then we do the calculations and save it/
		+ * In case of statistics res, we pass the results to the application context.
		+ */
			case STATISTICS_REQ:{
				System.out.println("STATISTICS_REQ: ServerInternal msg received!!!");
				ArrayList<String> incomingListOfYcsbClients = ((StatisticsRequestMessage)response).getListOfYcsbClients();
				ApplicationContext.setYcsbIPs(incomingListOfYcsbClients);
				Logger.d(TAG, "incomingListOfYcsbClients =" + Arrays.toString(incomingListOfYcsbClients.toArray()));
				if (ApplicationContext.getIsFirstTimeMeasuringRTT()){
					Logger.d(TAG, "first time receiving STATISTICS_REQ");
					ApplicationContext.setFirstTimeMeasuringRTT(false);
					emaStateObj = new ExponentialMovingAverageExpanded(alpha, incomingListOfYcsbClients);
					//Logger.d(TAG, "emaStateObj" + emaStateObj.toString());
					Logger.d(TAG, "getIsFirstTimeMeasuringRTT" + ApplicationContext.getIsFirstTimeMeasuringRTT());
				}
				Logger.d(TAG, "Starting delay measurement calculations");
				DelayMeasurement.CalculateDelayFromSlaveToClientNode(incomingListOfYcsbClients);
			    //EMAResults = emaStateObj.calculatExponentialMovingAverage(DelayMeasurement.getDelayResultsHashmap());
				HashMap<String, Double> delayResultsHashmap = DelayMeasurement.getDelayResultsHashmap();
				
				AbstractNetworkMessage statisticsResults = new StatisticsResultMessage (delayResultsHashmap);
//				ApplicationContext.statisticsResultstoMaster(statisticsResults);
				
				ArrayList<ConnectionMetaData> nodeIps =new ArrayList<ConnectionMetaData>();
				nodeIps.add(ApplicationContext.getMasterInternalConnection());
				NodeFarm nodeFarmToMaster = new NodeFarm(nodeIps);
				nodeFarmToMaster.broadCast(statisticsResults);
				break;
			}
			case STATISTICS_RES:{
				System.out.println("STATISTICS_RES: ServerInternal msg received!!!");	
				StatisticsResultMessage statisticsResults = (StatisticsResultMessage)response;
				Logger.d(TAG, "is statisticsResults empty ? "+String.valueOf(statisticsResults==null));
				HashMap<String, Double> results = statisticsResults.getResultsOfDelayMeasurement();
				Logger.d(TAG, "is results empty ?  "+String.valueOf(results==null));
				Logger.d(TAG, "results"+ results);
				ArrayList<String> ycsbIPs = ApplicationContext.getYcsbIPs();
				Logger.d(TAG, "ycsbIPs"+ ycsbIPs);
				for(String Key : ycsbIPs)
					Logger.d(TAG, String.valueOf(results.get(Key)));
//				Logger.d(TAG, String.valueOf(results.get("192.168.0.1")));
//				Logger.d(TAG, String.valueOf(results.get("192.168.0.6")));
//				Logger.d(TAG, String.valueOf(results.get("192.168.0.9")));
				//AbstractNetworkMessage statisticsResults = new StatisticsResultMessage (EMAResults);
				ApplicationContext.statisticsResultstoMaster(statisticsResults);
				break;
			}
			case MASTER_MOVED:{
				ConnectionMetaData newMasterInternal = ((MasterMovedMessage)message).getNewMasterInternal();
				ConnectionMetaData newMasterExternal = ((MasterMovedMessage)message).getNewMasterExternal();
				
				ApplicationContext.setMasterExternalConnection(newMasterExternal);
				ApplicationContext.setMasterInternalConnection(newMasterInternal);
				break;
			}
			case OPERATION_READ:{
				System.out.println("OPERATION_READ: ServerInternal msg received!!!");
				KeyValueEntry keyValueEntry = ((OperationWriteMessage)message).getKeyValueEntry();
				new KvsReader(keyValueEntry).execute();
				break;
			}
			case OPERATION_WRITE:{
				//TODO #Jawad, Mihret you should get the ip of the current communication from the Session object
				//that you get from the arguments locally, and not by relying on some remote static function
				
				// check if write is coming from Master Node
//				String currentSessionIp = ClientInternalInputInterface.getSessionIp(session);
//				int currentSessionPort = ClientInternalInputInterface.getPort(session);
//				
//				//ConnectionMetaData currentSessionMetaData = new ConnectionMetaData(currentSessionIp, currentSessionPort);
//				
//				//TODO #Jawad, Mihret you should not break twice. The keyword "break" is supposed to close a
//				// block that falls under the same "case" category.  
//				if (ApplicationContext.getMaster().getHost() == currentSessionIp && 
//						ApplicationContext.getMaster().getPort() == currentSessionPort){
//					
//				KeyValueEntry keyValueEntry = ((OperationReadMessage)message).getKeyValueEntry();
//				ApplicationContext.setUpdateTrue();
//				new KvsWriter(keyValueEntry).execute();
//				 break;
//				
//			}
//				else {
//	
				break;
			}
		}
	}
}


