package com.kth.csd.node.core;

import java.net.InetSocketAddress;
import java.util.HashMap;

import org.apache.mina.core.RuntimeIoException;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.future.IoFuture;
import org.apache.mina.core.future.IoFutureListener;
import org.apache.mina.core.future.WriteFuture;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.serialization.ObjectSerializationCodecFactory;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.nio.NioSocketConnector;

import com.kth.csd.networking.ConnectionMetaData;
import com.kth.csd.node.executors.OperationAnalyzer;
import com.kth.csd.node.operation.KeyValueEntry;
import com.kth.csd.node.operation.KvsOperation;
import com.kth.csd.node.operation.KvsOperation.YCSB_OPERATION;
import com.kth.csd.utils.Logger;

public class KvsClient implements IoFutureListener<IoFuture>{

	private static final String TAG = KvsClient.class.getCanonicalName();
	public static final String ATTRIBUTE_ID = "ATTRIBUTE_ID";
	private IoSession session = null;

	public KvsClient(ConnectionMetaData connectionMetaData){
	    session = initSession(connectionMetaData);
	}
	
	private IoSession initSession(ConnectionMetaData connectionMetaData){
		NioSocketConnector connector = new NioSocketConnector();

	    connector.getFilterChain().addLast("codec", new ProtocolCodecFilter(new ObjectSerializationCodecFactory()));
	    connector.getFilterChain().addLast("logger", new LoggingFilter());
	    connector.setHandler(new ClientIoHandlerAdapter());
	    
		IoSession result = null;
		try {
            ConnectFuture future = connector.connect(new InetSocketAddress(connectionMetaData.getHost(), connectionMetaData.getPort()));
            future.awaitUninterruptibly();
            result = future.getSession();
            
        } catch (RuntimeIoException e) {
            System.err.println("Failed to connect.");
            e.printStackTrace();
            try {
				Thread.sleep(5000);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
        }
		Logger.d(TAG, "new connection to " + connectionMetaData.getHost() +" port No. "+ connectionMetaData.getPort());
		
		return result;
	}
	
	public int read(KeyValueEntry entry){
		WriteFuture write = session.write(new KvsOperation(YCSB_OPERATION.READ, entry));
		write.addListener(this);
		try {
			write.await();
		} catch (InterruptedException e) {
			return 10; 
		}
		return 0;
	}
	
	public int write(KeyValueEntry entry){
        WriteFuture write = session.write(new KvsOperation(YCSB_OPERATION.WRITE, entry));
		write.addListener(this);
		try {
			write.await();
		} catch (InterruptedException e) {
			return 10;
		}
		return 0;
	}
	
	public void operationComplete(IoFuture arg0) {
		Object attribute = arg0.getSession().getAttribute(ATTRIBUTE_ID);
		Logger.d(TAG, "operationComplete ... " + attribute);
	}
	
	private class ClientIoHandlerAdapter extends IoHandlerAdapter {
		
		@Override
		public void messageReceived(IoSession session, Object message) throws Exception {
			session.setAttribute(ATTRIBUTE_ID, message);
			scanMessageForProblems(((KvsOperation) message).getKeyValue());
			Logger.d(TAG, "message Received session = "+ session );
			super.messageReceived(session, message);
		}

		private void scanMessageForProblems(KeyValueEntry message) throws Exception {
			Logger.d(TAG, "scanMessageForProblems ... ");
			if(OperationAnalyzer.hasMasterMoved(message)){
				Logger.d(TAG, "master moved");
				ConnectionMetaData newMasterConnectionMetadata = OperationAnalyzer.extractMasterConnection(message);
				session = initSession(newMasterConnectionMetadata);
			}
		}
	}
}
