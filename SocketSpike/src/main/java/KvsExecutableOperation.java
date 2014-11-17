package main.java;

import com.kth.csd.models.ApplicationContext;
import com.kth.csd.node.executors.KvsExecutor.KvsExecutable;
import com.kth.csd.node.executors.KvsMasterMovedErrorHandler;
import com.kth.csd.node.executors.KvsReader;
import com.kth.csd.node.executors.KvsWriter;
import com.kth.csd.node.operation.KvsOperation.YCSB_OPERATION;

public class KvsExecutableOperation {

	private KvsExecutable mExecutor;
	
	public KvsExecutableOperation(KvsOperation operation){
		
		if(operation.getYcsbOperationType() == YCSB_OPERATION.READ){
			mExecutor = new KvsReader(operation.getKeyValue(), operation.getCommunicationPoint());
		} else {
			if (ApplicationContext.isMaster()){
				mExecutor = new KvsWriter(operation.getKeyValue(), operation.getCommunicationPoint());
			} else {
				mExecutor = new KvsMasterMovedErrorHandler(operation.getKeyValue(), operation.getCommunicationPoint());
			}
		}
	}
	
	public void execute(){
		mExecutor.execute();
	}
}
