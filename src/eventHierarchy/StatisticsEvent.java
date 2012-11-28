package eventHierarchy;

public class StatisticsEvent extends Event implements EventTypeConstants
{
	private final double value;
	
	public StatisticsEvent(String id, String type, long timestamp, double value) throws IllegalArgumentException
	{
		super(id, type, timestamp);
		this.value = value;
		
		if(!(type.equals(USER_SESSIONTIME_MIN) || type.equals(USER_SESSIONTIME_MAX) || type.equals(USER_SESSIONTIME_AVG) ||
				type.equals(BID_PRICE_MAX) || type.equals(BID_COUNT_PER_MINUTE) ||
				type.equals(AUCTION_TIME_AVG) || type.equals(AUCTION_SUCCESS_RATIO)))
		{
			throw new IllegalArgumentException("invalid statistics event");
		}
	}
	
	public double getValue()
	{
		return value;
	}
}
