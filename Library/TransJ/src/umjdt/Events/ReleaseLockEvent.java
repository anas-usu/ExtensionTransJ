package umjdt.Events;

import java.util.Timer;
import java.util.TimerTask;
import umjdt.concepts.*;
import umjdt.util.LockType;
import umjdt.util.Timestamp;

public class ReleaseLockEvent extends LockEvent{

	private Timestamp releaseTime;
	private LockType lockType;
	
	public ReleaseLockEvent(Lock _lock, long _requestTime) {
		super(_lock);
		setLocking(_lock);
		setReleaseTime(getLocalTime());
		
		setTimer(new Timer());
		getTimer().schedule(new LockTask(), _requestTime);
	}

	public LockType getLockType() {
		return lockType;
	}

	public void setLockType(LockType lockType) {
		this.lockType = lockType;
	}

	class LockTask extends TimerTask {
	    public void run() {
	      System.out.println("Release lock !");
	      // release resource
	    }
	 }
	 public Timestamp getReleaseTime() {
		return releaseTime;
	}
	
	 public void setReleaseTime(Timestamp releasetLockTime) {
		this.releaseTime = releasetLockTime;
	}
}