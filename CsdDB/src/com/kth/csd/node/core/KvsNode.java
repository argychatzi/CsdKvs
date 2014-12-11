package com.kth.csd.node.core;

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

import com.kth.csd.networking.ConnectionMetaData;
import com.kth.csd.networking.interfaces.external.ServerExternalInputInterface;
import com.kth.csd.networking.interfaces.internal.ServerInternalInputInterface;
import com.kth.csd.networking.messages.MasterMovedMessage;
import com.kth.csd.node.Constants;
import com.kth.csd.utils.Logger;

/**
 * @author georgios.savvidis
 */
public class KvsNode {
	
	protected static final String TAG = KvsNode.class.getCanonicalName();
	private static ArrayList<ConnectionMetaData> allNodeProperties = new ArrayList<ConnectionMetaData>();
	public static String[] allNodeIp = { "192.168.0.2", "192.168.0.3", "192.168.0.4", "192.168.0.5", "192.168.0.7","192.168.0.8",
		"192.168.0.10","192.168.0.11","192.168.0.12","192.168.0.13" };
	//public static String[] allNodeIp = { "10.0.0.1", "10.0.0.3" };// for testing
	private static boolean mIsMaster;
		
    public static void main(String[] args) throws IOException {
    	
    	int externalPort = Constants.SECONDARY_EXTERNAL_PORT;
		int internalPort = Constants.SECONDARY_INTERNAL_PORT;
		
		
		
    	if(args.length > 0 ){
	    	if(args[0].equals("master")){
	    		mIsMaster = true;
	    		
	    		externalPort = Constants.DEFAULT_EXTERNAL_PORT; 
	    		internalPort = Constants.DEFAULT_INTERNAL_PORT;
	    		ApplicationContext.setMasterInternalConnection(new ConnectionMetaData("localhost", internalPort));
	    		ApplicationContext.setMasterExternalConnection(new ConnectionMetaData("localhost", externalPort));
	    	}
		}
    	
    	startMonitoringKvsSocket(new ServerExternalInputInterface(), externalPort);
		startMonitoringKvsSocket(new ServerInternalInputInterface(), internalPort);
		
		ConnectionMetaData internalConnection = new ConnectionMetaData("localhost",internalPort);
		ConnectionMetaData externalConnection = new ConnectionMetaData("localhost",externalPort);
		
		ApplicationContext.setOwnInternalConnection(internalConnection);
		ApplicationContext.setOwnExternalConnection(externalConnection);


		parseConfigurationFile();
		
		startMockNodeFarm();
		
    	startMasterSelector();
    }
    
    private static void parseConfigurationFile() {
    	//open file for read
    	//extract master node - one IP and two ports
    	//extract own connectionMetaData - one IP and two ports for each node
    	//extract metaData for the farm
	}

	private static void startMockNodeFarm(){
    	ArrayList<ConnectionMetaData> nodes = new ArrayList<ConnectionMetaData>();

    	ConnectionMetaData dummyLocalInternalConnection = new ConnectionMetaData("localhost", Constants.SECONDARY_INTERNAL_PORT);
		ConnectionMetaData dummyLocalInternalConnection2 = new ConnectionMetaData("localhost", Constants.DEFAULT_INTERNAL_PORT);
		
		nodes.add(dummyLocalInternalConnection);
    	nodes.add(dummyLocalInternalConnection2);
    	
    	ApplicationContext.generateNodeFarm(nodes);
    }


    private static void startMasterSelector() {
    	Timer timer = new Timer();
		timer.schedule(new MasterMovingMockTimer(), 0, 5000);
	}


    //TODO Jawad, Mihret this code is never used. If this is the case, and there are no plans to 
    //be used in the future either, you should remove it
   // generating the connection metadata for all internal nodes, return an array list  
   private static ArrayList<ConnectionMetaData> generateInternalConnectionMetaData(ArrayList<String> listNodesIp){
	   for (String key: listNodesIp){
		   ConnectionMetaData singleNodeProperty = new ConnectionMetaData(key, Constants.DEFAULT_INTERNAL_PORT);
		   allNodeProperties.add(singleNodeProperty); 
	   }
	   return allNodeProperties;
	   
   }

   //TODO Jawad, Mihret this code is never used. If this is the case, and there are no plans to 
   //be used in the future either, you should remove it
   // making a arraylist
   private static ArrayList<String> getAllNodeIp(String[] allNodeIp){
	   ArrayList<String> listNodesIp = new ArrayList<String>();
	   for (String key: allNodeIp){
		   listNodesIp.add(key);
	   }
	   return listNodesIp;
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
	
	
	private static class MasterMovingMockTimer extends TimerTask{
		public void run() {
			if(mIsMaster){
				MasterMovedMessage connectionMetaData = generateMasterMovedMessage();
				MasterSelector.notifyMasterChanged(connectionMetaData);
			}
		}

		private MasterMovedMessage generateMasterMovedMessage() {
			ConnectionMetaData internal = new ConnectionMetaData("localhost", Constants.SECONDARY_INTERNAL_PORT);
			ConnectionMetaData external = new ConnectionMetaData("localhost", Constants.SECONDARY_EXTERNAL_PORT);
			
			if(ApplicationContext.isMaster()){
				internal = new ConnectionMetaData("localhost", Constants.DEFAULT_INTERNAL_PORT);
				external = new ConnectionMetaData("localhost", Constants.DEFAULT_EXTERNAL_PORT);
			}
			
			return new MasterMovedMessage(internal, external);
		}
	}
	
}