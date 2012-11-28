package auctionServer;
import java.io.IOException;
import java.net.Socket;
import java.rmi.RemoteException;
import java.util.ArrayList;

import eventHierarchy.EventTypeConstants;
import eventHierarchy.UserEvent;

import analyticsserver.AnalyticsInterface;

public class ServerConnectionThread implements Runnable,EventTypeConstants 
{
	private final ServerConnection serverConnection;
	private ServerClientConnection con;
	private String currentUser;
	private AnalyticsInterface analyticsServer;
	private static int ID = 0;
	
	public ServerConnectionThread(Socket socket, ServerConnection serverConnection)
	{
		this.serverConnection = serverConnection;
		
		con = new ServerClientConnection(socket);
		
		currentUser = null;
		
		this.serverConnection.addServerClientConnection(con);
		//this.analyticsServer = serverConnection.getAnalyticsServer();
	}
	
	/*private String getID(){
		
		return "AS"+ID++;
	}
	
	private long getCurrentTimeStamp(){
		
		
		return System.currentTimeMillis();
	} 
	*/
	public void run()
	{
		try
		{
			// do Server Client logic here
			boolean exit = false;
			
			while (!exit)
			{
				// get a command from the Client
				
				String command = con.getMessage();
				
				if (command.contains("!login"))
				{
					// log the user in, if he is not logged in already
					String[] sub = command.split(" ");
					if (sub.length == 3)
					{
						if(currentUser == null)
						{
						// normal behaviour
						serverConnection.addUser(new User(sub[1]));
						
						if (!serverConnection.logUserIn(sub[1], Integer.parseInt(sub[2]), con.getAddress()))
						{
							// User is already logged in on another client
							con.sendMessage("User " + sub[1] + " is already logged in on another Client!");
						}
						else
						{
							currentUser = sub[1];
							//analyticsServer.processEvent(new UserEvent(getID() ,USER_LOGIN,getCurrentTimeStamp() , currentUser.toString()));
							con.sendMessage("Successfully logged in as " + sub[1] + "!");
						}
						}
						else
						{
							// a user is already logged in on this Client
							con.sendMessage("You are already logged in on the server!");
						}
					}
					else
					{
						// not enough or too many arguments
						con.sendMessage("Login failure!\nNot enough or too many arguments!");
					}
				}
				else if (command.contains("!logout"))
				{
					// log the user out, if he is logged in
					if (currentUser != null)
					{
						if (!serverConnection.logUserOut(currentUser))
						{
							// something went terribly wrong!!!
							con.sendMessage("Cant logout User " + currentUser + " although he should be logged in correctly!");
						}
						else
						{
							// Logged out correctly!
							//analyticsServer.processEvent(new UserEvent(getID(), USER_LOGOUT, getCurrentTimeStamp(), currentUser.toString()));
							con.sendMessage("Successfully logged out as " + currentUser + "!");
						}
						
						currentUser = null;
					}
					else
					{
						// not logged in, so he cant log out!
						con.sendMessage("You have to login first!");
					}
				}
				else if (command.contains("!list"))
				{
					// list all available auctions
					String message = "";
					
					ArrayList<Auction> list = serverConnection.getAuctionList();
					for (Auction auction : list)
					{
						message += auction.toString() + "\n";
					}
					
					con.sendMessage(message);
				}
				else if (command.contains("!create"))
				{
					// create a new auction if the user is logged in
					if (currentUser != null)
					{
						String[] sub = command.split(" ");
						if (sub.length >= 3)
						{
							int duration = Integer.parseInt(sub[1]);
							
							// the rest is the description of the Auction
							String description = "";
							
							for (int i = 2; i < sub.length; i++)
							{
								description += sub[i];
								if (i != (sub.length - 1))
								{
									// Add a whitespace between the words
									description += " ";
								}
							}
							Auction auction = new Auction(currentUser, duration, description);
							
							int id = auction.getId();
							String endDate = auction.getEndDateString();
							
							serverConnection.addAuction(auction);
							
							con.sendMessage("An auction '" + description + "' with id " + id + " has been created and will end on " + endDate + ".");
						}
						else
						{
							con.sendMessage("Not enough arguments to create an auction!");
						}
					}
					else
					{
						con.sendMessage("You have to login to create auctions!");
					}
					
				}
				else if (command.contains("!bid"))
				{
					// bid for the user on a specific auction
					if (currentUser != null)
					{
						String[] sub = command.split(" ");
						if (sub.length >= 3)
						{
							int id = Integer.parseInt(sub[1]);
							double amount = Double.parseDouble(sub[2]);
							
							Auction oldAuction = serverConnection.getAuction(id);
							String oldHighestBidder = oldAuction.getHighestBidder();
							
							if (serverConnection.bidOnAuction(currentUser, id, amount))
							{
								// successfully bided on the auction
								Auction auction = serverConnection.getAuction(id);
								con.sendMessage("You successfully bid with " + amount + " on '" + auction.getDescription() + "'.");
								
								if (oldHighestBidder != null)
								{
									// send a message to the old highest bidder, therefore add the message to the message queue
									/* TODO: not needed in exercise 2
									serverConnection.addUdpMessage(oldHighestBidder, "!overbid " + auction.getDescription());
									*/
								}
							}
							else
							{
								// unsuccessfully bided on the auction
								Auction auction = serverConnection.getAuction(id);
								con.sendMessage("You unsuccessfully bid with " + amount + " on '" + auction.getDescription() + "'. Current highest bid is "
										+ auction.getCurrentPrice() + ".");
							}
						}
						else
						{
							con.sendMessage("Not enough or too many arguments for bidding!");
						}
					}
					else
					{
						con.sendMessage("You have to login to bid on auctions!");
					}
				}
				else if (command.contains("!end"))
				{
					// close the connection and end this thread. (also log the user out, if not done already)
					exit = true;
					
					// the user gets logged out in the finally statement!
					con.sendMessage("Goodbye!");
				}
				else
				{
					// wrong command, Try Again!
					con.sendMessage("Your command is not recognized from the server!");
				}
				
			}
		}
		catch (Exception e)
		{
			
			try {
				//analyticsServer.processEvent(new UserEvent(getID(), USER_DISCONNECTED, getCurrentTimeStamp(), currentUser.toString()));
			} catch (Exception e1) {
				System.out.println("ERROR processEvent" + e1);
			} 
			System.out.println("Server Connection Thread interrupt" + e);
		}
		finally
		{
			/*try {
				analyticsServer.processEvent(new UserEvent(getID(),"USER_LOGOUT", getCurrentTimeStamp(), currentUser.toString()));
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("ERROR processEvent" + e);
			}*/
			// log the user out, if the Server gets stopped
			serverConnection.logUserOut(currentUser);	
			con.closeConnection();
		}
		
	}
}
