package eventHierarchy;

public class BidEvent extends Event
{
	private final String userName;
	private final long auctionId;
	private final double price;
	
	public static final String BID_PLACED = "BID_PLACED";
	public static final String BID_OVERBID = "BID_OVERBID";
	public static final String BID_WON = "BID_WON";
	
	public BidEvent(String id, String type, long timestamp, String userName, long auctionId, double price) throws IllegalArgumentException
	{
		super(id, type, timestamp);
		this.userName = userName;
		this.auctionId = auctionId;
		this.price = price;
		
		if(!type.equals(BID_PLACED) || !type.equals(BID_OVERBID) || !type.equals(BID_WON))
		{
			throw new IllegalArgumentException();
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
