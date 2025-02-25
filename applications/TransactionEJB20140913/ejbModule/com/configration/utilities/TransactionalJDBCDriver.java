package com.configration.utilities;

import java.net.Inet4Address;
import java.util.*;
import java.util.concurrent.Executor;
import java.sql.*;

import javax.sql.*;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.transaction.*;
import javax.transaction.xa.*;

//to guarantee the ACID properties
import com.arjuna.ats.jdbc.logging.*;
import com.arjuna.ats.jdbc.TransactionalDriver.*;
import com.arjuna.ats.jdbc.TransactionalDriver;
import com.arjuna.ats.arjuna.common.*;
//import com.arjuna.ats.arjuna.xa.*;
import com.arjuna.ats.jdbc.common.jdbcPropertyManager;
import com.arjuna.ats.jdbc.logging.jdbcLogger;
import com.arjuna.ats.*;
import com.arjuna.ats.jta.*;
import com.arjuna.ats.jta.utils.*;
import com.arjuna.ats.jta.xa.XidImple;
import com.arjuna.ats.jta.UserTransaction.*;
import com.arjuna.ats.jta.TransactionManager;
import com.arjuna.ats.jta.common.*;
//import com.arjuna.ats.jta.common.Environment;
import com.arjuna.ats.jta.recovery.*;
import com.arjuna.ats.jta.xa.XAModifier;
import com.arjuna.ats.jta.xa.RecoverableXAConnection;


import com.arjuna.ats.internal.jdbc.*;
import com.arjuna.ats.internal.jta.transaction.*;
import com.arjuna.ats.internal.jta.transaction.arjunacore.TransactionManagerImple;
import com.arjuna.ats.internal.jta.xa.*;
//import com.arjuna.ats.internal.jts.context.*;
import com.arjuna.ats.internal.jdbc.drivers.modifiers.ConnectionModifier;
import com.arjuna.ats.internal.jdbc.drivers.modifiers.ModifierFactory;
import com.arjuna.common.internal.util.propertyservice.PropertyManagerImpl;


import com.arjuna.ats.arjuna.common.arjPropertyManager;
//import com.arjuna.ats.arjuna.common.Configuration;


//import com.arjuna.common.util.propertyservice.PropertyManager;
//import com.arjuna.common.internal.util.propertyservice.PropertyManagerImpl;
import org.jnp.interfaces.NamingContextFactory;

//import org.jboss.util.property.*;
import com.microsoft.sqlserver.jdbc.*;

import org.jboss.jca.adapters.jdbc.extensions.mssql.MSSQLValidConnectionChecker;
import org.jboss.jca.adapters.jdbc.xa.XAManagedConnectionFactory;

import com.microsoft.sqlserver.jdbc.SQLServerConnection;

public class TransactionalJDBCDriver {
    
	//public static jts.TransactionManagerImple tm = new jts.TransactionManagerImple();
	public static TransactionManager tm =
			new  TransactionManager();
	
	public static Connection setupGooPileXAConnection(String dbtype, String dbName, String userName, String password,String sqlStatement){  
		   Connection con = null;
		   try{ 
			  loadDrivers(dbtype);
		      String connectionUrl = sqlDriver( dbName,  userName,  password); 
		      con = DriverManager.getConnection(connectionUrl); 
		      con= manageJDNI(userName, password,sqlStatement);
		      System.out.println("Connected with GooPile Datasource."); 
		      
		      // Create and execute an SQL statement that returns some data.  
		      String SQL = "SELECT ID, name FROM Goo";  
		      Statement stmt = con.createStatement();  
		      ResultSet rs = stmt.executeQuery(SQL);
		      // Iterate through the data in the result set and display it.  
		      while (rs.next()){  
		           System.out.println(rs.getString(1) + " " + rs.getString(2));  
		           }
		   }catch(Exception e){ 
		    System.out.println(e.getMessage()); 
		    e.printStackTrace();
		    System.exit(0);  
		   } 
		   return con;
		}
	
    public static void loadDrivers(String dbType){    	
 
    	// Register the driver via the system properties variable "jdbc.drivers"
		Properties p = System.getProperties();
		try{
		switch(dbType){
			case "ORACLE":
				p.put("jdbc.drivers", "oracle.jdbc.driver.OracleDriver");
				break;
			case "SQLSERVER":
				p.put("jdbc.drivers", "com.microsoft.sqlserver.jdbc.SQLServerDriver");
				break;
			case "MYSQL":
			    p.put("jdbc.drivers", "com.mysql.jdbc.Driver"); 
			    break;
			case "PGSQL":
			    p.put("jdbc.drivers", "org.postgresql.Driver"); 
			    break;
		}
		System.setProperties(p);
		}catch(Exception ex){
			ex.printStackTrace();
			System.out.println(ex.getMessage());
		}	
    }

    // Establish the connection.
    public static String getConnectionUrl(String prefix, String serverName, int portNumber,
    		String databaseName, String username, String password) throws SQLException{
    	
        String connectionUrl = prefix + serverName + ":" + portNumber
           + ";databaseName=" + databaseName + ";user=" + username + ";password=" + password;
        return connectionUrl;
    }
    
    public static String sqlDriver(String dbName, String userName, String password) throws ClassNotFoundException {
		// Load the SQLServerDriver class, build the 
	      // connection string, and get a connection 
	      String connectionUrl = null;
		try {
			connectionUrl = getConnectionUrl("jdbc:sqlserver://","localhost", 1433, dbName, userName, password);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return connectionUrl;
	}
    
    public static Connection manageJDNI(String username, String password, String sqlStatement){
    	Connection connection=null; 
    	XADataSource ds=null;
    	try{
    		ds = new SQLServerXADataSource();
	        
	        ((SQLServerDataSource) ds).setUser(username);
	        ((SQLServerDataSource) ds).setPassword(password);
	        ((SQLServerDataSource) ds).setServerName("localhost");
	        ((SQLServerDataSource) ds).setPortNumber(1433);
	        ((SQLServerDataSource) ds).setDatabaseName("GooPile");
	        
    		Context ctx = contextFactory();
	        ctx.bind("jdbc/foo", ds);
	        
	        Properties dbProps = new Properties();
	        dbProps.setProperty(TransactionalDriver.userName, username);
	        dbProps.setProperty(TransactionalDriver.password, password);
	        TransactionalDriver arjunaJDBCDriver = new TransactionalDriver();
	        connection = arjunaJDBCDriver.connect("jdbc:arjuna:jdbc/foo", dbProps);
	        
	        manageXADataSource(ds, connection,sqlStatement);
	        
    	} catch(Exception ex){
    		System.out.println(ex.getMessage());
    		ex.printStackTrace();
    	}
    	return connection;
    }

	private static Context contextFactory() throws NamingException {
		//Storing a datasource in a JNDI implementation
		//Communicate specific information about the JNDI service the application is using.
		//Context.INITIAL_CONTEXT_FACTORY specifies
		//the factory class that creates an initial context for the service we want to use
	
	    //java.naming.factory.initial=org.jnp.interfaces.NamingContextFactory
	    //java.naming.provider.url=jnp://hostname:1099
	    //java.naming.factory.url.pkgs=org.jboss.naming:org.jnp.interfaces

//		Properties env = new Properties();
//	    env.put(Context.INITIAL_CONTEXT_FACTORY,"org.jboss.naming.remote.client.InitialContextFactory");
//	    env.put(Context.PROVIDER_URL,"remote://localhost:8080");
//	    env.put(Context.SECURITY_PRINCIPAL, "");
//	    env.put(Context.SECURITY_CREDENTIALS, "");
//	    env.put("jboss.naming.client.ejb.context", true);
	   	    		
		Hashtable env = new Hashtable();
		String initialContext = "";//PropertyManager.getProperty("Context.INITIAL_CONTEXT_FACTORY");
		env.put(Context.INITIAL_CONTEXT_FACTORY, initialContext);
		env.put(Context.PROVIDER_URL, "http://localhost:8080/");
	    Context ctx = new InitialContext(env);
		return ctx;
	}
    
    //JNDI: Java Naming and Directory Interface
    public static void manageXADataSource(XADataSource ds, Connection con,String sqlStatement){
    	// Create the XA data source and XA ready connection.
    	 XAResource xaRes = null;
    	try{
	        XAConnection xaCon = ds.getXAConnection();
	        con = xaCon.getConnection();
	        
	     // Get the XAResource object and set the timeout value.
	         xaRes = xaCon.getXAResource();
	         xaRes.setTransactionTimeout(0);
	         TransactionManager.transactionManager().getTransaction().enlistResource(xaRes);
	         
	    // Perform the XA transaction.
	         xaRes.start(getXid(), XAResource.TMNOFLAGS);
	         PreparedStatement pstmt = con.prepareStatement(sqlStatement);
	         pstmt.executeUpdate();

	   // Commit the transaction.
	         xaRes.end(getXid(),XAResource.TMSUCCESS);
	         xaRes.commit(getXid(),true);

	   // Cleanup.
	         con.close();
	         xaCon.close();
	         
    	}catch(Exception ex){
    		System.out.print(ex.getMessage());
    		ex.printStackTrace();
    	}
    }
    
    public static Xid getXid(){
    	XidImple tid= new XidImple();
    	Xid xid= (Xid) tid.getXID();
    	System.out.println("xid = " + xid.toString());
    	return (Xid)xid;
    }   
}