package com.kth.csd.networking.interfaces.external;

import java.util.HashMap;

import org.apache.mina.core.service.IoHandler;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;

import com.google.gson.Gson;
import com.kth.csd.networking.ExecutionResultCommunicator;
import com.kth.csd.networking.messages.AbstractNetworkMessage;
import com.kth.csd.networking.messages.OperationReadMessage;
import com.kth.csd.networking.messages.OperationWriteMessage;
import com.kth.csd.node.executors.KvsWriter;
import com.kth.csd.node.executors.KvsExecutor.KvsExecutable;
import com.kth.csd.node.executors.KvsReader;
import com.kth.csd.node.operation.KeyValueEntry;
import com.kth.csd.node.operation.KvsOperation;
import com.kth.csd.node.operation.KvsOperation.YCSB_OPERATION;
import com.kth.csd.utils.Logger;

public class ServerExternalInputInterface extends IoHandlerAdapter implements IoHandler, ExecutionResultCommunicator{
	private static final String TAG = "ServerConnectionHandler";
	
	private HashMap<KvsOperation, IoSession> mSessionVault;
	public ServerExternalInputInterface() {
		Logger.d(TAG, "ExternalTrafficInputInterface ... ");
		mSessionVault = new HashMap<KvsOperation, IoSession>();
		Logger.d(TAG, "created !");
	}

	@Override
	public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
		cause.printStackTrace();
	}

	@Override
	public void messageReceived(IoSession session, Object message) throws Exception {
		AbstractNetworkMessage response = (AbstractNetworkMessage) message;
		KvsExecutable executableOperation = null;
		KeyValueEntry keyValueEntry = null;
		switch(response.getType()){
		
			case OPERATION_READ:{
				keyValueEntry = ((OperationReadMessage)response).getKeyValueEntry();
				executableOperation = new KvsReader(keyValueEntry);
				break;
			}
			case OPERATION_WRITE:{
				 keyValueEntry = ((OperationWriteMessage)response).getKeyValueEntry();
				executableOperation = new KvsWriter(keyValueEntry);
				break;
			}
		}
		AbstractNetworkMessage executionResult = executableOperation.execute();
		session.write(executionResult);
	}


	@Override
	public void excutionFinished(KeyValueEntry entry) {
//		IoSession session = mSessionVault.get(entry);
//		session.write(entry);
	}
}