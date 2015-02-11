package umjdt.concepts;

import umjdt.util.AccessType;

public class Operation implements Cloneable{

	private String name = "";
	private AccessType type;
	// return data 
	private Object data = new Object();
	//private TransactionEvent beforeEvent  = null;
	//private TransactionEvent afterEvent  = null;
	private Transaction transaction; 
	private TransactionThread transactionThread; 
		
	public Operation(){
	}
	
	public String getName(){
		return this.name;
	}
	
	public Operation(String _name, AccessType _type)
	{
		this.setId(_name);
		this.setType(_type);		
	}


	public String getId()
	{
		return name;
	}

	public void setId(String _name) {
		this.name = _name;
	}

	public AccessType getType() {
		return type;
	}

	public void setType(AccessType _type) {
		this.type = _type;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object _data) {
		this.data = _data;
	}
	
	public Transaction getTransaction() {
		return transaction;
	}

	public void setTransaction(Transaction transaction) {
		this.transaction = transaction;
	}

	public TransactionThread getTransactionThread() {
		return transactionThread;
	}

	public void setTransactionThread(TransactionThread transactionThread) {
		this.transactionThread = transactionThread;
	}

	public void setName(String name) {
		this.name = name;
	}


}