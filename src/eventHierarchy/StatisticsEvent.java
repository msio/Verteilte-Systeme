package eventHierarchy;

public class StatisticsEvent extends Event
{
	private final double value;
	
	public static final String USER_SESSIONTIME_MIN = "USER_SESSIONTIME_MIN";
	public static final String USER_SESSIONTIME_MAX = "USER_SESSIONTIME_MAX";
	public static final String USER_SESSIONTIME_AVG = "USER_SESSIONTIME_AVG";
	
	public static final String BID_PRICE_MAX = "BID_PRICE_MAX";
	public static final String BID_COUNT_PER_MINUTE = "BID_COUNT_PER_MINUTE";
	
	public static final String AUCTION_TIME_AVG = "AUCTION_TIME_AVG";
	public static final String AUCTION_SUCCESS_RATIO = "AUCTION_SUCCESS_RATIO";
	
	public StatisticsEvent(String id, String type, long timestamp, double value) throws IllegalArgumentException
	{
		super(id, type, timestamp);
		this.value = value;
		
		if(!type.equals(USER_SESSIONTIME_MIN) || !type.equals(USER_SESSIONTIME_MAX) || !type.equals(USER_SESSIONTIME_AVG) ||
				!type.equals(BID_PRICE_MAX) || !type.equals(BID_COUNT_PER_MINUTE) ||
				!type.equals(AUCTION_TIME_AVG) || !type.equals(AUCTION_SUCCESS_RATIO))
		{
			throw new IllegalArgumentException();
		}
	}
	
	public double getValue()
	{
		return value;
	}
}
