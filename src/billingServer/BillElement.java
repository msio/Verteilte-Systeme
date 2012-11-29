package billingServer;

import java.io.Serializable;

public class BillElement implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private long auctionID;
	private double strikePrice;
	private double fixedFee;
	private double variableFee;
	private double totalFee;
	
	public BillElement(long auctionID, double strikePrice, double fixedFee, double variableFee)
	{
		this.auctionID = auctionID;
		this.strikePrice = strikePrice;
		this.fixedFee = fixedFee;
		this.variableFee = variableFee/100.d * strikePrice;
		totalFee = fixedFee + variableFee;
	}
	
	public String toString()
	{
		return String.format("%10s %12s %9s %12s %9s", auctionID, strikePrice, fixedFee, variableFee, totalFee);
	}
}
