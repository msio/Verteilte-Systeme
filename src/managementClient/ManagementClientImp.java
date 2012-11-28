package managementClient;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import analyticsserver.AnalyticsInterface;

import eventHierarchy.Event;

public class ManagementClientImp {

	
	private static final String LOGIN = "!login";
	private static final String LOGOUT = "!logout";
	private static final String STEPS = "!steps";
	private static final String ADD_STEP = "!addStep";
	private static final String REMOVE_STEP ="!removeStep";
	private static final String BILL ="!bill";
	private static final String SUBSCRIBE = "!subscribe";
	private static final String UNSUBSCRIBE = "!unsubscribe";
 
	private String regex;
	private AnalyticsInterface analyticsServer;
	private String ID;
	
	public ManagementClientImp(AnalyticsInterface analyticsServer )  {
		this.analyticsServer=analyticsServer;
	}

	// send management client object to analytics server
	
	public void subscribe(){
		
		 try {
			ID =analyticsServer.subscribe(new ManagementClientCallback(regex));
		} catch (RemoteException e) {
			System.out.println("ERROR: Subscription Failed");
			e.printStackTrace();
		}
	
	}
	
	// 
	public void unsubscribe(){
		
		//analyticsServer.unsubscribe();
	}
	
	public String checkCommand(String command){
		
		if(command.contains(LOGIN)){
			
			//input code
			
		}else if(command.contains(LOGOUT)){
			//input code
			
		}else if (command.contains(STEPS)){
			//input code
		
		}else if (command.contains(ADD_STEP)){
			//input code
		
		}else if (command.contains(REMOVE_STEP)){
			//input code
		
		}else if (command.contains(BILL)){
			//input code
		
		}else if (command.contains(SUBSCRIBE)){
			
			subscribe();
			
			if(ID != null){
				
				return "";
			}
		
		}else if (command.contains(UNSUBSCRIBE)){
			//input code
		
		}else{
			
			return "ERROR: Invalid Command";
		}
		
		return null;
	}
	
}
