package umjdt.concepts;

import java.io.Serializable;

import umjdt.util.LockType;
import umjdt.util.Semaphore;

public class Lock implements Serializable{

	private Transaction trasaction;
	private Resource resource;
	private boolean is_trylock;
	private int lock_id;
	private String lock_name;
	private long timeout;
	private LockType type;
	
	transient private int numberOfReaders; // number of readers
	private Semaphore readerSemaphore; // synchronizing access to nr
	private Semaphore readWriteSemaphore; // synchronizing Read/Write access to an object
	transient private String readLockHolder, writeLockHolder, updateLockHolder; // holders of the respective locks
	transient private String operation; // the name of the method on which a lock was obtained

	
	public Lock(){	
		semaphoreLock();
	}
	
	public Lock(int _lock_id, String _lockName, Transaction _transaction){
		setLock_id(_lock_id);
		setLock_name(_lockName);
		setTrasaction(_transaction);
		semaphoreLock();
	}
	
	public Lock(int _lock_id, String _lockName, Resource _resouce, Transaction _transaction){
		setLock_id(_lock_id);
		setLock_name(_lockName);
		setTrasaction(_transaction);
		setResource(_resouce);
		semaphoreLock();
	}
	
	public Lock(int _lock_id, String _lockName, Resource _resource,Transaction _transaction, long _timeout, LockType _type){
		setLock_id(_lock_id);
		setLock_name(_lockName);
		setTrasaction(_transaction);
		setTimeout(_timeout);
		setType(_type);
		setResource(_resource);
	}
	
	public void semaphoreLock(){
		numberOfReaders = 0;
		
		readerSemaphore = new Semaphore();
		readWriteSemaphore = new Semaphore(); 
		
		readLockHolder = "#";
		writeLockHolder = "#";
		updateLockHolder = "#";
		operation = "#";
	}
	
	/**
	 * Get read lock. 
	 * 
	 */
	public void getReadLock(String holder, String name)
	{
		readerSemaphore.P();
		numberOfReaders = numberOfReaders + 1;
		
		if(numberOfReaders == 1)
		{
			readWriteSemaphore.P();
			//System.out.println("------------------Someone got the read lock!-----------");
		}
		readLockHolder = holder;
		operation = name;
		
		
		readerSemaphore.V();

	}

	public void releaseReadLock()
	{
		readerSemaphore.P();
		numberOfReaders = numberOfReaders - 1;

		if(numberOfReaders == 0)
		{
			readLockHolder = "";
			operation = "";
			readWriteSemaphore.V();
			//System.out.println("------------------Someone released the read lock!-----------");
		}				
	
	    readerSemaphore.V();
 
	
	}
	
	public void getWriteLock(String holder, String name)
	{
		readWriteSemaphore.P();
		//System.out.println("------------------Someone got the write lock!-----------");
		writeLockHolder = holder;
		operation = name;
	}
	

	public void releaseWriteLock()
	{
		writeLockHolder = "";
		operation = "";
			  	 
		readWriteSemaphore.V();
		//System.out.println("------------------Someone released the write lock!-----------");
				
	}
	
	public void getUpdateLock(String holder,String name)
	{
		readWriteSemaphore.P();
		updateLockHolder = holder;
		operation = name;
	}
	
	public void releaseUpdateLock()
	{
		updateLockHolder = "";
		operation = "";
			  
		readWriteSemaphore.V();

	}
	
	public String getReadLockHolder()
	{
		return readLockHolder;
	}
	
	public String getWriteLockHolder()
	{
		return writeLockHolder ;
	}
	
	public String getUpdateLockHolder()
	{
		return updateLockHolder;
	}
	
	public String getMethodName()
	{
		return operation;
	}
	public boolean isIs_trylock() {
		return is_trylock;
	}
	public void setIs_trylock(boolean is_trylock) {
		this.is_trylock = is_trylock;
	}
	public int getLock_id() {
		return lock_id;
	}
	public void setLock_id(int lock_id) {
		this.lock_id = lock_id;
	}
	public String getLock_name() {
		return lock_name;
	}
	public void setLock_name(String lock_name) {
		this.lock_name = lock_name;
	}
	public Transaction getTrasaction() {
		return trasaction;
	}
	public void setTrasaction(Transaction trasaction) {
		this.trasaction = trasaction;
	}
	public long getTimeout() {
		return timeout;
	}
	public void setTimeout(long timeout) {
		this.timeout = timeout;
	}
	public LockType getType() {
		return type;
	}
	public void setType(LockType type) {
		this.type = type;
	}
	public Resource getResource() {
		return resource;
	}

	public void setResource(Resource resource) {
		this.resource = resource;
	}
}