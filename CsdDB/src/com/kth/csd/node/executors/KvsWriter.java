package com.kth.csd.node.executors;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.HashMap;

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
		mYcsbclient = ycsbClientIp;
	}
	
	@Override
	public AbstractNetworkMessage execute() {
		Logger.d(TAG, "executing ...");
		ApplicationContext.getKeyValueStore().put(mKeyValue.getKey(), mKeyValue.getValues());
		if (ApplicationContext.isMaster()){
			ApplicationContext.getNodeFarm().broadCast(new OperationWriteMessage(mKeyValue));
		}
		
		return new OperationWriteMessage(mKeyValue); 
	}
}
