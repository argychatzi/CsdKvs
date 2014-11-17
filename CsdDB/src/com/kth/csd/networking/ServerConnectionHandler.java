package com.kth.csd.networking;

import java.util.HashMap;

import org.apache.mina.core.service.IoHandler;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;

import com.kth.csd.node.executors.KvsWriter;
import com.kth.csd.node.executors.KvsExecutor.KvsExecutable;
import com.kth.csd.node.executors.KvsReader;
import com.kth.csd.node.operation.KeyValueEntry;
import com.kth.csd.node.operation.KvsOperation;
import com.kth.csd.node.operation.KvsOperation.YCSB_OPERATION;
import com.kth.csd.utils.Logger;

public class ServerConnectionHandler extends IoHandlerAdapter implements IoHandler, ExecutionResultCommunicator{
	private static final String TAG = "ServerConnectionHandler";
	
	private HashMap<KvsOperation, IoSession> mSessionVault;
	
	public ServerConnectionHandler() {
		mSessionVault = new HashMap<KvsOperation, IoSession>();
	}

	@Override
	public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
		cause.printStackTrace();
	}

	@Override
	public void messageReceived(IoSession session, Object message) throws Exception {
		KvsOperation operation = (KvsOperation) message;
		mSessionVault.put(operation, session);
		KvsExecutable executable = generateExecutableFromOperation(operation);
		executable.execute();
		Logger.d(TAG, "operation ... "+ operation);
		session.write(operation);
	}

	private KvsExecutable generateExecutableFromOperation(KvsOperation operation) {
		KvsExecutable executableOperation = new KvsWriter(operation.getKeyValue());
		if(operation.getYcsbOperationType() == YCSB_OPERATION.READ){
			executableOperation = new KvsReader(operation.getKeyValue());	
		}
		return executableOperation;
	}

	@Override
	public void excutionFinished(KeyValueEntry entry) {
//		IoSession session = mSessionVault.get(entry);
//		session.write(entry);
	}
}