package com.kth.csd.networking.interfaces.internal;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;

import com.codahale.metrics.EWMA;
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
import com.kth.csd.node.core.NodeFarm;
import com.kth.csd.node.executors.KvsReader;
import com.kth.csd.node.executors.KvsWriter;
import com.kth.csd.node.executors.MasterSelector;
import com.kth.csd.node.executors.StatisticsCollector;
import com.kth.csd.node.operation.KeyValueEntry;
import com.kth.csd.node.operation.KvsOperation;
import com.kth.csd.utils.Logger;

public class ServerInternalInputInterface extends IoHandlerAdapter{
	
	private static final String TAG = ServerInternalInputInterface.class.getCanonicalName();
	
	
	@Override
	public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
		cause.printStackTrace();
	}

	@Override
	public void messageReceived(IoSession session, Object message) throws Exception {
		AbstractNetworkMessage response = (AbstractNetworkMessage) message;
		switch(response.getType()){
			case STATISTICS_REQ:{
				Logger.d(TAG,"messageReceived STATISTICS_REQ : "+((StatisticsRequestMessage)response).toString());
			
				ArrayList<String> listOfYcsbClients = ((StatisticsRequestMessage)response).getListOfYcsbClients();
				if(listOfYcsbClients != null){
					Logger.d(TAG, "list of clients::  " + listOfYcsbClients);
					
					//TODO#Ahmed call your class (the one that executes the ping command and collects the result)
					if(ApplicationContext.getIsFirstTimeMeasuringRTT()){
						ApplicationContext.setFirstTimeMeasuringRTT(false);
						ApplicationContext.ewmaKeeper = new HashMap <String, EWMA> ();
					}
					DelayMeasurement measurement = new DelayMeasurement();
					HashMap<String,Double> rawDelay = measurement.pingAndGetDelay(listOfYcsbClients);	
					Logger.d(TAG,"rawDelay"+ rawDelay.toString());
					HashMap<String,Double> processedDelay = measurement.getProcessedDelay(rawDelay);
					Logger.d(TAG,"processedDelay"+ processedDelay.toString());
//					HashMap<String,Double> delayResultsHashmap = mockLinkDelays(listOfYcsbClients);
					StatisticsResultMessage statisticsResults = new StatisticsResultMessage (processedDelay);
					session.write(statisticsResults);
				}
				break;
			}
			case MASTER_MOVED:{
				Logger.d(TAG,"messageReceived MASTER_MOVED: "+((MasterMovedMessage)response).toString());

				ConnectionMetaData newMasterInternal = ((MasterMovedMessage)message).getNewMasterInternal();
				ConnectionMetaData newMasterExternal = ((MasterMovedMessage)message).getNewMasterExternal();
				
				ApplicationContext.setMasterExternalConnection(newMasterExternal);
				ApplicationContext.setMasterInternalConnection(newMasterInternal);
				break;
			}
			case OPERATION_READ:{
				Logger.d(TAG,"messageReceived OPERATION_READ: "+((OperationReadMessage)response).toString());
				KeyValueEntry keyValueEntry = ((OperationReadMessage)message).getKeyValueEntry();
				new KvsReader(keyValueEntry).execute();
				break;
			}
			case OPERATION_WRITE:{
				//Logger.d(TAG,"messageReceived OPERATION_WRITE: "+((OperationWriteMessage)response).toString());
				ConnectionMetaData connectionMetaData = ConnectionMetaData.generateConnectionMetadaForRemoteEntityInSession(session);
				if(ApplicationContext.connectionMetadatBelongsToMasterInternal(connectionMetaData)){
					KeyValueEntry keyValueEntry = ((OperationWriteMessage)message).getKeyValueEntry();
					new KvsWriter(keyValueEntry).execute();
				}
				break;
			}
		}
	}

//	private HashMap<String, Double> mockLinkDelays(ArrayList<String> listOfYcsbClients) {
//		HashMap<String, Double> result = new HashMap<String, Double>();
//		for(int i=0; i < listOfYcsbClients.size(); i++){
//			if( i > 1){
//				result.put(listOfYcsbClients.get(i), i*0.1);
//			} else {
//				result.put(listOfYcsbClients.get(i), 10.00);
//			}
//		}
//		Logger.d(TAG, "mockLinkDelays -result" + result);
//		return result;
//	}
}


