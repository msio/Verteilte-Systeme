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
	
}
