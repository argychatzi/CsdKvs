package main.java;

public class KvsOperationExecutorPool {
	
	private static final String TAG = KvsOperationExecutorPool.class.getCanonicalName();
	private static KvsExecutor[] mThreadPool;
	
	public KvsOperationExecutorPool(KvsOperationMessageQueue inbox, int numberOfExecutors){
		
		Logger.d(TAG, "KvsOperationExecutorPool instatiated!");
		mThreadPool = new KvsExecutor[numberOfExecutors];
		
		for (int i = 0; i<numberOfExecutors; i++){
			mThreadPool[i] = new KvsExecutor(inbox, i);
		}
	}
	
	public void startWorking(){
		Logger.d(TAG, "startWorking!");
		Logger.d(TAG, "size of pool =" +mThreadPool.length);
		for (int i = 0; i<mThreadPool.length; i++){
			Logger.d(TAG, "thread #" + i + " startWorking!");
			mThreadPool[i].start();
		}
	}
	
}
