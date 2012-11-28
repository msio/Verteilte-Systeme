package billingServer;

import java.rmi.AccessException;
import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

public class BillingServerSecure implements BillingServerSecureInterface //, Runnable
{
	private Registry registry;
	private final static String rmiBindingName = "BillingServerSecureRef";
	private BillingServerSecureInterface stub;
	
	private PriceSteps priceSteps;
	private ArrayList<BillCreator> billCreators;
	
	BillingServerSecure()
	{
		super();
		
		priceSteps = new PriceSteps();
		billCreators = new ArrayList<BillCreator>();
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
		return priceSteps;
	}

	public void createPriceStep(double startPrice, double endPrice, double fixedPrice, double variablePricePercent) throws RemoteException
	{
		if(startPrice < 0 || endPrice < 0 || fixedPrice < 0 || variablePricePercent < 0)
			throw new RemoteException();
		
		try
		{
			priceSteps.addStep(startPrice, endPrice, fixedPrice, variablePricePercent);
		}
		catch (IllegalArgumentException e)
		{
			//startPrice or endPrice collides with an excisting price Step
			throw new RemoteException();
		}
		
	}

	public void deletPriceStep(double startPrice, double endPrice) throws RemoteException
	{
		try
		{
			priceSteps.deleteStep(startPrice, endPrice);
		}
		catch (IllegalArgumentException e)
		{
			throw new RemoteException();
		}
		
	}

	public void billAuction(String user, long auctionID, double price) throws RemoteException
	{
		for(BillCreator billElements : billCreators)
		{
			if(billElements.getUsername().equals(user))
			{
				billElements.addAuction(auctionID, price);
				return;
			}
		}
		
		BillCreator newCreator = new BillCreator(user);
		newCreator.addAuction(auctionID, price);
		
		billCreators.add(newCreator);
		
	}

	public Bill getBill(String user) throws RemoteException
	{
		Bill bill;
		
		for(BillCreator billElements : billCreators)
		{
			if(billElements.getUsername().equals(user))
			{
				bill = billElements.createBill(priceSteps);
				return bill;
			}
		}
		
		System.out.println("No User with the name: " + user + ".");
		throw new RemoteException();
	}
}
