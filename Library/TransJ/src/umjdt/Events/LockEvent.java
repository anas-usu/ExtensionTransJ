package umjdt.Events;

import umjdt.concepts.*;

public class LockEvent extends Event{

	private Lock locking; 
	private String markBoundary;
	private Operation operation = new Operation();
	private ResourceManager resourcemanager= new ResourceManager();
	
	public LockEvent(){	
	}
	
	public LockEvent(Lock _locking){
		super();
		setLocking(_locking);
	}

	public String getMarkBoundary() {
		return markBoundary;
	}

	public void setMarkBoundary(String markBoundary) {
		this.markBoundary = markBoundary;
	}

	public Operation getOperation() {
		return operation;
	}

	public void setOperation(Operation operation) {
		this.operation = operation;
	}

	public ResourceManager getResourcemanager() {
		return resourcemanager;
	}

	public void setResourcemanager(ResourceManager resourcemanager) {
		this.resourcemanager = resourcemanager;
	}
	
	public Lock getLocking() {
		return locking;
	}

	public void setLocking(Lock locking) {
		this.locking = locking;
	}
}
