package com.kth.csd.networking;

import java.net.InetSocketAddress;
import java.net.SocketAddress;

import org.apache.mina.core.RuntimeIoException;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.future.IoFuture;
import org.apache.mina.core.future.IoFutureListener;
import org.apache.mina.core.future.WriteFuture;
import org.apache.mina.core.service.IoHandler;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.serialization.ObjectSerializationCodecFactory;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.nio.NioSocketConnector;

import com.kth.csd.networking.messages.AbstractNetworkMessage;
import com.kth.csd.networking.messages.OperationReadMessage;
import com.kth.csd.networking.messages.OperationWriteMessage;
import com.kth.csd.node.operation.KeyValueEntry;
import com.kth.csd.utils.Logger;

public class KvsClient implements IoFutureListener<IoFuture>{

	//KVSClient seems to be used for connection
	private static final String TAG = KvsClient.class.getCanonicalName();
	public static final String ATTRIBUTE_ID = "ATTRIBUTE_ID";
	private IoSession session = null;
	
	public interface YcsbTrafficInputInteraceHolder{
		public void onMasterNodeMoved(ConnectionMetaData master);
	}


	public KvsClient(IoHandler handler, ConnectionMetaData connectionMetaData){
	    session = initSession(handler, connectionMetaData);
	}
		
	private IoSession initSession(IoHandler handler, ConnectionMetaData connectionMetaData){
		NioSocketConnector connector = new NioSocketConnector();

	    connector.getFilterChain().addLast("codec", new ProtocolCodecFilter(new ObjectSerializationCodecFactory()));
	    connector.getFilterChain().addLast("logger", new LoggingFilter());
	    connector.setHandler(handler);
	    
		IoSession result = null;	
		try {
            ConnectFuture future = connector.connect(new InetSocketAddress(connectionMetaData.getHost(), connectionMetaData.getPort()));
            Logger.d(TAG, "initSession FUTURE="+future);
            future.awaitUninterruptibly();
            result = future.getSession();
            
            SocketAddress remoteAddress = result.getRemoteAddress();
            Logger.d(TAG, "connecting to " + remoteAddress);
            
            
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

	
	public int send(AbstractNetworkMessage message){
		WriteFuture write  = session.write(message);
		try {
			write.await(5000);
		} catch (InterruptedException e) {
			return 10; 
		}
		return 0;
	}
	
	
	public int read(KeyValueEntry entry){
		return send(new OperationReadMessage(entry));
	}
	
	public int write(KeyValueEntry entry){
		return send(new OperationWriteMessage(entry));
	}
	
	public void operationComplete(IoFuture arg0) {
//		Object attribute = arg0.getSession().getAttribute(ATTRIBUTE_ID);
	}
}
