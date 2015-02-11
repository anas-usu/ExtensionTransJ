package umjdt.util;

import java.util.Calendar;
import java.util.Date;

import utilities.*;

public class Timestamp implements Comparable<Timestamp>{

	private Date localTime;
	private long deltaTime = 0;
	private long timestampPluDelta ;
	// 1) create a java calendar instance
	Calendar calendar = Calendar.getInstance();
	// 2) get a java.util.Date from the calendar instance.
	// this date will represent the current instant, or "now".
	Date now = calendar.getTime();

	public Timestamp()
	{
		localTime = new Date();		
		deltaTime = Constants.TimeToWait;
		setTimestampPluDelta(getTimestampPluDelta());
	}
	
	public Timestamp(long deltaTime)
	{
		localTime = new Date();
		Constants.loadProperties();
		this.deltaTime = deltaTime;
		setTimestampPluDelta(getTimestampPluDelta());
	}
	public Date getLocalTime() {
		return localTime;
	}

	public void setLocalTime(Date localTime) {
		this.localTime = localTime;
	}

	public long getDeltaTime() {
		return deltaTime;
	}

	public void setDeltaTime(long deltaTime) {
		this.deltaTime = deltaTime;
	}

	public long getTimestampPluDelta() {
		return timestampPluDelta;
	}

	public void setTimestampPluDelta(long timestampPluDelta) {
		timestampPluDelta= localTime.getTime()+deltaTime;
		this.timestampPluDelta = timestampPluDelta;
	}
	@Override
	public int compareTo(Timestamp o) {		
		if(this.getTimestampPluDelta() < o.getTimestampPluDelta())
			return 1;
		else
			return -1;
	}
	
	public Timestamp currentTimeStamp(){
		// 3) a java current time (now) instance
		Timestamp currentTimestamp = new Timestamp(now.getTime());
		return currentTimestamp;
	}
	
}