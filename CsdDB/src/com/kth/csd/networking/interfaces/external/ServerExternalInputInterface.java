package com.kth.csd.networking.interfaces.external;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import org.apache.mina.core.service.IoHandler;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;

import com.kth.csd.networking.ExecutionResultCommunicator;
import com.kth.csd.networking.messages.AbstractNetworkMessage;
import com.kth.csd.networking.messages.MasterMovedMessage;
import com.kth.csd.networking.messages.OperationReadMessage;
import com.kth.csd.networking.messages.OperationWriteMessage;
import com.kth.csd.node.core.ApplicationContext;
import com.kth.csd.node.executors.KvsExecutor.KvsExecutable;
import com.kth.csd.node.executors.KvsReader;
import com.kth.csd.node.executors.KvsWriter;
import com.kth.csd.node.operation.KeyValueEntry;
import com.kth.csd.node.operation.KvsOperation;
import com.kth.csd.utils.Logger;

public class ServerExternalInputInterface extends IoHandlerAdapter implements IoHandler, ExecutionResultCommunicator{
	private static final String TAG = "ServerConnectionHandler";

	public HashSet <String> updatedYCSBClientList = new HashSet <String>();
	public static ArrayList <String> ycsbClientsList = new ArrayList<String>();	
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
		
		AbstractNetworkMessage executionResult = null;

		switch(response.getType()){
			case OPERATION_READ:{
				keyValueEntry = ((OperationReadMessage)response).getKeyValueEntry();
				executionResult = new KvsReader(keyValueEntry).execute();
				break;
			}
		
			case OPERATION_WRITE:{
				if(ApplicationContext.isMaster()) {
					keyValueEntry = ((OperationWriteMessage)response).getKeyValueEntry();
					// here we need the client IP, to calculate writepersecond
					String ycsbClientIp = getSessionIp(session);
					Logger.d(TAG, "OPERATION_WRITE"+ycsbClientIp);
					ApplicationContext.addIpToYcsbIPs(ycsbClientIp); 
					executionResult = new KvsWriter(keyValueEntry, ycsbClientIp).execute();
				} else {
					executionResult = new MasterMovedMessage(ApplicationContext.getMasterInternalConnection(), ApplicationContext.getMasterExternalConnection());
				}
				break;
			}
		}
		session.write(executionResult);
	}
	
	public String getSessionIp (IoSession session){
		InetSocketAddress socketAddress = (InetSocketAddress) session.getRemoteAddress();
		InetAddress inetAddress = socketAddress.getAddress();
		return inetAddress.getHostAddress();
	}

	@Override
	public void excutionFinished(KeyValueEntry entry) {
//		IoSession session = mSessionVault.get(entry);
//		session.write(entry);
	}
}