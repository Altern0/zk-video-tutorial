package org.test.zk.database.dao;

import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import org.test.zk.database.CDatabaseConnection;
import org.test.zk.database.datamodel.TBLOperator;

import commonlibs.commonclasses.CLanguage;
import commonlibs.extendedlogger.CExtendedLogger;

public class OperatorDAO {
    

    public static TBLOperator loadData ( final CDatabaseConnection dataBaseConnection, final String strId, CExtendedLogger localLogger, CLanguage localLanguage ) {
   
        TBLOperator result = null; 
        
        try {
            if ( dataBaseConnection != null && dataBaseConnection.getDBConnection() != null){
                
                Statement statement = dataBaseConnection.getDBConnection().createStatement();
                
                ResultSet resultSet = statement.executeQuery( "Select * from tbloperator where id = '" + strId + "'");
                
                if (resultSet.next() ){
                    
                    result = new TBLOperator();
                    
                    result.setId( resultSet.getString( "Id" ) );
                    result.setName( resultSet.getString( "Name" ) );
                    result.setPassword( resultSet.getString( "Password" ) );
                    result.setComment( resultSet.getString( "Comment" ) );
                    // estos metodos son de la clase CAuditableDatamodel 
                    result.setCreatedBy( resultSet.getString( "CreatedBy" ));
                    result.setCreatedAtDate( resultSet.getDate( "CreateAtDate" ).toLocalDate() );    
                    result.setCreatedAtTime( resultSet.getTime( "CreateAtTime" ).toLocalTime() );
                    // los UpdatedBy pueden ser null pero no es relevante porque no accedo a nimgunmetodo de la clase string
                    result.setUpdatedBy( resultSet.getString( "UpdatedBy" ) );
                    // cndiciono porque accedo al metodo toLocal???? y no pueden ser Null 
                    result.setUpdatedAtDate( resultSet.getDate( "UpdatedAtDate" ).toLocalDate() != null ? resultSet.getDate( "UpdatedAtDate" ).toLocalDate() : null );    
                    result.setUpdatedAtTime( resultSet.getTime( "UpdatedAtTime" ).toLocalTime() != null ? resultSet.getTime( "UpdatedAtTime" ).toLocalTime()  : null );
                    
                    result.setDisiabledBy( resultSet.getString( "DisiabledBy") );
                    result.setDisiabledAtDate ( resultSet.getDate( "DisiabledAtDate" ).toLocalDate() != null ? resultSet.getDate( "DisiabledAtDate" ).toLocalDate() : null );
                    result.setDisiabledAtTime ( resultSet.getTime( "DisiabledAtTime" ).toLocalTime() != null ? resultSet.getTime( "DisiabledAtTime" ).toLocalTime()  : null );   
                    result.setLastLoginAtDate ( resultSet.getDate( "LastLoginAtDate" ).toLocalDate() != null ? resultSet.getDate( "LastLoginAtDate" ).toLocalDate() : null);
                    result.setLastLoginAtTime ( resultSet.getTime( "LastLoginAtTime" ).toLocalTime() != null ? resultSet.getTime( "LastLoginAtTime" ).toLocalTime()  : null );
                 
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
            
            final String SQLstr = "Delete from tbloperator where id = '" + strId + "'"; 
            
            statement.executeUpdate(SQLstr);
            
            dataBaseConnection.getDBConnection().commit();
            
            statement.close();
            
            bresult = true;
        }
        catch ( Exception ex ){
            
            if ( dataBaseConnection != null && dataBaseConnection.getDBConnection() != null) 
                try {
                  
                    dataBaseConnection.getDBConnection().rollback(); // en caso de error vuelve atras en la DB
                  
                }
                catch ( Exception ex1 ){
                
                    if ( localLogger != null ) localLogger.logException( "-1021" , ex.getMessage(), ex );
                }
              
            if ( localLogger != null )  localLogger.logException( "-1022" , ex.getMessage(), ex );
              
        }
        
        return bresult;
        
    }
    
    public static boolean insertData ( final CDatabaseConnection dataBaseConnection, final TBLOperator tblOperator, CExtendedLogger localLogger, CLanguage localLanguage ) {
        
   boolean bresult = false;
        
        try{
            
            Statement statement = dataBaseConnection.getDBConnection().createStatement();
            
            final String SQLstr = "Insert Into tbloperator(Id, Name, Password, Comment, "
                    + "CreatedBy, CreatedAtDate, CreatedAtTime, UpdatedBy, UpdatedAtDate, UpdatedAtTime, "
                    + "DisiabledBy, DisiabledAtDate, DisiabledAtTime, LastLoginAtDate, LastLoginAtTime ) "
                    + "Values('" + tblOperator.getId() + "', '" + tblOperator.getName() 
                    + "', '" + tblOperator.getPassword()  + "', '" + tblOperator.getComment() 
                    + "', '" + tblOperator.getCreatedBy() + "', '" + LocalDate.now().toString()
                    + "', '" + LocalTime.now().toString() + "', null, null, null, null, null, null,null, null)"; 
            
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
            
            if ( localLogger != null )  localLogger.logException( "-1022" , ex.getMessage(), ex );
            
        }
        
        return bresult;
        
    }
 
    
    public static boolean updateLastLoginData ( final CDatabaseConnection dataBaseConnection, final String strId, CExtendedLogger localLogger, CLanguage localLanguage ) {
        
        boolean bresult = false;
        
        try{
            
            Statement statement = dataBaseConnection.getDBConnection().createStatement();
            
            // Esto se puede hacer con un ORM como hybermate o mybasty
            final String SQLstr = "Update tbloperator set LastLoginAtDate = '" + LocalDate.now().toString() 
                    + "', LastLoginAtTime = '" + LocalTime.now().toString() + "' where id = '" + strId + "'"; 
            
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
            
            if ( localLogger != null )  localLogger.logException( "-1022" , ex.getMessage(), ex );
            
        }
        
        return bresult;
    
    }
    
    public static List<TBLOperator> searchData( final CDatabaseConnection dataBaseConnection, CExtendedLogger localLogger, CLanguage localLanguage ) {
        
        List<TBLOperator> result = new ArrayList<TBLOperator>();

        return result;
    }
    
    public static boolean updateData ( final CDatabaseConnection dataBaseConnection, final TBLOperator tblOperator, CExtendedLogger localLogger, CLanguage localLanguage ) {
        
        boolean bresult = false;
        
        try{
            
            Statement statement = dataBaseConnection.getDBConnection().createStatement();
            // Esto se puede hacer con un ORM como hybermate o mybasty
            final String SQLstr = "Update tbloperator set Id ='" + tblOperator.getId() + "', Name = '" + tblOperator.getName() 
                    + "', Password = '" + tblOperator.getPassword() + "', Comment = '" + tblOperator.getComment() 
                    + "', UpdatedBy = '" + tblOperator.getUpdatedBy() + "', UpdatedAtDate = '" + LocalDate.now().toString() 
                    + "', UpdatedAtTime = '" + tblOperator.getUpdatedAtTime().toString() 
                    + "', DisiabledBy = '" + tblOperator.getDisiabledBy() 
                    + "', DisiabledAtDate = '" + tblOperator.getDisiabledBy() != null ? "'" + tblOperator.getDisiabledAtDate().toString() + "'" : "'null'" 
                    + "', DisiabledAtTime = '" + tblOperator.getDisiabledBy() != null ? "'" + tblOperator.getDisiabledAtTime().toString() + "'" : "'null'"
                    + "' where id = '" + tblOperator.getId() + "'"; 
            
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
            
            if ( localLogger != null )  localLogger.logException( "-1022" , ex.getMessage(), ex );
            
        }
        
        return bresult;
    
    }
        
    public static TBLOperator checkValid( final CDatabaseConnection dataBaseConnection, final String strName, String strPassword, CExtendedLogger localLogger, CLanguage localLanguage ) {
        
        TBLOperator result = null;
        
        try {
            if ( dataBaseConnection != null && dataBaseConnection.getDBConnection() != null){
                
                Statement statement = dataBaseConnection.getDBConnection().createStatement();
                
                ResultSet resultSet = statement.executeQuery( "Select * from tbloperator where Name = '" + strName + "' and Password = '" + strPassword + "'");
                
                if (resultSet.next() ){
                    
                    result = new TBLOperator();
                    
                    result.setId( resultSet.getString( "Id" ) );
                    result.setName( resultSet.getString( "Name" ) );
                    result.setPassword( resultSet.getString( "Password" ) );
                    result.setComment( resultSet.getString( "Comment" ) );
                    // estos metodos son de la clase CAuditableDatamodel 
                    result.setCreatedBy( resultSet.getString( "CreatedBy" ));
                    result.setCreatedAtDate( resultSet.getDate( "CreateAtDate" ).toLocalDate() );    
                    result.setCreatedAtTime( resultSet.getTime( "CreateAtTime" ).toLocalTime() );
                    // los UpdatedBy pueden ser null pero no es relevante porque no accedo a nimgunmetodo de la clase string
                    result.setUpdatedBy( resultSet.getString( "UpdatedBy" ) );
                    // cndiciono porque accedo al metodo toLocal???? y no pueden ser Null 
                    result.setUpdatedAtDate( resultSet.getDate( "UpdatedAtDate" ).toLocalDate() != null ? resultSet.getDate( "UpdatedAtDate" ).toLocalDate() : null );    
                    result.setUpdatedAtTime( resultSet.getTime( "UpdatedAtTime" ).toLocalTime() != null ? resultSet.getTime( "UpdatedAtTime" ).toLocalTime()  : null );
                    
                    result.setDisiabledBy( resultSet.getString( "DisiabledBy") );
                    result.setDisiabledAtDate ( resultSet.getDate( "DisiabledAtDate" ).toLocalDate() != null ? resultSet.getDate( "DisiabledAtDate" ).toLocalDate() : null );
                    result.setDisiabledAtTime ( resultSet.getTime( "DisiabledAtTime" ).toLocalTime() != null ? resultSet.getTime( "DisiabledAtTime" ).toLocalTime()  : null );   
                    result.setLastLoginAtDate ( resultSet.getDate( "LastLoginAtDate" ).toLocalDate() != null ? resultSet.getDate( "LastLoginAtDate" ).toLocalDate() : null);
                    result.setLastLoginAtTime ( resultSet.getTime( "LastLoginAtTime" ).toLocalTime() != null ? resultSet.getTime( "LastLoginAtTime" ).toLocalTime()  : null );
                 
                 
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
}
