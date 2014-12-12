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

	public static HashSet <String> updatedYCSBClientList = new HashSet <String>();
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
		
		switch(response.getType()){
			case OPERATION_READ:{
				System.out.println("recieving messages");
				keyValueEntry = ((OperationReadMessage)response).getKeyValueEntry();
				executableOperation = new KvsReader(keyValueEntry);
				break;
			}
			case OPERATION_WRITE:{
				keyValueEntry = ((OperationWriteMessage)response).getKeyValueEntry();
				// here we need the client IP, to calculate writepersecond
				String ycsbClientIp = getSessionIp(session);
				
				keyValueEntry = ((OperationWriteMessage)response).getKeyValueEntry();
				// in kvs writer constructor we have the ycsbClientIp 
				executableOperation = new KvsWriter(keyValueEntry, ycsbClientIp);
				// setting isMaster() true;
				ApplicationContext.setIsMasterTrue();
				//executableOperation = new KvsWriter(keyValueEntry);
				break;
			}
		}
		updatelistOfYcsbClients(session); // calling the update client list method
		Logger.d("", "calling update client list");
		
		AbstractNetworkMessage executionResult = executableOperation.execute();
		session.write(executionResult);
	}
	// method for having the ycsb clients list. 
		public void updatelistOfYcsbClients(IoSession session){
			updatedYCSBClientList.add(getSessionIp(session));
			ArrayList <String> ycsbClientsListtemp = new ArrayList<String>(updatedYCSBClientList);
			ycsbClientsList = ycsbClientsListtemp;
		}
		// getter for ycsb client list
		public static ArrayList<String> getlistOfYcsbClients(){
			return ycsbClientsList;
		}
		public static String getSessionIp (IoSession session){
			InetSocketAddress socketAddress = (InetSocketAddress) session.getRemoteAddress();
			InetAddress inetAddress = socketAddress.getAddress();
			String sessionIp = inetAddress.getHostAddress();
				return sessionIp;
		}

	@Override
	public void excutionFinished(KeyValueEntry entry) {
//		IoSession session = mSessionVault.get(entry);
//		session.write(entry);
	}
}