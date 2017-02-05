package org.test.zk.controllers.person.manager;


import java.io.File;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.test.zk.contants.SystemConstants;
import org.test.zk.database.CDatabaseConnection;
import org.test.zk.database.CDatabaseConnectionConfig;
import org.test.zk.database.dao.PersonDAO;
import org.test.zk.database.datamodel.TBLOperator;
import org.test.zk.database.datamodel.TBLPerson;
import org.test.zk.utilities.SystemUtilities;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Button;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.ListitemRenderer;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

import commonlibs.commonclasses.CLanguage;
import commonlibs.commonclasses.ConstantsCommonClasses;
import commonlibs.extendedlogger.CExtendedConfigLogger;
import commonlibs.extendedlogger.CExtendedLogger;
import commonlibs.utils.Utilities;


public class CManagerController extends SelectorComposer<Component> {
    
    private static final long serialVersionUID = -1591648938821366036L;
    
    protected CDatabaseConnection databaseConnection = null; 
    
    protected ListModelList<TBLPerson> dataModel =  null; //new ListModelList<TBLPerson>();
    
    public class rendererHelper implements ListitemRenderer<TBLPerson>{
        
        @Override
        public void render( Listitem listitem, TBLPerson person, int intIndex ) throws Exception {
            
            try {
                
                Listcell cell = new Listcell();
                cell.setLabel( person.getID() );
                listitem.appendChild(cell);
                
                cell = new Listcell();
                cell.setLabel( person.getFirtsName() );
                listitem.appendChild(cell);
                
                cell = new Listcell();
                cell.setLabel( person.getLastName() );
                listitem.appendChild(cell);
                
                cell = new Listcell();
                cell.setLabel( person.getGender() == 0  ? "Famele" : "Male" );
                listitem.appendChild(cell);
                
                cell = new Listcell();
                cell.setLabel( person.getBirthDate().toString() );
                listitem.appendChild(cell);
                
                cell = new Listcell();
                cell.setLabel( person.getComment() );
                listitem.appendChild(cell);
                
            }
            catch ( Exception e ) {
                
                e.printStackTrace();
                
            }
            
        }     
        
    }
    
    @Wire
    Listbox listboxPersons;
    
    @Wire
    Button buttonAdd;
    
    @Wire
    Button buttonRefresh;
    
  //  @Wire
  //  Button buttonConnectionToDB;
    
    @Wire
    Button buttonModify;
    
    @Wire
    Window windowPersonManager;
    
    protected CExtendedLogger controllerLogger = new CExtendedLogger( null );
              
    protected CLanguage controllerLanguage = null;   
              
    public void initcontrollerLoggerAndcontrollerLanguage  ( String strRunningPath, Session currentSession ) {
        
        //Leemos la configuración del logger del archivo o de la sesión
        CExtendedConfigLogger extendedConfigLogger = SystemUtilities.initLoggerConfig( strRunningPath, currentSession );

        //Obtenemos las credenciales del operador las cuales debieron ser guardadas por el CLoginController.java
        TBLOperator operatorCredential = (TBLOperator) currentSession.getAttribute( SystemConstants._Operator_Credential_Session_Key );
 
        //Inicializamos los valores de las variables
        String strOperator = SystemConstants._Operator_Unknown; //Esto es un valor por defecto no debería quedar con el pero si lo hacer el algoritmo no falla
        String strLoginDateTime = (String) currentSession.getAttribute( SystemConstants._Login_Date_Time_Session_Key ); //Recuperamos información de fecha y hora del inicio de sesión Login
        String strLogPath = (String) currentSession.getAttribute( SystemConstants._Log_Path_Session_Key ); //Recuperamos el path donde se guardarn los log ya que cambia según el nombre de l operador que inicie sesion  

        if ( operatorCredential != null )
            strOperator = operatorCredential.getName();  //Obtenemos el nombre del operador que hizo login

        if ( strLoginDateTime == null ) //En caso de ser null no ha fecha y hora de inicio de sesión colocarle una por defecto
            strLoginDateTime = Utilities.getDateInFormat( ConstantsCommonClasses._Global_Date_Time_Format_File_System_24, null );

        final String strLoggerName = SystemConstants._Person_Manager_Controller_Logger_Name;
        final String strLoggerFileName = SystemConstants._Person_Manager_Controller_File_Log;
        
        //Aqui creamos el logger para el operador que inicio sesión login en el sistem
        controllerLogger = CExtendedLogger.getLogger( strLoggerName + " " + strOperator + " " + strLoginDateTime );

        //strRunningPath = Sessions.getCurrent().getWebApp().getRealPath( SystemConstanst._WEB_INF_Dir ) + File.separator;

        //Esto se ejecuta si es la primera vez que esta creando el logger recuerden lo que pasa 
        //Cuando el usuario hace recargar en el navegador todo el .zul se vuelve a crear de cero, 
        //pero el logger persiste de manera similar a como lo hacen la viriables de session
        if ( controllerLogger.getSetupSet() == false ) {

            //Aquí vemos si es null esa varible del logpath intentamos poner una por defecto
            if ( strLogPath == null )
                strLogPath = strRunningPath + File.separator + SystemConstants._Logs_Dir;

            //Si hay una configucación leida de session o del archivo la aplicamos
            if ( extendedConfigLogger != null )
                controllerLogger.setupLogger( strOperator + " " + strLoginDateTime, false, strLogPath, strLoggerFileName, extendedConfigLogger.getClassNameMethodName(), extendedConfigLogger.getExactMatch(), extendedConfigLogger.getLevel(), extendedConfigLogger.getLogIP(), extendedConfigLogger.getLogPort(), extendedConfigLogger.getHTTPLogURL(), extendedConfigLogger.getHTTPLogUser(), extendedConfigLogger.getHTTPLogPassword(), extendedConfigLogger.getProxyIP(), extendedConfigLogger.getProxyPort(), extendedConfigLogger.getProxyUser(), extendedConfigLogger.getProxyPassword() );
            else    //Si no usamos la por defecto
                controllerLogger.setupLogger( strOperator + " " + strLoginDateTime, false, strLogPath, strLoggerFileName, SystemConstants._Log_Class_Method, SystemConstants._Log_Exact_Match, SystemConstants._Log_Level, "", -1, "", "", "", "", -1, "", "" );

            //Inicializamos el lenguage para ser usado por el logger
            controllerLanguage = CLanguage.getLanguage( controllerLogger, strRunningPath + SystemConstants._Langs_Dir + strLoggerName + "." + SystemConstants._Lang_Ext ); 

            //Protección para el multi hebrado, puede que dos usuarios accedan exactamente al mismo tiempo a la página web, este código en el servidor se ejecuta en dos hebras
            synchronized( currentSession ) { //Aquí entra un asunto de habras y acceso multiple de varias hebras a la misma variable
            
                //Guardamos en la sesisón los logger que se van creando para luego ser destruidos.
                @SuppressWarnings("unchecked")
                LinkedList<String> loggedSessionLoggers = (LinkedList<String>) currentSession.getAttribute( SystemConstants._Logged_Session_Loggers );

                if ( loggedSessionLoggers != null ) {

                    //sessionLoggers = new LinkedList<String>();

                    //El mismo problema de la otra variable
                    synchronized( loggedSessionLoggers ) {

                        //Lo agregamos a la lista
                        loggedSessionLoggers.add( strLoggerName + " " + strOperator + " " + strLoginDateTime );

                    }

                    //Lo retornamos la sesión de este operador
                    currentSession.setAttribute( SystemConstants._Logged_Session_Loggers, loggedSessionLoggers );

                }
            
            }
            
        }
    
    }
    
    
    @Override
    public void doAfterCompose( Component comp ) {
        
        try {
            
            super.doAfterCompose( comp );
            
            /*
            TBLPerson person01 = new TBLPerson ("111","Al","Perez",1,LocalDate.parse("1978-09-24"),"Papa");
            TBLPerson person02 = new TBLPerson ("222","Yle","Prieto",0,LocalDate.parse("1982-11-03"),"Mama");
            TBLPerson person03 = new TBLPerson ("333","Nico","Perez Prieto",1,LocalDate.parse("2013-10-28"),"Hijo");
            TBLPerson person04 = new TBLPerson ("444","aaa","AAA",1,LocalDate.parse("2004-04-04"),"444");
            TBLPerson person05 = new TBLPerson ("555","bbb","BBB",1,LocalDate.parse("2005-05-05"),"555");
            TBLPerson person06 = new TBLPerson ("666","ccc","CCC",0,LocalDate.parse("2006-06-06"),"666");
            TBLPerson person07 = new TBLPerson ("777","ddd","DDD",0,LocalDate.parse("2007-07-07"),"777");
            
            dataModel.add( person01 );
            dataModel.add( person02 );
            dataModel.add( person03 );
            dataModel.add( person04 );
            dataModel.add( person05 );
            dataModel.add( person06 );
            dataModel.add( person07 );
            
            
            listboxPersons.setModel( dataModel );
            listboxPersons.setItemRenderer( new rendererHelper() ); // aqui asociamos el renderizado de la vista
            */
            
           
            
            Session currentSession = Sessions.getCurrent();
            
            // obtenemos el logger del objeto webApp y guardamos una referencia en la variable de clase controllerLogger 
            // controllerLogger = (CExtendedLogger) Sessions.getCurrent().getWebApp().getAttribute(  SystemConstants._Webapp_Logger_App_Attribute_Key );
                        
            // obtenemos la direccion del archivo de configuracion de los logger
            final String strRunningPath = Sessions.getCurrent().getWebApp().getRealPath( SystemConstants._WEB_INF_Dir ) + File.separator + SystemConstants._Config_Dir;
           
            //Inicializacmos el Logger y el Lenguaje
            initcontrollerLoggerAndcontrollerLanguage( strRunningPath, Sessions.getCurrent() );
            
            if ( currentSession.getAttribute( SystemConstants._DB_Connection_Session_Key ) instanceof CDatabaseConnection ) {
                
                //Vamos a recuperar la sesion
                // se usa otra cast o conversion de tipo forzado
                databaseConnection = ( CDatabaseConnection ) currentSession.getAttribute( SystemConstants._DB_Connection_Session_Key );
                
                // buttonConnectionToDB.setLabel( "Disconnect" );
             
                //forzamos a refrescar
                Events.echoEvent( new Event( "onClick", buttonRefresh ) ); // llamamos al evento onClick del Boton Refrescar. 
            
            }
            
        }
        catch ( Exception ex ) {
            
            if ( controllerLogger != null ) controllerLogger.logException( "-1021" , ex.getMessage(), ex );
         
        }
    }
    
    /*
    @Listen( "onClick=#buttonConnectionToDB")
    public void onClickbuttonConnectionToDB ( Event event ){
        
        Session currentSession = Sessions.getCurrent();
       
         
       // if ( buttonConnectionToDB.getLabel().equalsIgnoreCase( "Connect" ) ){

        if  ( databaseConnection == null ) { // al estar persitida por la sesion se puede usar la coneccion como bandera
            
            
            databaseConnection = new CDatabaseConnection(); 
            
            CDatabaseConnectionConfig databaseConnectionConfig = new CDatabaseConnectionConfig(); 
            
            // em esta linea obtenemos la ruta completa del Archivo de Configuracion de la base de datos inclido el /Config/
            String strRunningPath = Sessions.getCurrent().getWebApp().getRealPath( SystemConstants._WEB_INF_Dir ) + File.separator + SystemConstants._Config_Dir;
            
            
            if (databaseConnectionConfig.loadConfig( strRunningPath + SystemConstants._Database_Connection_Config_File_Name, controllerLogger, controllerLanguage )) {
            
                if ( databaseConnection.makeConnectionToDatabase( databaseConnectionConfig, controllerLogger, controllerLanguage ) ) {
        
                    //Salvamos la configuracion en el objeto databaseConnection
                    //databaseConnection.setDBConnectionConfig( databaseConnectionConfig, webAppLogger, null );
                
                    // agrego la sesion abierta
                    currentSession.setAttribute( SystemConstants._DB_Connection_Session_Key, databaseConnection ); 
                
                    buttonConnectionToDB.setLabel( "Disconnect" );
                
                    Messagebox.show( "Conexion Exitosa" );                
                
                }
                else {
            
                    Messagebox.show( "Conexion Fallida" );
                }
            }
            else {
                
                Messagebox.show( "error al leer el archivo de configuracion" );
            }
        
        }
        else {
            
            if ( databaseConnection != null ){
                
                // obtenemos el logger del objeto ebApp
                CExtendedLogger webAppLogger = (CExtendedLogger) Sessions.getCurrent().getWebApp().getAttribute(  SystemConstants._Webapp_Logger_App_Attribute_Key );
                
                if ( databaseConnection.closeConnectionToDatabase(webAppLogger, null) ){
                 
                    databaseConnection = null;
                    
                    //currentSession.setAttribute( _DATABASE_CONNECTION_KEY, null ); // elimino la sesion abierta
                    // se puede realizar de esta otra forma
                    
                    currentSession.removeAttribute( SystemConstants._DB_Connection_Session_Key );
                    
                    Messagebox.show( "Conexion Cerrada" ); 
                    
                    buttonConnectionToDB.setLabel( "Connect" );
                }
                else {
                 
                    Messagebox.show( "eror al cerrar" ); 
                }
            }
            else{
                
                Messagebox.show( "¡No esta Conectado!" );
                buttonConnectionToDB.setLabel( "Connect" );
            }
        }
        
        //forzamos a refrescar
        Events.echoEvent( new Event( "onClick", buttonRefresh ) ); // llamamos al evento onClick del Boton Refrescar.  
        
    }
    */     
    @Listen( "onClick=#buttonRefresh")
    public void onClickbuttonRefresh ( Event event ){
        

        //inicializamos el Listbox
        listboxPersons.setModel( (ListModelList <?>) null );
        
        // vamos a cargar en el dataModel la DB pero primero recuperamos la Conexion
        Session currentSession = Sessions.getCurrent();
                
        if ( currentSession.getAttribute( SystemConstants._DB_Connection_Session_Key ) instanceof CDatabaseConnection ) {
           
             databaseConnection = ( CDatabaseConnection ) currentSession.getAttribute( SystemConstants._DB_Connection_Session_Key ); //recuperamos la sesion
        
             // para recargar el modelo
             List<TBLPerson> listData = PersonDAO.searchData( databaseConnection, controllerLogger, controllerLanguage );
             
             // cargamos el Datamodel con la lista que nos retrona la base de datso
             dataModel = new ListModelList<TBLPerson>( listData );
             // Seleccion multiple
             dataModel.setMultiple( true );
             //luego recargamos el modelo
             listboxPersons.setModel( dataModel );
             // y lo renderizamos con nuestra funcion
             listboxPersons.setItemRenderer( new rendererHelper() ); // aqui asociamos el renderizado de la vista
             
             
        }    
        
    }
    
    @Listen( "onClick=#buttonAdd")
    public void onClickbuttonAdd ( Event event ){
        
        // paso de parametros a la ventana dialog
        
        Map<String,Object> params = new HashMap<String,Object>();
        params.put( "callerComponent", listboxPersons); // buttonAdd );
        
        Window win = ( Window ) Executions.createComponents("/views/person/editor/editor.zul", null, params);
        
        win.doModal();
        
    }
    
    @Listen( "onClick=#buttonModify")
    public void onClickbuttonModify ( Event event ){
        
        Set<TBLPerson> selectedItems = dataModel.getSelection();
        
        if (selectedItems != null && selectedItems.size() > 0) {
            
            TBLPerson person = selectedItems.iterator().next(); // se crea la referencia del CPerson Seleccionado no se crea uno nuevo
            
            
            Map<String,Object> params = new HashMap<String,Object>();
            
           // params.put( "personToModify", person ); // se pasa la referencia del CPerson seleccionada
            // ya no enviamos como parametros PersonToModify  sino el IdPerson
            params.put( "IdPerson", person.getID() );
            
            params.put( "callerComponent", listboxPersons ); //buttonModify ); // se pasa el componente en este caso el botun donde se va a ejecutar
            Window win = ( Window ) Executions.createComponents("/views/person/editor/editor.zul", null, params);
            
            win.doModal();
            
            
        }
        else{
            
            Messagebox.show( "No hay Seleccion " );
            
        }   
        
    }
    
    @SuppressWarnings( { "rawtypes", "unchecked" } )
    @Listen( "onClick=#buttonDelete")
    public void onClickbuttonDelete ( Event event ){
        
        Set<TBLPerson> selectedItems = dataModel.getSelection();
        
        if (selectedItems != null && selectedItems.size() > 0) {
            
            String strBuffer = null;
            
            for ( TBLPerson person : selectedItems ) {
                
                if (strBuffer == null){    
                    
                    strBuffer = person.getID() + " " + person.getFirtsName() + " " + person.getLastName();
                }
                else{
                    
                    strBuffer = strBuffer + "\n" + person.getID() + " " + person.getFirtsName() + " " + person.getLastName();
                    
                }
            }  
            
            Messagebox.show("You are sure delete the next " + Integer.toString( selectedItems.size()) + " records?\n" + strBuffer, "Delete", Messagebox.OK | Messagebox.CANCEL, Messagebox.QUESTION, new org.zkoss.zk.ui.event.EventListener() {
                public void onEvent(Event evt) throws InterruptedException {
                    
                    if ( evt.getName().equals( "onOK" ) ) {
                        
                        while (selectedItems.iterator().hasNext()) {
                            
                            TBLPerson person = selectedItems.iterator().next();
                                                        
                            PersonDAO.deletaData( databaseConnection, person.getID(), controllerLogger, controllerLanguage );
                            
                            dataModel.remove( person );
                            
                            
                        }       
                        
                    }
                    
                }
            });
            
        } 
        else{
            
            Messagebox.show( "No hay Seleccion " );
        }   
    }
    
    
    @Listen( "onDialogFinished=#listboxPersons" ) // indica que se modifico la DB y que debe refrescar el lisbox
    public void onDialogFinishedlistboxPersons ( Event event) {
        
        Events.echoEvent( new Event( "onClick", buttonRefresh ) ); // llamamos al evento onClick del Boton Refrescar.
        
    }
    
    @Listen( "onClick=#buttonCloset")
    public void onClickbuttonCloset( Event event ){
 
        windowPersonManager.detach();
        
    }
    
    
    /*estos Eventos ya no son necesarios aqui en el MAnager porque solo lo vamos a limitar para listar elementos, 
      todo lo demas lo dejaremos en el dialog
    @Listen( "onDialogFinished=#buttonAdd" )
    public void onDialogFinishedbuttonAdd ( Event event) {
        
        //Este evento recibe los controladores de Dialog.zul 
        
        System.out.println( "evento recibido Add" );
        
        if (event.getData() != null ){
            
            TBLPerson person = (TBLPerson) event.getData(); // typecast
            
            dataModel.add( person ); 
            
            //temporalmente probamos aqui el insertar de la DB
            
            //TBLPersonDAO.insertData( databaseConnection, person ); ya no madamos a guardar aqui sino en el DialogController en el onclick de aceptar
        }
        
    }
    
    @Listen( "onDialogFinished=#buttonModify" )
    public void onDialogFinishedbuttonModify ( Event event ) {
        
        //Este evento recibe los controladores de Dialog.zul 
        System.out.println( "evento recibido Modify" );
        
        if (event.getData() != null ){
            
            TBLPerson person = (TBLPerson) event.getData(); // typecast
            dataModel.notifyChange( person );   
        }
        // Una forma de  Refrescar el ListBox
        // listboxPersons.setModel( (ListModelList <?>) null ); // se hace null el setodel 
        // listboxPersons.setModel( dataModel ); // se asina denuevo el setModel
        
    }
    */
    
}
