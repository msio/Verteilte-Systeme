package managementClient;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import eventHierarchy.Event;

public class ManagementClientCallback extends UnicastRemoteObject implements ManagementClientInterface  {

	private final String regex;
	
	protected ManagementClientCallback(String regex) throws RemoteException {
		super();
		this.regex=regex;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void processEvent(Event event) throws RemoteException {
		
		System.out.println(event.getType());
	}

	@Override
	public String getRegex() throws RemoteException {
		// TODO Auto-generated method stub
		return regex;
	}
	

	
	
	
}
