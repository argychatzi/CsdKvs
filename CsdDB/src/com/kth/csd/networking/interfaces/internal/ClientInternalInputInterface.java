package com.kth.csd.networking.interfaces.internal;

import java.util.HashMap;

import org.apache.mina.core.service.IoHandler;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;

import com.kth.csd.networking.ConnectionMetaData;
import com.kth.csd.networking.ExecutionResultCommunicator;
import com.kth.csd.networking.messages.AbstractNetworkMessage;
import com.kth.csd.networking.messages.MasterMovedMessage;
import com.kth.csd.networking.messages.StatisticsResultMessage;
import com.kth.csd.node.operation.KeyValueEntry;
import com.kth.csd.node.operation.KvsOperation;
import com.kth.csd.node.core.ApplicationContext;
import com.kth.csd.node.executors.CostFunctionCalculator;
import com.kth.csd.node.executors.MasterSelector;
import com.kth.csd.utils.Logger;

public class ClientInternalInputInterface extends IoHandlerAdapter implements IoHandler, ExecutionResultCommunicator{
	
	private static final String TAG = ClientInternalInputInterface.class.getCanonicalName();
	
	@Override
	public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
		cause.printStackTrace();
	}

	@Override
	public void messageReceived(IoSession session, Object message) throws Exception {
		AbstractNetworkMessage response = (AbstractNetworkMessage) message;
		
		switch(response.getType()){
			case STATISTICS_RES:{		
				HashMap<String, Double> ycsbclientsRttMapFromSlave = ((StatisticsResultMessage)response).getResultsOfDelayMeasurement();	
				String remoteIp = ConnectionMetaData.generateConnectionMetadaForRemoteEntityInSession(session).getHost();
				Logger.d(TAG,"messageReceived STATISTICS_RES from: " + remoteIp + ":::" + ((StatisticsResultMessage)response).toString() );
				Logger.d(TAG,"ycsbclientsRttMapFromSlave"+ycsbclientsRttMapFromSlave);
		
				//TODO Add master RTT to the hashmap
				Logger.d(TAG,"ycsbclientsRttMapFromSlave"+ycsbclientsRttMapFromSlave.toString());
				//Input the RTT, Calculate the cost, and put it into mNodeWithDelayCostMap
				storeDelayStatisticsForNode(ycsbclientsRttMapFromSlave, remoteIp);
				break;
			}
		}
	}
	
	private void storeDelayStatisticsForNode(HashMap<String, Double> ycsbclientsRttMapFromSlave, String slaveIp){		
		//TODO revert!
		double cost=0;
		HashMap<String, Double> throuputMap = ApplicationContext.getmYcsbClientsStatisticsMapPerSecondWithEma();
		if(ycsbclientsRttMapFromSlave!=null && throuputMap!=null){
			cost = CostFunctionCalculator.calculateCostForNode(ycsbclientsRttMapFromSlave,throuputMap);
			ApplicationContext.updateNodeWithDelayCostMap(slaveIp, cost);
			Logger.d(TAG, "storeDelayStatisticsForNode:  "+ slaveIp + " has delay cost "+ cost);
		}	

	}
	
	@Override
	public void excutionFinished(KeyValueEntry entry) {
		// TODO Auto-generated method stub
	}
}