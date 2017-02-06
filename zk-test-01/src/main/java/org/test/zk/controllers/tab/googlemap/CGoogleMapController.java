package org.test.zk.controllers.tab.googlemap;


import org.zkoss.gmaps.Gmarker;
import org.zkoss.gmaps.event.MapMouseEvent;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;


@SuppressWarnings( "serial" )
public class CGoogleMapController extends SelectorComposer<Component> {
        
    @Listen("onMapClick = #gmaps")
    public void onMapClick(MapMouseEvent event) {

        Gmarker gmarker = event.getGmarker();
        
        if (gmarker != null) {
        
            gmarker.setOpen(true);
        
        }
    
    }

}
