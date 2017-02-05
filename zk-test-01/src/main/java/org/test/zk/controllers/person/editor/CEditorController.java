package org.test.zk.controllers.person.editor;

import java.io.File;
import java.util.LinkedList;

import org.test.zk.contants.SystemConstants;
import org.test.zk.database.CDatabaseConnection;
import org.test.zk.database.dao.PersonDAO;
import org.test.zk.database.datamodel.TBLOperator;
import org.test.zk.database.datamodel.TBLPerson;
import org.test.zk.utilities.SystemUtilities;
import org.zkoss.zhtml.Label;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Execution;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Selectbox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import commonlibs.commonclasses.CLanguage;
import commonlibs.commonclasses.ConstantsCommonClasses;
import commonlibs.extendedlogger.CExtendedConfigLogger;
import commonlibs.extendedlogger.CExtendedLogger;
import commonlibs.utils.Utilities;


public class CEditorController extends SelectorComposer<Component> {
   
    private static final long serialVersionUID = -8977563222707532143L;
    
    protected ListModelList<String> dataModel = new ListModelList<String>(); 
    
    protected CDatabaseConnection databaseConnection;
    
    protected CExtendedLogger controllerLogger = null;
    
    protected CLanguage controllerLanguage = null;   
    
    protected Component callerComponent = null; // Variave de clase 
    
    protected TBLPerson personToModify = null;
    
    protected TBLPerson personToAdd = null; 
    
    @Wire
    Window windowPerson;
    
    @Wire
    Label labelId;
    
    @Wire
    Textbox textboxId;
        
    @Wire
    Label labelFirstName;
    
    @Wire
    Textbox textboxFirstName; 
        
    @Wire
    Label labelLastName;
    
    @Wire
    Textbox textboxLastName;
        
    @Wire    
    Label labelBirthday;
        
    @Wire
    Datebox dateboxBirthday;
    
    @Wire
    Label labelGender;
    
    @Wire
    Selectbox selectboxGender;
    
    @Wire
    Label labeComent;
    
    @Wire
    Textbox textboxComment;  
  
    public void initcontrollerLoggerAndcontrollerLanguage  ( String strRunningPath, Session currentSession ) {
        
        //Leemos la configuraci�n del logger del archivo o de la sesi�n
        CExtendedConfigLogger extendedConfigLogger = SystemUtilities.initLoggerConfig( strRunningPath, currentSession );

        //Obtenemos las credenciales del operador las cuales debieron ser guardadas por el CLoginController.java
        TBLOperator operatorCredential = (TBLOperator) currentSession.getAttribute( SystemConstants._Operator_Credential_Session_Key );
 
        //Inicializamos los valores de las variables
        String strOperator = SystemConstants._Operator_Unknown; //Esto es un valor por defecto no deber�a quedar con el pero si lo hacer el algoritmo no falla
        String strLoginDateTime = (String) currentSession.getAttribute( SystemConstants._Login_Date_Time_Session_Key ); //Recuperamos informaci�n de fecha y hora del inicio de sesi�n Login
        String strLogPath = (String) currentSession.getAttribute( SystemConstants._Log_Path_Session_Key ); //Recuperamos el path donde se guardarn los log ya que cambia seg�n el nombre de l operador que inicie sesion  

        if ( operatorCredential != null )
            strOperator = operatorCredential.getName();  //Obtenemos el nombre del operador que hizo login

        if ( strLoginDateTime == null ) //En caso de ser null no ha fecha y hora de inicio de sesi�n colocarle una por defecto
            strLoginDateTime = Utilities.getDateInFormat( ConstantsCommonClasses._Global_Date_Time_Format_File_System_24, null );

        final String strLoggerName = SystemConstants._Person_Editor_Controller_Logger_Name;
        final String strLoggerFileName = SystemConstants._Person_Editor_Controller_File_Log;
        
        //Aqui creamos el logger para el operador que inicio sesi�n login en el sistem
        controllerLogger = CExtendedLogger.getLogger( strLoggerName + " " + strOperator + " " + strLoginDateTime );

        //strRunningPath = Sessions.getCurrent().getWebApp().getRealPath( SystemConstanst._WEB_INF_Dir ) + File.separator;

        //Esto se ejecuta si es la primera vez que esta creando el logger recuerden lo que pasa 
        //Cuando el usuario hace recargar en el navegador todo el .zul se vuelve a crear de cero, 
        //pero el logger persiste de manera similar a como lo hacen la viriables de session
        if ( controllerLogger.getSetupSet() == false ) {

            //Aqu� vemos si es null esa varible del logpath intentamos poner una por defecto
            if ( strLogPath == null )
                strLogPath = strRunningPath + File.separator + SystemConstants._Logs_Dir;

            //Si hay una configucaci�n leida de session o del archivo la aplicamos
            if ( extendedConfigLogger != null )
                controllerLogger.setupLogger( strOperator + " " + strLoginDateTime, false, strLogPath, strLoggerFileName, extendedConfigLogger.getClassNameMethodName(), extendedConfigLogger.getExactMatch(), extendedConfigLogger.getLevel(), extendedConfigLogger.getLogIP(), extendedConfigLogger.getLogPort(), extendedConfigLogger.getHTTPLogURL(), extendedConfigLogger.getHTTPLogUser(), extendedConfigLogger.getHTTPLogPassword(), extendedConfigLogger.getProxyIP(), extendedConfigLogger.getProxyPort(), extendedConfigLogger.getProxyUser(), extendedConfigLogger.getProxyPassword() );
            else    //Si no usamos la por defecto
                controllerLogger.setupLogger( strOperator + " " + strLoginDateTime, false, strLogPath, strLoggerFileName, SystemConstants._Log_Class_Method, SystemConstants._Log_Exact_Match, SystemConstants._Log_Level, "", -1, "", "", "", "", -1, "", "" );

            //Inicializamos el lenguage para ser usado por el logger
            controllerLanguage = CLanguage.getLanguage( controllerLogger, strRunningPath + SystemConstants._Langs_Dir + strLoggerName + "." + SystemConstants._Lang_Ext ); 

            //Protecci�n para el multi hebrado, puede que dos usuarios accedan exactamente al mismo tiempo a la p�gina web, este c�digo en el servidor se ejecuta en dos hebras
            synchronized( currentSession ) { //Aqu� entra un asunto de habras y acceso multiple de varias hebras a la misma variable
            
                //Guardamos en la sesis�n los logger que se van creando para luego ser destruidos.
                @SuppressWarnings("unchecked")
                LinkedList<String> loggedSessionLoggers = (LinkedList<String>) currentSession.getAttribute( SystemConstants._Logged_Session_Loggers );

                if ( loggedSessionLoggers != null ) {

                    //sessionLoggers = new LinkedList<String>();

                    //El mismo problema de la otra variable
                    synchronized( loggedSessionLoggers ) {

                        //Lo agregamos a la lista
                        loggedSessionLoggers.add( strLoggerName + " " + strOperator + " " + strLoginDateTime );

                    }

                    //Lo retornamos la sesi�n de este operador
                    currentSession.setAttribute( SystemConstants._Logged_Session_Loggers, loggedSessionLoggers );

                }
            
            }
            
        }
    
    }
        
    @Override
    public void doAfterCompose( Component comp ) {
        
        try {
            
            super.doAfterCompose( comp );
            
            // obtenemos el logger del objeto webApp y guardamos una referencia en la variable de clase controllerLogger 
            // controllerLogger = (CExtendedLogger) Sessions.getCurrent().getWebApp().getAttribute(  SystemConstants._Webapp_Logger_App_Attribute_Key );
            //en su lugar llamamos a la funcion Initcontroller

            // obtenemos la direccion del archivo de configuracion de los logger
            final String strRunningPath = Sessions.getCurrent().getWebApp().getRealPath( SystemConstants._WEB_INF_Dir ) + File.separator + SystemConstants._Config_Dir + File.separator;
           
            //Inicializacmos el Logger y el Lenguaje
            initcontrollerLoggerAndcontrollerLanguage( strRunningPath, Sessions.getCurrent() );
            
            dateboxBirthday.setFormat( "dd-MM-yyyy" );
            
            dataModel.add( "Femenino" );
            dataModel.add( "Masculino" );
            
            selectboxGender.setModel( dataModel );
            selectboxGender.setSelectedIndex( 0 );
            
            dataModel.addToSelection( "Femenino" );
            
            final Execution execution = Executions.getCurrent();
            
            
            //personToModify = (TBLPerson) execution.getArg().get( "personToModify" ); // recibimos la person
            // PersonToModify ahora vie de la DB y del Argumento que recibimos 
            // primero recuperamos la sesion y pasamos el id a modificar 
            
            Session currentSession = Sessions.getCurrent();
            
            if ( currentSession.getAttribute( SystemConstants._DB_Connection_Session_Key ) instanceof CDatabaseConnection ) {
                
                //Vamos a recuperar la sesion
                // se usa otra cast o conversion de tipo forzado
                databaseConnection = ( CDatabaseConnection ) currentSession.getAttribute( SystemConstants._DB_Connection_Session_Key );
                
              //  buttonConnectionToDB.setLabel( "Disconnect" );
                
                if ( execution.getArg().get( "IdPerson" ) instanceof String ) {
                    
                    //cargamos la data de la DB
                    personToModify = PersonDAO.loadData( databaseConnection, (String) execution.getArg().get( "IdPerson" ), controllerLogger, controllerLanguage );
                    
                }
                    
            }
            
            
         // cuando se crea una nueva persona no es necesario personToModify
            if ( personToModify != null ) {
                textboxId.setValue( personToModify.getID() );
                textboxFirstName.setValue( personToModify.getFirtsName() );
                textboxLastName.setValue( personToModify.getLastName() );
                dataModel.addToSelection( ( personToModify.getGender() == 0 ? "Femenino" : "Masculino" ) );
                selectboxGender.setSelectedIndex( personToModify.getGender() );
                dateboxBirthday.setValue( java.sql.Date.valueOf( personToModify.getBirthDate() ) );
                textboxComment.setValue( personToModify.getComment() );
            }      
            
            // debemos guardar la referencia al componente que nos envia el controlador  del manager.zul
            callerComponent = (Component) execution.getArg().get( "callerComponent" ); 
                 
        }
        catch ( Exception ex ) {
            
            if ( controllerLogger != null ) controllerLogger.logException( "-1021" , ex.getMessage(), ex );
            
        }
     }
    
    @Listen( "onClick=#buttonAccept")
    public void onClickbuttonAcept ( Event event ){

        windowPerson.detach();
        
        if ( personToModify != null ) {
            personToModify.setID( textboxId.getValue());
            personToModify.setFirtsName(textboxFirstName.getValue());
            personToModify.setLastName(textboxLastName.getValue());
            personToModify.setGender(selectboxGender.getSelectedIndex());
            personToModify.setBirthDate( new java.sql.Date(dateboxBirthday.getValue().getTime()).toLocalDate() );
            personToModify.setComment(textboxComment.getValue());

            PersonDAO.updateData( databaseConnection, personToModify, controllerLogger, controllerLanguage ); // ACUALIZAAMOS EN LA DB
            
            // lanzamos el evento retornamos la persona a modificar
            Events.echoEvent( new Event( "onDialogFinished", callerComponent, personToModify ) );
                    
        } 
        else {
            
            personToAdd = new TBLPerson();
            
            personToAdd.setID( textboxId.getValue());
            personToAdd.setFirtsName(textboxFirstName.getValue());
            personToAdd.setLastName(textboxLastName.getValue());
            personToAdd.setGender(selectboxGender.getSelectedIndex());
            personToAdd.setBirthDate( new java.sql.Date(dateboxBirthday.getValue().getTime()).toLocalDate() );
            personToAdd.setComment(textboxComment.getValue());
            
            PersonDAO.insertData( databaseConnection, personToAdd, controllerLogger, controllerLanguage ); // INSERTAMOS EN LA DB
            
            Events.echoEvent( new Event( "onDialogFinished", callerComponent, personToAdd ) );
            
        }
        
        
        
    }
    
    @Listen( "onClick=#buttonCancel")
    public void onClickbuttonCancel( Event event ){
 
      
        windowPerson.detach();
    }
}
