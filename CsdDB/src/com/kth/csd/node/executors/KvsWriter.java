package com.kth.csd.node.executors;

import java.util.HashMap;

import com.kth.csd.node.executors.KvsExecutor.KvsExecutable;
import com.kth.csd.node.operation.KvsOperation;
import com.kth.csd.utils.Logger;

public class KvsWriter extends KvsOperation implements KvsExecutable {

	private static final String TAG = KvsWriter.class.getCanonicalName();

	public KvsWriter(String key, HashMap<String, String> value) {
		super(YCSB_OPERATION.WRITE, key, value);
	}

	@Override
	public void execute() {
		Logger.d(TAG, "performing write, on key:" + mKey );
	}

}
