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
	
	public void setHost(String newHost){
		host = newHost;
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

	public static ConnectionMetaData generateConnectionMetadaForRemoteEntityInSession(IoSession session){
		String host = ((InetSocketAddress)session.getRemoteAddress()).getHostName();
		int port =((InetSocketAddress)session.getRemoteAddress()).getPort();
		return new ConnectionMetaData(host, port);
	}
	
	public static ConnectionMetaData generateConnectionMetadaForLocalEntityInSession(IoSession session){
		String host = ((InetSocketAddress)session.getLocalAddress()).getHostName();
		int port = ((InetSocketAddress)session.getLocalAddress()).getPort();
		return new ConnectionMetaData(host, port);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((host == null) ? 0 : host.hashCode());
		result = prime * result + port;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ConnectionMetaData other = (ConnectionMetaData) obj;
		if (host == null) {
			if (other.host != null)
				return false;
		} else if (!host.equals(other.host))
			return false;
		if (port != other.port)
			return false;
		return true;
	}
	
}
