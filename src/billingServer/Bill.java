package billingServer;

import java.io.Serializable;
import java.util.ArrayList;

public class Bill implements Serializable
{
	private ArrayList<BillElement> billElements;
	
	public Bill()
	{
		billElements = new ArrayList<BillElement>();
	}
	
	public void addBillElement(long auctionID, double strikePrice, double fixedFee, double variableFeePercent)
	{
		billElements.add(new BillElement(auctionID, strikePrice, fixedFee, variableFeePercent * strikePrice));
	}
	
	public String toString()
	{
		String ret = "auction_ID strike_price fee_fixed fee_variable fee_total\n";
		
		for(BillElement element : billElements)
		{
			ret += element.toString() + "\n";
		}
		
		return ret;
	}
}
