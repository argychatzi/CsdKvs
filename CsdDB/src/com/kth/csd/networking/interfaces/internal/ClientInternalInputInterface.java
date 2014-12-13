package com.kth.csd.networking.interfaces.internal;

import java.util.HashMap;

import org.apache.mina.core.service.IoHandler;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;

import com.kth.csd.networking.ExecutionResultCommunicator;
import com.kth.csd.networking.messages.AbstractNetworkMessage;
import com.kth.csd.networking.messages.StatisticsResultMessage;
import com.kth.csd.node.operation.KeyValueEntry;
import com.kth.csd.node.operation.KvsOperation;
import com.kth.csd.node.core.AssignNewMaster;

public class ClientInternalInputInterface extends IoHandlerAdapter implements IoHandler, ExecutionResultCommunicator{
	
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
		System.out.println("ClientInternal msg received!!!");
		AbstractNetworkMessage response = (AbstractNetworkMessage) message;
		
		switch(response.getType()){
		
			case STATISTICS_RES:{			
				ycsbclientsRttMapFromSlave = ((StatisticsResultMessage)response).getResultsOfDelayMeasurement();		
				AssignNewMaster.assignNewMaster(ycsbclientsRttMapFromSlave, session);
				break;
			}
		}
	}
	
	@Override
	public void excutionFinished(KeyValueEntry entry) {
		// TODO Auto-generated method stub
	}
}