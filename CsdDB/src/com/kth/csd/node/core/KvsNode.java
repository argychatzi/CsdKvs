package com.kth.csd.node.core;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.ArrayList;

import org.apache.mina.core.service.IoAcceptor;
import org.apache.mina.core.service.IoHandler;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.serialization.ObjectSerializationCodecFactory;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;

import com.kth.csd.networking.ConnectionMetaData;
import com.kth.csd.networking.interfaces.external.ServerExternalInputInterface;
import com.kth.csd.networking.interfaces.internal.ClientInternalInputInterface;
import com.kth.csd.node.Constants;
import com.kth.csd.utils.Logger;



/**
 * @author georgios.savvidis
 */
public class KvsNode {
	
	protected static final String TAG = KvsNode.class.getCanonicalName();
	
    public static void main(String[] args) throws IOException {
    	
    	startMonitoringKvsSocket(new ServerExternalInputInterface(), Constants.DEFAULT_PORT);
    	startMonitoringKvsSocket(new ClientInternalInputInterface(), Constants.INTERNAL_PORT);

    	parseConfigurationFile();
    }

	private static void parseConfigurationFile() {
    	//TODO parse configuration file and assign a master node
//		ConnectionMetaData masterNode;
//		ApplicationContext.setMasterNode(masterNode);
//		//TODO parse configuration file and generate farm	
//		ArrayList<ConnectionMetaData> nodeFarm;
//		ApplicationContext.generatNodeFarm(nodeFarm);
	}

	private static void startMonitoringKvsSocket(IoHandler handler, final int portNumber) throws IOException {
		
		Logger.d(TAG, "starting Monitoring socket ...");
		
		IoAcceptor acceptor = new NioSocketAcceptor();

        acceptor.getFilterChain().addLast( "codec", new ProtocolCodecFilter( new ObjectSerializationCodecFactory()));
        acceptor.getFilterChain().addLast( "logger", new LoggingFilter() );

        acceptor.setHandler(handler);

        acceptor.getSessionConfig().setReadBufferSize( 2*2048 );
        acceptor.bind( new InetSocketAddress(portNumber) );
        
        Logger.d(TAG, "Opened port " + portNumber);
	}
	
}