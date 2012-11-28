package billingServer;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;

public class PriceSteps implements Serializable 
{
	private ArrayList<PriceStep> priceSteps;
	
	PriceSteps()
	{
		priceSteps = new ArrayList<PriceStep>();
	}
	
	public void addStep(double startPrice, double endPrice, double fixedPrice, double variablePricePercent) throws IllegalArgumentException
	{
		for(PriceStep step : priceSteps)
		{
			if((startPrice <= step.getStartPrice() && step.getStartPrice() <= endPrice) || (startPrice <= step.getEndPrice() && step.getEndPrice() <= endPrice))
				throw new IllegalArgumentException();
		}
		
		double correctedEndPrice = endPrice;
		if(endPrice == 0)
		{
			correctedEndPrice = Double.POSITIVE_INFINITY;
		}
		
		
		priceSteps.add(new PriceStep(startPrice, correctedEndPrice, fixedPrice, variablePricePercent));
		
		
		
	}
	
	public void deleteStep(double startPrice, double endPrice) throws IllegalArgumentException
	{
		double correctedEndPrice = endPrice;
		if(endPrice == 0)
		{
			correctedEndPrice = Double.POSITIVE_INFINITY;
		}
		
		// get an Iterator
		Iterator<PriceStep> itr = priceSteps.iterator();
		
		PriceStep step;
		while (itr.hasNext())
		{
			step = (PriceStep) itr.next();
			
			if (step.getStartPrice() == startPrice && step.getEndPrice() == correctedEndPrice)
			{
				itr.remove();
				return;
			}
		}
		
		//no existing interval 
		throw new IllegalArgumentException();
	}
	
	public double findFixedFee(double price)
	{
		for(PriceStep step : priceSteps)
		{
			if(step.getStartPrice() <= price && price <= step.getEndPrice())
			{
				return step.getFixedPrice();
			}
		}
		
		return 0;
	}
	
	public double findVariableFeePercent(double price)
	{
		for(PriceStep step : priceSteps)
		{
			if(step.getStartPrice() <= price && price <= step.getEndPrice())
			{
				return step.getVariablePricePercent();
			}
		}
		
		return 0;
	}
}
