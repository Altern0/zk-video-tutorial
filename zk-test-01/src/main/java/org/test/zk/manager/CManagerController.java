package org.test.zk.manager;

import java.util.Iterator;
import java.util.Set;

import org.test.zk.dao.CPerson;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.ListitemRenderer;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;


public class CManagerController extends SelectorComposer<Component> {

   private static final long serialVersionUID = -1591648938821366036L;

   protected ListModelList<CPerson> dataModel = new ListModelList<CPerson>();
   
   public class rendererHelper implements ListitemRenderer<CPerson>{

    @Override
    public void render( Listitem listitem, CPerson person, int intIndex ) throws Exception {
        
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
                        
        }
        catch ( Exception e ) {
            
            e.printStackTrace();
            
        }
        
    }     
   
   }
      
   @Wire
   Listbox listboxPersons;
   
   @Override
   public void doAfterCompose( Component comp ) {
       
       try {
           
           super.doAfterCompose( comp );
           
           CPerson person01 = new CPerson ("111","Al","Perez");
           CPerson person02 = new CPerson ("222","Yle","Prieto");
           CPerson person03 = new CPerson ("333","Nico","Perez Prieto");
           CPerson person04 = new CPerson ("444","aaa","AAA");
           CPerson person05 = new CPerson ("555","bbb","BBB");
           CPerson person06 = new CPerson ("666","ccc","CCC");
           CPerson person07 = new CPerson ("777","ddd","DDD");
           
           dataModel.add( person01 );
           dataModel.add( person02 );
           dataModel.add( person03 );
           dataModel.add( person04 );
           dataModel.add( person05 );
           dataModel.add( person06 );
           dataModel.add( person07 );
           
           dataModel.setMultiple( true );
           
           listboxPersons.setModel( dataModel );
           listboxPersons.setItemRenderer( new rendererHelper() );
       }
       catch ( Exception e ) {
           
           e.printStackTrace();
           
       }
    }
     
   @Listen( "onClick=#buttonAdd")
   public void onClickbuttonAdd ( Event event ){
      
    //   Map arg = new HashMap();
    //   arg.put("someName", someValue);
       Window win = ( Window ) Executions.createComponents("/dialog.zul", null, null);
       
       win.doModal();
   
   }
   
   @Listen( "onClick=#buttonModify")
   public void onClickbuttonModify ( Event event ){
      
   
   }

   @SuppressWarnings( { "rawtypes", "unchecked" } )
@Listen( "onClick=#buttonDelete")
   public void onClickbuttonDelete ( Event event ){
  
     Set<CPerson> selectedItems = dataModel.getSelection();
      
     if (selectedItems != null && selectedItems.size() > 0) {
        
       String strBuffer = null;
           
         for ( CPerson person : selectedItems ) {
           
           if (strBuffer == null){    
               
               strBuffer = person.getID() + " " + person.getFirtsName() + " " + person.getLastName();
           }
           else{
              
             strBuffer = strBuffer + "\n" + person.getID() + " " + person.getFirtsName() + " " + person.getLastName();
               
           }
         }  

         Messagebox.show("¿seguro quieres elenimas los " + Integer.toString( selectedItems.size()) + " registros?\n" + strBuffer, "Eliminar", Messagebox.OK | Messagebox.CANCEL, Messagebox.QUESTION, new org.zkoss.zk.ui.event.EventListener() {
             public void onEvent(Event evt) throws InterruptedException {
               
               if ( evt.getName().equals( "onOK" ) ) {
               
                   while (selectedItems.iterator().hasNext()) {
                     
                     CPerson person = selectedItems.iterator().next();
                     
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
   
}
