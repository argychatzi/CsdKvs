package com.kth.csd.networking.interfaces.external;

import java.util.ArrayList;
import java.util.HashSet;

import org.apache.mina.core.service.IoHandler;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;

import com.kth.csd.networking.ConnectionMetaData;
import com.kth.csd.networking.ExecutionResultCommunicator;
import com.kth.csd.networking.messages.AbstractNetworkMessage;
import com.kth.csd.networking.messages.MasterMovedMessage;
import com.kth.csd.networking.messages.OperationReadMessage;
import com.kth.csd.networking.messages.OperationWriteMessage;
import com.kth.csd.node.core.ApplicationContext;
import com.kth.csd.node.executors.KvsReader;
import com.kth.csd.node.executors.KvsWriter;
import com.kth.csd.node.operation.KeyValueEntry;
import com.kth.csd.utils.Logger;

public class ServerExternalInputInterface extends IoHandlerAdapter implements IoHandler, ExecutionResultCommunicator{

	private static final String TAG = ServerExternalInputInterface.class.getCanonicalName();

	public HashSet <String> updatedYCSBClientList = new HashSet <String>();
	public static ArrayList <String> ycsbClientsList = new ArrayList<String>();	

	public ServerExternalInputInterface() {
		Logger.d(TAG, "ExternalTrafficInputInterface ... created");
	}

	@Override
	public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
		cause.printStackTrace();
	}

	@Override
	public void messageReceived(IoSession session, Object message) throws Exception {

		AbstractNetworkMessage response = (AbstractNetworkMessage) message;
		KeyValueEntry keyValueEntry = null;

		AbstractNetworkMessage executionResult = null;

		switch(response.getType()){
		case OPERATION_READ:{
			String ycsbClientIp = ConnectionMetaData.generateConnectionMetadaForRemoteEntityInSession(session).getHost();
			Logger.d(TAG, "messageReceived by:" + ycsbClientIp +" OPERATION_READ" + ((OperationReadMessage)response).getKeyValueEntry());
			keyValueEntry = ((OperationReadMessage)response).getKeyValueEntry();
			executionResult = new KvsReader(keyValueEntry).execute();
			break;
		}

		case OPERATION_WRITE:{
			String ycsbClientIp = ConnectionMetaData.generateConnectionMetadaForRemoteEntityInSession(session).getHost();
			Logger.d(TAG, "messageReceived by:" + ycsbClientIp +" OPERATION_WRITE" + ((OperationWriteMessage)response).getKeyValueEntry());			

			if(ApplicationContext.isMaster()) {
				Logger.d(TAG, "performing write");
				keyValueEntry = ((OperationWriteMessage)response).getKeyValueEntry();
				ApplicationContext.addIpToYcsbWritingIPs(ycsbClientIp); 
				executionResult = new KvsWriter(keyValueEntry, ycsbClientIp).execute();
			} else {
				Logger.d(TAG, "Received request by " + ycsbClientIp + " I am no longer the master");
				executionResult = new MasterMovedMessage(ApplicationContext.getMasterInternalConnection(), ApplicationContext.getMasterExternalConnection());
			}
			break;
		}
		}
		session.write(executionResult);
	}


	@Override
	public void excutionFinished(KeyValueEntry entry) {
		//		IoSession session = mSessionVault.get(entry);
		//		session.write(entry);
	}
}