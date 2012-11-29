package testComponent;

import java.io.IOException;
import java.util.ArrayList;
import java.util.TimerTask;

public class ReloadListTask extends TimerTask
{
	private TestClientConnection con;
	
	public ReloadListTask(TestClientConnection con)
	{
		this.con = con;
	}
	
	public void run()
	{
		
		
		con.sendMessage("!list");
		con.setAuctionList();
	}
	
}
