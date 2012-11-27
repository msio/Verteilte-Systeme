package biddingClient;
import java.io.IOException;

public class ClientTcpListener implements Runnable
{
	private ClientConnection clientConnection;
	
	public ClientTcpListener(ClientConnection clientConnection)
	{
		this.clientConnection = clientConnection;
	}
	
	public void run()
	{
		try
		{
			while (true)
			{
				String message = clientConnection.getMessage();
				
				if(message != null && message.equals("!end"))
				{
					//shit
				}
				
				if(message != null)
					System.out.println(message);
			}
		}
		catch (IOException e)
		{
			System.out.println("Client Tcp Listener interrupt");
		}
	}
	
}
