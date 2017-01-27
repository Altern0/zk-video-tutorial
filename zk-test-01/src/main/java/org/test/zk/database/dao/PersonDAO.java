package org.test.zk.database.dao;

import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

import org.test.zk.database.CDatabaseConnection;
import org.test.zk.database.datamodel.TBLPerson;

import commonlibs.commonclasses.CLanguage;
import commonlibs.extendedlogger.CExtendedLogger;

import java.util.List;

public class PersonDAO {
    
    public static TBLPerson loadData ( final CDatabaseConnection dataBaseConnection, final String strId, CExtendedLogger localLogger, CLanguage localLanguage ) {
        
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
                    result.setCreatedAtDate( resultSet.getDate( "CreateAtDate" ).toLocalDate() );    
                    result.setCreatedAtTime( resultSet.getTime( "CreateAtTime" ).toLocalTime() );
                    // los UpdatedBy pueden ser null pero no es relevante porque no accedo a nimgunmetodo de la clase string
                    result.setUpdatedBy( resultSet.getString( "UpdatedBy" ) );
                    // cndiciono porque accedo al metodo toLocal???? y no pueden ser Null 
                    result.setUpdatedAtDate( resultSet.getDate( "UpdatedAtDate" ).toLocalDate() != null ? resultSet.getDate( "UpdatedAtDate" ).toLocalDate() : null );    
                    result.setUpdatedAtTime( resultSet.getTime( "UpdatedAtTime" ).toLocalTime() != null ? resultSet.getTime( "UpdatedAtTime" ).toLocalTime()  : null);
                }
                
                //cerramos la consulta para liberar los recursos, pro dejamos la conexion abierta para utilizarla en otras operaciones 
                statement.close();
                resultSet.close();
                
            }
            
        }
        catch ( Exception ex ){
           
            if ( localLogger != null ) localLogger.logException( "-1021" , ex.getMessage(), ex );
                
        }
        
        return result;
        
    }
    
    public static boolean deletaData ( final CDatabaseConnection dataBaseConnection, final String strId, CExtendedLogger localLogger, CLanguage localLanguage ) {
        
        boolean bresult = false;
        try{
            
            Statement statement = dataBaseConnection.getDBConnection().createStatement();
            
            final String SQLstr = "Delete from tblperson where id = '" + strId + "'"; 
            
            statement.executeUpdate(SQLstr);
            
            dataBaseConnection.getDBConnection().commit();
            
            statement.close();
            
            bresult = true;
        }
        catch ( Exception ex ){
            
            if ( dataBaseConnection != null && dataBaseConnection.getDBConnection() != null) 
                try {
                  
                    dataBaseConnection.getDBConnection().rollback(); // en caso de error vuelve atras
                  
                }
                catch ( Exception ex1 ){
                
                    if ( localLogger != null ) localLogger.logException( "-1021" , ex.getMessage(), ex );
                }
              
            if ( localLogger != null )  localLogger.logException( "-1021" , ex.getMessage(), ex );
              
        }
        
        return bresult;
        
    }
    
    public static boolean insertData ( final CDatabaseConnection dataBaseConnection, final TBLPerson tblPerson, CExtendedLogger localLogger, CLanguage localLanguage ) {
        
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
            
            statement.close();
            
            bresult = true;
        }
        catch ( Exception ex ){
            
            if ( dataBaseConnection != null && dataBaseConnection.getDBConnection() != null)
              try {
                
                  dataBaseConnection.getDBConnection().rollback(); // en caso de error vuelve atras
                
              }
              catch ( Exception ex1 ){
              
                  if ( localLogger != null )  localLogger.logException( "-1021" , ex1.getMessage(), ex1 );
              }
            
            if ( localLogger != null )  localLogger.logException( "-1021" , ex.getMessage(), ex );
            
        }
        
        return bresult;
        
    }
    
    public static boolean updateData ( final CDatabaseConnection dataBaseConnection, final TBLPerson tblPerson, CExtendedLogger localLogger, CLanguage localLanguage ) {
        
        boolean bresult = false;
        
        try{
            
            Statement statement = dataBaseConnection.getDBConnection().createStatement();
            // Esto se puede hacer con un ORM como hybermate o mybasty
            final String SQLstr = "Update tblperson set Id ='" + tblPerson.getID() + "', FirtsName = '" + tblPerson.getFirtsName() 
                    + "', LastName = '" + tblPerson.getLastName()  + "', Gender = '" + tblPerson.getGender() 
                    + "', BirthDate = '" + tblPerson.getBirthDate().toString() + "', Comment = '" + tblPerson.getComment() 
                    + "', UpdatedBy = 'Al', " + " UpdatedAtDate = '" + LocalDate.now().toString() 
                    + "', UpdatedAtTime = '" + LocalTime.now().toString() +"' where id = '" + tblPerson.getID() + "'"; 
            
            statement.executeUpdate(SQLstr);
            
            dataBaseConnection.getDBConnection().commit();
            
            statement.close();
            
            bresult = true;
        }
        catch ( Exception ex ){
            
            if ( dataBaseConnection != null && dataBaseConnection.getDBConnection() != null)
              try {
                
                  dataBaseConnection.getDBConnection().rollback(); // en caso de error vuelve atras
                
              }
              catch ( Exception ex1 ){
              
                  if ( localLogger != null )  localLogger.logException( "-1021" , ex1.getMessage(), ex1 );
              }
            
            if ( localLogger != null )  localLogger.logException( "-1021" , ex.getMessage(), ex );
            
        }
        
        return bresult;
        
    }
    
    public static List<TBLPerson> searchData( final CDatabaseConnection dataBaseConnection, CExtendedLogger localLogger, CLanguage localLanguage ) {
        
        List<TBLPerson> result = new ArrayList<TBLPerson>();
        
        try {
            if ( dataBaseConnection != null && dataBaseConnection.getDBConnection() != null){
                
                Statement statement = dataBaseConnection.getDBConnection().createStatement();
                
                ResultSet resultSet = statement.executeQuery( "Select * from tblperson" );  
                
                while (resultSet.next() ){
                    
                    TBLPerson  tblPerson = new TBLPerson();
                    
                    tblPerson.setID( resultSet.getString( "Id" ) );
                    tblPerson.setFirtsName( resultSet.getString( "FirtsName" ) );
                    tblPerson.setLastName( resultSet.getString( "LastName" ) );
                    tblPerson.setGender( resultSet.getInt( "Gender" ) );
                    tblPerson.setBirthDate( resultSet.getDate( "Birthdate" ).toLocalDate() );
                    tblPerson.setComment( resultSet.getString( "Comment" ) );
                    // estos metodos son de la clase CAuditableDatamodel 
                    tblPerson.setCreatedBy( resultSet.getString( "CreatedBy" ));
                    tblPerson.setCreatedAtDate( resultSet.getDate( "CreatedAtDate" ).toLocalDate() );    
                    tblPerson.setCreatedAtTime( resultSet.getTime( "CreatedAtTime" ).toLocalTime() );
                    // los UpdatedBy pueden ser null pero no es relevante porque no accedo a nimgunmetodo de la clase string
                    tblPerson.setUpdatedBy( resultSet.getString( "UpdatedBy" ) );
                    // cndiciono porque accedo al metodo toLocal???? y no pueden ser Null 
                    tblPerson.setUpdatedAtDate( resultSet.getDate( "UpdatedAtDate" ) != null ? resultSet.getDate( "UpdatedAtDate" ).toLocalDate() : null );    
                    tblPerson.setUpdatedAtTime( resultSet.getTime( "UpdatedAtTime" ) != null ? resultSet.getTime( "UpdatedAtTime" ).toLocalTime()  : null);
                 
                    result.add( tblPerson ); // se carga a la lista
                }
                
                //cerramos la consulta para liberar los recursos, pro dejamos la conexion abierta para utilizarla en otras operaciones 
                statement.close();
                resultSet.close();
                
            }
            
        }
        catch ( Exception ex ){
           
            if ( localLogger != null )  localLogger.logException( "-1021" , ex.getMessage(), ex );
            
        }
                
        return result;
        
    }
    
    
}
