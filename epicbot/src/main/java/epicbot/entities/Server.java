package epicbot.entities;

import java.io.Serializable;

/**
 * @author Kyle Minter (Kale Monkey)
 */
public class Server implements Serializable
{
	private static final long serialVersionUID = 1;
	private String serverName;
	private String hostname;
	private int port;
	private int rconPort;
	private String rconPassword;
	
	public Server(String name, String host, int p, int rp, String r$)
	{
		serverName = name;
		hostname = host;
		port = p;
		rconPort = rp;
		rconPassword = r$;
	}
	
	public String getServerName()
	{
		return serverName;
	}
	
	public String getHostName()
	{
		return hostname;
	}
	
	public int getPort()
	{
		return port;
	}
	
	public int getRconPort()
	{
		return rconPort;
	}
	
	public String getRconPassword()
	{
		return rconPassword;
	}
	
	public String toString()
	{
		return hostname + "-" + port + "-" + rconPort;
	}
}
