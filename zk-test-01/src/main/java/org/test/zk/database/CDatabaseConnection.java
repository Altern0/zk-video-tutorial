package org.test.zk.database;

import java.io.Serializable;
import java.sql.DriverManager;
import com.mysql.jdbc.Connection;



public class CDatabaseConnection implements Serializable {

    private static final long serialVersionUID = -3231584933279647710L;
    
    static final String _DB_URL = "jdbc:mysql://localhost/testdb";
    static final String _USER = "root";        
    static final String _PASS = "mysql";         
 
    protected Connection dbConnection = null;
                        
    public boolean makeConnectionToDatabase () {  
        
        boolean bResult = false;
        try {
            
            Class.forName( "com.mysql.jdbc.Driver");
            
            dbConnection = ( Connection ) DriverManager.getConnection( _DB_URL, _USER, _PASS );
            
            dbConnection.setTransactionIsolation( Connection.TRANSACTION_READ_COMMITTED ); // aislamiento de la transaccion 
            
            dbConnection.setAutoCommit( false ); //estableser confirmacion automatica
            
            bResult = true;
        } 
        catch (Exception ex ) {
            
            ex.printStackTrace();
            
        }
          
        return bResult; 
    }

    
    public Connection getDBConnection() {
        
        return dbConnection;
    }

    
    public void setDBConnection( Connection dbConnection ) {
        
        this.dbConnection = dbConnection;
    }
    
    public boolean closeConnectionToDatabase () {
    
        boolean bResult = false;
        
        try { 
            if (dbConnection != null) {
            
              dbConnection.close(); // libera recursos
              
              dbConnection = null; 
              
              bResult = true;
           
            }
            
        }
        catch ( Exception ex ) {
            
            ex.printStackTrace();
        }
        
        return bResult;
        
        
    }
    
}
