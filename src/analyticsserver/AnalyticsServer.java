package analyticsserver;

import java.io.IOException;
import java.io.InputStream;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Properties;
import java.util.Scanner;

import eventHierarchy.Event;

public class AnalyticsServer implements AnalyticsInterface{

	public AnalyticsServer(){
		
		super();
	}
	
	
	public static void main(String[] args) throws IOException {
		
		int port=0;
		String rmiBindingName=null;
	
		
		if(args.length == 1){
			
			
			rmiBindingName = args[0];
		}else{
			
			System.out.println("Usage:\n" + "java AnalyticsServer <rmiBindingName>\n" + "rmiBindingName: RMI Binding Name of the Billing Server");
			System.exit(1);
		}
		
		
		
		
		InputStream in = ClassLoader.getSystemResourceAsStream("registry.properties");
		
			
		if(in != null){
			
			Properties pros = new Properties();
				
			try {
				pros.load(in);
				port = Integer.valueOf(pros.getProperty("registry.port"));
				
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
			
			
		 Registry registry=null;
			
			try{
				
				
				
				AnalyticsInterface serverObj = new AnalyticsServer();
			   AnalyticsInterface stub = (AnalyticsInterface) UnicastRemoteObject.exportObject(serverObj, 0);
				
			  
				
				//try{
					
				//registry = LocateRegistry.getRegistry(port);
						
				
				/*}catch(RemoteException e){
					e.printStackTrace();
					System.out.println("reference could not be created");
				*/	
					registry = LocateRegistry.createRegistry(port);
					
				//}
				 
				// bind the object to the registry	
				registry.rebind(rmiBindingName, stub);
				
				
			}catch(Exception e2){
				e2.printStackTrace();
				System.out.println("Error in connection in Analytics Server " + e2);
			}
			
			
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
			}
			catch (Exception e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			System.out.println("AnalyticsServer closed.");
			System.exit(0);
			
		}

	@Override
	public String subscribe(String regex) throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void unsubscribe(String ID) throws RemoteException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void processEvent(Event event) throws RemoteException {
		// TODO Auto-generated method stub
		System.out.println(event.getType());
	}
	

}
