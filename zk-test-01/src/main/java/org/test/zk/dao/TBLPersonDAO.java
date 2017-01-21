package org.test.zk.dao;

import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

import org.test.zk.database.CDatabaseConnection;
import org.test.zk.datamodel.TBLPerson;

import java.util.List;

public class TBLPersonDAO {
    
    public static TBLPerson loadData ( final CDatabaseConnection dataBaseConnection, final String strId ) {
        
        TBLPerson result = null;
        
        try {
            if ( dataBaseConnection != null && dataBaseConnection.getDBConnection() != null){
                
                Statement statement = dataBaseConnection.getDBConnection().createStatement();
                
                ResultSet resultSet = statement.executeQuery( "Select * from tblperson where id = '" + strId + "'");
                
                if (resultSet.next() ){
                    
                    result = new TBLPerson();
                    
                    result.setID( resultSet.getString( "Id" ) );
                    result.setFirtsName( resultSet.getString( "FirtsName" ) );
                    result.setLastName( resultSet.getString( "LastName" ) );
                    result.setGender( resultSet.getInt( "Gender" ) );
                    result.setBirthDate( resultSet.getDate( "Birthdate" ).toLocalDate() );
                    result.setComment( resultSet.getString( "Comment" ) );
                    // estos metodos son de la clase CAuditableDatamodel 
                    result.setCreatedBy( resultSet.getString( "CreatedBy" ));
                    result.setCreatedAtDate( resultSet.getDate( "CreateDate" ).toLocalDate() );    
                    result.setCreatedAtTime( resultSet.getTime( "CreateTime" ).toLocalTime() );
                    // los UpdatedBy pueden ser null pero no es relevante porque no accedo a nimgunmetodo de la clase string
                    result.setUpdatedBy( resultSet.getString( "UpdatedBy" ) );
                    // cndiciono porque accedo al metodo toLocal???? y no pueden ser Null 
                    result.setUpdatedAtDate( resultSet.getDate( "UpdatedDate" ).toLocalDate() != null ? resultSet.getDate( "UpdatedDate" ).toLocalDate() : null );    
                    result.setUpdatedAtTime( resultSet.getTime( "UpdatedTime" ).toLocalTime() != null ? resultSet.getTime( "UpdatedTime" ).toLocalTime()  : null);
                }
                
                //cerramos la consulta para liberar los recursos, pro dejamos la conexion abierta para utilizarla en otras operaciones 
                statement.close();
                resultSet.close();
                
            }
            
        }
        catch ( Exception ex ){
           
            ex.printStackTrace();
            
        }
        
        return result;
        
    }
    
    public static boolean deletaData ( final CDatabaseConnection dataBaseConnection, final String strId ) {
        
        boolean bresult = false;
        try{
            
            Statement statement = dataBaseConnection.getDBConnection().createStatement();
            
            final String SQLstr = "Delete tblperson where id = '" + strId + "'"; 
            
            statement.executeUpdate(SQLstr);
            
            bresult = true;
        }
        catch ( Exception ex ){
            
            ex.printStackTrace();
            
        }
        
        return bresult;
        
    }
    
    public static boolean insertData ( final CDatabaseConnection dataBaseConnection, final TBLPerson tblPerson ) {
        
        boolean bresult = false;
        
        try{
            
            Statement statement = dataBaseConnection.getDBConnection().createStatement();
            
            final String SQLstr = "Insert Into tblperson(Id,FirtsName,LastName,Gender,Birthdate,"
                    + "Comment,CreatedBy,CreatedAtDate,CreatedAtTime,UpdatedBy,UpdatedAtDate,UpdatedAtTime)"
                    + " Values('" + tblPerson.getID() + "','" + tblPerson.getFirtsName() 
                    + "','" + tblPerson.getLastName()  + "','" + tblPerson.getGender() 
                    + "','" + tblPerson.getBirthDate().toString() + "','" + tblPerson.getComment() 
                    + "','" + tblPerson.getCreatedBy() + "','" + LocalDate.now().toString()
                    + "','" + LocalTime.now().toString() + "', null, null, null)"; 
            
            statement.executeUpdate(SQLstr);
            
            dataBaseConnection.getDBConnection().commit();
            
            bresult = true;
        }
        catch ( Exception ex ){
            
            ex.printStackTrace();
            
        }
        
        
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
                    
                    tblperson.setID( resultSet.getString( "Id" ) );
                    tblperson.setFirtsName( resultSet.getString( "FirtsName" ) );
                    tblperson.setLastName( resultSet.getString( "LastName" ) );
                    tblperson.setGender( resultSet.getInt( "Gender" ) );
                    tblperson.setBirthDate( resultSet.getDate( "Birthdate" ).toLocalDate() );
                    tblperson.setComment( resultSet.getString( "Comment" ) );
                    // estos metodos son de la clase CAuditableDatamodel 
                    tblperson.setCreatedBy( resultSet.getString( "CreatedBy" ));
                    tblperson.setCreatedAtDate( resultSet.getDate( "CreatedAtDate" ).toLocalDate() );    
                    tblperson.setCreatedAtTime( resultSet.getTime( "CreatedAtTime" ).toLocalTime() );
                    // los UpdatedBy pueden ser null pero no es relevante porque no accedo a nimgunmetodo de la clase string
                    tblperson.setUpdatedBy( resultSet.getString( "UpdatedBy" ) );
                    // cndiciono porque accedo al metodo toLocal???? y no pueden ser Null 
                    tblperson.setUpdatedAtDate( resultSet.getDate( "UpdatedAtDate" ).toLocalDate() != null ? resultSet.getDate( "UpdatedDate" ).toLocalDate() : null );    
                    tblperson.setUpdatedAtTime( resultSet.getTime( "UpdatedAtTime" ).toLocalTime() != null ? resultSet.getTime( "UpdatedTime" ).toLocalTime()  : null);
                 
                    result.add( tblperson ); // se carga a la lista
                }
                
                //cerramos la consulta para liberar los recursos, pro dejamos la conexion abierta para utilizarla en otras operaciones 
                statement.close();
                resultSet.close();
                
            }
            
        }
        catch ( Exception ex ){
           
            ex.printStackTrace();
            
        }
        
                
        return result;
        
    }
    
    
}
