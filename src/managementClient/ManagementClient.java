package managementClient;

import java.io.IOException;
import java.io.InputStream;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Properties;
import java.util.Scanner;

import billingServer.BillingServerInterface;

import analyticsserver.AnalyticsInterface;
import analyticsserver.AnalyticsServerImp;

public class ManagementClient {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		
		String analyticsBindingName=null;
		String billingBindingname=null;
		
		int port=0;
		String host=null;
		
		if(args.length == 2){
			
			analyticsBindingName= args[0];
			billingBindingname= args[1];
		}else{
			
			System.out.println("Usage:\n" + "java ManagementClient <analyticsBindingName> <billingBindingname>\n");
			System.exit(1);
		}

		//read register.properties
		

		InputStream in = ClassLoader.getSystemResourceAsStream("registry.properties");
		
			
		if(in != null){
			
			Properties pros = new Properties();
				
			try {
				pros.load(in);
				port = Integer.valueOf(pros.getProperty("registry.port"));
				host=pros.getProperty("registry.host");
				
			} catch (IOException e) {
				System.out.println("Could not read from the stream");
			}finally{
				
				try {
					in.close();
				} catch (IOException e) {
					System.out.println("Could not close the stream");
				}
			}
			
		}else{
			
				throw new IOException("Could not find registry.properties");
		}
		
		//make connection 
		
		
		AnalyticsInterface analyticsServer=null;
		BillingServerInterface billingServer = null;
		
		try{
			
					Registry registry = LocateRegistry.getRegistry(host, port);
					// get Analytics Server
					analyticsServer= (AnalyticsInterface) registry.lookup(analyticsBindingName);
					
					//get Billing Server
					billingServer = (BillingServerInterface) registry.lookup(billingBindingname);
			
		}catch(Exception e){
			System.out.println("Error in Management client");
			e.printStackTrace();
			
		}
		
		 // ---- PASS BILLING SERVER OBJECT IN ManagementClientImp 
		
		ManagementClientImp clientImp = new ManagementClientImp(analyticsServer, billingServer);
		
		
		boolean end=false;
		
		Scanner sc = new Scanner(System.in);
		
		String command =null;
		
		while(!end){
			
			command=sc.nextLine();
			
			if(command.equals("!end")){
				end=true;
			}
			
			String output = clientImp.checkCommand(command);
			
			System.out.println(output);
		}
		
		//closing client 
		sc.close();
		
		System.exit(0);
	}

}
