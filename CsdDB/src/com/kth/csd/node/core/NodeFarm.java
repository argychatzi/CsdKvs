package com.kth.csd.node.core;

import java.util.ArrayList;

import com.kth.csd.networking.ConnectionMetaData;
import com.kth.csd.networking.KvsClient;
import com.kth.csd.networking.interfaces.internal.ClientInternalInputInterface;
import com.kth.csd.networking.interfaces.internal.ServerInternalInputInterface;
import com.kth.csd.networking.messages.AbstractNetworkMessage;

public class NodeFarm {

	//NodeFarm is a list of many connections
	private static ArrayList<KvsClient> mNodeFarm;

	public NodeFarm(ArrayList<ConnectionMetaData> nodeIps) {
		mNodeFarm = new ArrayList<KvsClient>();
		for(ConnectionMetaData connectionMetaData: nodeIps){
			if(!nodeIps.equals(ApplicationContext.getMasterInternalConnection())){
				mNodeFarm.add(new KvsClient(new ClientInternalInputInterface(), connectionMetaData));
			}
		}
	}

	public void broadCast(AbstractNetworkMessage message){
		System.out.print("mNodeFarm="+mNodeFarm==null);
		for(KvsClient node: mNodeFarm){
			System.out.print("message="+message);
			System.out.print("node="+node);
			node.send(message);
		}
	}
}
