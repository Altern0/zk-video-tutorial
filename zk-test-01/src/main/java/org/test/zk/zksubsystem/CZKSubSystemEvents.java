package org.test.zk.zksubsystem;

import java.util.List;
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
    public void cleanup( WebApp webapp ) throws Exception {
        
        System.out.println( "cleanup webapp" );
        
    }

    @Override
    public void init( WebApp webapp ) throws Exception {
        
        System.out.println( "init webapp" );
        
    }

    @Override
    public void cleanup( Session session ) throws Exception {
        
        System.out.println( "cleanup session" );
        
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
