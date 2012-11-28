package auctionServer;
import java.io.IOException;
import java.io.InputStream;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Properties;
import java.util.Scanner;

import analyticsserver.AnalyticsInterface;


public class Server
{
	public static void main(String[] args) throws IOException
	{
		int tcpPortNumber = 0;
		int registryPort = 0;
		String registryHost = null;
		String analyticsBinginName = null;
		String billingBindingName = null;
		
		
		if(args.length == 3)
		{
			try
			{
				tcpPortNumber = Integer.parseInt(args[0]);
				analyticsBinginName= args[1];
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
				
				try{
					
					
					
					Registry registry = LocateRegistry.getRegistry(registryHost,registryPort);
					analyticsServer = (AnalyticsInterface) registry.lookup(analyticsBinginName);
					
					
					
				}catch(Exception e){
					
					System.out.println("Error connection to AnalyticsServer" + e);
				}
		
		
		// pass remote object AnalyticsServer to ServerConnection 
				
		ServerConnection con = new ServerConnection(tcpPortNumber,analyticsServer);
		
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
