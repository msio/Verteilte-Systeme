package billingServer;

import java.rmi.AccessException;
import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Scanner;

public class BillingServer implements BillingServerInterface
{
	// first start the BillingServerSecure
	// after correct login return a reference to the BillingServerSecure
	// at shutdown close the BillingServerSecure too
	
	public BillingServer()
	{
		super();
	}
	
	public static void main(String args[])
	{
		String rmiBindingName = "";
		
		if(args.length == 1)
		{
			rmiBindingName = args[0];
		}
		else
		{
			System.out.println("Usage:\n"+
						"java BillingServer <rmiBindingName>\n"+
						"rmiBindingName: RMI Binding Name of the Billing Server");
			System.exit(1);
		}
		
		//initialize the SecurityManager		
		if (System.getSecurityManager() == null)
		{
            System.setSecurityManager(new SecurityManager());
        }
		
		BillingServerInterface stub = null;
		
		try
		{
			BillingServer obj = new BillingServer();
			stub = (BillingServerInterface) UnicastRemoteObject.exportObject(obj, 0);
		}
		catch (RemoteException e2)
		{
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		
		Registry registry = null;
		
		try
		{
			//look for RMI Registry
			registry = LocateRegistry.getRegistry();
			
			//bind the remote object to the rmi register
			registry.bind(rmiBindingName, stub);
		}
		catch (Exception e) //RemoteException e)
		{
			try
			{
				//if the RMI Registry does not exist, create one on the standard port 1099!
				registry = LocateRegistry.createRegistry(1099);
				
				//bind the remote object to the rmi register
				registry.rebind(rmiBindingName, stub);
			}
			catch (RemoteException e1)
			{
				System.out.println("Something went terribly wrong!");
				System.out.println("Cant create RMI Registry!");
				e1.printStackTrace();
				System.exit(1);
			}
		}
		
		System.out.println("BillingServer ready.");
		
		//wait for !end to shutdown the server
		
		Scanner scan = new Scanner(System.in);
		
		//Block until Enter for next Line is pressed
		boolean end = false;
		
		while(!end)
		{
			String msg = scan.next();
			
			if(msg.equals("!end"))
			{
				end = true;
			}
		}
		
		scan.close();
		
		//unbind the rmi object
		try
		{
			registry.unbind(rmiBindingName);
		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		System.out.println("BillingServer closed.");
		System.exit(0);
	}
	
	public BillingServerSecure login(String username, String password) throws RemoteException
	{
		// TODO Auto-generated method stub
		return null;
	}
}
