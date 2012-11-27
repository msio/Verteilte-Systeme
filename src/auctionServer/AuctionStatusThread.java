package auctionServer;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.TimerTask;

public class AuctionStatusThread extends TimerTask
{
	public final ServerConnection serverConnection;
	
	public AuctionStatusThread(ServerConnection connection)
	{
		this.serverConnection = connection;
	}
	
	public void run()
	{
		try
		{
			
			//look for expired auctions, delete them and send message to the highest bidder and the auction owner
			serverConnection.lookForEndedAuctions();
			
			//Send all queued messages
			/* TODO: not needed in exercise 2
			serverConnection.sendUdpMessages();
			*/
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
