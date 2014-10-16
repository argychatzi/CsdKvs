package com.kth.csd.node.executors;

import java.util.HashMap;

import com.kth.csd.node.executors.KvsExecutor.KvsExecutable;
import com.kth.csd.node.operation.KvsOperation;
import com.kth.csd.utils.Logger;

public class KvsReader extends KvsOperation implements KvsExecutable{
	
	private static final String TAG = KvsReader.class.getCanonicalName();

	public KvsReader(String key,HashMap<String, String> value) {
		super (YCSB_OPERATION.READ, key, value);
	}

	@Override
	public void execute() {
		Logger.d(TAG, "performing read, on key:" + mKey);
	}

}
