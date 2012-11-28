package auctionServer;

public class IDgenerator {

	private static int ID;
	
	public synchronized	static String getID(){
		
		
		return "AUS"+ ID++;
	}
}
