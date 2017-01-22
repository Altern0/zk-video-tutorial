package org.test.zk.database;

import java.io.Serializable;

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
