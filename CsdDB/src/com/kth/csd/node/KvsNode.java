package com.kth.csd.node;

import com.kth.csd.networking.SocketMonitor;
import com.kth.csd.node.executors.KvsOperationExecutorPool;
import com.kth.csd.utils.Logger;



/**
 * @author georgios.savvidis
 */
public class KvsNode {
	
	protected static final String TAG = KvsNode.class.getCanonicalName();
	private static KvsOperationMessageQueue mInBox;
	private static KvsOperationMessageQueue mOutBox;
	
	private static KvsOperationExecutorPool mWorkers;
	
    public static void main(String[] args) {
    	
    	int numberOfExecutors = Integer.parseInt(args[0]);
    	
    	mInBox = new KvsOperationMessageQueue();
    	mWorkers = new KvsOperationExecutorPool(mInBox, numberOfExecutors);

    	startMonitoringSocket(Constants.DEFAULT_PORT);
    	startWorkers();
    }

	private static void startMonitoringSocket(final int portNumber) {
		new Thread(){
    		@Override
    		public void run() {
    			super.run();
		    	SocketMonitor socketMonitor = new SocketMonitor(mInBox, portNumber);
				socketMonitor.startListening();		
    		}
    	}.start();		
	}
	
	private static void startWorkers() {
		new Thread(){
    		@Override
    		public void run() {
    			super.run();
    			mWorkers.startWorking();
    		}
    	}.start();
	}
}