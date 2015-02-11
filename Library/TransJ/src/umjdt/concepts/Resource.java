package umjdt.concepts;

import umjdt.Events.IsBlockedEvent;
import umjdt.Events.UnexpectedOutcomeException;
import umjdt.util.LockType;

public class Resource {

	private String name;
	private int resourceId;
	private Transaction transaction;
	private String status;	
	private LockType _locktype; 
	private ResourceManager resourceManager;
	private TransactionManager transactionManager;
	private IsBlockedEvent isblockedEvent;
	private UnexpectedOutcomeException exceptionThrownEvent;
	
	public Resource(){
	}
	public Resource(int _id){
	}
	public Resource(String _name){
	}
	public Resource(ResourceManager _rm){
	}
	public Resource(TransactionManager _tm){
	}
	public Resource(TransactionManager _tm, ResourceManager _rm){	
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getResourceId() {
		return resourceId;
	}
	public void setResourceId(int resourceId) {
		this.resourceId = resourceId;
	}
	public Transaction getTransaction() {
		return transaction;
	}
	public void setTransaction(Transaction transaction) {
		this.transaction = transaction;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public ResourceManager getResourceManager() {
		return resourceManager;
	}
	public void setResourceManager(ResourceManager resourceManager) {
		this.resourceManager = resourceManager;
	}
	public TransactionManager getTransactionManager() {
		return transactionManager;
	}
	public void setTransactionManager(TransactionManager transactionManager) {
		this.transactionManager = transactionManager;
	}
	public IsBlockedEvent getIsblockedEvent() {
		return isblockedEvent;
	}
	public void setIsblockedEvent(IsBlockedEvent isblockedEvent) {
		this.isblockedEvent = isblockedEvent;
	}
	public UnexpectedOutcomeException getExceptionThrownEvent() {
		return exceptionThrownEvent;
	}
	public void setExceptionThrownEvent(UnexpectedOutcomeException exceptionThrownEvent) {
		this.exceptionThrownEvent = exceptionThrownEvent;
	}
	public LockType get_locktype() {
		return _locktype;
	}
	public void set_locktype(LockType _locktype) {
		this._locktype = _locktype;
	}
}
