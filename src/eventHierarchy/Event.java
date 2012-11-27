package eventHierarchy;

public abstract class Event
{
	private final String id;
	private final String type;
	private final long timestamp;
	
	public Event(String id, String type, long timestamp)
	{
		this.id = id;
		this.type = type;
		this.timestamp = timestamp;
	}
	
	public String getId()
	{
		return id;
	}
	
	public String getType()
	{
		return type;
	}
	
	public long getTimestamp()
	{
		return timestamp;
	}
}
