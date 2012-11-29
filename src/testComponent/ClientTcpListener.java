package testComponent;
import java.io.IOException;

public class ClientTcpListener implements Runnable
{
	private TestClientConnection clientConnection;
	
	public ClientTcpListener(TestClientConnection clientConnection)
	{
		this.clientConnection = clientConnection;
	}
	
	public void run()
	{
		try
		{
			while (!Thread.interrupted())
			{
				String message = clientConnection.getMessage();
				
				//System.out.println(message);
				
				//add the message to the message list
				clientConnection.addTcpMessage(message);
			}
		}
		catch (IOException e)
		{
			System.out.println("Client Tcp Listener interrupt");
		}
	}
	
}
