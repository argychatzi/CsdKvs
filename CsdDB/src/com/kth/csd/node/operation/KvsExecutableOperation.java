package com.kth.csd.node.operation;

import com.kth.csd.node.executors.KvsExecutable;
import com.kth.csd.node.executors.KvsReader;
import com.kth.csd.node.executors.KvsWriter;
import com.kth.csd.node.operation.KvsOperation.YCSB_OPERATION;

public class KvsExecutableOperation {

	private KvsExecutable mExecutor;

	public KvsExecutableOperation(KvsOperation operation) {

		if (operation.getYcsbOperationType() == YCSB_OPERATION.READ) {
			mExecutor = new KvsReader(operation.getKeyValue());
		} else {
			mExecutor = new KvsWriter(operation.getKeyValue());
		}
	}

	public void execute() {
		 mExecutor.execute();
	}
}
