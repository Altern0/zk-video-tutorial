package org.test.zk.datamodel;

import java.io.Serializable;
import java.time.LocalDate;

public class CPerson implements Serializable  {
    
    private static final long serialVersionUID = -7068448936191065864L;

    protected String strID;
    protected String strFirtsName;
    protected String strLastName;
    protected int intGender; // 0 Female 1 Male
    protected LocalDate birthDate;
    protected String strComment;  
    
    public CPerson ( String strID, String strFirtsName, String strLastName, int intGender, LocalDate birthDate, String strComment) {
        
        this.strID = strID;
        this.strFirtsName = strFirtsName;
        this.strLastName = strLastName;
        this.intGender = intGender;
        this.birthDate = birthDate;
        this.strComment = strComment;    
        
    }
    
    public CPerson () {
        
    }
    
    
    //Geter an Seter
    public String getID() {
        
        return strID;
    }
    
    public void setID( String strID ) {
        
        this.strID = strID;
    }
    
    public String getLastName() {
        
        return strLastName;
    }
    
    public void setLastName( String strLastName ) {
        
        this.strLastName = strLastName;
    }
    
    public String getFirtsName() {
        
        return strFirtsName;
    }
    
    public void setFirtsName( String strFirtsName ) {
        
        this.strFirtsName = strFirtsName;
    }
    
    public int getGender() {
        
        return intGender;
    }
    
    public void setGender( int intGender ) {
        
        this.intGender = intGender;
    }
    
    public LocalDate getBirthDate() {
        
        return birthDate;
    }
    
    public void setBirthDate( LocalDate birthDate ) {
        
        this.birthDate = birthDate;
    }
    
    public String getComment() {
        
        return strComment;
    }
    
    public void setComment( String strComment ) {
        
        this.strComment = strComment;
    }
        
}
