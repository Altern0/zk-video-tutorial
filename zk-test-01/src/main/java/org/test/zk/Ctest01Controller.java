package org.test.zk;

import java.time.LocalDate;

import org.test.zk.datamodel.CPerson;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Button;
import org.zkoss.zul.ItemRenderer;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Selectbox;
import org.zkoss.zul.Window;


public class Ctest01Controller extends SelectorComposer<Component> implements ItemRenderer<CPerson> {

   
    private static final long serialVersionUID = -8770470515037000449L;
    
    @Wire
    Button buttonTest01;
    
    @Wire ("#buttonMiSuperBoton")
    Button buttonTest02;
    
    @Wire
    Button buttonTest03;
    
    @Wire
    Window windowTest01;
    
    @Wire
    Selectbox selectbox01;
    
    @Wire
    Selectbox selectbox02;
    
    protected ListModelList<String> dataModel = new ListModelList<String>(); 
    
    protected ListModelList<CPerson> dataModelPerson = new ListModelList<CPerson>();
    
    @Listen( "onClick=#buttonTest01")
    public void onClickButtonTest01( Event event ){
 
        windowTest01.setTitle( "Click Button Test 01" );
        
        dataModel.add( "1" );
        dataModel.add( "2" );
        dataModel.add( "3" );
        dataModel.add( "4" );
        dataModel.add( "5" );
  
        dataModelPerson.add( new CPerson ( "12674936", "Al", " Perez",1,LocalDate.parse("1978-24-09"),"Papa") );
        dataModelPerson.add( new CPerson ( "16082546", "Yle", " Prieto",0,LocalDate.parse("1982-03-11"),"Mama") );
        dataModelPerson.add( new CPerson ( "00000000", "Nico", " Perez",1,LocalDate.parse("2013-28-10"),"Hijo") );
        
        selectbox02.setModel( dataModelPerson );
        selectbox02.setItemRenderer( this );
        
        selectbox01.setModel( dataModel );
        dataModel.addToSelection( "1" );
        selectbox01.setSelectedIndex( 0 );
        
    }
    
    @Listen( "onClick=#buttonMiSuperBoton")
    public void sellamacomomedalagana( Event event ){
 
        buttonTest02.setLabel( "Funciona" );
    
    }
    
    @Listen( "onClick=#buttonTest03")
    public void onClickTest03( Event event ){
 
        windowTest01.doModal();
    
    }
    
    @Listen( "onSelect=#selectbox01")
    public void onSelectselectbox01( Event event ){
 
        if (selectbox01.getSelectedIndex() >= 0 ){
            
            windowTest01.setTitle( dataModel.get( selectbox01.getSelectedIndex() ) );
            
        }
    
    }
    
    @Listen( "onSelect=#selectbox02")
    public void onSelectselectbox02( Event event ){
 
        if (selectbox02.getSelectedIndex() >= 0 ){
            
            CPerson personSelect =  dataModelPerson.get( selectbox02.getSelectedIndex() );
            
            windowTest01.setTitle( personSelect.getFirtsName() + " " + personSelect.getLastName() );
            
        }
    
    }

    @Override
    public String render( Component arg0, CPerson arg1, int arg2 ) throws Exception {
                return arg1.getFirtsName() + " " + arg1.getLastName();
    }
    
}