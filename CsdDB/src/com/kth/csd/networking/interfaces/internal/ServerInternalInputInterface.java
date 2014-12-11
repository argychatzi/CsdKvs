package com.kth.csd.networking.interfaces.internal;

import java.util.HashMap;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;

import com.kth.csd.networking.ConnectionMetaData;
import com.kth.csd.networking.messages.MasterMovedMessage;
import com.kth.csd.networking.messages.AbstractNetworkMessage;
import com.kth.csd.networking.messages.OperationReadMessage;
import com.kth.csd.networking.messages.OperationWriteMessage;
import com.kth.csd.node.core.ApplicationContext;
import com.kth.csd.node.executors.KvsReader;
import com.kth.csd.node.executors.KvsWriter;
import com.kth.csd.node.operation.KeyValueEntry;
import com.kth.csd.node.operation.KvsOperation;

public class ServerInternalInputInterface extends IoHandlerAdapter{
	
private static final String TAG = "ServerConnectionHandler";
	
	private HashMap<KvsOperation, IoSession> mSessionVault;
	
	@Override
	public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
		cause.printStackTrace();
	}

	@Override
	public void messageReceived(IoSession session, Object message) throws Exception {
		
		AbstractNetworkMessage response = (AbstractNetworkMessage) message;
		
		switch(response.getType()){
		
			case STATISTICS_REQ:{
				
				
				break;
			}
			case STATISTICS_RES:{
				
				
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


