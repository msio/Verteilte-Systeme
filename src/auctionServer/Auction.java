package auctionServer;
import java.text.SimpleDateFormat;
import java.util.Calendar;


public class Auction
{
	private static int nextId = 0;
	private int myId;
	private String owner;
	private String description;
	private Calendar endDate;
	private String highestBidder;
	private double currentPrice;
	private SimpleDateFormat dateFormat;
	
	public Auction(String owner, int duration, String description)
	{
		dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm z");
		
		this.owner = owner;
		this.description = description;
		
		endDate = Calendar.getInstance();
		endDate.add(Calendar.MILLISECOND, duration);
		
		currentPrice = 0;
		highestBidder = null;
		
		myId = getNewId();
	}
	
	private synchronized int getNewId()
	{
		return nextId ++;
	}
	
	public int getId()
	{
		return myId;
	}
	
	public Calendar getEndDate()
	{
		return endDate;
	}
	
	public String getEndDateString()
	{
		return dateFormat.format(endDate.getTime());
	}
	
	public boolean bidOnAuction(String userName, double amount)
	{
		if(amount > currentPrice)
		{
			//the new bid is the new highest bid
			currentPrice = amount;
			highestBidder = userName;
		}
		else
		{
			//the new bid is smaller than the old one
			return false;
		}
		
		//successfully bided
		return true;
	}
	
	public String getHighestBidder()
	{
		return highestBidder;
	}
	
	public String getOwner()
	{
		return owner;
	}
	
	public double getCurrentPrice()
	{
		return currentPrice;
	}
	
	public String getDescription()
	{
		return description;
	}
	
	public String toString()
	{
		String output = "";
		
		output += myId + ".\t";
		output += "'"+ description +"' ";
		output += owner + " ";
		output += dateFormat.format(endDate.getTime()) + " ";
		
		output += currentPrice + " ";
		
		if(highestBidder == null)
		{
			output += "none";
		}
		else
		{
			output += highestBidder;
		}
		
		return output;
	}
}
