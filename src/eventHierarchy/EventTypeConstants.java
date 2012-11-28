package eventHierarchy;

public  interface EventTypeConstants {

	
	public static final String AUCTION_STARTED = "AUCTION_STARTED";
	public static final String AUCTION_ENDED = "AUCTION_ENDED";
	public static final String BID_PLACED = "BID_PLACED";
	public static final String BID_OVERBID = "BID_OVERBID";
	public static final String BID_WON = "BID_WON";
	public static final String USER_SESSIONTIME_MIN = "USER_SESSIONTIME_MIN";
	public static final String USER_SESSIONTIME_MAX = "USER_SESSIONTIME_MAX";
	public static final String USER_SESSIONTIME_AVG = "USER_SESSIONTIME_AVG";
	
	public static final String BID_PRICE_MAX = "BID_PRICE_MAX";
	public static final String BID_COUNT_PER_MINUTE = "BID_COUNT_PER_MINUTE";
	
	public static final String AUCTION_TIME_AVG = "AUCTION_TIME_AVG";
	public static final String AUCTION_SUCCESS_RATIO = "AUCTION_SUCCESS_RATIO";
	public static final String USER_LOGIN = "USER_LOGIN";
	public static final String USER_LOGOUT = "USER_LOGOUT";
	public static final String USER_DISCONNECTED = "USER_DISCONNECTED";
	
	public static final String ALL_EVENTS[] ={AUCTION_STARTED,AUCTION_ENDED,AUCTION_SUCCESS_RATIO,AUCTION_TIME_AVG,BID_COUNT_PER_MINUTE,BID_OVERBID,BID_PLACED,BID_PRICE_MAX,BID_WON,USER_DISCONNECTED,USER_LOGIN,USER_LOGOUT,USER_SESSIONTIME_AVG,USER_SESSIONTIME_MAX,USER_SESSIONTIME_MIN}; 
	
	public static final int NUMBER_OF_ALL_EVENTS=15;
}
