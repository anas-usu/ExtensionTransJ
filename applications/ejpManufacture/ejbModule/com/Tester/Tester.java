package com.Tester;

import java.util.*;
import java.sql.*;
import javax.naming.InitialContext;
import javax.sql.*;
import com.arjuna.ats.arjuna.common.Uid;
import com.arjuna.ats.arjuna.coordinator.TransactionReaper;
import com.arjuna.ats.arjuna.coordinator.TxControl;
import com.arjuna.ats.internal.jbossatx.jta.PropagationContextManager;
import com.arjuna.ats.internal.jta.resources.arjunacore.XAOnePhaseResource;
import com.arjuna.ats.internal.jta.transaction.arjunacore.TransactionManagerImple;
import com.arjuna.ats.internal.jta.transaction.arjunacore.UserTransactionImple;
import com.arjuna.ats.jta.xa.XidImple;
import javax.transaction.xa.XAResource;
import javax.transaction.xa.Xid;
import com.client.utilities.ClientUtility;
import com.microsoft.sqlserver.jdbc.SQLServerDataSource;
import com.microsoft.sqlserver.jdbc.SQLServerXADataSource;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;


/**
 * Session Bean implementation class Tester
 */
@Stateless
@TransactionAttribute(TransactionAttributeType.REQUIRED)
@TransactionManagement(TransactionManagementType.CONTAINER)
public class Tester {

    /**
     * Default constructor. 
     */
    public Tester() {
        // TODO Auto-generated constructor stub
    }
    
    public static void main (String[] args) throws SQLException
    {
    	Connection conn = null;
    	Connection conn2 = null;
    	Statement stmtx = null;  // will be a tx-statement
    	
    	XADataSource xaDS = null;   
    	XAConnection xaCon = null;   
    	XAResource xaRes;   
    	XidImple xid;   
    	int ret; 
    	ResultSet result=null;
    	UserTransactionImple userTransaction = new UserTransactionImple();
    	TransactionManagerImple transactionManager= new TransactionManagerImple();    	
    	PropagationContextManager propagationContextManager = new PropagationContextManager();
    	Properties dbProperties = new Properties(); 
    	
    	try{
    		
    		Properties jdniproperties = new Properties();
    		jdniproperties =System.getProperties();
    		ClientUtility cUtil = new ClientUtility();
    		InitialContext cnx = (InitialContext) cUtil.getInitialContext();
    		
    		DriverManager.registerDriver(new com.arjuna.ats.jdbc.TransactionalDriver());
    		xaDS = getDS();   
    		System.out.println(DriverManager.getDrivers());
    		xaCon = xaDS.getXAConnection("anas", "tedi");
    		System.out.println("XA connection is " + xaCon.getConnection());
    		xaRes = xaCon.getXAResource();   
    		System.out.println("XA Connection is "+ xaRes);
    		conn = xaCon.getConnection();
    		System.out.println("XA connection is " + conn+ " Client Information is " + conn.getClientInfo());
    		
    		System.out.println("Starting top-level transaction.");
    		
    		userTransaction.begin();
    		TxControl.enable();
    		//transactionManager.begin();
    		boolean enlistresult = transactionManager.getTransaction().enlistResource(xaRes);
    		stmtx = conn.createStatement();
    		xid = new XidImple(new Uid()); 
    		System.out.println(" Format ID "+xid.getFormatId() +" Global Transaction ID "+ xid.getGlobalTransactionId() + " Quailfier ID "+ xid.getBranchQualifier());
    		System.out.println("\nAdding entries to GooPile");
    		XAOnePhaseResource xaOnePhase = new XAOnePhaseResource(xaRes, xid, null);
    		xaRes.start((Xid) xid, XAResource.TMNOFLAGS);   
    			stmtx.executeUpdate("insert into Goo(name, code, type, price) values('Goo102', '03', 'BC', 20)");
    		xaRes.end((Xid) xid, XAResource.TMSUCCESS);
    		ret = xaRes.prepare((Xid) xid); 
    		System.out.println("Result of prepare method "+ret);
    		if (ret == XAResource.XA_OK) {
    			xaRes.commit((Xid) xid, false);
    		}
    		result= stmtx.executeQuery("Select * from Goo");
    		while(result.next())
    		{
    			System.out.println("Column 1: "+result.getString(1));
    			System.out.println("Column 2: "+result.getString(2));
    		}
    		boolean delistresult= transactionManager.getTransaction().delistResource(xaRes, XAResource.TMSUCCESS);
    		//transactionManager.commit();
    		TxControl.disable();
    		TransactionReaper.terminate(true);
    		//xaOnePhase.rollback();
    		//userTransaction.rollback();
    		userTransaction.commit();
    		 
    		///
    		// Rollback
    		///
    		//System.out.print("\nNow attempting to rollback changes.");
    		//com.arjuna.ats.jta.UserTransaction.userTransaction().rollback();
    		
    	}catch(Exception ex){
    		System.out.println(ex.getMessage());
    		ex.printStackTrace();
    	}
    	finally{
    		stmtx.close();
    		conn.close();
    		xaCon.close();
    	}
    }
    
    public static XADataSource getDS(){
    	XADataSource ds = new SQLServerXADataSource();
    	try{
			/*((SQLServerDataSource) ds).setUser("anas");
			((SQLServerDataSource) ds).setPassword("tedi");
			((SQLServerDataSource) ds).setServerName("localhost");
			((SQLServerDataSource) ds).setPortNumber(1433);*/
			((SQLServerDataSource) ds).setDatabaseName("GooPile");
    	} catch(Exception ex){
    		System.out.println(ex.getMessage());
    		ex.printStackTrace();
    	}
    	return ds;
    }
}
