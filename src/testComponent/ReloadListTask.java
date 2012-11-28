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
		ArrayList<Integer> auctionList = new ArrayList<Integer>();
		
		try
		{
			String message = con.sendAndReceive("!list");
			System.out.println(message);
			
			String[] split = message.split("\\r?\\n");
			
			for(int i = 0; i < split.length; i++)
			{
				String[] splitLine = split[i].split(" ");
				
				int value = 0;
				
				try
				{
					value = Integer.parseInt(splitLine[0]);
				}
				catch (NumberFormatException e)
				{
					//continue with the next line
					continue;
				}
				
				auctionList.add(value);
			}
			
			con.setAuctionList(auctionList);
		}
		catch (IOException e)
		{
			System.out.println("Could not send reload message.");
		}
	}
	
}
