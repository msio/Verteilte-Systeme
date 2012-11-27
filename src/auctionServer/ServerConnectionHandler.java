package auctionServer;
import java.io.IOException;

public class ServerConnectionHandler implements Runnable
{
	private final ServerConnection con;
	
	public ServerConnectionHandler(ServerConnection connection)
	{
		this.con = connection;
	}
	
	public void run()
	{
		// Wait in an endless loop for Connections from Clients
		try
		{
			
			while (true)
			{
				con.waitForConnection();
			}
			
		}
		catch (IOException e)
		{
			System.out.println("ServerConnectionHandler interrupt");
		}
	}
	
}
