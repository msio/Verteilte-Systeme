package analyticsserver;

import java.rmi.Remote;
import java.rmi.RemoteException;

import eventHierarchy.Event;

public interface AnalyticsInterface extends Remote{
			
		public String subscribe(String regex) throws RemoteException;
		
		public void unsubscribe(String ID) throws RemoteException;
		
		public void processEvent(Event event) throws RemoteException;
}
