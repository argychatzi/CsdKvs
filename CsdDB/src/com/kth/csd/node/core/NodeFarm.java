package com.kth.csd.node.core;

import java.util.ArrayList;

import com.kth.csd.networking.ConnectionMetaData;
import com.kth.csd.networking.KvsClient;
import com.kth.csd.networking.interfaces.internal.ClientInternalInputInterface;
import com.kth.csd.networking.messages.AbstractNetworkMessage;

public class NodeFarm {

	private static ArrayList<KvsClient> mNodeFarm;

	public NodeFarm(ArrayList<ConnectionMetaData> nodeIps) {
		for(ConnectionMetaData connectionMetaData: nodeIps){
			mNodeFarm.add(new KvsClient(new ClientInternalInputInterface(), connectionMetaData));
		}
	}

	public void broadCast(AbstractNetworkMessage message){
		for(KvsClient node: mNodeFarm){
			node.send(message);
		}
	}
	
}
