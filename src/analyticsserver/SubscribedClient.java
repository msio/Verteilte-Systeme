package analyticsserver;

import java.util.ArrayList;

import managementClient.ManagementClientInterface;

public class SubscribedClient {
	private ManagementClientInterface client;
	private ArrayList<String> events;
	
	public SubscribedClient(ManagementClientInterface client){
		this.client=client;
		
	}
	
	public void addEvents(ArrayList<String> events){
		
		this.events= events;
	}
	
	public ArrayList<String> getEvents(){
		
		return events;
	} 
}
