package umjdt.Events;

import umjdt.util.Timestamp;

public class test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Timestamp t = new Timestamp(10);
		
		System.out.println("Local Time "+ t.getLocalTime());
		System.out.println("\nDelta Time "+ t.getDeltaTime());
		System.out.println("\nDelta Time "+ t.getTimestampPluDelta());
		System.out.println("\nDelta Time "+ t.currentTimeStamp().getLocalTime());
		
		IsBlockedEvent isblocked = new IsBlockedEvent();
		isblocked.checkStatus();

	}

}
