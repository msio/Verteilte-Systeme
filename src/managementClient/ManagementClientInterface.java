package managementClient;

import java.rmi.Remote;
import java.rmi.RemoteException;

import eventHierarchy.Event;


public interface ManagementClientInterface extends Remote{
		
		public String processEvent(Event event) throws RemoteException; 
		
		public String getRegex() throws RemoteException;
}
