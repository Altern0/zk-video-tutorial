package org.test.zk.controllers.login;

import java.io.File;
import java.time.LocalDate;

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
import org.zkoss.zul.Label;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;

import commonlibs.commonclasses.CLanguage;
import commonlibs.extendedlogger.CExtendedLogger;
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
           
           // obtenemos el logger del objeto webApp y guardamos una referencia en la variable de clase controllerLogger 
           controllerLogger = (CExtendedLogger) Sessions.getCurrent().getWebApp().getAttribute(  SystemConstants._Webapp_Logger_App_Attribute_Key );
        
           
       }
       catch ( Exception ex ) {
           
           if ( controllerLogger != null ) controllerLogger.logException( "-1021" , ex.getMessage(), ex );
        
       }
   }
   
   @Listen( "onClick=#buttonLogin")
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
               String strRunningPath = Sessions.getCurrent().getWebApp().getRealPath( SystemConstants._WEB_INF_Dir ) + File.separator + SystemConstants._Config_Dir;
               
               
               if (databaseConnectionConfig.loadConfig( strRunningPath + SystemConstants._Database_Connection_Config_File_Name, controllerLogger, controllerLanguage )) {
          
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
                           currentSession.setAttribute( SystemConstants._Login_Date_Time_Session_Key, LocalDate.now().toString() );
                           
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
   
}
