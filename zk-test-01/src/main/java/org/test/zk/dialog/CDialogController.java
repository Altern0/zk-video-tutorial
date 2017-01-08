package org.test.zk.dialog;

import org.test.zk.dao.CPerson;
import org.zkoss.zhtml.Label;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Execution;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Selectbox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import net.sf.jasperreports.engine.util.Java14BigDecimalHandler;


public class CDialogController extends SelectorComposer<Component> {
   
    private static final long serialVersionUID = -8977563222707532143L;
    
    protected ListModelList<String> dataModel = new ListModelList<String>(); 

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
    Textbox textboxIdComent;  
        
    @Override
    public void doAfterCompose( Component comp ) {
        
        try {
            
            super.doAfterCompose( comp );
            dateboxBirthday.setFormat( "dd-MM-yyyy" );
            
            dataModel.add( "Femenino" );
            dataModel.add( "Masculino" );
            
            selectboxGender.setModel( dataModel );
            selectboxGender.setSelectedIndex( 0 );
            
            dataModel.addToSelection( "Femenino" );
            
            final Execution execution = Executions.getCurrent();
                    
            CPerson personToModify = (CPerson) execution.getArg().get( "personToModify" );
            
            textboxId.setValue( personToModify.getID() );
            textboxFirstName.setValue( personToModify.getFirtsName() );
            textboxLastName.setValue( personToModify.getLastName() );
            dataModel.addToSelection( (personToModify.getGender() == 0 ? "Femenino" : "Masculino" ) );
            selectboxGender.setSelectedIndex( personToModify.getGender() );
            dateboxBirthday.setValue( java.sql.Date.valueOf( personToModify.getBirthDate() ) );
            textboxIdComent.setValue( personToModify.getComment() );

            
                 
        }
        catch ( Exception e ) {
            
            e.printStackTrace();
            
        }
     }

    
    
    @Listen( "onClick=#buttonAccept")
    public void onClickbuttonAcept ( Event event ){
       
       // Messagebox.show("Id=" + textboxId.getValue() + " First Name " + textboxFirstName.getValue() , "Aceptar", Messagebox.OK, Messagebox.INFORMATION);
       // System.out.println( "Hola Aceptar" );
 
        windowPerson.detach();
        
    }
    
    @Listen( "onClick=#buttonCancel")
    public void onClickbuttonCancel( Event event ){
 
       // Messagebox.show("Cancelar", "Cancelar", Messagebox.OK, Messagebox.EXCLAMATION);
       // System.out.println( "Hola Cancelar" );
    
        windowPerson.detach();
    }
}
