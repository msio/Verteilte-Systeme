package eventHierarchy;

public class UserEvent extends Event
{
	private final String userName;
	
	public static final String USER_LOGIN = "USER_LOGIN";
	public static final String USER_LOGOUT = "USER_LOGOUT";
	public static final String USER_DISCONNECTED = "USER_DISCONNECTED";
	
	public UserEvent(String id, String type, long timestamp, String userName) throws IllegalArgumentException
	{
		super(id, type, timestamp);
		this.userName = userName;
		
		if(!type.equals(USER_LOGIN) || !type.equals(USER_LOGOUT) || !type.equals(USER_DISCONNECTED))
		{
			throw new IllegalArgumentException();
		}
	}
	
	public String getUserName()
	{
		return userName;
	}
}
