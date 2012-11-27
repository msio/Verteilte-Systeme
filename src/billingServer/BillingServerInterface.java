package billingServer;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface BillingServerInterface extends Remote
{
	BillingServerSecure login(String username, String password) throws RemoteException;
}
