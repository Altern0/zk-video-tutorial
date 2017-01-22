package org.test.zk.database;

import java.io.File;
import java.io.FileInputStream;
import java.io.Serializable;
import java.util.Properties;

public class CDatabaseConnectionConfig implements  Serializable{

    private static final long serialVersionUID = -292714503181660487L;
    
    protected String strDriver = null;
    protected String strPrefix = null;
    protected String strHost = null;
    protected String strPort = null;
    protected String strUser = null;
    protected String strPassword = null;
    protected String strDatabase = null;
    
    public CDatabaseConnectionConfig (String strDriver, String strPrefix, String strHost, String strPort, String strUser, String strPassword, String strDatabase){
        
        this.strDriver = strDriver;
        this.strPrefix = strPrefix;
        this.strHost = strHost;
        this.strPort = strPort;
        this.strUser = strUser;
        this.strPassword = strPassword;
        this.strDatabase = strDatabase;
     
    }
    
    public CDatabaseConnectionConfig(){
        
    }
    
    public boolean loadConfig( String strConfigPath){
        
        boolean bresult = false;
        
        try {
            
            File configFilePath =  new File(strConfigPath);

            if ( configFilePath.exists() ){
                
                Properties configsData = new Properties();
                
                FileInputStream inpuStream = new FileInputStream( configFilePath );
                
                configsData.loadFromXML( inpuStream ); //aqui leemos del archivo
                
                this.strDriver = configsData.getProperty( "driver" );
                this.strPrefix = configsData.getProperty( "prefix" );
                this.strHost = configsData.getProperty( "host" );
                this.strPort = configsData.getProperty( "port" );
                this.strUser = configsData.getProperty( "user" );
                this.strPassword = configsData.getProperty( "password" );
                this.strDatabase = configsData.getProperty( "database" );
                
                inpuStream.close(); // cerramos el stream
                
                bresult = true;
            }
            else {
                
                System.out.println( "ERROR THE FILE NO FOUND" );
                
            }
            
            
        }
        catch ( Exception ex ){
            
            ex.printStackTrace();
        }
        
        return bresult;
    }

    
    public String getDriver() {
        
        return strDriver;
    }

    
    public void setDriver( String strDriver ) {
        
        this.strDriver = strDriver;
    }

    
    public String getPrefix() {
        
        return strPrefix;
    }

    
    public void setPrefix( String strPrefix ) {
        
        this.strPrefix = strPrefix;
    }

    
    public String getHost() {
        
        return strHost;
    }

    
    public void setHost( String strHost ) {
        
        this.strHost = strHost;
    }

    
    public String getPort() {
        
        return strPort;
    }

    
    public void setPort( String strPort ) {
        
        this.strPort = strPort;
    }

    
    public String getUser() {
        
        return strUser;
    }

    
    public void setUser( String strUser ) {
        
        this.strUser = strUser;
    }

    
    public String getPassword() {
        
        return strPassword;
    }

    
    public void setPassword( String strPassword ) {
        
        this.strPassword = strPassword;
    }

    
    public String getDatabase() {
        
        return strDatabase;
    }

    
    public void setDatabase( String strDatabase ) {
        
        this.strDatabase = strDatabase;
    }
    
}
