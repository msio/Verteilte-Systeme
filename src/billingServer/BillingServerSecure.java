package billingServer;

import java.rmi.RemoteException;

public class BillingServerSecure implements BillingServerSecureInterface
{

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
