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
				ConnectionMetaData newMasterConnectionMetadata = ((MasterMovedMessage)response).getNewMaster();
				ApplicationContext.updateMaster(newMasterConnectionMetadata);
				break;
			}
			case OPERATION_READ:{
				KeyValueEntry keyValueEntry = ((OperationWriteMessage)message).getKeyValueEntry();
				new KvsReader(keyValueEntry).execute();
				break;
			}
			case OPERATION_WRITE:{
				KeyValueEntry keyValueEntry = ((OperationReadMessage)message).getKeyValueEntry();
				new KvsWriter(keyValueEntry).execute();
				break;
			}
		}
	}

}
