package auctionServer;
import java.util.Scanner;


public class Server
{
	public static void main(String[] args)
	{
		int tcpPortNumber = 0;
		
		if(args.length == 1)
		{
			try
			{
				tcpPortNumber = Integer.parseInt(args[0]);
			}
			catch (NumberFormatException e)
			{
				System.err.println("The tcpPort Number must be an integer.");
				System.exit(1);
			}
		}
		else
		{
			System.out.println("Usage:\n"+
						"java Server <tcpPort>\n"+
						"tcpPort: Integer holding the TCP Port for incoming massages.");
			System.exit(1);
		}
		
		ServerConnection con = new ServerConnection(tcpPortNumber);
		
		Thread t1 = new Thread(new ServerConnectionHandler(con));
		t1.start();
		
		Scanner scan = new Scanner(System.in);
		
		//Block until Enter for next Line is pressed
		scan.hasNextLine();
		
		scan.close();
		
		//at the end close the connection
		con.closeConnection();
		
		
	}
}
