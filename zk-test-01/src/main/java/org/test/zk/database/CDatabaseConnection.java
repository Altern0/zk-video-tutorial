package org.test.zk.database;


import java.sql.Connection;
import java.sql.DriverManager;
import commonlibs.commonclasses.CLanguage;
import commonlibs.extendedlogger.CExtendedLogger;

public class CDatabaseConnection  {
   
    protected Connection dbConnection;
    
    protected CDatabaseConnectionConfig dbConnectionConfig;                    
  
    
    public CDatabaseConnection() {
        
        dbConnection = null;
        
        dbConnectionConfig = null;                    
          
    }
    
    public boolean makeConnectionToDatabase ( CDatabaseConnectionConfig localDBConnectionConfig, CExtendedLogger localLogger, CLanguage localLanguage ) {  
        
        boolean bResult = false;
        try {
            
            if ( localDBConnectionConfig != null ) {
                
              //Class.forName( localDBConnectionConfig.getDriver()); //"com.mysql.jdbc.Driver" tambien funciona de la siguiente manera
                Class.forName( localDBConnectionConfig.strDriver );//"com.mysql.jdbc.Driver"
                
                localLogger.logMessage( "1" , CLanguage.translateIf( localLanguage, "Loaded driver [%s]", localDBConnectionConfig.strDriver ) );
    
                final String strDBURL = localDBConnectionConfig.getPrefix() + localDBConnectionConfig.getHost() + "/" + localDBConnectionConfig.strDatabase;// "jdbc:mysql://localhost/testdb";

                localLogger.logMessage( "1", CLanguage.translateIf( localLanguage, "Try to connecting to [%s] user [%s] password [%s]", strDBURL, localDBConnectionConfig.strUser, localDBConnectionConfig.strPassword ) );
                                
                final String strUSER = localDBConnectionConfig.getUser(); // "root";        
                
                final String strPASS =  localDBConnectionConfig.getPassword(); // "mysql";         
                
               // dbConnection = ( Connection ) DriverManager.getConnection( strDBURL, strUSER, strPASS ); 
               // dbConnection.setTransactionIsolation( Connection.TRANSACTION_READ_COMMITTED ); // aislamiento de la transaccion 
               // dbConnection.setAutoCommit( false ); //estableser confirmacion automatica
               // lo sustituimos por esta ora form
                
                Connection localDBConnection = DriverManager.getConnection( strDBURL, strUSER, strPASS );
                
                localDBConnection.setTransactionIsolation( Connection.TRANSACTION_READ_COMMITTED ); // aislamiento de la transaccion
                
                localLogger.logMessage( "1", CLanguage.translateIf( localLanguage, "Connected to [%s] user [%s] password [%s]", strDBURL, strUSER, strPASS) );
                
                bResult = localDBConnection != null && localDBConnection.isValid( 5 );
               
                if ( bResult ) {
                    
                    localDBConnection.setAutoCommit( false );
                    
                    localLogger.logMessage( "1", CLanguage.translateIf( localLanguage, "Connection auto commit set to false" ) );
                    
                    this.dbConnection = localDBConnection; // Guardo la coneccion a la base de datos en el objeto de instacia
                    
                    this.dbConnectionConfig = localDBConnectionConfig; // guardo la configuracion ade la coneccion de la base de datos
                    
                    localLogger.logMessage( "1", CLanguage.translateIf( localLanguage, "Connection checked" ) );
                    
                }
                else {
                    
                    localDBConnection.close();
                    
                    localDBConnection = null;

                    localLogger.logError( "-1001" , CLanguage.translateIf( localLanguage, "Failed check the connection" ) );
                }
                    
            }
            else {
                
                localLogger.logWarning( "-1" , CLanguage.translateIf( localLanguage, "The database is already initiated" ) );

            }
            
        } 
        catch (Exception ex ) {
            
            if ( localLogger != null ) {
                
                localLogger.logException( "-1021" , ex.getMessage(), ex );
                
            }

        }
          
        return bResult; 
    }

  
    public boolean closeConnectionToDatabase (CExtendedLogger localLogger, CLanguage localLanguage ) {
    
        boolean bResult = false;
        
        try { 
            
            localLogger.logMessage( "1", CLanguage.translateIf( localLanguage, "Trying to close the connection to the database" ) );
            
            if (dbConnection != null) {
            
              dbConnection.close(); // libera recursos
              
              localLogger.logMessage( "1", CLanguage.translateIf( localLanguage, "Database connection closed successfully" ) );
        
              // ojo pienso que va aqui  bResult = true; 
              
            } 
            else {
                
                localLogger.logWarning( "-1" , CLanguage.translateIf( localLanguage, "The connection variable is null" ) );
                
            }

            dbConnection = null;
            dbConnectionConfig = null;
            bResult = true; // OJO pienso que va arriva 
            localLogger.logMessage( "1", CLanguage.translateIf( localLanguage, "Set to null connection and conection config variable" ) );

        }
        catch ( Exception ex ) {
            
            if ( localLogger != null ) {
                
                localLogger.logException( "-1021" , ex.getMessage(), ex );
                
            }
        }
        
        return bResult;
        
    }

    public boolean isValid( CExtendedLogger localLogger, CLanguage localLanguage ) {
        
        boolean bResult = false;
        
        try {
            
            localLogger.logMessage( "1", CLanguage.translateIf( localLanguage, "Checking for database connection is valid" ) );
            
            bResult = dbConnection.isValid( 5 ); //wait max for 5 seconds
            
        } 
        catch ( Exception Ex ) {
            
            if ( localLogger != null ) {
                
                localLogger.logException( "-1021" , Ex.getMessage(), Ex );
                
            }
            
        }
        
        return bResult;
        
    }

    
    
    public Connection getDBConnection() {
        
        return dbConnection;
    }

    
    public void setDBConnection( Connection dbConnection, CExtendedLogger localLogger, CLanguage localLanguage  ) {
        
        localLogger.logWarning( "-1" , CLanguage.translateIf( localLanguage, "Set database connection manually" ) );
        
        this.dbConnection = dbConnection;
        
    }
    
    public CDatabaseConnectionConfig getDBConnectionConfig() {
        
        return dbConnectionConfig;
    }

    
    public void setDBConnectionConfig( CDatabaseConnectionConfig databaseConnectionConfig, CExtendedLogger localLogger, CLanguage localLanguage   ) {
        
        localLogger.logWarning( "-1" , CLanguage.translateIf( localLanguage, "Set database connection config manually" ) );
        
        this.dbConnectionConfig = databaseConnectionConfig;
    }
    
}
