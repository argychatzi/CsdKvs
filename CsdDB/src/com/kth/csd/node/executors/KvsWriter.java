package com.kth.csd.node.executors;

import java.util.HashMap;

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
	public void execute() {
		Logger.d(TAG, "executing ...");
		if (ApplicationContext.isMaster()){
			ApplicationContext.getKeyValueStore().put(mKeyValue.getKey(), mKeyValue.getValues());
		} else {
			generateMessageForMasterMoved();
		}
	}

	private void generateMessageForMasterMoved(){
		HashMap messageMoved = OperationAnalyzer.generateMessageMoved(ApplicationContext.getMasterNodeConnectionMetaData());
		mKeyValue.setValue(messageMoved);
		Logger.d(TAG, mKeyValue.toString());
	}
}
