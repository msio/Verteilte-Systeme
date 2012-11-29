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
	private ArrayList<BidEvent> bidList;
	private double userSessionMax=0;
	private double userSessionMin=0;
	private double userSessionAvg=0;
	private double bidPriceMax=0;
	private double auctionTimeAvg=0;
	private double auctionSucessRatio=0;
	private int bidsNum=0;
	private long startTime=0;
	private int auctionEnded=0;
	private int auctionBidded=0;
	
	private static final long serialVersionUID = 1L;

	public AnalyticsServerImp() throws RemoteException{
		
		super();
		
		startTime=System.nanoTime(); 
		subscribedClients = new HashMap<String, SubscribedClient>();
		userLogin = new ArrayList<UserEvent>();
		auctionStart = new ArrayList<AuctionEvent>();
		bidList = new ArrayList<BidEvent>();
		
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
				
				
				auctionEnded++;
			}
			
			if(auctionTimeAvg == 0){
				
				auctionTimeAvg =auctionTimeAvg + tempTime; 
			}else{
				
				auctionTimeAvg = (auctionTimeAvg +tempTime) / 2;
			}
			
		
		}else if(event.getType() == BID_PLACED){
			
			
			BidEvent tempBid = (BidEvent) event;
			if(bidPriceMax < tempBid.getPrice()){
					bidPriceMax=tempBid.getPrice();
			}
			
			bidsNum++;
			
		
		}/*else if(event.getType() == BID_WON){
			
			
		}*/		
		  
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
				
				if(eventString.equals(USER_SESSIONTIME_MIN)){
					try {
						tempManagementClient.processEvent(new StatisticsEvent(getIDEvent(), USER_SESSIONTIME_MIN, System.currentTimeMillis(), userSessionMin));
					} catch (Exception e) {
						System.out.println("user sessiontime min");
						e.printStackTrace();
					} 
				}
				
				if(eventString.equals(USER_SESSIONTIME_AVG)){
					try {
						tempManagementClient.processEvent(new StatisticsEvent(getIDEvent(), USER_SESSIONTIME_AVG, System.currentTimeMillis(), userSessionAvg));
					} catch (Exception e) {
						System.out.println("user sessiontime avg");
						e.printStackTrace();
					} 
				}
				
				if(eventString.equals(BID_PRICE_MAX)){
					try {
						tempManagementClient.processEvent(new StatisticsEvent(getIDEvent(), BID_PRICE_MAX, System.currentTimeMillis(), bidPriceMax));
					} catch (Exception e) {
						System.out.println("bid price max");
						e.printStackTrace();
					} 
				}
				
				if(eventString.equals(BID_COUNT_PER_MINUTE)){
					try {
						long currentTime = System.nanoTime();
						
						double bidPerMinute = (double) ((long)bidsNum / ((currentTime - startTime) *1000 *100 *1000 *60)); 
						
						tempManagementClient.processEvent(new StatisticsEvent(getIDEvent(), BID_COUNT_PER_MINUTE, System.currentTimeMillis(),bidPerMinute));
					} catch (Exception e) {
						System.out.println("bid count per minute");
						e.printStackTrace();
					} 
				}
				
				if(eventString.equals(AUCTION_TIME_AVG)){
					try {
						tempManagementClient.processEvent(new StatisticsEvent(getIDEvent(),AUCTION_TIME_AVG, System.currentTimeMillis(), auctionTimeAvg));
					} catch (Exception e) {
						System.out.println("auction time avg");
						e.printStackTrace();
					} 
				}


				
				
				


				
			} 
		}
	}
	
	
}