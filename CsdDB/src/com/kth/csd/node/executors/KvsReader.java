package com.kth.csd.node.executors;

import java.util.HashMap;

import com.kth.csd.node.core.ApplicationContext;
import com.kth.csd.node.core.KeyValueStore;
import com.kth.csd.node.executors.KvsExecutor.KvsExecutable;
import com.kth.csd.node.operation.KeyValueEntry;
import com.kth.csd.node.operation.KvsOperation;
import com.kth.csd.utils.Logger;

public class KvsReader extends KvsOperation implements KvsExecutable {

	private static final long serialVersionUID = -8712103519172890713L;
	private static final String TAG = KvsReader.class.getCanonicalName();
	
	public KvsReader(KeyValueEntry keyValueEntry) {
		super(YCSB_OPERATION.READ, keyValueEntry);
	}

	@Override
	public void execute() {
		Logger.d(TAG, "executing ...");
		KeyValueStore keyValueStore = ApplicationContext.getKeyValueStore();
		HashMap<String,String> hashMap = keyValueStore.get(mKeyValue);
		mKeyValue.getValues().putAll(hashMap);
	}
}
