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
		
		//get auction list from con
		ArrayList<Integer> auctionList = con.getAuctionList();
		
		if(auctionList.isEmpty())
			return;
		
		//select one random auction from the auctionList
		Random random = con.getRandom();
		int rand = random.nextInt(auctionList.size());
		int auctionId = auctionList.get(rand);
		
		//calculate the amount: startTime - currentTime
		double amount = (startTime - currentTime)/1000.d;
		
		//send Message
		try
		{
			con.sendAndReceive("!bid " + auctionId + " " + amount);
		}
		catch (IOException e)
		{
			System.out.println("Could not send bid message.");
		}
		
	}
	
}
