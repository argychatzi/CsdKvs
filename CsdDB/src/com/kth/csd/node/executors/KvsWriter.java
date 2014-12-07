package com.kth.csd.node.executors;

import java.net.InetAddress;
import java.net.InetSocketAddress;

import com.kth.csd.networking.messages.AbstractNetworkMessage;
import com.kth.csd.networking.messages.MasterMovedMessage;
import com.kth.csd.networking.messages.OperationWriteMessage;
import com.kth.csd.node.core.ApplicationContext;
import com.kth.csd.node.executors.KvsExecutor.KvsExecutable;
import com.kth.csd.node.operation.KeyValueEntry;
import com.kth.csd.node.operation.KvsOperation;
import com.kth.csd.utils.Logger;

public class KvsWriter extends KvsOperation implements KvsExecutable {

	private static final long serialVersionUID = 635271620411613274L;

	private static final String TAG = KvsWriter.class.getCanonicalName();
	
	public static String mYcsbclient;
	
	public KvsWriter(KeyValueEntry keyValue) {
		mKeyValue = keyValue;
	}
	
	public KvsWriter(KeyValueEntry keyValue, String ycsbClientIp){
		mKeyValue = keyValue;
		this.mYcsbclient = ycsbClientIp;
	}
	
	@Override
	public AbstractNetworkMessage execute() {
		Logger.d(TAG, "executing ...");
		AbstractNetworkMessage result = null;
		// suspected error as it is never true isMaster(); 
		if (ApplicationContext.isMaster()){
			
			ApplicationContext.getKeyValueStore().updateWritingClientIP(mYcsbclient);
			
			ApplicationContext.getKeyValueStore().put(mKeyValue.getKey(), mKeyValue.getValues());
			
			result = new OperationWriteMessage(mKeyValue);
		}
		// for the slave data replication
		else if (ApplicationContext.isUpdate()){
			
			ApplicationContext.getKeyValueStore().put(mKeyValue.getKey(), mKeyValue.getValues());
			result = new OperationWriteMessage(mKeyValue);
		}
		
		else  {
			result = new MasterMovedMessage(ApplicationContext.getMasterNode());
			
		}
		return result; 
	}
}
