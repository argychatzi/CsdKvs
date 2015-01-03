package com.kth.csd.node.core;

import java.io.IOException;
import java.net.InetSocketAddress;

import org.apache.mina.core.service.IoAcceptor;
import org.apache.mina.core.service.IoHandler;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.serialization.ObjectSerializationCodecFactory;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;

import com.kth.csd.networking.interfaces.external.ServerExternalInputInterface;
import com.kth.csd.networking.interfaces.internal.ServerInternalInputInterface;
import com.kth.csd.node.executors.MasterSelector;
import com.kth.csd.node.executors.StatisticsCollector;
import com.kth.csd.node.executors.WriteOperationsPerSecCollector;
import com.kth.csd.utils.Configuration;
import com.kth.csd.utils.ConfigurationReader;
import com.kth.csd.utils.Logger;


public class KvsNode {

	private static final int SETUP_DELAY = 5000;
	protected static final String TAG = KvsNode.class.getCanonicalName();

    public static void main(String[] args) throws IOException {
    	
    	if(args.length >0 ){
    		final Configuration configuration = parseConfigurationFile(args[0]);
    		ApplicationContext.setMasterExternalConnection(configuration.getMasterExternalConnectionMetaData());
        	ApplicationContext.setMasterInternalConnection(configuration.getMasterInternalConnectionMetaData());
        	
        	ApplicationContext.setOwnExternalConnection(configuration.getOwnExternalConnectionMetaData());
        	ApplicationContext.setOwnInternalConnection(configuration.getOwnInternalConnectionMetaData());
    		
        	startMonitoringKvsSocket(new ServerInternalInputInterface(), ApplicationContext.getOwnInternalConnection().getPort());
        	startMonitoringKvsSocket(new ServerExternalInputInterface(), ApplicationContext.getOwnExternalConnection().getPort());
        	
        	Logger.d(TAG,"MY IP is " + configuration.getOwnInternalConnectionMetaData());
        	Logger.d(TAG,"I am Master ? " + ApplicationContext.isMaster());
        	Thread thread = new Thread(){
        		public void run() {
        			try {
						Thread.sleep(SETUP_DELAY);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} finally{
						ApplicationContext.generateNodeFarm(configuration.getNodesInFarm());
						//We need here a timer to repeat the statistics collection and master selection tasks
						/*if (ApplicationContext.isMaster()){
							Logger.d(TAG,"inside");
							new StatisticsCollector().startPollingFarm();			
							new MasterSelector().execute();
							new WriteOperationsPerSecCollector().execute();
						}*/
						//There is no need for checking isMaster condition here
						//otherwise after master handover, these tasks won't be performed by the new master
						new StatisticsCollector().startPollingFarm();
						new WriteOperationsPerSecCollector().execute();
						new MasterSelector().execute();
						
					}
        		}
        	};
        	thread.start();
    	}
    }
    
	private static Configuration parseConfigurationFile(String fileNo) throws IOException {
    	Configuration configuration = ConfigurationReader.loadConfigurationFile(fileNo);
    	return configuration; 
	}

    private static void startMonitoringKvsSocket(IoHandler handler, final int portNumber) throws IOException {
		System.out.println("handler="+handler.toString());
		
		IoAcceptor acceptor = new NioSocketAcceptor();
		acceptor.getFilterChain().addLast( "codec", new ProtocolCodecFilter( new ObjectSerializationCodecFactory()));
		acceptor.getFilterChain().addLast( "logger", new LoggingFilter() );

		acceptor.setHandler(handler);

		acceptor.getSessionConfig().setReadBufferSize( 2*2048 );
		acceptor.bind( new InetSocketAddress(portNumber) );
		System.out.println("acceptor after bind="+acceptor.toString());
	}
 
}