package eventHierarchy;

import java.io.Serializable;

public abstract class Event implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
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
