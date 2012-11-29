package managementClient;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import billingServer.Bill;
import billingServer.BillingServerInterface;
import billingServer.BillingServerSecureInterface;
import billingServer.PriceSteps;

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
	private static final String HIDE = "!hide";
	private static final String AUTO = "!auto";
	private static final String PRINT = "!print";
 
	private String regex;
	private AnalyticsInterface analyticsServer;
	private BillingServerInterface billingServer;
	private BillingServerSecureInterface billingServerSecure;
	private String ID;
	
	private boolean loggedIn = false;
	private boolean auto = false;
	private boolean print = false;
	private ArrayList<String> messageList;
	
	public ManagementClientImp(AnalyticsInterface analyticsServer, BillingServerInterface billingServer, boolean autoMode )  {
		this.analyticsServer=analyticsServer;
		this.billingServer = billingServer;
		messageList = new ArrayList<String>();
		auto = autoMode;
	}

	// send management client object to analytics server
	
	public void subscribe(){
		
		ManagementClientCallback clientCallback=null;
		try {
			clientCallback = new ManagementClientCallback(getRegex(), this);
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
			
			String[] subs = command.split(" ");
			
			if(subs.length != 3){
				
				return "ERROR: Invalid Number Of Command Arguments";
			}
			
			try
			{
				billingServerSecure = billingServer.login(subs[1], subs[2]);
				loggedIn = true;
			}
			catch (RemoteException e)
			{
				return "ERROR: Problem with logging in at BillingServer";
			}
			
			return subs[1] + " successfully logged in";
			
		}else if(command.contains(LOGOUT)){
			if(!loggedIn)
			{
				return "ERROR: You have to login first.";
			}
			
			String[] subs = command.split(" ");
			
			if(subs.length != 1){
				
				return "ERROR: Invalid Number Of Command Arguments";
			}
			
			billingServerSecure = null;
			loggedIn = false;
			
			return "successfully logged out";
			
		}else if (command.contains(STEPS)){
			
			if(!loggedIn)
			{
				return "ERROR: You are not logged in.";
			}
			
			String[] subs = command.split(" ");
			
			if(subs.length != 1){
				
				return "ERROR: Invalid Number Of Command Arguments";
			}
			
			try
			{
				PriceSteps priceSteps = billingServerSecure.getPriceSteps();
				return priceSteps.toString();
			}
			catch (RemoteException e)
			{
				return "ERROR: Problem with viewing the Price Steps";
			}
			
		
		}else if (command.contains(ADD_STEP)){
			
			if(!loggedIn)
			{
				return "ERROR: You are not logged in.";
			}
			
			String[] subs = command.split(" ");
			
			if(subs.length != 5){
				
				return "ERROR: Invalid Number Of Command Arguments";
			}
			
			try
			{
				double startPrice = Double.parseDouble(subs[1]);
				double endPrice = Double.parseDouble(subs[2]);
				double fixedPrice = Double.parseDouble(subs[3]);
				double variablePricePercent = Double.parseDouble(subs[4]);
				billingServerSecure.createPriceStep(startPrice, endPrice, fixedPrice, variablePricePercent);
				double fixedEnd = endPrice;
				if(endPrice == 0)
				{
					fixedEnd = Double.POSITIVE_INFINITY;
				}
				
				return "Price Step [" + startPrice + " " + fixedEnd + "] successfully added.";
			}
			catch (RemoteException e)
			{
				return "ERROR: Price step not added";
			}
		
		}else if (command.contains(REMOVE_STEP)){
			
			if(!loggedIn)
			{
				return "ERROR: You are not logged in.";
			}
			
			String[] subs = command.split(" ");
			
			if(subs.length != 3){
				
				return "ERROR: Invalid Number Of Command Arguments";
			}
			
			try
			{
				double startPrice = Double.parseDouble(subs[1]);
				double endPrice = Double.parseDouble(subs[2]);
				billingServerSecure.deletPriceStep(startPrice, endPrice);
				double fixedEnd = endPrice;
				if(endPrice == 0)
				{
					fixedEnd = Double.POSITIVE_INFINITY;
				}
				
				return "Price Step [" + startPrice + " " + fixedEnd + "] successfully removed.";
			}
			catch (RemoteException e)
			{
				return "ERROR: Price step not removed";
			}
		
		}else if (command.contains(BILL)){
			
			if(!loggedIn)
			{
				return "ERROR: You are not logged in.";
			}
			
			String[] subs = command.split(" ");
			
			if(subs.length != 2){
				
				return "ERROR: Invalid Number Of Command Arguments";
			}
			
			try
			{
				String username = subs[1];
				Bill bill = billingServerSecure.getBill(username);
				
				return bill.toString();
			}
			catch (RemoteException e)
			{
				return "ERROR: No bill for this user ";
			}
		
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
			
			
			if(ID == null){
				
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
		
		}else if(command.contains(HIDE)){
			
			auto = false;
			
			return "Hide mode activated.";
			
		}else if(command.contains(AUTO)){
			
			auto = true;
			
			return "Auto mode activated.";
			
		}else if(command.contains(PRINT)){
			
			//print current messages
			String retMessage = "";
			
			for(String message : messageList)
			{
				retMessage += message + "\n";
			}
			
			//clear messageList
			messageList.clear();
			
			return retMessage;
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
	
	public boolean getAuto()
	{
		return auto;
	}
	
	public boolean getPrint()
	{
		return print;
	}
	
	public void setPrintFalse()
	{
		print = false;
	}

	public void addMessage(String message)
	{
		messageList.add(message);
		
	}
	
}
