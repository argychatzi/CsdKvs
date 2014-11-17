package com.kth.csd.node.core;

import java.io.IOException;
import java.net.InetSocketAddress;

import org.apache.mina.core.service.IoAcceptor;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.serialization.ObjectSerializationCodecFactory;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;

import com.kth.csd.networking.ServerConnectionHandler;
import com.kth.csd.node.Constants;



/**
 * @author georgios.savvidis
 */
public class KvsNode {
	
	protected static final String TAG = KvsNode.class.getCanonicalName();
	private static KvsOperationMessageQueue mInBox;
	
	private static KvsOperationExecutorPool mWorkers;
	
    public static void main(String[] args) throws IOException {
    	
    	int numberOfExecutors; 
    	
    	if(args.length > 0){
    		numberOfExecutors = Integer.parseInt(args[0]);
    	} else {
    		numberOfExecutors = Constants.NUMBER_OF_EXECUTORS;
    	}
    	
    	mInBox = new KvsOperationMessageQueue();
    	mWorkers = new KvsOperationExecutorPool(mInBox, numberOfExecutors);

    	startMonitoringSocket(Constants.DEFAULT_PORT);
//    	startWorkers();
    }

	private static void startMonitoringSocket(final int portNumber) throws IOException {
		IoAcceptor acceptor = new NioSocketAcceptor();

        acceptor.getFilterChain().addLast( "codec", new ProtocolCodecFilter( new ObjectSerializationCodecFactory()));
        acceptor.getFilterChain().addLast( "logger", new LoggingFilter() );

        acceptor.setHandler(  new ServerConnectionHandler() );

        acceptor.getSessionConfig().setReadBufferSize( 2*2048 );
//        acceptor.getSessionConfig().setIdleTime( IdleStatus.BOTH_IDLE, 10 );
        acceptor.bind( new InetSocketAddress(Constants.DEFAULT_PORT) );
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