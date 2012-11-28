package billingServer;

public class BillElement
{
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
		this.variableFee = variableFee;
		totalFee = fixedFee + variableFee;
	}
	
	public String toString()
	{
		return String.format("%10s\t %12s\t %9s\t %12s\t %9s\t", auctionID, strikePrice, fixedFee, variableFee, totalFee);
	}
}
