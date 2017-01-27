package org.test.zk.database.datamodel;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;

public class TBLOperator extends CAuditableDataModel implements Serializable  {

    private static final long serialVersionUID = -460145854236713435L;
 
    protected String strId;
    protected String strName;
    protected String strPassword;
    protected String strComment;  
    protected String strDisiabledBy;
    protected LocalDate disiabledAtDate;
    protected LocalTime disiabledAtTime;
    protected LocalDate lastLoginAtDate;
    protected LocalTime lastLoginAtTime;
    
    public TBLOperator ( String strId, String strName, String strPassword, String strComment, String strDisiabledBy, LocalDate disiabledAtDate, LocalTime disiabledAtTime, LocalDate lastLoginAtDate, LocalTime lastLoginAtTime) {
       
        this.strId= strId;
        this.strName = strName;
        this.strPassword = strPassword;
        this.strComment = strComment;  
        this.strDisiabledBy = strDisiabledBy;
        this.disiabledAtDate = disiabledAtDate;
        this.disiabledAtTime = disiabledAtTime;   
        this.lastLoginAtDate = lastLoginAtDate;
        this.lastLoginAtTime = lastLoginAtTime;
        
    }
     
    public TBLOperator() {
    
    }

    
    public String getId() {
        
        return strId;
    }

    
    public void setId( String strId ) {
        
        this.strId = strId;
    }

    
    public String getName() {
        
        return strName;
    }

    
    public void setName( String strName ) {
        
        this.strName = strName;
    }

    
    public String getPassword() {
        
        return strPassword;
    }

    
    public void setPassword( String strPassword ) {
        
        this.strPassword = strPassword;
    }

    
    public String getComment() {
        
        return strComment;
    }

    
    public void setComment( String strComment ) {
        
        this.strComment = strComment;
    }

    
    public String getDisiabledBy() {
        
        return strDisiabledBy;
    }

    
    public void setDisiabledBy( String strDisiabledBy ) {
        
        this.strDisiabledBy = strDisiabledBy;
    }

    
    public LocalDate getDisiabledAtDate() {
        
        return disiabledAtDate;
    }

    
    public void setDisiabledAtDate( LocalDate disiabledAtDate ) {
        
        this.disiabledAtDate = disiabledAtDate;
    }

    
    public LocalTime getDisiabledAtTime() {
        
        return disiabledAtTime;
    }

    
    public void setDisiabledAtTime( LocalTime disiabledAtTime ) {
        
        this.disiabledAtTime = disiabledAtTime;
    }

    
    public LocalDate getLastLoginAtDate() {
        
        return lastLoginAtDate;
    }

    
    public void setLastLoginAtDate( LocalDate lastLoginAtDate ) {
        
        this.lastLoginAtDate = lastLoginAtDate;
    }

    
    public LocalTime getLastLoginAtTime() {
        
        return lastLoginAtTime;
    }

    
    public void setLastLoginAtTime( LocalTime lastLoginAtTime ) {
        
        this.lastLoginAtTime = lastLoginAtTime;
    }
    
}
