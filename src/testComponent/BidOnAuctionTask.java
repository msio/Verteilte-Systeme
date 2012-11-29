package testComponent;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.TimerTask;

public class BidOnAuctionTask extends TimerTask
{
	private TestClientConnection con;
	private final long startTime;
	
	public BidOnAuctionTask(long startTime, TestClientConnection con)
	{
		this.con = con;
		this.startTime = startTime;
	}
	
	public void run()
	{
		long currentTime = System.nanoTime();
		
		int auctionId = con.getRandomAuction();
		
		if(auctionId < 0)
			return;
		
		//calculate the amount: startTime - currentTime
		double amount = (currentTime - startTime)/1000000000.d;
		
		con.sendMessage("!bid " + auctionId + " " + amount);
		
		//System.out.println("!bid " + auctionId + " " + amount);
		
	}
	
}
