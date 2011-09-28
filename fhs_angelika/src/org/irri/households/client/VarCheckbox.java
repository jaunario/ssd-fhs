package org.irri.households.client;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.HasChangeHandlers;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class VarCheckbox extends Composite implements HasChangeHandlers{
	public VerticalPanel CheckboxVPanel;
	public CheckBox VarCheck;

	public VarCheckbox() {
		
		ScrollPanel CheckboxScroll = new ScrollPanel();
		initWidget(CheckboxScroll);
		CheckboxScroll.setSize("414px", "226px");
		
		CheckboxVPanel = new VerticalPanel();
		CheckboxVPanel.setStyleName("FHS-VarScrollMargin");
		CheckboxScroll.setWidget(CheckboxVPanel);
	}
	
	public void populateCheckbox(String[][] result){
		for (int i = 1; i < result.length; i++) {
        	CheckBox VarCheck = new CheckBox(result[i][0]+" - "+result[i][1]);
        	VarCheck.setName(result[i][0]);
        	VarCheck.setValue(true);
        	CheckboxVPanel.add(VarCheck);
		}
	}
	
	public void populateCheckboxLoc(String[][] result){
    	for (int i = 1; i < result.length; i++) {
        	String[] vars = result[i][2].split(";");
        	String[] vars2 = result[i][1].split(";");
			if (vars.length>1){
        		for (int j=0;j<vars.length;j++){
        			if (vars2[j].equalsIgnoreCase("project")){
        				VarCheck = new CheckBox("project_id - Project_Id");
        				VarCheck.setName(vars2[j]);
            			VarCheck.setValue(true);
            			CheckboxVPanel.add(VarCheck);
        			}else{
        			VarCheck = new CheckBox(vars2[j]+" - "+vars[j]);
        			VarCheck.setName(vars2[j]);
        			VarCheck.setValue(true);
        			CheckboxVPanel.add(VarCheck);
        			}
        		}
        	}else{
        		VarCheck = new CheckBox(result[i][2]);
        		VarCheck.setName(result[i][1]);
        		VarCheck.setValue(true);
        		CheckboxVPanel.add(VarCheck);
        	}
        	
		}
	}

	@Override
	public HandlerRegistration addChangeHandler(ChangeHandler handler) {
		return addDomHandler(handler, ChangeEvent.getType());
	}
	
	public CheckBox getItem(int index){
		return (CheckBox) CheckboxVPanel.getWidget(index);
	}
	
	public int getItemCount(){
		return CheckboxVPanel.getWidgetCount();
	}
	
	
}
