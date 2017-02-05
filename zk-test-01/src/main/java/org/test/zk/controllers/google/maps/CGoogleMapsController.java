package org.test.zk.controllers.google.maps;


import org.zkoss.gmaps.Gmarker;
import org.zkoss.gmaps.event.MapMouseEvent;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;


@SuppressWarnings( "serial" )
public class CGoogleMapsController extends SelectorComposer<Component> {
 
    
    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
         
        Executions.createComponents("/views/google/google.maps.ctrl.zul", null, null);
        
    }
    
        
    @Listen("onMapClick = #gmaps")
    public void onMapClick(MapMouseEvent event) {
        Gmarker gmarker = event.getGmarker();
        if (gmarker != null) {
            gmarker.setOpen(true);
        }
    }

}
