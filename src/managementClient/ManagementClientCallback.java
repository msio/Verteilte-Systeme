package managementClient;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

import eventHierarchy.Event;

public class ManagementClientCallback extends UnicastRemoteObject implements ManagementClientInterface  {

	private final String regex;
	private ArrayList<String> messageList;
	private ManagementClientImp managementClientImp;
	
	protected ManagementClientCallback(String regex, ManagementClientImp managementClientImp) throws RemoteException {
		super();
		this.regex=regex;
		messageList = new ArrayList<String>();
		
		this.managementClientImp = managementClientImp;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void processEvent(Event event) throws RemoteException {
		
		String message = event.getType();
		
		if(managementClientImp.getAuto())
		{
			System.out.println(message);
		}
		else
		{
			managementClientImp.addMessage(message);
		}
	}

	@Override
	public String getRegex() throws RemoteException {
		// TODO Auto-generated method stub
		return regex;
	}
	
	
}
