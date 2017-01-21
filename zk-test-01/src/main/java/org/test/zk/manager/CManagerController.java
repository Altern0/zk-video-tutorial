package org.test.zk.manager;



import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.test.zk.dao.TBLPersonDAO;
import org.test.zk.database.CDatabaseConnection;
import org.test.zk.datamodel.TBLPerson;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Event;
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


public class CManagerController extends SelectorComposer<Component> {
    
    private static final long serialVersionUID = -1591648938821366036L;
    
    protected ListModelList<TBLPerson> dataModel = new ListModelList<TBLPerson>();
    
    public static final String _DATABASE_CONNECTION_KEY = "databaseConnection";
    
    protected CDatabaseConnection databaseConnection;
    
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
    Button buttonConnectionToDB;
    
    @Wire
    Button buttonModify;
    
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
            */
            dataModel.setMultiple( true );
            
            listboxPersons.setModel( dataModel );
            listboxPersons.setItemRenderer( new rendererHelper() ); // aqui asociamos el renderizado de la vista
            
            Session currentSession = Sessions.getCurrent();
            
            if ( currentSession.getAttribute( _DATABASE_CONNECTION_KEY ) instanceof CDatabaseConnection ) {
                
                //Vamos a recuperar la sesion
                // se usa otra cast o conversion de tipo forzado
                databaseConnection = ( CDatabaseConnection ) currentSession.getAttribute( _DATABASE_CONNECTION_KEY );
                
                buttonConnectionToDB.setLabel( "Disconnect" );
            }
            
        }
        catch ( Exception e ) {
            
            e.printStackTrace();
            
        }
    }
    
    @Listen( "onClick=#buttonConnectionToDB")
    public void onClickbuttonConnectionToDB ( Event event ){
        
        Session currentSession = Sessions.getCurrent();
        
       // if ( buttonConnectionToDB.getLabel().equalsIgnoreCase( "Connect" ) ){

        if  ( databaseConnection == null ) { // al estar persitida por la sesion se puede usar la coneccion como bandera
            
            databaseConnection = new CDatabaseConnection();
            
            if ( databaseConnection.makeConnectionToDatabase() ) {
                
                currentSession.setAttribute( _DATABASE_CONNECTION_KEY, databaseConnection ); // agrego la sesion abierta
                
                buttonConnectionToDB.setLabel( "Disconnect" );
                
                Messagebox.show( "Conexion Exitosa" );                
                
            }
            else {
            
                Messagebox.show( "Conexion Fallida" );
            }
        }
        else {
            if ( databaseConnection != null ){
                
                if ( databaseConnection.closeConnectionToDatabase() ){
                 
                    databaseConnection = null;
                    
                    //currentSession.setAttribute( _DATABASE_CONNECTION_KEY, null ); // elimino la sesion abierta
                    // se puede realizar de esta otra forma
                    
                    currentSession.removeAttribute( _DATABASE_CONNECTION_KEY );
                    
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
        
        
        
    }
         
    
    @Listen( "onClick=#buttonAdd")
    public void onClickbuttonAdd ( Event event ){
        
        // paso de parametros a la ventana dialog
        
        Map<String,Object> params = new HashMap<String,Object>();
        params.put( "callerComponent", buttonAdd );
        
        Window win = ( Window ) Executions.createComponents("/dialog.zul", null, params);
        
        win.doModal();
        
    }
    
    @Listen( "onClick=#buttonModify")
    public void onClickbuttonModify ( Event event ){
        
        Set<TBLPerson> selectedItems = dataModel.getSelection();
        
        if (selectedItems != null && selectedItems.size() > 0) {
            
            TBLPerson person = selectedItems.iterator().next(); // se crea la referencia del CPerson Seleccionado no se crea uno nuevo
            
            
            Map<String,Object> params = new HashMap<String,Object>();
            
            params.put( "personToModify", person ); // se pasa la referencia del CPerson seleccionada
            params.put( "callerComponent", buttonModify ); // se pasa el componente en este caso el botun donde se va a ejecutar
            Window win = ( Window ) Executions.createComponents("/dialog.zul", null, params);
            
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
            
            Messagebox.show("¿seguro quieres elenimas los " + Integer.toString( selectedItems.size()) + " registros?\n" + strBuffer, "Eliminar", Messagebox.OK | Messagebox.CANCEL, Messagebox.QUESTION, new org.zkoss.zk.ui.event.EventListener() {
                public void onEvent(Event evt) throws InterruptedException {
                    
                    if ( evt.getName().equals( "onOK" ) ) {
                        
                        while (selectedItems.iterator().hasNext()) {
                            
                            TBLPerson person = selectedItems.iterator().next();
                            
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
    
    
    @Listen( "onDialogFinished=#buttonAdd" )
    public void onDialogFinishedbuttonAdd ( Event event) {
        
        //Este evento recibe los controladores de Dialog.zul 
        
        System.out.println( "evento recibido Add" );
        
        if (event.getData() != null ){
            
            TBLPerson person = (TBLPerson) event.getData(); // typecast
            
            dataModel.add( person ); 
            
            //temporalmente probamos aqui el insertar de la DB
            
            TBLPersonDAO.insertData( databaseConnection, person );
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
    
    
}
