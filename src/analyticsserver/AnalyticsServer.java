package analyticsserver;

import java.io.IOException;
import java.io.InputStream;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Properties;

public class AnalyticsServer {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		int port=0;
		
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
			
			
			try{
				String name="AnalyticsServer";
				AnalyticsServerImp serverObj = new AnalyticsServerImp();
				//AnalyticsServerImp stub = (AnalyticsServerImp) UnicastRemoteObject.exportObject(serverObj, 0);
				Registry registry=null;
				
				try{	
					
					registry = LocateRegistry.getRegistry(port);
					System.out.println("Register found");
					
					//----- DONT FORGET TO UNBIND TO REGISTER
					//registry.unbind(name);	
				
				}catch(Exception e){
					
					registry = LocateRegistry.createRegistry(port);
				}
				 
				registry.bind(name, serverObj);
				
						
			}catch(Exception e){
				
				System.out.println("ERROR in AnalyticsServer " + e);
			}
			
			
		}
	}

}
