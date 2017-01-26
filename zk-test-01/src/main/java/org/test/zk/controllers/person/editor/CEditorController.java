package org.test.zk.controllers.person.editor;

import org.test.zk.contants.SystemConstants;
import org.test.zk.dao.TBLPersonDAO;
import org.test.zk.database.CDatabaseConnection;
import org.test.zk.datamodel.TBLPerson;
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


public class CEditorController extends SelectorComposer<Component> {
   
    private static final long serialVersionUID = -8977563222707532143L;
    
    protected ListModelList<String> dataModel = new ListModelList<String>(); 
    
    protected CDatabaseConnection databaseConnection;
    
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
                    personToModify = TBLPersonDAO.loadData( databaseConnection, (String) execution.getArg().get( "IdPerson" ) );
                    
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
        catch ( Exception e ) {
            
            e.printStackTrace();
            
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

            TBLPersonDAO.updateData( databaseConnection, personToModify ); // ACUALIZAAMOS EN LA DB
            
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
            
            TBLPersonDAO.insertData( databaseConnection, personToAdd ); // INSERTAMOS EN LA DB
            
            Events.echoEvent( new Event( "onDialogFinished", callerComponent, personToAdd ) );
            
        }
        
        
        
    }
    
    @Listen( "onClick=#buttonCancel")
    public void onClickbuttonCancel( Event event ){
 
      
        windowPerson.detach();
    }
}
