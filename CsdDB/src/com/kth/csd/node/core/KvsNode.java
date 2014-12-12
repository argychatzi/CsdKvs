package com.kth.csd.node.core;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.mina.core.service.IoAcceptor;
import org.apache.mina.core.service.IoHandler;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.serialization.ObjectSerializationCodecFactory;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.kth.csd.networking.ConnectionMetaData;
import com.kth.csd.networking.interfaces.external.ServerExternalInputInterface;
import com.kth.csd.networking.interfaces.internal.ServerInternalInputInterface;
import com.kth.csd.networking.messages.MasterMovedMessage;
import com.kth.csd.node.Constants;
import com.kth.csd.utils.Configuration;
import com.kth.csd.utils.ConfigurationReader;
import com.kth.csd.utils.Logger;

/**
 * @author georgios.savvidis
 */
public class KvsNode {
	
	protected static final String TAG = KvsNode.class.getCanonicalName();
//	public static String[] allNodeIp = { "192.168.0.2", "192.168.0.3", "192.168.0.4", "192.168.0.5", "192.168.0.7","192.168.0.8",
//		"192.168.0.10","192.168.0.11","192.168.0.12","192.168.0.13" }; // Mininet topology database Servers Ip  
	
		
	
    public static void main(String[] args) throws IOException {
    	if(args.length >0 ){
    		
    		Logger.d(TAG, args[0]);
    		
    		parseConfigurationFile(args[0]);
    		
        	startMonitoringKvsSocket(new ServerInternalInputInterface(), ApplicationContext.getInternalConnection().getPort());
        	startMonitoringKvsSocket(new ServerExternalInputInterface(), ApplicationContext.getExternalConnection().getPort());

    	}
    }
    
    private static void parseConfigurationFile(String fileNo) throws IOException {
    	
    	Logger.d(TAG, fileNo);
    	Configuration configuration = ConfigurationReader.loadConfigurationFile(fileNo);
    	
    	Logger.d(TAG, "configuration :: " + configuration);
    	
    	ApplicationContext.setMasterExternalConnection(configuration.getMasterExternalConnectionMetaData());
    	ApplicationContext.setMasterInternalConnection(configuration.getMasterInternalConnectionMetaData());
    	
    	ApplicationContext.setOwnExternalConnection(configuration.getOwnExternalConnectionMetaData());
    	ApplicationContext.setOwnInternalConnection(configuration.getOwnInternalConnectionMetaData());
    	
    	ApplicationContext.generateNodeFarm(configuration.getNodesInFarm());
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