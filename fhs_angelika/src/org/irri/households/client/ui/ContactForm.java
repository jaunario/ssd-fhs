package org.irri.households.client.ui;

import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.ClickEvent;

public class ContactForm extends DialogBox {

	public ContactForm() {
		setGlassEnabled(true);
		setHTML("Contact");
		
		VerticalPanel verticalPanel = new VerticalPanel();
		verticalPanel.setSpacing(2);
		setWidget(verticalPanel);
		verticalPanel.setSize("100%", "100%");
		
		TextBox txtbxName = new TextBox();
		txtbxName.setText("Name");
		verticalPanel.add(txtbxName);
		
		TextBox txtbxEmail = new TextBox();
		txtbxEmail.setText("Email");
		verticalPanel.add(txtbxEmail);
		
		TextArea txtrMessage = new TextArea();
		txtrMessage.setText("Message");
		verticalPanel.add(txtrMessage);
		txtrMessage.setSize("321px", "229px");
		
		HorizontalPanel horizontalPanel = new HorizontalPanel();
		horizontalPanel.setSpacing(2);
		verticalPanel.add(horizontalPanel);
		verticalPanel.setCellHorizontalAlignment(horizontalPanel, HasHorizontalAlignment.ALIGN_RIGHT);
		
		Button btnSubmit = new Button("Submit");
		horizontalPanel.add(btnSubmit);
		
		Button btnCancel = new Button("Cancel");
		btnCancel.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				hide();
			}
		});
		horizontalPanel.add(btnCancel);
	}

}
