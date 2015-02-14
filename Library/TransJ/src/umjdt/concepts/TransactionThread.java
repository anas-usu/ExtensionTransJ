package umjdt.concepts;

import java.util.*;
import org.apache.commons.collections4.MultiMap;
import org.apache.commons.collections4.map.MultiValueMap;
import umjdt.Events.Event;
import umjdt.Events.OperationEvent;
import umjdt.Events.TransactionEvent;
import umjdt.util.Timestamp;

public class TransactionThread implements Runnable{

	private List<Event> events = new ArrayList<Event>();	
	private Thread thisThread;
	private String threadName;
	private static int threadId=0;
	
	public TransactionThread(){
		Thread thread = new Thread();
		TransactionThread.threadId = threadId +1;
		setThisThread(thread);
	}
	
	public TransactionThread(String _name){
		TransactionThread.threadId = threadId +1; 
		setThreadId(threadId);
		setThisThread(new Thread());
		getThisThread().setName(_name);
	}
	
	public TransactionThread(Thread _thread, String _name){
		TransactionThread.threadId = threadId +1; 
		setThreadId(threadId);
		_thread.setName(_name);
		setThisThread(_thread); 
	}
	
	public List<Event> getEvents() {
		return events;
	}

	public Timestamp getTimestamp(){
		return new Timestamp();
	}
	public void setEvents(List<Event> events) {
		this.events = events;	
	}
	
	public void addEvent(TransactionEvent event){
		events.add(event);
	}
	public void removeEvent(TransactionEvent event){
		events.remove(event);
	}
	
	public boolean contains(Event e) {
		return events.contains(e);
	}
	
	@Override
	public void run() {		
	}
	
	public Thread getThisThread() {
		return thisThread;
	}
	
	public void setThisThread(Thread thisThread) {
		this.thisThread = thisThread;
	}

	public String getThreadName() {
		return threadName;
	}

	public void setThreadName(String threadName) {
		this.threadName = threadName;
	}
	public static int getThreadId() {
		return threadId;
	}
	public static void setThreadId(int threadId) {
		TransactionThread.threadId = threadId;
	}
}