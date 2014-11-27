package com.kth.csd.node.executors;

import java.util.HashMap;

import com.kth.csd.networking.ConnectionMetaData;
import com.kth.csd.networking.messages.MasterMovedMessage;
import com.kth.csd.networking.messages.AbstractNetworkMessage;
import com.kth.csd.networking.messages.OperationWriteMessage;
import com.kth.csd.node.Constants;
import com.kth.csd.node.core.ApplicationContext;
import com.kth.csd.node.executors.KvsExecutor.KvsExecutable;
import com.kth.csd.node.operation.KeyValueEntry;
import com.kth.csd.node.operation.KvsOperation;
import com.kth.csd.utils.Logger;

public class KvsWriter extends KvsOperation implements KvsExecutable {

	private static final long serialVersionUID = 635271620411613274L;

	private static final String TAG = KvsWriter.class.getCanonicalName();
	
	public KvsWriter(KeyValueEntry keyValue) {
		mKeyValue = keyValue;
	}
	
	@Override
	public AbstractNetworkMessage execute() {
		Logger.d(TAG, "executing ...");
		AbstractNetworkMessage result = null;
		if (ApplicationContext.isMaster()){
			ApplicationContext.getKeyValueStore().put(mKeyValue.getKey(), mKeyValue.getValues());
			result = new OperationWriteMessage(mKeyValue);
		} else {
			result = new MasterMovedMessage(ApplicationContext.getMasterNode());
		}
		return result; 
	}
}
