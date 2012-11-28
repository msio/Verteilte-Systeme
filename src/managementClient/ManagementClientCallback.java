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

	public String processEvent(Event event) throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getRegex() throws RemoteException {
		// TODO Auto-generated method stub
		return regex;
	}

	
	
	
}
