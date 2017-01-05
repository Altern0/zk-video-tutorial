package org.test.zk.dao;

import java.io.Serializable;

public class CPerson implements Serializable  {
    
    private static final long serialVersionUID = -7068448936191065864L;

    protected String StrID;
    protected String StrFirtsName;
    protected String StrLastName;

    public CPerson ( String StrID, String StrFirtsName, String StrLastName) {
        
        this.StrID = StrID;
        this.StrFirtsName = StrFirtsName;
        this.StrLastName = StrLastName;
        
    }
    
    
    public String getID() {
        
        return StrID;
    }
    
    public void setID( String strID ) {
        
        StrID = strID;
    }
    
    public String getLastName() {
        
        return StrLastName;
    }
    
    public void setLastName( String strLastName ) {
        
        StrLastName = strLastName;
    }
    
    public String getFirtsName() {
        
        return StrFirtsName;
    }
    
    public void setFirtsName( String strFirtsName ) {
        
        StrFirtsName = strFirtsName;
    }
        
}
