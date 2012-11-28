package analyticsserver;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

import eventHierarchy.Event;

public class AnalyticsServerImp extends UnicastRemoteObject implements AnalyticsInterface {
	
	private ArrayList<Event> eventBuffer;
	
	
	private static final long serialVersionUID = 1L;

	public AnalyticsServerImp() throws RemoteException{
		
		super();
		
		eventBuffer = new ArrayList<Event>();
	}

	@Override
	public String subscribe(String regex) throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void unsubscribe(String ID) throws RemoteException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void processEvent(Event event) throws RemoteException {
		
		eventBuffer.add(event);
		System.out.println(event.getType());
		
	}
	
}
