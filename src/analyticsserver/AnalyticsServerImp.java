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

import eventHierarchy.AuctionEvent;
import eventHierarchy.BidEvent;
import eventHierarchy.Event;
import eventHierarchy.EventTypeConstants;
import eventHierarchy.StatisticsEvent;
import eventHierarchy.UserEvent;

public class AnalyticsServerImp extends UnicastRemoteObject implements AnalyticsInterface,EventTypeConstants {
	
	private static int IDEvent;
	private static int UserID;
	private HashMap<String,SubscribedClient> subscribedClients;
	private ArrayList<UserEvent> userLogin;
	private ArrayList<AuctionEvent> auctionStart;
	private ArrayList<Event> auctionList;
	private double userSessionMax=0;
	private double userSessionMin=0;
	private double userSessionAvg=0;
	private double bidPriceMax=0;
	private double auctionTimeAvg=0;
	private double AuctionSucessRatio=0;
	
	private static final long serialVersionUID = 1L;

	public AnalyticsServerImp() throws RemoteException{
		
		super();
		
		subscribedClients = new HashMap<String, SubscribedClient>();
		userLogin = new ArrayList<UserEvent>();
		auctionStart = new ArrayList<AuctionEvent>();
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
		
		for(int i= 0;i<15;i++){
			Matcher match = pattern.matcher(ALL_EVENTS[i]);
			
			if(match.find()){
				
				all.add(ALL_EVENTS[i]);
			}
		}
		
		return all;
	}
	
	
	@Override
	public String subscribe(ManagementClientInterface client) throws RemoteException {
			SubscribedClient subClient = new SubscribedClient(client);
			
			
			ArrayList<String> allEvents=computeRegex(client.getRegex());
			String ID = getUserID();
			
		   
			
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
		
		// AUCTION_SUCCESS_RATIO implement
		
		if(event.getType() == USER_LOGIN){
			userLogin.add((UserEvent) event);
		
		}else if((event.getType() == USER_LOGOUT) || (event.getType() == USER_DISCONNECTED)){
				
			
			UserEvent tempUserEvent= (UserEvent)event;	
			double tempSession=0;
			
			for(UserEvent e: userLogin){
			
				if(e.getUserName() == tempUserEvent.getUserName()) {
					
					tempSession = tempUserEvent.getTimestamp() - e.getTimestamp(); 
					userLogin.remove(e);
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
				if(userSessionAvg == 0){
					userSessionAvg = (userSessionAvg + tempSession); 
				}else{
					
					userSessionAvg = (userSessionAvg + tempSession) / 2;
				}	
			
			
				
		}else if(event.getType() == AUCTION_STARTED){
			
			auctionStart.add((AuctionEvent)event);
			
		}else if(event.getType() == AUCTION_ENDED){
			
			AuctionEvent tempAuctionEvent = (AuctionEvent)event;
			
			double tempTime=0;
			
			for(AuctionEvent a : auctionStart){
				
				if(a.getAuctionId() == tempAuctionEvent.getAuctionId()){
					
					tempTime=tempAuctionEvent.getTimestamp() - a.getTimestamp();
					auctionStart.remove(a);
					break;
				}
			}
			
			if(auctionTimeAvg == 0){
				
				auctionTimeAvg =auctionTimeAvg + tempTime; 
			}else{
				
				auctionTimeAvg = (auctionTimeAvg +tempTime) / 2;
			}
			
		
		}else if(event.getType() == BID_OVERBID){
			bidList.add(event); 
			
		}else if(event.getType() == BID_PLACED){
			bidList.add(event);
		
		}else if(event.getType() == BID_WON){
			
			bidList.add(event);
		}
		
		  
		sendToManagementClients(event);
		
	}
	
	private void sendToManagementClients(Event event){
		
		ArrayList<SubscribedClient> allSubscribedClients = new ArrayList<SubscribedClient>(subscribedClients.values());
		
		for(SubscribedClient client : allSubscribedClients){
			
			ManagementClientInterface tempManagementClient=null;
			
			 tempManagementClient=client.getManagementClient();
			 
			for(String eventString : client.getEvents()){
				
				if(eventString.equals(USER_LOGIN)){
					try {
						tempManagementClient.processEvent((UserEvent)event);
					} catch (RemoteException e) {
						System.out.println("Error UserLogin in AnalyticsServer");
						e.printStackTrace();
					}
				}
				
				if(eventString.equals(USER_LOGOUT)){
					try {
						tempManagementClient.processEvent((UserEvent)event);
					} catch (RemoteException e) {
						System.out.println("Error User in AnalyticsServer");
						e.printStackTrace();
					}
					
				}
				
				if(eventString.equals(USER_DISCONNECTED)){
					try {
						tempManagementClient.processEvent((UserEvent)event);
					} catch (RemoteException e) {
						System.out.println("Error User in AnalyticsServer");
						e.printStackTrace();
					}
					
				}
				
				if(eventString.equals(AUCTION_STARTED)){
					try {
						tempManagementClient.processEvent((AuctionEvent)event);
					} catch (RemoteException e) {
						System.out.println("Error AuctionStarted in AnalyticsServer");
						e.printStackTrace();
					}
					
				}
			
				if(eventString.equals(AUCTION_ENDED)){
					try {
						tempManagementClient.processEvent((AuctionEvent)event);
					} catch (RemoteException e) {
						System.out.println("Error Auction in AnalyticsServer");
						e.printStackTrace();
					}
					
				}

				if(eventString.equals(BID_OVERBID)){
					try {
						tempManagementClient.processEvent((BidEvent)event);
					} catch (RemoteException e) {
						System.out.println("Error Bid in AnalyticsServer");
						e.printStackTrace();
					}
					
				}

				if(eventString.equals(BID_PLACED)){
					try {
						tempManagementClient.processEvent((BidEvent)event);
					} catch (RemoteException e) {
						System.out.println("Error Bid in AnalyticsServer");
						e.printStackTrace();
					}
					
				}
				
				if(eventString.equals(BID_WON)){
					try {
						tempManagementClient.processEvent((BidEvent)event);
					} catch (RemoteException e) {
						System.out.println("Error Bid in AnalyticsServer");
						e.printStackTrace();
					}
					
				}
				
				
				
				
				// events that are computed
				if(eventString.equals(USER_SESSIONTIME_MAX)){
						try {
							tempManagementClient.processEvent(new StatisticsEvent(getIDEvent(), USER_SESSIONTIME_MAX, System.currentTimeMillis(), userSessionMax));
						} catch (Exception e) {
							System.out.println("user sessiontime max");
							e.printStackTrace();
						} 
				}
				
				if(eventString.equals(anObject))
				
			} 
		}
	}
	
	
}