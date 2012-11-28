package billingServer;

import java.io.IOException;
import java.io.InputStream;
import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.security.AccessControlException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Properties;
import java.util.Scanner;

public class BillingServer implements BillingServerInterface
{
	// first start the BillingServerSecure
	// after correct login return a reference to the BillingServerSecure
	// at shutdown close the BillingServerSecure too
	
	private static BillingServerSecure secure;
	
	public BillingServer()
	{
		super();
	}
	
	public static void main(String args[])
	{
		// read the binding name
		String rmiBindingName = "";
		
		if (args.length == 1)
		{
			rmiBindingName = args[0];
		}
		else
		{
			System.out.println("Usage:\n" + "java BillingServer <rmiBindingName>\n" + "rmiBindingName: RMI Binding Name of the Billing Server");
			System.exit(1);
		}
		
		InputStream in = ClassLoader.getSystemResourceAsStream("registry.properties");
		
		if (in == null)
		{
			System.out.println("Can't read from registry.properties file");
			System.exit(1);
		}
		
		int port = 0;
		String hostname = "";
		Properties pros = new Properties();
		
		try
		{
			pros.load(in);
			port = Integer.valueOf(pros.getProperty("registry.port"));
			hostname = pros.getProperty("registry.host");
			
		}
		catch (IOException e)
		{
			System.out.println("Could not read from the stream");
		}
		
		try
		{
			in.close();
		}
		catch (IOException e)
		{
			System.out.println("Could not close the stream");
			System.exit(1);
		}
		
		// initialize the SecurityManager
		/*
		 * if (System.getSecurityManager() == null) { System.setProperty("java.security.policy", "src/standard.policy"); System.setSecurityManager(new
		 * SecurityManager()); }
		 */
		
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
			System.exit(1);
		}
		
		Registry registry = null;
		
		try
		{
			// look for RMI Registry
			registry = LocateRegistry.getRegistry(hostname, port);
			
			// bind the remote object to the rmi register
			registry.bind(rmiBindingName, stub);
		}
		catch (RemoteException e) // RemoteException e)
		{
			try
			{
				// if the RMI Registry does not exist, create one at the specific port!
				registry = LocateRegistry.createRegistry(port);
				
				// bind the remote object to the rmi register
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
		catch (AccessControlException e)
		{
			System.out.println("Something went wrong with the security policy.");
			e.printStackTrace();
			System.exit(1);
			
		}
		catch (AlreadyBoundException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(1);
		}
		
		// start the BillingServerSecure
		
		// Thread billingServerSecure = new Thread(new BillingServerSecure());
		
		// billingServerSecure.start();
		
		secure = new BillingServerSecure();
		
		try
		{
			secure.bindRMI(hostname, port);
		}
		catch (RemoteException e1)
		{
			System.out.println("Error Binding the Billing Server Secure.");
			e1.printStackTrace();
		}
		catch (AlreadyBoundException e1)
		{
			System.out.println("Billing Server Secure already bounded.");
			e1.printStackTrace();
		}
		
		System.out.println("BillingServer and BillingServerSecure ready.");
		
		// wait for !end to shutdown the server
		
		Scanner scan = new Scanner(System.in);
		
		// Block until Enter for next Line is pressed
		boolean end = false;
		
		while (!end)
		{
			String msg = scan.next();
			
			if (msg.equals("!end"))
			{
				end = true;
			}
		}
		
		scan.close();
		
		// unbind the rmi object
		try
		{
			registry.unbind(rmiBindingName);
			secure.unbindRMI();
		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println("BillingServer and BillingServerSecure closed.");
		System.exit(0);
	}
	
	public BillingServerSecureInterface login(String username, String password) throws RemoteException
	{
		InputStream in = ClassLoader.getSystemResourceAsStream("user.properties");
		
		if (in == null)
		{
			System.out.println("Can't read from user.properties file");
			throw new RemoteException();
		}
		
		String savedPassword;
		Properties pros = new Properties();
		
		try
		{
			pros.load(in);
			savedPassword = pros.getProperty(username);
			
		}
		catch (IOException e)
		{
			System.out.println("Could not read from the stream");
			throw new RemoteException();
		}
		
		try
		{
			in.close();
		}
		catch (IOException e)
		{
			System.out.println("Could not close the stream");
			throw new RemoteException();
		}
		
		if (savedPassword == null)
		{
			System.out.println("username or password are wrong.");
			throw new RemoteException();
		}
		
		// create MD5 hashed password
		String md5password = "";
		MessageDigest md5Handler;
		try
		{
			md5Handler = MessageDigest.getInstance("MD5");
			md5Handler.update(password.getBytes());
			
			byte[] md5Array = md5Handler.digest();
			
			StringBuffer hexString = new StringBuffer();
			
			for (int i = 0; i < md5Array.length; i++)
			{
				hexString.append(String.format("%02x", md5Array[i]));
			}
			
			md5password = hexString.toString();
		}
		catch (NoSuchAlgorithmException e)
		{
			System.out.println("Some idiot typed the worng algorith name!");
			e.printStackTrace();
		}
		
		if (savedPassword.equals(md5password))
		{
			// successfully logged in
			return secure.getStub();
		}
		
		System.out.println("username or password are wrong.");
		throw new RemoteException();
	}
}
