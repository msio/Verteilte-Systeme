package billingServer;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface BillingServerSecureInterface extends Remote 
{
	public PriceSteps getPriceSteps() throws RemoteException;
	
	public void createPriceStep(double startPrice, double endPrice, double fixedPrice, double variablePricePercent) throws RemoteException;
	
	public void deletPriceStep(double startPrice, double endPrice) throws RemoteException;
	
	public void billAuction(String user, long auctionID, double price) throws RemoteException;
	
	public Bill getBill(String user) throws RemoteException;
}
