package eventHierarchy;

public class AuctionEvent extends Event implements EventTypeConstants
{
	private final long auctionId;
	
	
	
	public AuctionEvent(String id, String type, long timestamp, long auctionId) throws IllegalArgumentException 
	{
		super(id, type, timestamp);
		this.auctionId = auctionId;
		
		if(!(type.equals(AUCTION_STARTED) || type.equals(AUCTION_ENDED)))
		{
			throw new IllegalArgumentException("Invalid auction event");
		}
	}
	
	public long getAuctionId()
	{
		return auctionId;
	}
}
