package com.kth.csd.node.core;

import java.util.ArrayList;

import com.kth.csd.networking.ConnectionMetaData;
import com.kth.csd.networking.KvsClient;
import com.kth.csd.networking.interfaces.internal.ClientInternalInputInterface;
import com.kth.csd.networking.messages.AbstractNetworkMessage;

public class NodeFarm {
	private static ArrayList<KvsClient> mNodeFarm;
	private static final String TAG = NodeFarm.class.getCanonicalName();

	public NodeFarm(ArrayList<ConnectionMetaData> nodeIps) {
		mNodeFarm = new ArrayList<KvsClient>();

		for(ConnectionMetaData connectionMetaData: nodeIps){
			if(!connectionMetaData.equals(ApplicationContext.getOwnInternalConnection())){
				KvsClient newOne = new KvsClient(new ClientInternalInputInterface(), connectionMetaData);
				mNodeFarm.add(newOne);
			}
		}
	}

	public void broadCast(AbstractNetworkMessage message){
		System.out.print("mNodeFarm="+mNodeFarm==null);
		for(KvsClient node: mNodeFarm){
			//TODO check if master should be receiving braodcast messages!
			System.out.print("message="+message);
			System.out.print("node="+node);
			node.send(message);
		}
	}
}
