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
import com.kth.csd.networking.interfaces.internal.ClientInternalInputInterface;



/**
 * @author georgios.savvidis
 */
public class KvsNode {
	
	protected static final String TAG = KvsNode.class.getCanonicalName();
	private static ArrayList<ConnectionMetaData> allNodeProperties = new ArrayList<ConnectionMetaData>();
	public static String[] allNodeIp = { "192.168.0.2", "192.168.0.3", "192.168.0.4", "192.168.0.5", "192.168.0.7","192.168.0.8",
		"192.168.0.10","192.168.0.11","192.168.0.12","192.168.0.13" };
	//public static String[] allNodeIp = { "10.0.0.1", "10.0.0.3" };// for testing
		
    public static void main(String[] args) throws IOException {
    	
    	startMonitoringKvsSocket(new ServerExternalInputInterface(), Constants.DEFAULT_PORT);
    	startMonitoringKvsSocket(new ClientInternalInputInterface(), Constants.INTERNAL_PORT);

    	parseConfigurationFile();
    }
    
   // generating the connection metadata for all internal nodes, return an array list  
   private static ArrayList<ConnectionMetaData> generateInternalConnectionMetaData(ArrayList<String> listNodesIp){
	   for (String key: listNodesIp){
		   ConnectionMetaData singleNodeProperty = new ConnectionMetaData(key, Constants.INTERNAL_PORT);
		   allNodeProperties.add(singleNodeProperty); 
	   }
	   return allNodeProperties;
	   
   }
   // making a arraylist
   
   private static ArrayList<String> getAllNodeIp(String[] allNodeIp){
	   ArrayList<String> listNodesIp = new ArrayList<String>();
	   for (String key: allNodeIp){
		   listNodesIp.add(key);
	   }
	   return listNodesIp;
   }
   
   

	private static void parseConfigurationFile() {
    	//TODO parse configuration file and assign a master node
//		ConnectionMetaData masterNode;
//		ApplicationContext.setMasterNode(masterNode);
		
		
		ApplicationContext.generatNodeFarm(allNodeProperties);
		Logger.d(TAG, "Genrating farm of nodes");
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