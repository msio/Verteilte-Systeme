package testComponent;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Random;

public class TestClientConnection
{
	private Socket serverSocket;
	// private DatagramSocket udpSocket;
	private PrintWriter out;
	private BufferedReader in;
	private String currentUserName;
	
	private Random random;
	private ArrayList<Integer> auctionList;
	private ArrayList<String> tcpMessage;
	
	public TestClientConnection(String serverName, int tcpPort)
	{
		try
		{
			serverSocket = new Socket(serverName, tcpPort);
			// udpSocket = new DatagramSocket(udpPort);
			
			// true = with auto Flush
			out = new PrintWriter(serverSocket.getOutputStream(), true);
			
			in = new BufferedReader(new InputStreamReader(serverSocket.getInputStream()));
		}
		catch (UnknownHostException e)
		{
			System.err.println("Don't know about host: " + serverName);
			System.exit(1);
		}
		catch (IOException e)
		{
			System.err.println("Couldn't get I/O for the connection to: " + serverName);
			System.exit(1);
		}
		
		random = new Random(System.nanoTime());
		auctionList = new ArrayList<Integer>();
		tcpMessage = new ArrayList<String>();
	}
	
	public synchronized void sendMessage(String message)
	{
		out.println(message);
	}
	
	public String getMessage() throws IOException
	{
		return in.readLine();
	}
	
	public synchronized String sendAndReceive(String message) throws IOException, InterruptedException
	{
		out.println(message);
		return in.readLine();
	}
	
	public synchronized void setAuctionList()
	{
		auctionList.clear();
		//System.out.println(tcpMessage);
		
		for (String message : tcpMessage)
		{
			
			String[] splitLine = message.split("\t");
			
			int value = 0;
			
			try
			{
				value = Integer.parseInt(splitLine[0].substring(0, splitLine[0].length() - 1));
			}
			catch (Exception e)
			{
				// continue with the next line
				continue;
			}
			
			auctionList.add(value);
			
		}
		
		tcpMessage.clear();
		
	}
	
	public synchronized ArrayList<Integer> getAuctionList()
	{
		// System.out.println(auctionList);
		return auctionList;
	}
	
	public Random getRandom()
	{
		return random;
	}
	
	public synchronized void addTcpMessage(String message)
	{
		if(message != null)
			tcpMessage.add(message);
	}
	
	/*
	 * public String getUdpMessage() throws IOException
	 * {
	 * byte[] buf = new byte[256];
	 * 
	 * DatagramPacket packet = new DatagramPacket(buf, buf.length);
	 * 
	 * udpSocket.receive(packet);
	 * 
	 * String message = new String(packet.getData());
	 * 
	 * return message;
	 * }
	 */
	
	public String getCurrentUserName()
	{
		return currentUserName;
	}
	
	public void setCurrentUserName(String userName)
	{
		currentUserName = userName;
	}
	
	public void closeConnection()
	{
		try
		{
			out.close();
			in.close();
			serverSocket.close();
			// udpSocket.close();
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public synchronized int getRandomAuction()
	{
		// get auction list from con
		
		
		if (auctionList.isEmpty())
			return -1;
		
		// select one random auction from the auctionList
		int rand = random.nextInt(auctionList.size());
		return auctionList.get(rand);
	}
	
}
