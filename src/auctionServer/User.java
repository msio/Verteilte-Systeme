package auctionServer;
import java.net.InetAddress;


public class User
{
	private String username;
	private int udpPort;
	private InetAddress address;
	private boolean loggedIn;
	
	public User(String username)
	{
		this.username = username;
		loggedIn = false;
		
		udpPort = -1;
		address = null;
	}
	
	
	public String toString()
	{
		return username;
	}
	
	public boolean userLogin(int udpPort, InetAddress address)
	{
		if(loggedIn)
		{
			//already logged in
			return false;
		}
		this.address = address;
		this.udpPort = udpPort;
		loggedIn = true;
		
		//Successfully logged in
		return true;
	}
	
	public boolean userLogout()
	{
		if(!loggedIn)
		{
			//user is not logged in, so he cant log out
			return false;
		}
		//just make udpPort negative so you know it is not there
		udpPort = -1;
		address =  null;
		loggedIn = false;
		
		//Successfully logged out
		return true;
	}
	
	public boolean isLoggedIn()
	{
		return loggedIn;
	}
	
	public int getUdpPort()
	{
		return udpPort;
	}
	
	public InetAddress getAddress()
	{
		return address;
	}
	

}
