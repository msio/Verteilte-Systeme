package managementClient;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

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
		
		ManagementClientCallback clientCallback=null;
		try {
			clientCallback = new ManagementClientCallback(getRegex());
		} catch (RemoteException e) {
			System.out.println("ERROR Create Managmenent.. ");
			e.printStackTrace();
		}
		
		 
			try {
				ID = analyticsServer.subscribe(clientCallback);
			} catch (RemoteException e) {
				System.out.println("ERROR Subscribtion Failed ");
				e.printStackTrace();
			}
		
			
	
	}
	
	private String getRegex(){
		
		return regex;
	}
	
	// 
	public boolean unsubscribe(){
		
		boolean unsub=false;
		
		try {
			
			unsub=analyticsServer.unsubscribe(ID);
		
		} catch (RemoteException e) {
			
			System.out.println("ERROR: Unsubscribtion Failed");
		}
		
		return unsub;
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
			
			String[] subs = command.split(" ");
			
			if(subs.length != 2){
				
				return "ERROR: Invalid Number Of Command Arguments";
			}
			
			if(!checkRegexString(subs[1])){
				return "ERROR: Invalid Regular Expression String";
			}
			
			this.regex=subs[1];
			
			subscribe();
			
			
			if(ID != null){
				
				return "Subscription Not Created";
			}else{
				
				return "Created Subscription With "+ ID +" For Events Using Filter " + regex;
			}
		
		}else if (command.contains(UNSUBSCRIBE)){
			
			
			if(unsubscribe()){
				
				return "Subscription "+ID+ " terminated" ;
			}else{
				
				return "Subscription "+ID+ " Not terminated" ;
			}
		
		}
		
		return "ERROR:  Invalid Command";
	}
	
	private boolean checkRegexString(String regex){
		boolean checkRegex;
		
		try{
			
			Pattern.compile(regex);
			checkRegex=true;
		}catch(PatternSyntaxException e){
			checkRegex=false;
		}
		return checkRegex;
	}
	
}
