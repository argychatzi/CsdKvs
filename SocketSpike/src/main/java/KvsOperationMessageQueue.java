package main.java;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.kth.csd.node.operation.KvsOperation;
import com.kth.csd.node.operation.KvsExecutableOperation;
import com.kth.csd.utils.Logger;

public class KvsOperationMessageQueue{

	private static final String TAG = "OperationMessageQueue";
	private ConcurrentLinkedQueue<KvsExecutableOperation> mQueue;
	
	public KvsOperationMessageQueue(){
		mQueue = new ConcurrentLinkedQueue<KvsExecutableOperation>();
	}
	
	public KvsExecutableOperation dequeueRequestOperation() throws NoSuchElementException {
		return mQueue.remove();
	}
	
	public void enqueueRequestOperation(KvsOperation baseOperation){
		KvsExecutableOperation operation = new KvsExecutableOperation(baseOperation);
		mQueue.add(operation);
	}
	
	public boolean isEmpty(){
		return mQueue.isEmpty();
	}

	private void printQueue() {
		Iterator<KvsExecutableOperation> iterator = mQueue.iterator();
		int numberOfElement = 0;
		while (iterator.hasNext()) {
			numberOfElement ++;
			KvsExecutableOperation nextOperation = iterator.next();
			Logger.d(TAG,  "Request Operation #" + numberOfElement + ": " + nextOperation);
		}
	}
	
	
}
