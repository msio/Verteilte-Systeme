package testComponent;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.Scanner;

public class TestComponent
{
	public static void main(String[] args)
	{
		
		// read the parameters
		String serverHostName = "";
		int tcpPort = 0;
		String analyticsBindingName = "";
		String billingBindingName = "";
		
		if (args.length == 4)
		{
			try
			{
				serverHostName = args[0];
				tcpPort = Integer.parseInt(args[1]);
				analyticsBindingName = args[2];
				billingBindingName = args[3];
				
			}
			catch (NumberFormatException e)
			{
				System.err.println("The tcpPort Number must be an integer.");
				System.exit(1);
			}
		}
		else
		{
			System.out.println("Usage:\n" + "java TestComponent <serverHostName> <tcpPort> <analytics bindingName> <billing bindingName>\n"
					+ "tcpPort: Integer holding the TCP Port for incoming massages.");
			System.exit(1);
		}
		
		// get the settings from loadtest.properties
		int numberOfClients = 0;
		int auctionsPerMinute = 0;
		int auctionDuration = 0;
		int updateIntervalSec = 0;
		int bidsPerMinute = 0;
		
		InputStream in = ClassLoader.getSystemResourceAsStream("loadtest.properties");
		
		if (in != null)
		{
			
			Properties pros = new Properties();
			
			try
			{
				pros.load(in);
				numberOfClients = Integer.valueOf(pros.getProperty("clients"));
				auctionsPerMinute = Integer.valueOf(pros.getProperty("auctionsPerMin"));
				auctionDuration = Integer.valueOf(pros.getProperty("auctionDuration"));
				updateIntervalSec = Integer.valueOf(pros.getProperty("updateIntervalSec"));
				bidsPerMinute = Integer.valueOf(pros.getProperty("bidsPerMin"));
				
			}
			catch (IOException e)
			{
				System.out.println("Could not read from the stream");
			}
			finally
			{
				
				try
				{
					in.close();
				}
				catch (IOException e)
				{
					System.out.println("Could not close the stream " + e);
				}
			}
			
		}
		else
		{
			
			System.out.println("Could not find loadtest.properties");
			System.exit(1);
		}
		
		// start the clients
		Thread[] clients = new Thread[numberOfClients];
		
		for (int i = 0; i < numberOfClients; i++)
		{
			clients[i] = new Thread(new Client(serverHostName, tcpPort, "user" + i, auctionsPerMinute, auctionDuration, updateIntervalSec, bidsPerMinute));
			clients[i].start();
		}
		
		// start the managementClient
		
		Scanner scan = new Scanner(System.in);
		
		// Block until Enter for next Line is pressed
		/*
		boolean end = false;
		
		while (!end)
		{
			String msg = scan.next();
			
			if (msg.equals("!end"))
			{
				end = true;
			}
		}
		
		scan.close();
		*/
		
		//better is
		
		String [] arguments = new String[3];
		arguments[0] = analyticsBindingName;
		arguments[1] = billingBindingName;
		arguments[2] = "true";
		try
		{
			managementClient.ManagementClient.main(arguments);
		}
		catch (IOException e)
		{
			System.out.println("IOExceptoin when starting the ManagementClient.");
		}
		
		
		//interrupt the clients
		
		for (int i = 0; i < numberOfClients; i++)
		{
			clients[i].interrupt();
		}
	}
}
