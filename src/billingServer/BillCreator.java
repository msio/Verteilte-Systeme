package billingServer;

import java.util.HashMap;
import java.util.Map;

public class BillCreator
{
	private String username;
	private HashMap<Long, Double> auctions;
	
	public BillCreator(String username)
	{
		this.username = username;
		auctions = new HashMap<Long, Double>();
	}
	
	public Bill createBill(PriceSteps priceSteps)
	{
		Bill bill = new Bill();
		
		for(Map.Entry<Long, Double> entry : auctions.entrySet())
		{
			double fixed = priceSteps.findFixedFee(entry.getValue());
			double variablePercent = priceSteps.findVariableFeePercent(entry.getValue());
			
			bill.addBillElement(entry.getKey(), entry.getValue(), fixed, variablePercent );
		}
		
		return bill;
	}

	public String getUsername()
	{
		return username;
	}

	public void addAuction(long auctionID, double price)
	{
		auctions.put(auctionID, price);
		
	}
}
