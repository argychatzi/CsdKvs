package com.kth.csd.networking;

import java.io.Serializable;
import java.net.InetAddress;
import java.net.InetSocketAddress;

import org.apache.mina.core.session.IoSession;

public class ConnectionMetaData implements Serializable{

	private static final long serialVersionUID = 3994328432578306965L;
	private String host;
	private int port;
	
	public ConnectionMetaData(String host, int port){
		this.host = host;
		this.port = port;
	}
	
	public String getHost() {
		return host;
	}
	public int getPort() {
		return port;
	}

	@Override
	public String toString() {
		return "ConnectionMetaData [host=" + host + ", port=" + port + "]";
	}

	public ConnectionMetaData(IoSession session){
		this.host = ((InetSocketAddress)session.getRemoteAddress()).getHostName();
		this.port =((InetSocketAddress)session.getRemoteAddress()).getPort();
	}
	
}
