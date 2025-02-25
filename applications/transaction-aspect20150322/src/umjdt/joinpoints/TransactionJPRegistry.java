package umjdt.joinpoints;

import java.util.Collection;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.apache.log4j.Logger;

import umjdt.concepts.TID;

public class TransactionJPRegistry 
{
	Logger logger = Logger.getLogger(TransactionJPRegistry.class);

	
	private static Hashtable<TID, TransJP> transactionJPs;
	
	public static boolean add(TID _key, TransJP _transjp)
	{
		boolean result= false;
		try
		{
			transactionJPs.put(_key, _transjp);
			result=true;
		}
		catch (Exception ex)
		{
			System.out.println(ex.getMessage());
		}
		return result;
	}
	
	public static boolean remove(TID _key)
	{
		boolean result= false;
		try
		{
			transactionJPs.remove(_key);
			result=true;
		}
		catch (Exception ex)
		{
			System.out.println(ex.getMessage());
		}
		return result;
	}
	
	public static List<TransJP> print()
	{
		List<TransJP> result= null;
		if(transactionJPs != null)
		{
			Collection collection = transactionJPs.values();
			Iterator itr = collection.iterator();
			 while(itr.hasNext())
			 {
				result.add(((TransJP) itr.next()));
				System.out.println(itr.next() + " Transaction ID: " + ((TransJP) itr.next()).getTransaction().getTId());
			 }
		}
		return result;
	}
	
	public static TransJP lookup(TID _tid)
	{
		for (Object o: transactionJPs.entrySet())
		 {
		        Map.Entry entry = (Map.Entry) o;
		        if(entry.getKey().equals(_tid))
		        {
		            return (TransJP) entry.getValue();
		        }
		    }
		    return null;
	}
}