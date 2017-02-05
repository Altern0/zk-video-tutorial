package org.test.zk.controllers.login;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import org.test.zk.contants.SystemConstants;
import org.test.zk.database.CDatabaseConnection;
import org.test.zk.database.CDatabaseConnectionConfig;
import org.test.zk.database.dao.OperatorDAO;
import org.test.zk.database.datamodel.TBLOperator;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Label;
import org.zkoss.zul.Textbox;

import commonlibs.commonclasses.CLanguage;
import commonlibs.commonclasses.ConstantsCommonClasses;
import commonlibs.extendedlogger.CExtendedLogger;
import commonlibs.utils.Utilities;
import commonlibs.utils.ZKUtilities;


public class CLoginController extends SelectorComposer<Component> {
   
   private static final long serialVersionUID = 2607061613647188753L;
   
   protected CExtendedLogger controllerLogger = null;
    
   protected CLanguage controllerLanguage = null;
   
   protected CDatabaseConnection databaseConnection = null;
     
   @Wire
   Textbox textboxOperator;
   
   @Wire
   Textbox textboxPassword;
   
   @Wire
   Label labelMessage;
   
   @Override
   public void doAfterCompose( Component comp ) {
       
       try {
           
           super.doAfterCompose( comp );
           
           labelMessage.setValue( "" ); //inicializo el mensaje para que elimine el espacio que ocupa
           
           // obtenemos el logger del objeto webApp y guardamos una referencia en la variable de clase controllerLogger 
           controllerLogger = (CExtendedLogger) Sessions.getCurrent().getWebApp().getAttribute(  SystemConstants._Webapp_Logger_App_Attribute_Key );
        
           
       }
       catch ( Exception ex ) {
           
           if ( controllerLogger != null ) controllerLogger.logException( "-1021" , ex.getMessage(), ex );
        
       }
   }
   
   
   // el @listen como se observa puede manejar dos eventos y
   // luego los piede diferenciar con el event.getTarget().equals( Nombredelobjeto );
   @Listen( "onChanging=#textboxOperator; onChanging=#textboxPassword ")
   public void onChanging ( Event event ){
       
       //caundo se edite cualquiera de los dos textbox se borra el mensase de error
       labelMessage.setValue( "" );
       
   }
   
   
   @Listen( "onClick=#buttonLogin; onOK=#windowLogin")
   public void onClickbuttonLogin ( Event event ){
       
       Session currentSession = Sessions.getCurrent();
       
       try { 
           // vamos a conectar a la DB y verificar si el operador existe
           
           final String strOperator = ZKUtilities.getTextBoxValue( textboxOperator, controllerLogger );
           final String strPassword = ZKUtilities.getTextBoxValue( textboxPassword, controllerLogger );
           
           if ( strOperator.isEmpty() == false && strPassword.isEmpty() == false ) {
           
               databaseConnection = new CDatabaseConnection(); 
               
               CDatabaseConnectionConfig databaseConnectionConfig = new CDatabaseConnectionConfig(); 
               
                // em esta linea obtenemos la ruta completa del Archivo de Configuracion de la base de datos inclido el /Config/
               String strRunningPath = Sessions.getCurrent().getWebApp().getRealPath( SystemConstants._WEB_INF_Dir );
               
               
               if (databaseConnectionConfig.loadConfig( strRunningPath + File.separator + SystemConstants._Config_Dir + SystemConstants._Database_Connection_Config_File_Name, controllerLogger, controllerLanguage )) {
          
                   if ( databaseConnection.makeConnectionToDatabase( databaseConnectionConfig, controllerLogger, controllerLanguage ) ) {
           
                       TBLOperator tblOperator = OperatorDAO.checkValid( databaseConnection, strOperator, strPassword, controllerLogger, controllerLanguage );
                       
                       if ( tblOperator != null ) {
                           
                           //mensaje de bienvenida
                           // Esta etiqueta esta configurado el color de la fuente Rojo por el Estilo War, lo vamos a inicializar a negro
                           labelMessage.setClass( "" );
                           labelMessage.setValue( "Welcome " + tblOperator.getName() +"!" );
                        
                           // si es valido guardamos la conexion creada y guardamos la identidad del operador
                           currentSession.setAttribute( SystemConstants._DB_Connection_Session_Key, databaseConnection ); 
                        
                           // Salvammos la identidad del Operador
                           currentSession.setAttribute( SystemConstants._Operator_Credential_Session_Key, tblOperator );
                           
                           // Salvamos la fecha y hora de la sesion 
                           //currentSession.setAttribute( SystemConstants._Login_Date_Time_Session_Key, LocalDate.now().toString() );

                           controllerLogger.logMessage( "1" , CLanguage.translateIf( controllerLanguage, "Saved user credential in session for user [%s]", tblOperator.getName() ) );

                           //Obtenemos la fecha y la hora en el formato yyyy-MM-dd-HH-mm-ss
                           String strDateTime = Utilities.getDateInFormat( ConstantsCommonClasses._Global_Date_Time_Format_File_System_24, null );
                           
                           //Creamos la variable del logpath
                           String strLogPath = strRunningPath + File.separator + SystemConstants._Logs_Dir + strOperator + File.separator + strDateTime + File.separator;
                           
                           //La guardamos en la sesion
                           currentSession.setAttribute( SystemConstants._Log_Path_Session_Key, strLogPath );

                           controllerLogger.logMessage( "1" , CLanguage.translateIf( controllerLanguage, "Saved user log path [%s] in session for user [%s]", strLogPath, strOperator ) );
                           
                           //Guardamos la fecha y la hora del inicio de sesión
                           currentSession.setAttribute( SystemConstants._Login_Date_Time_Session_Key, strDateTime );
                           
                           controllerLogger.logMessage( "1" , CLanguage.translateIf( controllerLanguage, "Saved user login date time [%s] in session for user [%s]", strDateTime, strOperator ) );
                           
                           //Creamos la lista de logger de esta sesion
                           List<String> loggedSessionLoggers = new LinkedList<String>();
                           
                           //Guardamos la lista vacia en la sesion
                           currentSession.setAttribute( SystemConstants._Logged_Session_Loggers, loggedSessionLoggers );
                        
                           // Actualizamos en la DB la Ultima Sesion
                           OperatorDAO.updateLastLoginData( databaseConnection, tblOperator.getId(), controllerLogger, controllerLanguage );
                           
                           //redireccionar al home.zul
                           Executions.sendRedirect( "views/home/home.zul" );
                           
                       }
                       else {
                        
                           labelMessage.setValue( "Invalid operator name or password" );
                           //Messagebox.show( "Invalid operator name or password" );  
                       }
                       
                   }
                   else {
               
                       labelMessage.setValue( "Database connetion failed" );
                       //Messagebox.show( "Connetion failed" );    
                   }
               }
               else {
                   
                   labelMessage.setValue( "error to read the database config file" );
                   //Messagebox.show( "error to read the config file" );
               }
           
               
           }
        
           
       }
       catch ( Exception ex ) {
           
           if ( controllerLogger != null ) controllerLogger.logException( "-1021" , ex.getMessage(), ex );
        
       }
       
   }
   
   
   @Listen( "onTimer=#timerKeepAliveSession")
   public void onTimer( Event event ){
       
       Clients.showNotification( "Automatic renewal of session successful", "info", null, "before_center", 20000, true);
       
   }
    
   
}
