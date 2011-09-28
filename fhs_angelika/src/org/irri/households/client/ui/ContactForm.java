package org.irri.households.client.ui;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DecoratedPopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HorizontalPanel;

public class ContactForm extends Composite {
	public DecoratedPopupPanel PopupContactUs;

	public ContactForm() {
		
		PopupContactUs = new DecoratedPopupPanel();
		PopupContactUs.setAnimationEnabled(true);
		PopupContactUs.setGlassStyleName("FHS-AboutUsGlass");
		PopupContactUs.setGlassEnabled(true);
		PopupContactUs.setStyleName("FHS-DecoratorPanelContactUs");
		
		VerticalPanel verticalPanelContactUs = new VerticalPanel();
		verticalPanelContactUs.setSpacing(3);
		PopupContactUs.setWidget(verticalPanelContactUs);
		
		TextBox textBoxName = new TextBox();
		textBoxName.setStyleName("FHS-TextBoxContactUs");
		textBoxName.setText("Name (required)");
		verticalPanelContactUs.add(textBoxName);
		textBoxName.setSize("50%", "20px");
		
		TextBox textBoxEmail = new TextBox();
		textBoxEmail.setStyleName("FHS-TextBoxContactUs");
		textBoxEmail.setText("Email address (required)");
		verticalPanelContactUs.add(textBoxEmail);
		textBoxEmail.setSize("50%", "20px");
		
		TextArea textAreaMessage = new TextArea();
		textAreaMessage.setStyleName("FHS-TextBoxContactUs");
		verticalPanelContactUs.add(textAreaMessage);
		textAreaMessage.setSize("380px", "300px");
		
		HorizontalPanel horizontalPanel = new HorizontalPanel();
		verticalPanelContactUs.add(horizontalPanel);
		horizontalPanel.setWidth("150px");
		
		Button btnSend = new Button("SEND");
		horizontalPanel.add(btnSend);
		btnSend.setStyleName("FHS-ButtonBrowseData");
		btnSend.setSize("70px", "25px");
		
		Button btnCancel = new Button("CANCEL");
		horizontalPanel.add(btnCancel);
		btnCancel.setStyleName("FHS-ButtonBrowseData");
		btnCancel.setSize("70px", "25px");
		btnCancel.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				PopupContactUs.hide();
			}
		});
	}
}
