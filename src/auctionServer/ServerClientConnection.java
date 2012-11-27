package auctionServer;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

public class ServerClientConnection
{
	private PrintWriter out;
	private BufferedReader in;
	private final Socket clientSocket;
	
	public ServerClientConnection(Socket client)
	{
		this.clientSocket = client;
		
		try
		{
			out = new PrintWriter(client.getOutputStream(), true);
			
			in = new BufferedReader(new InputStreamReader(client.getInputStream()));
			
			//get the UDP Port from the Client after Login
			//String udpPort = in.readLine();
			
			
		}
		catch (IOException e)
		{
			System.err.println("Error reading from Socket.");
			e.printStackTrace();
			
			closeConnection();
		}
	}
	
	public synchronized void sendMessage(String message)
	{
		out.println(message);
	}
	
	public String getMessage() throws IOException
	{
		return in.readLine();
	}
	
	public InetAddress getAddress()
	{
		return clientSocket.getInetAddress();
	}
	
	public void closeConnection()
	{
		try
		{
			//send a shutdown message to the client
			sendMessage("Server shutdown. Close your client!");
			
			out.close();
			in.close();
			
			if (!clientSocket.isClosed())
			{
				System.out.println("closing ClientSocket");
				clientSocket.close();
			}
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
