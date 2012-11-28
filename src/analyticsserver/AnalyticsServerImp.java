package analyticsserver;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;

import managementClient.ManagementClientInterface;

import eventHierarchy.Event;

public class AnalyticsServerImp extends UnicastRemoteObject implements AnalyticsInterface {
	
	private static int ID;
	private ArrayList<Event> eventBuffer;
	private HashMap<String,ManagementClientInterface> clients; 
	
	
	private static final long serialVersionUID = 1L;

	public AnalyticsServerImp() throws RemoteException{
		
		super();
		
		eventBuffer = new ArrayList<Event>();
		clients= new HashMap<String, ManagementClientInterface>();
	}

	private static String getID(){
		
		return "ALS" + ID++;
	}
	
	
	@Override
	public String subscribe(ManagementClientInterface client) throws RemoteException {
			//clients	
		
			return null;
	}

	@Override
	public void unsubscribe(ManagementClientInterface client) throws RemoteException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void processEvent(Event event) throws RemoteException {
		
		
		
	}
	
}
