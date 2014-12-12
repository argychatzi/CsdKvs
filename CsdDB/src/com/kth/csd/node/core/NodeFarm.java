package com.kth.csd.node.core;

import java.util.ArrayList;

import com.kth.csd.networking.ConnectionMetaData;
import com.kth.csd.networking.KvsClient;
import com.kth.csd.networking.interfaces.internal.ClientInternalInputInterface;
import com.kth.csd.networking.interfaces.internal.ServerInternalInputInterface;
import com.kth.csd.networking.messages.AbstractNetworkMessage;
import com.kth.csd.utils.Logger;

public class NodeFarm {

	//NodeFarm is a list of many connections
	private static ArrayList<KvsClient> mNodeFarm;
	private static final String TAG = NodeFarm.class.getCanonicalName();

	public NodeFarm(ArrayList<ConnectionMetaData> nodeIps) {
		mNodeFarm = new ArrayList<KvsClient>();
		Logger.d(TAG,  "size "+nodeIps.size());

		for(ConnectionMetaData connectionMetaData: nodeIps){
			Logger.d(TAG, " connectionMetaData " + connectionMetaData.toString());
			Logger.d(TAG, " ApplicationContext.getMasterInternalConnection()" + ApplicationContext.getMasterInternalConnection());
			if(!connectionMetaData.equals(ApplicationContext.getMasterInternalConnection())){
				Logger.d(TAG,  "connectionMetaData " + connectionMetaData.toString());
				KvsClient newOne = new KvsClient(new ClientInternalInputInterface(), connectionMetaData);
				Logger.d(TAG,  "Return ");
				mNodeFarm.add(newOne);
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
