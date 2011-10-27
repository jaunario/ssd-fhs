package org.irri.households.client.ui;


import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DecoratedPopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;


public class ContactDetails extends Composite {
	public DecoratedPopupPanel PopupContactUs;

	public ContactDetails() {
		
		PopupContactUs = new DecoratedPopupPanel();
		PopupContactUs.setAutoHideEnabled(true);
		PopupContactUs.setAnimationEnabled(true);
		PopupContactUs.setGlassStyleName("FHS-AboutUsGlass");
		PopupContactUs.setGlassEnabled(true);
		PopupContactUs.setStyleName("FHS-DecoratorPanelContactUs");
		
		VerticalPanel verticalPanelContactUs = new VerticalPanel();
		verticalPanelContactUs.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		verticalPanelContactUs.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		verticalPanelContactUs.setSpacing(3);
		PopupContactUs.setWidget(verticalPanelContactUs);
		verticalPanelContactUs.setHeight("200px");
		
		HTML htmlNewHtml = new HTML("For inquiries/feedback, you may send an e-mail to abc123@defgh.com. Please indicate your name and organization.", true);
		verticalPanelContactUs.add(htmlNewHtml);
		htmlNewHtml.setSize("300px", "");
	}
}
