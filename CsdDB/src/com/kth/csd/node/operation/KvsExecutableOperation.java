package com.kth.csd.node.operation;

import com.kth.csd.node.executors.KvsExecutor.KvsExecutable;
import com.kth.csd.node.executors.KvsReader;
import com.kth.csd.node.executors.KvsWriter;
import com.kth.csd.node.operation.KvsOperation.YCSB_OPERATION;

public class KvsExecutableOperation {

	private KvsExecutable mExecutor;
	
	public KvsExecutableOperation(KvsOperation operation){
		if(operation.getYcsbOperationType() == YCSB_OPERATION.READ){
			mExecutor = new KvsReader(operation.getKey(), operation.getValue());
		} else {
			mExecutor = new KvsWriter(operation.getKey(), operation.getValue());
		}
	}
	
	public void execute(){
		mExecutor.execute();
	}
	
}
