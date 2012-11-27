package eventHierarchy;

public class AuctionEvent extends Event
{
	private final long auctionId;
	
	public static final String AUCTION_STARTED = "AUCTION_STARTED";
	public static final String AUCTION_ENDED = "AUCTION_ENDED";
	
	public AuctionEvent(String id, String type, long timestamp, long auctionId) throws IllegalArgumentException 
	{
		super(id, type, timestamp);
		this.auctionId = auctionId;
		
		if(!type.equals(AUCTION_STARTED) || !type.equals(AUCTION_ENDED))
		{
			throw new IllegalArgumentException();
		}
	}
	
	public long getAuctionId()
	{
		return auctionId;
	}
}
