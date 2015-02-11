package umjdt.util;

import java.io.Serializable;

public class ContextTrace implements Serializable
{
	private String trackedContext;
	private String accessType;
	
	public ContextTrace(String newContext, String newType)
	{
		trackedContext = newContext;
		accessType = newType;
	}
	public String getAccessType() 
	{
		return accessType;
	}
	
	public void setAccessType(String accessType) 
	{
		this.accessType = accessType;
	}
	
	public String getTrackedContext() 
	{
		return trackedContext;
	}
	
	public String getTrackedContextName()
	{	
		return trackedContext;
	}
	
	public void setTrackedContext(String newContext) 
	{
		trackedContext = newContext;
	}
	
	public boolean equals(Object o)
	{
		ContextTrace otherTrail = (ContextTrace) o;
		
		boolean result = false;
		
		if(this.trackedContext.equals(otherTrail.getTrackedContext()) &&
				this.accessType.equals(otherTrail.getAccessType()))
			result = true;
		
		return result;
	}
	
	public int hashCode()
	{
		return accessType.hashCode() + trackedContext.hashCode();
	}
}

