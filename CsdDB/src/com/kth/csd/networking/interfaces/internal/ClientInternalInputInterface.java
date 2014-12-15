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
import com.kth.csd.node.core.NewMasterSelector;
import com.kth.csd.utils.Logger;

public class ClientInternalInputInterface extends IoHandlerAdapter implements IoHandler, ExecutionResultCommunicator{
	
	private static final String TAG = ClientInternalInputInterface.class.getCanonicalName();
	private HashMap<KvsOperation, IoSession> mSessionVault;
	private HashMap<String, Double> ycsbclientsRttMapFromSlave;
	
	public ClientInternalInputInterface() {
		mSessionVault = new HashMap<KvsOperation, IoSession>();
	}
	
	@Override
	public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
		cause.printStackTrace();
	}

	@Override
	public void messageReceived(IoSession session, Object message) throws Exception {
		AbstractNetworkMessage response = (AbstractNetworkMessage) message;
		
		switch(response.getType()){
		
			case STATISTICS_RES:{		
				Logger.d(TAG,"messageReceived : "+((StatisticsResultMessage)response).toString());
				HashMap<String, Double> ycsbclientsRttMapFromSlave = ((StatisticsResultMessage)response).getResultsOfDelayMeasurement();	
				String remoteIp = ConnectionMetaData.generateConnectionMetadaForRemoteEntityInSession(session).getHost();

				ycsbclientsRttMapFromSlave = new  HashMap<String, Double> ();
				ycsbclientsRttMapFromSlave.put("192.168.0.5", 0.01);
				
				NewMasterSelector.putNodeWithCorrespondingDelay(ycsbclientsRttMapFromSlave, remoteIp);
				break;
			}
		}
	}
	
	@Override
	public void excutionFinished(KeyValueEntry entry) {
		// TODO Auto-generated method stub
	}
}