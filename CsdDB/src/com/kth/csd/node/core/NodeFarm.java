package com.kth.csd.node.core;

import java.util.ArrayList;
import java.util.List;

import com.kth.csd.networking.ConnectionMetaData;
import com.kth.csd.networking.KvsClient;
import com.kth.csd.networking.interfaces.internal.ClientInternalInputInterface;
import com.kth.csd.networking.messages.AbstractNetworkMessage;
import com.kth.csd.networking.messages.OperationReadMessage;
import com.kth.csd.node.operation.KeyValueEntry;
import com.kth.csd.utils.Logger;

public class NodeFarm {
	
	public static ArrayList<KvsClient> mNodeFarm;
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
				KvsClient client = new KvsClient(new ClientInternalInputInterface(), connectionMetaData);
				mNodeFarm.add(client);
			}
		}
	}

	public void broadCast(AbstractNetworkMessage message){
		Logger.d(TAG, "broadCast : " + message);
		for(KvsClient node: mNodeFarm){
			node.send(message);
		}
	}
}
