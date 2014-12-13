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
import com.kth.csd.utils.Configuration;
import com.kth.csd.utils.ConfigurationReader;
import com.kth.csd.utils.Logger;


public class KvsNode {

	protected static final String TAG = KvsNode.class.getCanonicalName();
	private static final int sendRequestInterval = 10000;//1 second

    public static void main(String[] args) throws IOException {
    	
    	if(args.length >0 ){
    		final Configuration configuration = parseConfigurationFile(args[0]);
    		ApplicationContext.setMasterExternalConnection(configuration.getMasterExternalConnectionMetaData());
        	ApplicationContext.setMasterInternalConnection(configuration.getMasterInternalConnectionMetaData());
        	
        	ApplicationContext.setOwnExternalConnection(configuration.getOwnExternalConnectionMetaData());
        	ApplicationContext.setOwnInternalConnection(configuration.getOwnInternalConnectionMetaData());
    		
        	startMonitoringKvsSocket(new ServerInternalInputInterface(), ApplicationContext.getOwnInternalConnection().getPort());
        	startMonitoringKvsSocket(new ServerExternalInputInterface(), ApplicationContext.getOwnExternalConnection().getPort());
        	
        	Thread thread = new Thread(){
        		public void run() {
        			try {
						Thread.sleep(10000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} finally{
						ApplicationContext.generateNodeFarm(configuration.getNodesInFarm());
					}
        		}
        	};
        	thread.start();
        	startPollingFarmForStatistics();
    	}
    }
    
    private static void startPollingFarmForStatistics(){
    	Logger.d(TAG,"startPollingFarmForStatistics");
    	if(ApplicationContext.isMaster()) {
    		Logger.d(TAG,"isMaster");
			Timer timer = new Timer();
			TimerTask task = new TimerTask(){
				//For every second, broadcast a request
				@Override
				public void run() {
					//Broadcast to a nodeFarm. 
					//You put the IP and the port of slaves you want to connect into myArray
					//Then generate a NodeFarm
					if(ApplicationContext.getYcsbIPs()!=null){
						ArrayList<String> listOfYcsbClients = ApplicationContext.getYcsbIPs();
						AbstractNetworkMessage requestMsg = new StatisticsRequestMessage(listOfYcsbClients);		
						Logger.d(TAG, "requestMsg = " +  requestMsg.toString() );
						//Broadcast
						ApplicationContext.getNodeFarm().broadCast(requestMsg);
						Logger.d(TAG, "Broadcast finished" );
					}

				}
			};
			timer.scheduleAtFixedRate(task, 0, sendRequestInterval);	
		}
    }

    private static Configuration parseConfigurationFile(String fileNo) throws IOException {
    	Configuration configuration = ConfigurationReader.loadConfigurationFile(fileNo);
    	Logger.d(TAG, "configuration :: " + configuration);
    	return configuration; 
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