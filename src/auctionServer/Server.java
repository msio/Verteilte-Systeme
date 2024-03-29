package auctionServer;
import java.io.IOException;
import java.io.InputStream;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Properties;
import java.util.Scanner;

import billingServer.BillingServerInterface;
import billingServer.BillingServerSecureInterface;

import analyticsserver.AnalyticsInterface;


public class Server
{
	public static void main(String[] args) throws IOException
	{
		int tcpPortNumber = 0;
		int registryPort = 0;
		String registryHost = null;
		String analyticsBindingName = null;
		String billingBindingName = null;
		
		
		if(args.length == 3)
		{
			try
			{
				tcpPortNumber = Integer.parseInt(args[0]);
				analyticsBindingName= args[1];
				billingBindingName= args[2];
				
			}
			catch (NumberFormatException e)
			{
				System.err.println("The tcpPort Number must be an integer.");
				System.exit(1);
			}
		}
		else
		{
			System.out.println("Usage:\n"+
						"java Server <tcpPort> <analytics bindingName> <billing bindingName>\n"+
						"tcpPort: Integer holding the TCP Port for incoming massages.");
			System.exit(1);
		}
		
		// read properties from the registry.properties 
		
				InputStream in = ClassLoader.getSystemResourceAsStream("registry.properties");
				
				
				if(in != null){
					
					Properties pros = new Properties();
						
					try {
						pros.load(in);
						registryPort = Integer.valueOf(pros.getProperty("registry.port"));
						registryHost = pros.getProperty("registry.host");
						
					} catch (IOException e) {
						System.out.println("Could not read from the stream");
					}finally{
						
						try {
							in.close();
						} catch (IOException e) {
							System.out.println("Could not close the stream " + e);
						}
					}
				
				}else{
					
						throw new IOException("Could not find registry.properties");
				}
				
				
				// Connect to the Analitycs Server
				
				//reference to remote object AnalyticsServer
				
				AnalyticsInterface analyticsServer = null;
				BillingServerInterface billingServer = null;
				
				try{
					Registry registry = LocateRegistry.getRegistry(registryHost,registryPort);
					
					//analytics server
					analyticsServer = (AnalyticsInterface) registry.lookup(analyticsBindingName);
					
				}catch(Exception e){
					
					System.out.println("Error connection to AnalyticsServer " + e);
				}
				
				try{
					Registry registry = LocateRegistry.getRegistry(registryHost,registryPort);
					
					// billing server
					billingServer = (BillingServerInterface) registry.lookup(billingBindingName);
					
				}catch(Exception e){
					
					System.out.println("Error connection to BillingServer " + e);
				}
				

		//login to Billing Server, to get a Billing Server Secure
		
		BillingServerSecureInterface billingServerSecure = null;
		
		try
		{
			billingServerSecure = billingServer.login("auctionClientUser", "EinPasswort");
		}
		catch (Exception e)
		{
			System.out.println("Cant login into BillingServer. " + e);
		}
		
		
		// pass remote object AnalyticsServer to ServerConnection 
				
		ServerConnection con = new ServerConnection(tcpPortNumber,analyticsServer, billingServerSecure);
		
		Thread t1 = new Thread(new ServerConnectionHandler(con));
		t1.start();
	
		
		
		
		
		Scanner scan = new Scanner(System.in);
		
		//Block until Enter for next Line is pressed
		scan.hasNextLine();
		
		scan.close();
		
		//at the end close the connection
		con.closeConnection();
		
		
	}
}
