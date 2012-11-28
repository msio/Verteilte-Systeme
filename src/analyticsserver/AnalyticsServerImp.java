package analyticsserver;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.security.AllPermission;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import managementClient.ManagementClientInterface;

import eventHierarchy.Event;
import eventHierarchy.EventTypeConstants;

public class AnalyticsServerImp extends UnicastRemoteObject implements AnalyticsInterface,EventTypeConstants {
	
	private static int IDEvent;
	private static int UserID;
	private HashMap<String,SubscribedClient> subscribedClients;
	private ArrayList<Event> userLogin;
	private HashMap<String,Event> userList;
	private ArrayList<Event> bidList;
	private ArrayList<Event> auctionList;
	private double userSessionMax;
	private double userSessionMin;
	private double userSessionAvg;
	private double bidPriceMax;
	private double auctionTimeAvg;
	private double AuctionSucessRatio;
	
	private static final long serialVersionUID = 1L;

	public AnalyticsServerImp() throws RemoteException{
		
		super();
		
		subscribedClients = new HashMap<String, SubscribedClient>();
		userLogin = new ArrayList<Event>();
		userList = new HashMap<String,Event>();
		bidList = new ArrayList<Event>();
		auctionList = new ArrayList<Event>(); 
	}

	private static String getIDEvent(){
		
		return "ALS" + IDEvent++;
	}
	
	private static String getUserID(){
		
		
		return String.valueOf(UserID++); 
	}
	
	private ArrayList<String> computeRegex(String regex){
		ArrayList<String> all = new ArrayList<String>();
		
		Pattern pattern = Pattern.compile(regex);		
		
		for(int i= 0;i<NUMBER_OF_ALL_EVENTS;i++){
			Matcher match = pattern.matcher(ALL_EVENTS[i]);
			
			if(match.matches()){
				all.add(ALL_EVENTS[i]);
			}
		}
		
		return all;
	}
	
	
	@Override
	public String subscribe(ManagementClientInterface client) throws RemoteException {
			SubscribedClient subClient = new SubscribedClient(client);
			
			ArrayList<String> allEvents=computeRegex(client.getRegex());
			String ID = getIDEvent();
			
			if(allEvents != null){
				subClient.addEvents(allEvents);
				subscribedClients.put(ID,subClient);
				
				return ID;
			}
			
			 
				
		return null;
	}

	@Override
	public boolean unsubscribe(String ID) throws RemoteException {
		
		if(subscribedClients.remove(ID) != null){
			return true;
		}
		 
		return false;
	}

	@Override
	public void processEvent(Event event) throws RemoteException {
		
		if(event.getType() == USER_LOGIN){
			userLogin.add(event);
		
		}else if(event.getType() == USER_LOGOUT){
				userList.put(USER_LOGOUT,event);
		
		}else if(event.getType() == USER_DISCONNECTED){
			
			userList.put(USER_DISCONNECTED,event);
			
		}else if(event.getType() == AUCTION_STARTED){
			
			auctionList.add(event);
		
		}else if(event.getType() == AUCTION_ENDED){
			auctionList.add(event);
			
		}else if(event.getType() == BID_OVERBID){
			bidList.add(event); 
			
		}else if(event.getType() == BID_PLACED){
			bidList.add(event);
		
		}else if(event.getType() == BID_WON){
			
			bidList.add(event);
		}
		
		  
		
		
	}
	
	public void sendToManagementClient(Event event){
		
		
	}
	
	
}
