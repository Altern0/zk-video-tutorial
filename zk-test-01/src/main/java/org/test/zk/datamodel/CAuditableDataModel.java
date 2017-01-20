package org.test.zk.datamodel;

import java.time.LocalDate;
import java.time.LocalTime;

public class CAuditableDataModel implements IAuditableDataModel {
    
    private static final long serialVersionUID = 8828055737133143396L;

    protected String strCretedBy = null;
    protected LocalDate CreatedAtDate = null;
    protected LocalTime CreatedAtTime = null;
    protected String strUpdatedBy = null;
    protected LocalDate UpdatedAtDate = null;
    protected LocalTime UpdatedAtTime = null;
  
    @Override
    public String getCreatedBy() {
        
        return strCretedBy;
    }

    @Override
    public void setCreatedBy( String strCreatedBy ) {
        
        this.strCretedBy = strCreatedBy;
        
    }

    @Override
    public LocalDate getCreatedAtDate() {
        
        return CreatedAtDate;
    }

    @Override
    public void setCreatedAtDate( LocalDate createdAtDate ) {
        
        this.CreatedAtDate = createdAtDate;
    }

    @Override
    public LocalTime getCreatedAtTime() {
        
        return CreatedAtTime;
    }

    @Override
    public void setCreatedAtTime( LocalTime createdAtTime ) {
        
        this.CreatedAtTime = createdAtTime;
        
    }

    @Override
    public String getUpdatedBy() {
        
        return strUpdatedBy;
    }

    @Override
    public void setUpdatedBy( String strUpdatedBy ) {
        
        this.strUpdatedBy =  strUpdatedBy;
        
    }

    @Override
    public LocalDate getUpdatedAtDate() {
        
        return UpdatedAtDate;
    }

    @Override
    public void setUpdatedAtDate( LocalDate updatedAtDate ) {
        
        this.UpdatedAtDate = updatedAtDate;
        
    }

    @Override
    public LocalTime getUpdatedAtTime() {
        
        return UpdatedAtTime;
    }

    @Override
    public void setUpdatedAtTime( LocalTime updatedAtTime ) {
        
        this.UpdatedAtTime = updatedAtTime;
        
    }
    
}
