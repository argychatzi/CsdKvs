package com.kth.csd.node.executors;

import com.kth.csd.node.KvsOperationMessageQueue;
import com.kth.csd.node.operation.KvsExecutableOperation;
import com.kth.csd.utils.Logger;

public class KvsExecutor extends Thread {

	public interface KvsExecutable {
		public void execute();
	}

	private static final long SLEEP_DURATION = 1000;

	private static final String TAG = "KvsExecutor";
	private Object mutex = new Object();

	private KvsOperationMessageQueue mBox;
	private int mId;

	public KvsExecutor(KvsOperationMessageQueue box, int id) {
		mBox = box;
		mId = id;
		Logger.d(TAG, "thread with id "+ id +" instantiated");
	}

	@Override
	public void run() {
		super.run();
		Logger.d(TAG, "thread # " + mId +  "run");
		
		while (true) {
			
			Logger.d(TAG, "thread # " + mId +  "is awake");
			if (!mBox.isEmpty()){
				
				synchronized (mutex) {
					KvsExecutableOperation operation = mBox.dequeueRequestOperation();
					operation.execute();
				}
			} else {
				try {
					
					Logger.d(TAG, "thread # " + mId +  "goes to sleep");
					
					Thread.sleep(SLEEP_DURATION);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

}
