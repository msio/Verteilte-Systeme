package auctionServer;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Timer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import eventHierarchy.UserEvent;

import analyticsserver.AnalyticsInterface;

public class ServerConnection 
{
	
	private ServerSocket serverSocket;
	/* TODO: not used in exercise 2
	private DatagramSocket udpSocket;
	private final int udpPort = 50005;
	*/
	private ExecutorService pool;
	private ArrayList<User> userList;
	private ArrayList<Auction> auctionList;
	private HashMap<String, String> udpMessageList;
	private Timer timer;
	private ArrayList<ServerClientConnection> clientConnectionList;
	private AnalyticsInterface analyticsServer;
	
	public ServerConnection(int tcpPortNumber,AnalyticsInterface analyticsServer)
	{
		try
		{
			serverSocket = new ServerSocket(tcpPortNumber);
			/* TODO: not used in exercise 2
			udpSocket = new DatagramSocket(udpPort);
			*/
		}
		catch (IOException e)
		{
			System.err.println("Could not listen on port: " + tcpPortNumber + ".");
			System.exit(1);
		}
		
		pool = Executors.newCachedThreadPool();
		
		userList = new ArrayList<User>();
		auctionList = new ArrayList<Auction>();
		udpMessageList = new HashMap<String, String>();
		clientConnectionList = new ArrayList<ServerClientConnection>();
		this.analyticsServer = analyticsServer;
		
		// Start the auction status handler
		AuctionStatusThread statusThread = new AuctionStatusThread(this);
		
		timer = new Timer();
		timer.schedule(statusThread, 500, 500);
	}
	
	public void waitForConnection() throws IOException
	{
		Socket clientSocket = serverSocket.accept();
		
		pool.execute(new ServerConnectionThread(clientSocket,this));
		
	}
	
	/**
	 * get Analytics Server
	 * @return Analytics Server Interface
	 */
	
	public AnalyticsInterface getAnalyticsServer(){
		
		return analyticsServer;
	}
	
	/**
	 * adds a User to the userList
	 * 
	 * @param user
	 * @return true: if the user is created; false: if the user already exists and therefore is not added to the list
	 */
	public synchronized boolean addUser(User user)
	{
		// if username does not exist: add the user to the userList
		for (User listUser : userList)
		{
			if (user.toString().compareToIgnoreCase(listUser.toString()) == 0)
			{
				// User already exists, so dont add the User to the list
				return false;
			}
		}
		userList.add(user);
		
		return true;
	}
	
	
	/**
	 * 
	 * @param username
	 * @param udpPort
	 * @return true if the user is logged in; false if the user does not exist, or cant log in
	 */
	public synchronized boolean logUserIn(String username, int udpPort, InetAddress address)
	{
		for (User listUser : userList)
		{
			if (listUser.toString().compareToIgnoreCase(username) == 0)
			{
				// log in the founded user
				if (!listUser.userLogin(udpPort, address))
				{
					return false;
				}
				
				try {
					analyticsServer.processEvent(new UserEvent("ID","USER_LOGIN" , System.currentTimeMillis(), username.toString()));
				} catch (RemoteException e) {
					System.out.println("LOGIN processEvent: Remote Exception ");
					e.printStackTrace();
				
				} catch (IllegalArgumentException e) {
					System.out.println("LOGIN processEvent: Illegal Argument Exception");
					e.printStackTrace();
				}
				return true;
			}
		}
		
		return false;
	}
	
	/**
	 * 
	 * @param username
	 * @return true if the user is logged out; false if the user does not exist, or cant log out
	 */
	public synchronized boolean logUserOut(String username)
	{
		for (User listUser : userList)
		{
			if (username != null)
			{
				if (listUser.toString().compareToIgnoreCase(username) == 0)
				{
					// log out the founded user
					if (!listUser.userLogout())
					{
						return false;
					}
					return true;
				}
			}
		}
		
		return false;
	}
	
	public synchronized void addAuction(Auction auction)
	{
		auctionList.add(auction);
	}
	
	public synchronized boolean removeAuction(int auctionId)
	{
		for (Auction auction : auctionList)
		{
			if (auction.getId() == auctionId)
			{
				auctionList.remove(auction);
				return true;
			}
		}
		
		// Auction does not exist
		return false;
	}
	
	public synchronized ArrayList<Auction> getAuctionList()
	{
		return auctionList;
	}
	
	public synchronized Auction getAuction(int auctionId)
	{
		for (Auction auction : auctionList)
		{
			if (auction.getId() == auctionId)
			{
				return auction;
			}
		}
		
		return null;
	}
	
	public synchronized boolean bidOnAuction(String userName, int auctionId, double amount)
	{
		for (Auction auction : auctionList)
		{
			if (auction.getId() == auctionId)
			{
				if (!auction.bidOnAuction(userName, amount))
				{
					// the old bid is higher
					return false;
				}
			}
		}
		
		return true;
	}
	
	public synchronized void addUdpMessage(String userName, String message)
	{
		udpMessageList.put(userName, message);
	}
	
	/* TODO: not used in exercise 2
	public synchronized void sendUdpMessages()
	{
		try
		{
			Iterator<Map.Entry<String, String>> iter = udpMessageList.entrySet().iterator();
			
			while (iter.hasNext())
			{
				Map.Entry<String, String> entry = iter.next();
				
				// look if the User is logged in
				for (User listUser : userList)
				{
					if (listUser.toString().compareToIgnoreCase(entry.getKey()) == 0)
					{
						// see if the user is logged in
						if (listUser.isLoggedIn())
						{
							// send him the message
							
							byte[] buf = entry.getValue().getBytes();
							
							DatagramPacket packet = new DatagramPacket(buf, buf.length, listUser.getAddress(), listUser.getUdpPort());
							
							udpSocket.send(packet);
							
							// now delete the message from the queue
							iter.remove();
						}
					}
				}
			}
		}
		catch (IOException e)
		{
			// something is wrong with sending the message, so close the Server!!!
			closeConnection();
		}
	}
	*/
	
	public synchronized void lookForEndedAuctions()
	{
		// create new Calendar object with the actual time
		Calendar now = Calendar.getInstance();
		
		// look for expired auctions, delete them and send message to the highest bidder and the auction owner
		Iterator<Auction> iter = auctionList.iterator();
		
		while (iter.hasNext())
		{
			Auction auction = iter.next();
			
			// look if an auction is over
			if (now.after(auction.getEndDate()))
			{
				// auction is over, so delete the auction and send message to highest bidder and the owner of the auction
				
				String highestBidder = auction.getHighestBidder();
				
				if (highestBidder != null)
				{
					// highest bidder
					addUdpMessage(auction.getHighestBidder(),
							"!auction-end " + highestBidder + " " + auction.getCurrentPrice() + " " + auction.getDescription());
				}
				else
				{
					highestBidder = "none";
				}
				
				// owner
				addUdpMessage(auction.getOwner(), "!auction-end " + highestBidder + " " + auction.getCurrentPrice() + " " + auction.getDescription());
				
				// delete auction, so nobody can bid anymore
				iter.remove();
				
			}
		}
	}
	
	public synchronized void addServerClientConnection(ServerClientConnection con)
	{
		clientConnectionList.add(con);
	}
	
	public void closeConnection()
	{
		System.out.println("Cancel the timer schcedule!");
		// end the scheduled timer
		timer.cancel();
		
		// dont accept new Connections any more
		pool.shutdown();
		
		for(ServerClientConnection con : clientConnectionList)
		{
			con.closeConnection();
		}
		
		try
		{
			System.out.println("Shuting down the ThreadPool");
			// wait 4 Seconds for ending of all Connections
			pool.awaitTermination(4L, TimeUnit.SECONDS);
			if (!serverSocket.isClosed())
			{
				System.out.println("Closing ServerSocket");
				serverSocket.close();
			}
			/* TODO: not used in exercise 2
			if (!udpSocket.isClosed())
			{
				System.out.println("Closing udpSocket");
				udpSocket.close();
			}
			*/
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (InterruptedException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
