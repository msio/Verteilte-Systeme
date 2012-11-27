package biddingClient;
import java.util.Scanner;

public class Client
{
	public static void main(String[] args)
	{
		String hostName = "";
		int tcpPortNumber = -1;
		int udpPortNumber = -1;
		
		if (args.length == 3)
		{
			try
			{
				hostName = args[0];
				tcpPortNumber = Integer.parseInt(args[1]);
				udpPortNumber = Integer.parseInt(args[2]);
			}
			catch (NumberFormatException e)
			{
				System.err.println("The tcpPort Number or udpPort Number must be an integer.");
				System.exit(1);
			}
		}
		else
		{
			System.out.println("Usage:\n" + "java Client <hostName> <tcpPort> <udpPort>\n" + "hostName: hostName or IP Address of the auction server.\n"
					+ "tcpPort: Integer holding the TCP Port of the auction server.\n"
					+ "udpPort: The clients own udp Address for incoming messages from the server.");
			System.exit(1);
		}
		
		ClientConnection con = new ClientConnection(hostName, tcpPortNumber, udpPortNumber);
		
		// Start Threads for listing on Tcp and Udp Port
		
		Thread tcpListener = new Thread(new ClientTcpListener(con));
		/* TODO: not needed in exercise 2
		Thread udpListener = new Thread(new ClientUdpListener(con));
		*/
		
		tcpListener.start();
		/* TODO: not needed in exercise 2
		udpListener.start();
		*/
		
		boolean exit = false;
		
		Scanner scan = new Scanner(System.in);
		
		String command = "";
		
		while (!exit)
		{
			// get a command from the commandline and send it to the server
			if(scan.hasNextLine())
			{
				command = scan.nextLine();
				
				if(command.contains("!login"))
				{
					command += " " + udpPortNumber;
					String[] sub = command.split(" ");
					con.setCurrentUserName(sub[1]);
				}
				else if(command.contains("!logout"))
				{
					con.setCurrentUserName(null);
				}
				
				con.sendMessage(command);
				
				if(command.contains("!end"))
				{
					exit = true;
				}
			}
		}
		
		scan.close();
		
		tcpListener.interrupt();
		/* TODO: not needed in exercise 2
		udpListener.interrupt();
		*/
		
		System.out.println("closing the connection to the server");
		con.closeConnection();
	}
}
