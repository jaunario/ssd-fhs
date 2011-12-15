package org.irri.households.client.ui;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DecoratedPopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.CheckBox;

public class GetEmailAdd extends Composite {
	public Button btnSend;
	public DecoratedPopupPanel PopupGetEmail;
	public TextBox textBoxEmail;
	public CheckBox chkremember;

	public GetEmailAdd() {
		
		PopupGetEmail = new DecoratedPopupPanel();
		PopupGetEmail.setAnimationEnabled(true);
		PopupGetEmail.setGlassStyleName("FHS-AboutUsGlass");
		PopupGetEmail.setGlassEnabled(true);
		PopupGetEmail.setStyleName("FHS-DecoratorPanelContactUs");
		
		VerticalPanel verticalPanelContactUs = new VerticalPanel();
		verticalPanelContactUs.setStyleName("gwt-VPContactUs");
		verticalPanelContactUs.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		verticalPanelContactUs.setSpacing(3);
		PopupGetEmail.setWidget(verticalPanelContactUs);
		
		HTML htmlNewHtml = new HTML("<p align=\"justify\">The size of the data you are trying to download is large and will take long to finish. Please enter your email address below and we will just send you a copy of the data.</p>", true);
		verticalPanelContactUs.add(htmlNewHtml);
		htmlNewHtml.setWidth("400px");
		
		textBoxEmail = new TextBox();
		textBoxEmail.setStyleName("FHS-TextBoxContactUs");
		verticalPanelContactUs.add(textBoxEmail);
		textBoxEmail.setSize("300px", "20px");
		
		
		HorizontalPanel horizontalPanel_1 = new HorizontalPanel();
		verticalPanelContactUs.add(horizontalPanel_1);
		horizontalPanel_1.setWidth("300px");
		
		chkremember = new CheckBox("Remember my email address");
		horizontalPanel_1.add(chkremember);
		chkremember.setWidth("300px");			
		
		HorizontalPanel horizontalPanel = new HorizontalPanel();
		horizontalPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_BOTTOM);
		verticalPanelContactUs.add(horizontalPanel);
		horizontalPanel.setSize("150px", "35px");
		
		btnSend = new Button("SEND");
		horizontalPanel.add(btnSend);
		btnSend.setStyleName("FHS-ButtonBrowseData");
		btnSend.setSize("70px", "25px");
		
		Button btnCancel = new Button("CANCEL");
		horizontalPanel.add(btnCancel);
		btnCancel.setStyleName("FHS-ButtonBrowseData");
		btnCancel.setSize("73px", "25px");
		btnCancel.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				PopupGetEmail.hide();
			}
		});
	}
	
	public void SetGetEmailSendBtn(ClickHandler click){
		btnSend.addClickHandler(click);
	}
}
