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
		
		priceSteps.add(new PriceStep(startPrice, endPrice, fixedPrice, variablePricePercent));
		
	}
	
	public void deleteStep(double startPrice, double endPrice) throws IllegalArgumentException
	{
		// get an Iterator
		Iterator itr = priceSteps.iterator();
		
		PriceStep step;
		while (itr.hasNext())
		{
			step = (PriceStep) itr.next();
			
			if (step.getStartPrice() == startPrice && step.getEndPrice() == endPrice)
			{
				itr.remove();
				return;
			}
		}
		
		//no existing interval 
		throw new IllegalArgumentException();
	}
	
}
