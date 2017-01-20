package org.test.zk.dao;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import org.test.zk.database.CDatabaseConnection;
import org.test.zk.datamodel.TBLPerson;

import java.util.List;

public class TBLPersonDAO {
    
    public static TBLPerson loadData ( final CDatabaseConnection dataBaseConnection, final String strId ) {
        
        TBLPerson result = null;
        
        return result;
        
    }
    
    public static boolean deletaData ( final CDatabaseConnection dataBaseConnection, final String strId ) {
        
        boolean bresult = false;
        
        return bresult;
        
    }
    
    public static boolean insertData ( final CDatabaseConnection dataBaseConnection, final TBLPerson tblPerson ) {
        
        boolean bresult = false;
        
        return bresult;
        
    }
    
    public static boolean updateData ( final CDatabaseConnection dataBaseConnection, final TBLPerson tblPerson) {
        
        boolean bresult = false;
        
        return bresult;
        
    }
    
    public static List<TBLPerson> searchtData( final CDatabaseConnection dataBaseConnection) {
        
        List<TBLPerson> result = new ArrayList<TBLPerson>();
        
        try {
            if ( dataBaseConnection != null && dataBaseConnection.getDBConnection() != null){
                
                Statement statement = dataBaseConnection.getDBConnection().createStatement();
                
                ResultSet resultSet = statement.executeQuery( "Select * from tblperson" );  
                
                while (resultSet.next() ){
                    
                    TBLPerson  tblperson = new TBLPerson();
                    
                    tblperson.setID( resultSet.getString( "ID" ) );
                    tblperson.setFirtsName( resultSet.getString( "FirtsName" ) );
                    tblperson.setLastName( resultSet.getString( "LastName" ) );
                    tblperson.setGender( resultSet.getInt( "Gender" ) );
                    tblperson.setBirthDate( resultSet.getDate( "Birthdate" ).toLocalDate() );
                    tblperson.setComment( resultSet.getString( "Comment" ) );
                    // estos metodos son de la clase CAuditableDatamodel 
                    tblperson.setCreatedBy( resultSet.getString( "CreatedBy" ));
                    tblperson.setCreatedAtDate( resultSet.getDate( "CreateDate" ).toLocalDate() );    
                    tblperson.setCreatedAtTime( resultSet.getTime( "CreateTime" ).toLocalTime() );
                    // los UpdatedBy pueden ser null pero no es relevante porque no accedo a nimgunmetodo de la clase string
                    tblperson.setUpdatedBy( resultSet.getString( "UpdatedBy" ) );
                    // cndiciono porque accedo al metodo toLocal???? y no pueden ser Null 
                    tblperson.setUpdatedAtDate( resultSet.getDate( "UpdatedDate" ).toLocalDate() != null ? resultSet.getDate( "UpdatedDate" ).toLocalDate() : null );    
                    tblperson.setUpdatedAtTime( resultSet.getTime( "UpdatedTime" ).toLocalTime() != null ? resultSet.getTime( "UpdatedTime" ).toLocalTime()  : null);
                    
                }
                
            }
            
            
            
            
        }
        catch ( Exception ex ){
           
            ex.printStackTrace();
            
        }
        
                
        return result;
        
    }
    
    
}
