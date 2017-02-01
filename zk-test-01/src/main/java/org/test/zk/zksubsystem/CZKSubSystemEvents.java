package org.test.zk.zksubsystem;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import org.test.zk.contants.SystemConstants;
import org.zkoss.zk.ui.Desktop;
import org.zkoss.zk.ui.Execution;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.WebApp;
import org.zkoss.zk.ui.util.DesktopCleanup;
import org.zkoss.zk.ui.util.DesktopInit;
import org.zkoss.zk.ui.util.ExecutionCleanup;
import org.zkoss.zk.ui.util.ExecutionInit;
import org.zkoss.zk.ui.util.SessionCleanup;
import org.zkoss.zk.ui.util.SessionInit;
import org.zkoss.zk.ui.util.WebAppCleanup;
import org.zkoss.zk.ui.util.WebAppInit;

import commonlibs.commonclasses.CLanguage;
import commonlibs.extendedlogger.CExtendedConfigLogger;
import commonlibs.extendedlogger.CExtendedLogger;

public class CZKSubSystemEvents implements  DesktopInit, DesktopCleanup, SessionInit, SessionCleanup, WebAppInit, WebAppCleanup, ExecutionInit, ExecutionCleanup {

    @Override
    public void cleanup( Execution execution0, Execution execution1, List<Throwable> errs ) throws Exception {
        
        System.out.println( "cleanup execution" );
        
    }

    @Override
    public void init( Execution execution0, Execution execution1 ) throws Exception {
        
        System.out.println( "init execution" );
        
    }

    @Override
    public void cleanup( WebApp webApp ) throws Exception {
        
        System.out.println( "cleanup webapp" );
        
        try {
            
            //obtenemos el Logger
            CExtendedLogger webAppLogger = new CExtendedLogger( SystemConstants._Webapp_Logger_Name );
            
            if ( webAppLogger != null ) {
             
                //escribimos un mensaje al Log
                webAppLogger.logMessage( "1" , CLanguage.translateIf( null, "Webapp ending now.") );
                
                //cerramos el log
                webAppLogger.flushAndClose();
            }
            
            //eliminamos el atributo del webApp
            webApp.removeAttribute( SystemConstants._Webapp_Logger_Name );
                                    
        }
        catch ( Exception ex ){
            
            System.out.println( ex.getMessage() );
        }
        
    }

    @Override
    public void init( WebApp webApp ) throws Exception {
        
        System.out.println( "init webapp" );
        
        try{
            
            String strRunningPath = webApp.getRealPath( SystemConstants._WEB_INF_Dir ) + File.separator; ;
            
            //se encarga de leer el archivo de configuracion logger.config.xml
            CExtendedConfigLogger configLogger = new CExtendedConfigLogger(); //clase lib Tomas importada en la extructura de proyecto
            
            //le decimos la ruta completa del archivo WEB-INF/config/logger.config.xml
            String strConfigpath = strRunningPath + SystemConstants._Config_Dir + SystemConstants._Logger_Config_File_Name;
            
            if ( configLogger.loadConfig( strConfigpath, null, null ) ) { // Crgamos la configuracion
                
                //Aqui creamos el logger como tal
                CExtendedLogger webAppLogger = CExtendedLogger.getLogger( SystemConstants._Webapp_Logger_Name );
                
                if ( webAppLogger.getSetupSet() == false  ) { // aqui preguntamos si todavia no esta configurado
                    
                    //le decimos donde va a crear los Archivos de log WEB_INF/logs/system
                    String strLogPath = strRunningPath + SystemConstants._Logs_Dir + SystemConstants._System_Dir;
                    
                    //configuramos el logger segun los parametros del archivo de configuracion logger.config.xml y la ruta para escribir los archivos de log
                    webAppLogger.setupLogger( configLogger.getInstanceID(), configLogger.getLogToScreen(), strLogPath, SystemConstants._Webapp_Logger_File_Log, configLogger.getClassNameMethodName() , configLogger.getExactMatch(), configLogger.getLevel(), configLogger.getLogIP(), configLogger.getLogPort(), configLogger.getHTTPLogURL(), configLogger.getHTTPLogUser(), configLogger.getHTTPLogPassword(), configLogger.getProxyIP(), configLogger.getProxyPort(), configLogger.getProxyUser(), configLogger.getProxyPassword() );
                    
                    //Guardamos el Logger Principal en un atributo del webapp
                    webApp.setAttribute( SystemConstants._Webapp_Logger_App_Attribute_Key, webAppLogger );
                    
                }
                
                //Aquí escribimos al log en un archivo en WEB-INF/logs/system/webapplogger.log
                //Fijense en la clase CLanguage es otra de mis clases, pero no le presten mucha atención todavía
                //Basta con decir que es una clase que permite escribir los mensajes del log en varios idiomas
                webAppLogger.logMessage( "1" , CLanguage.translateIf( null, "Webapp logger loaded and configured [%s].", SystemConstants._Webapp_Logger_Name ) );

            }
            
        }
        catch ( Exception ex ) {
            
            System.out.println( ex.getMessage() );  //Aquí no queda más remedio que usar la salida estandar el sistem la consola, por que todavia no existe el logger
            
        }
        
    }


    @Override
    public void cleanup( Session session ) throws Exception {
        
        System.out.println( "cleanup session" );
        
        //aqui debemos Limpiar logger que estan en la session del operado que invoco session.getcurrent().invalidate().
        
        @SuppressWarnings( "unchecked" )
        LinkedList<String> loggedSessionLoggers = (LinkedList<String>) session.getAttribute( SystemConstants._Logged_Session_Loggers );
        
        for ( String strLoggername : loggedSessionLoggers) {
            
            CExtendedLogger currentLogger = CExtendedLogger.getLogger( strLoggername );
            
            currentLogger.flushAndClose(); //cierro cada logger
            
        }
        
        loggedSessionLoggers.clear(); // vacio la lista
    
    }
    
    @Override
    public void init( Session session, Object object ) throws Exception {
        
        System.out.println( "init session" );
        
    }

    @Override
    public void cleanup( Desktop desktop ) throws Exception {
        
        System.out.println( "cleanup desktop" );
        
    }

    @Override
    public void init( Desktop desktop, Object object ) throws Exception {
        
        System.out.println( "init desktop" );
        
    } 
    
    
}
