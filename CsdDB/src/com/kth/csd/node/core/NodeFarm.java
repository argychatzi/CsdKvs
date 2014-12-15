package com.kth.csd.node.core;

import java.util.ArrayList;

import com.kth.csd.networking.ConnectionMetaData;
import com.kth.csd.networking.KvsClient;
import com.kth.csd.networking.interfaces.internal.ClientInternalInputInterface;
import com.kth.csd.networking.messages.AbstractNetworkMessage;

public class NodeFarm {
	
	private static ArrayList<KvsClient> mNodeFarm;
	private static final String TAG = NodeFarm.class.getCanonicalName();
	private static NodeFarm sNodeFarm;
	
	public static NodeFarm getInstance(ArrayList<ConnectionMetaData> nodeIps){
		if (sNodeFarm == null){
			sNodeFarm = new NodeFarm(nodeIps);
		}
		return sNodeFarm;
	}
	
	
	private NodeFarm(ArrayList<ConnectionMetaData> nodeIps) {
		mNodeFarm = new ArrayList<KvsClient>();

		for(ConnectionMetaData connectionMetaData: nodeIps){
			if(!connectionMetaData.equals(ApplicationContext.getOwnInternalConnection())){
				KvsClient newOne = new KvsClient(new ClientInternalInputInterface(), connectionMetaData);
				mNodeFarm.add(newOne);
			}
		}
	}

	public void broadCast(AbstractNetworkMessage message){
		for(KvsClient node: mNodeFarm){
			System.out.print("Sending message="+message + " to node = "+node);
			node.send(message);
		}
	}
}
