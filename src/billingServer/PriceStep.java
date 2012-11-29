package billingServer;

public class PriceStep
{
	private double startPrice;
	private double endPrice;
	private double fixedPrice;
	private double variablePricePercent;
	
	public PriceStep(double startPrice, double endPrice, double fixedPrice, double variablePricePercent)
	{
		this.startPrice = startPrice;
		this.endPrice = endPrice;
		this.fixedPrice = fixedPrice;
		this.variablePricePercent = variablePricePercent;
	}

	public double getStartPrice()
	{
		return startPrice;
	}

	public double getEndPrice()
	{
		return endPrice;
	}
	
	public double getFixedPrice()
	{
		return fixedPrice;
	}
	
	public double getVariablePricePercent()
	{
		return variablePricePercent;
	}
	
	public String toString()
	{
		return String.format("%9s\t %9s\t %9s\t %12s\t", startPrice, endPrice, fixedPrice, variablePricePercent);
	}
}
