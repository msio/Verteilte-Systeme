package analyticsserver;

import java.rmi.Remote;
import java.rmi.RemoteException;

import managementClient.ManagementClientInterface;

import eventHierarchy.Event;

public interface AnalyticsInterface extends Remote{
			
		public String subscribe(ManagementClientInterface client) throws RemoteException;
		
		public void unsubscribe(ManagementClientInterface client) throws RemoteException;
		
		public void processEvent(Event event) throws RemoteException;
}
