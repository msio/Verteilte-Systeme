package billingServer;

import java.rmi.AccessException;
import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class BillingServerSecure implements BillingServerSecureInterface //, Runnable
{
	private Registry registry;
	private final static String rmiBindingName = "BillingServerSecureRef";
	private BillingServerSecureInterface stub;
	
	BillingServerSecure()
	{
		super();
	}
	
	/*public void run()
	{
		
		
		
		wait for interruption from  BillingServer
		try
		{
			wait();
		}
		catch (InterruptedException e)
		{
			//end the BillingServerSecure
		}
	}*/
	
	public void bindRMI(String hostname, int port) throws RemoteException, AlreadyBoundException
	{
		BillingServerSecure obj = new BillingServerSecure();
		stub = (BillingServerSecureInterface) UnicastRemoteObject.exportObject(obj, 0);
		
		// look for RMI Registry
		registry = LocateRegistry.getRegistry(hostname, port);
		
		// bind the remote object to the rmi register
		registry.bind(rmiBindingName, stub);
	}
	
	public void unbindRMI() throws AccessException, RemoteException, NotBoundException
	{
		registry.unbind(rmiBindingName);
	}
	
	public BillingServerSecureInterface getStub()
	{
		return stub;
	}
	
	
	public PriceSteps getPriceSteps() throws RemoteException
	{
		// TODO Auto-generated method stub
		return null;
	}

	public void createPriceStep(double startPrice, double endPrice, double fixedPice, double variablePricePercent) throws RemoteException
	{
		// TODO Auto-generated method stub
		
	}

	public void deletPriceStep(double startPrice, double endPrice) throws RemoteException
	{
		// TODO Auto-generated method stub
		
	}

	public void billAuction(String user, long auctionID, double price) throws RemoteException
	{
		// TODO Auto-generated method stub
		
	}

	public Bill getBill(String user) throws RemoteException
	{
		// TODO Auto-generated method stub
		return null;
	}
}
