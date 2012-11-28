package eventHierarchy;

public class BidEvent extends Event implements EventTypeConstants
{
	private final String userName;
	private final long auctionId;
	private final double price;
	
	
	
	public BidEvent(String id, String type, long timestamp, String userName, long auctionId, double price) throws IllegalArgumentException
	{
		super(id, type, timestamp);
		this.userName = userName;
		this.auctionId = auctionId;
		this.price = price;
		
		if(!(type.equals(BID_PLACED) || type.equals(BID_OVERBID) || type.equals(BID_WON)))
		{
			throw new IllegalArgumentException("Invalid bid event");
		}
	}
	
	public String getUserName()
	{
		return userName;
	}
	
	public long getAuctionId()
	{
		return auctionId;
	}
	
	public double getPrice()
	{
		return price;
	}
}
