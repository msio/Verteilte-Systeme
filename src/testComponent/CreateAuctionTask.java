package testComponent;

import java.io.IOException;
import java.util.TimerTask;

public class CreateAuctionTask extends TimerTask
{
	private TestClientConnection con;
	
	private final int auctionDuration;
	
	public CreateAuctionTask(int auctionDuration, TestClientConnection con)
	{
		this.auctionDuration = auctionDuration;
		
		this.con = con;
	}
	
	public void run()
	{
		con.sendMessage("!create " + auctionDuration * 1000 + " another auction");
		
	}
	
}
