package com.kth.csd.node.operation;

import java.net.Socket;

import com.kth.csd.node.executors.KvsExecutor.KvsExecutable;
import com.kth.csd.node.executors.KvsReader;
import com.kth.csd.node.executors.KvsWriter;
import com.kth.csd.node.operation.KvsOperation.YCSB_OPERATION;

public class KvsExecutableOperation {

	private KvsExecutable mExecutor;
	
	public KvsExecutableOperation(KvsOperation operation){
		
		if(operation.getYcsbOperationType() == YCSB_OPERATION.READ){
			mExecutor = new KvsReader(operation.getKeyValue(), operation.getCommunicationSocket());
		} else {
			mExecutor = new KvsWriter(operation.getKeyValue(), operation.getCommunicationSocket());
		}
	}
	
	public void execute(){
		mExecutor.execute();
	}
	
}
