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
import eventHierarchy.StatisticsEvent;
import eventHierarchy.UserEvent;

public class AnalyticsServerImp extends UnicastRemoteObject implements AnalyticsInterface,EventTypeConstants {
	
	private static int IDEvent;
	private static int UserID;
	private HashMap<String,SubscribedClient> subscribedClients;
	private ArrayList<UserEvent> userLogin;
	private ArrayList<Event> bidList;
	private ArrayList<Event> auctionList;
	private double userSessionMax;
	private double userSessionMin;
	private double userSessionAvg;
	private int userSessionAvgNum;
	private double bidPriceMax;
	private double auctionTimeAvg;
	private double AuctionSucessRatio;
	
	private static final long serialVersionUID = 1L;

	public AnalyticsServerImp() throws RemoteException{
		
		super();
		
		subscribedClients = new HashMap<String, SubscribedClient>();
		userLogin = new ArrayList<UserEvent>();
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
			userLogin.add((UserEvent) event);
		
		}else if((event.getType() == USER_LOGOUT) || (event.getType() == USER_DISCONNECTED)){
			
			UserEvent tempUserEvent= (UserEvent)event;	
			double tempSession=0;
			
			for(UserEvent e: userLogin){
			
				if(e.getUserName() == tempUserEvent.getUserName()) {
					
					tempSession = tempUserEvent.getTimestamp() - e.getTimestamp(); 
					userSessionAvgNum++;
					break;  
				}
			}	
				
				// user min session duration 
				if(userSessionMin > tempSession){
					
					userSessionMin= tempSession;
				}
				
				// user max session duration 
				if(userSessionMax < tempSession){
					
					userSessionMax = tempSession;
				}
				
				// user session average duration
				userSessionAvg = (userSessionAvg + tempSession) / userSessionAvgNum;
				
			
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
		
		  
		sendToManagementClients(event);
		
	}
	// ---- THERE IS A BUG .. FIND IT 
	
	private void sendToManagementClients(Event event){
		
		ArrayList<SubscribedClient> allSubscribedClients = new ArrayList<SubscribedClient>(subscribedClients.values());
		
		for(SubscribedClient client : allSubscribedClients){
			
			ManagementClientInterface tempManagementClient=null;
			
			 tempManagementClient=client.getManagementClient();
			 
			for(String eventString : client.getEvents()){
				
				try {
					tempManagementClient.processEvent(event);
				} catch (RemoteException e1) {
					System.out.println("event");
					e1.printStackTrace();
				}
				
				if(eventString.equals(USER_SESSIONTIME_MAX)){
						try {
							tempManagementClient.processEvent(new StatisticsEvent(getIDEvent(), USER_SESSIONTIME_MAX, System.currentTimeMillis(), userSessionMax));
						} catch (Exception e) {
							System.out.println("user sessiontime max");
							e.printStackTrace();
						} 
				}
				
				
				
			} 
		}
	}
	
	
}