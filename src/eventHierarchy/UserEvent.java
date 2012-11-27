package eventHierarchy;

public class UserEvent extends Event implements EventTypeConstants
{
	private final String userName;
	
	
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
