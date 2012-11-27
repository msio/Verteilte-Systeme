package biddingClient;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class ClientUdpListener implements Runnable
{
	private ClientConnection clientConnection;
	
	public ClientUdpListener(ClientConnection clientConnection)
	{
		this.clientConnection = clientConnection;
	}
	
	public void run()
	{
		try
		{
			while (true)
			{
				// Just wait for messages, and send them to the clientConnection
				
				String message = clientConnection.getUdpMessage();
				
				if (message.contains("!overbid"))
				{
					// Display the overbid message
					String[] sub = message.split(" ");
					if (sub.length >= 2)
					{
						String description = "";
						
						for (int i = 1; i < sub.length; i++)
						{
							description += sub[i].trim();
							if (i != (sub.length - 1))
							{
								// Add a whitespace between the words
								description += " ";
							}
						}
						
						System.out.println("You have been overbid on '" + description + "'!");
					}
					else
					{
						// invalid message
					}
				}
				else if (message.contains("!auction-end"))
				{
					// display the auction end message
					String[] sub = message.split(" ");
					if (sub.length >= 4)
					{
						String winner = sub[1];
						Double amount = Double.valueOf(sub[2]);
						String description = "";
						
						for (int i = 3; i < sub.length; i++)
						{
							description += sub[i].trim();
							if (i != (sub.length - 1))
							{
								// Add a whitespace between the words
								description += " ";
							}
						}
						
						if (winner.equalsIgnoreCase(clientConnection.getCurrentUserName()))
						{
							winner = "You";
						}
						
						System.out.println("The auction '" + description + "' has ended. " + winner + " won with " + amount + "!");
					}
					else
					{
						// nothing, the message is invalid
					}
				}
				else
				{
					// unrecognized message
				}
			}
		}
		catch (IOException e)
		{
			System.out.println("Client Udp Listener interrupt");
		}
	}
}
