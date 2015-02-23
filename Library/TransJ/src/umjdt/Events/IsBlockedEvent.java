package umjdt.Events;

import java.util.Timer;
import java.util.TimerTask;

import javax.transaction.SystemException;

import umjdt.concepts.*;
import umjdt.util.Status;

public class IsBlockedEvent extends Event{

	private Resource resource;
	private boolean isblocked;
	private Transaction transaction;
	Timer timer = new Timer();
	
	public boolean IsBlocked(Transaction _transaction) throws SystemException
	{
		this.transaction = _transaction;
		
		if(transaction.getStatus()== Status.BLOCKED)
			return true;
		return false;
	}

	public void checkStatus()
	{
		timer.schedule( new TimerTask() 
		{ 
		    public void run() 
		    { 
		    	try
		    	{
					IsBlocked(getTransaction());
				} 
		    	catch (SystemException e) 
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		    } 
		}, 0, 60*(1000*1));
	}
	
	public Resource getResource() 
	{
		return resource;
	}

	public void setResource(Resource resource) 
	{
		this.resource = resource;
	}

	public boolean isIsblocked() 
	{
		return isblocked;
	}

	public void setIsblocked(boolean isblocked) 
	{
		this.isblocked = isblocked;
	}

	public Transaction getTransaction() 
	{
		return transaction;
	}

	public void setTransaction(Transaction transaction) 
	{
		this.transaction = transaction;
	}

	public Timer getTimer() {
		return timer;
	}

	public void setTimer(Timer timer) {
		this.timer = timer;
	}
	
	
	// run 
	private static final class TimeoutTask extends Thread 
	{
        private final long _timeoutMs;
        private Runnable _runnable;

        private TimeoutTask(long timeoutMs, Runnable runnable) {
            _timeoutMs = timeoutMs;
            _runnable = runnable;
        }

        @Override
        public void run() 
        {
            long start = System.currentTimeMillis();
            while (System.currentTimeMillis() < (start + _timeoutMs)) 
            {
                _runnable.run();
            }
            System.out.println("execution took " + (System.currentTimeMillis() - start) +" ms");
        }
    }
	
    public void status() throws Exception {
        new TimeoutTask(2000L, new Runnable() {
            
        	@Override
            public void run() {
                System.out.println(" ");
                try {
                    // pretend it's taking somewhat longer than it really does
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();
    }
}
