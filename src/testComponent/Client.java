package testComponent;

import java.io.IOException;
import java.util.Scanner;
import java.util.Timer;

import auctionServer.AuctionStatusThread;

public class Client implements Runnable
{
	private final String hostname;
	private final int tcpPort;
	
	private final String username;
	
	// udpPort is not in use
	private final int udpPort = 0;
	
	private final int auctionsPerMinute;
	private final int auctionDuration;
	private final int updateIntervalSec;
	private final int bidsPerMinute;
	
	public Client(String hostname, int tcpPort, String username, int auctionsPerMinute, int auctionDuration, int updateIntervalSec, int bidsPerMinute)
	{
		this.hostname = hostname;
		this.tcpPort = tcpPort;
		
		this.username = username;
		
		this.auctionsPerMinute = auctionsPerMinute;
		this.auctionDuration = auctionDuration;
		this.updateIntervalSec = updateIntervalSec;
		this.bidsPerMinute = bidsPerMinute;
	}
	
	public void run()
	{
		TestClientConnection con = new TestClientConnection(hostname, tcpPort);
		
		Thread tcpListener = new Thread(new ClientTcpListener(con));
		tcpListener.start();
		
		long startTime = System.nanoTime();
		
		// login in auctionServer
		con.sendMessage("!login " + username + " 0");
		
		// Timer for the Tasks
		Timer timer1 = new Timer();
		Timer timer2 = new Timer();
		Timer timer3 = new Timer();
		
		// create Auctions
		CreateAuctionTask createAuctionTask = new CreateAuctionTask(auctionDuration, con);
		timer1.schedule(createAuctionTask, 0, 1000 * 60 / auctionsPerMinute);
		
		// bid on auctions
		BidOnAuctionTask bidOnAuctionTask = new BidOnAuctionTask(startTime, con);
		timer2.schedule(bidOnAuctionTask, 5000, 1000 * 60 / bidsPerMinute);
		
		// reload the !list
		ReloadListTask reloadListTask = new ReloadListTask(con);
		timer3.schedule(reloadListTask, 4000, 1000 * updateIntervalSec);
		
		try
		{
			while (!Thread.interrupted())
			{
				Thread.sleep(10000);
			}
		}
		catch (InterruptedException e)
		{
			
		}
		
		// logout from auctinServer
		con.sendMessage("!logout");
		
		System.out.println("closing the connection to the server");
		
		tcpListener.interrupt();
		
		timer1.cancel();
		timer2.cancel();
		timer3.cancel();
		
		
		
		con.closeConnection();
		
		
	}
}
