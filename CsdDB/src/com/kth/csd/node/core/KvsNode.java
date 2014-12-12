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
import com.kth.csd.networking.messages.AbstractNetworkMessage;
import com.kth.csd.networking.messages.StatisticsRequestMessage;
import com.kth.csd.node.Constants;
import com.kth.csd.utils.Configuration;
import com.kth.csd.utils.ConfigurationReader;
import com.kth.csd.utils.Logger;


public class KvsNode {

	protected static final String TAG = KvsNode.class.getCanonicalName();
	private static ArrayList<ConnectionMetaData> allNodeProperties = new ArrayList<ConnectionMetaData>();
	public static String[] allNodeIp = { "192.168.0.2", "192.168.0.3", "192.168.0.4", "192.168.0.5", "192.168.0.7","192.168.0.8",
		"192.168.0.10","192.168.0.11","192.168.0.12","192.168.0.13" };
	public static String[] allYCSBIp = { "192.168.0.1", "192.168.0.6","192.168.0.9" };// for testing
	private static AbstractNetworkMessage requestMsg;
	private static AbstractNetworkMessage resultsMsg;
	private static ArrayList<ConnectionMetaData> myArray;
	private static final int sendRequestInterval = 100000;//1 second

	public KvsNode(ArrayList<ConnectionMetaData> myArray) {

	}

	public static void main(String[] args) throws IOException {
    	if(args.length >0 ){
    		parseConfigurationFile(args[0]);
    		
        	startMonitoringKvsSocket(new ServerInternalInputInterface(), ApplicationContext.getInternalConnection().getPort());
        	startMonitoringKvsSocket(new ServerExternalInputInterface(), ApplicationContext.getExternalConnection().getPort());
    	}

		String nodeType = args[0];
		System.out.println("nodeType = "+ nodeType);
		if (nodeType.equals("master")){
			ApplicationContext.setIsMasterTrue();	
			Logger.d(TAG, "I am a master");
		}
		else
			Logger.d(TAG, "I am a slave");
			

		if(ApplicationContext.isMaster()){
			System.out.println("I am the master");
			System.out.println("Sending req for stats");
			Timer timer = new Timer();
			TimerTask task = new TimerTask(){
				//For every second, broadcast a request
				@Override
				public void run() {
					//Broadcast to a nodeFarm. 
					//You put the IP and the port of slaves you want to connect into myArray
					//Then generate a NodeFarm
					myArray = new ArrayList<ConnectionMetaData>();
					myArray.add(new ConnectionMetaData(allNodeIp[1],ApplicationContext.getInternalConnection().getPort()));
					ApplicationContext.generateNodeFarm(myArray);
					//The message will tell the slaves, please ping the clients
					//So, the listOfYcsbClient should be a part of the message		
					ArrayList<String> listOfYcsbClients = new ArrayList<String>();
					for(int i =0;i<allYCSBIp.length;i++){
						listOfYcsbClients.add(allYCSBIp[i]);
					}
					ApplicationContext.setYcsbIPs(listOfYcsbClients);		
					AbstractNetworkMessage requestMsg = new StatisticsRequestMessage(listOfYcsbClients);		
					Logger.d(TAG, "requestMsg = " +  requestMsg.toString() );
					
					//Broadcast
					ApplicationContext.getNodes().broadCast(requestMsg);
					Logger.d(TAG, "Broadcast finished" );
				}
				
			};
			timer.scheduleAtFixedRate(task, 0, sendRequestInterval);	
		}
		

//		HashMap<String, Integer> results = new HashMap<String, Integer>();
//		results.put("resultsRequest", 1);
//		AbstractNetworkMessage resultsMsg = new StatisticsResultMessage(results);
//		System.out.println("resultsMsg =  " +resultsMsg);
//		ApplicationContext.getNodes().broadCast(resultsMsg);
//		System.out.println("*******Message sent on broadcast*******");
//		System.out.println("Listening for incoming connections as master");

	}

//	public static String[] allNodeIp = { "192.168.0.2", "192.168.0.3", "192.168.0.4", "192.168.0.5", "192.168.0.7","192.168.0.8",
//		"192.168.0.10","192.168.0.11","192.168.0.12","192.168.0.13" };
	//public static String[] allNodeIp = { "10.0.0.1", "10.0.0.3" };// for testing
		
    
    private static void parseConfigurationFile(String fileNo) throws IOException {
    	
    	Configuration configuration = ConfigurationReader.loadConfigurationFile(fileNo);
    	
    	Logger.d(TAG, "configuration :: " + configuration);
    	
    	ApplicationContext.setMasterExternalConnection(configuration.getMasterExternalConnectionMetaData());
    	ApplicationContext.setMasterInternalConnection(configuration.getMasterInternalConnectionMetaData());
    	
    	ApplicationContext.setOwnExternalConnection(configuration.getOwnExternalConnectionMetaData());
    	ApplicationContext.setOwnInternalConnection(configuration.getOwnInternalConnectionMetaData());
    	
    	ApplicationContext.generateNodeFarm(configuration.getNodesInFarm());
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
		System.out.println("handler="+handler.toString());
		
		IoAcceptor acceptor = new NioSocketAcceptor();
		acceptor.getFilterChain().addLast( "codec", new ProtocolCodecFilter( new ObjectSerializationCodecFactory()));
		acceptor.getFilterChain().addLast( "logger", new LoggingFilter() );

		acceptor.setHandler(handler);

		acceptor.getSessionConfig().setReadBufferSize( 2*2048 );
		acceptor.bind( new InetSocketAddress(portNumber) );
		System.out.println("acceptor after bind="+acceptor.toString());

		Logger.d(TAG, "Opened port " + portNumber);
	}
}