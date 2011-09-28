package org.irri.households.client.ui;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DecoratedPopupPanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;

public class AboutDialog extends Composite{
	public DecoratedPopupPanel aboutUsPopup;

	public AboutDialog() {
		
		aboutUsPopup = new DecoratedPopupPanel();
		aboutUsPopup.setGlassEnabled(true);
		aboutUsPopup.setAnimationEnabled(true);
		aboutUsPopup.setGlassStyleName("FHS-AboutUsGlass");
		aboutUsPopup.setSize("350px", "300px");
		aboutUsPopup.setStyleName("FHS-AboutUsPopup");
		VerticalPanel VPanWrapper = new VerticalPanel();
		VPanWrapper.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		VPanWrapper.setSpacing(5);
		
		aboutUsPopup.add(VPanWrapper);
		
		Button btnClose = new Button("CLOSE");
		btnClose.setStyleName("FHS-ButtonBrowseData");
		btnClose.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				aboutUsPopup.hide();
			}
		});
		
		HTMLPanel HTMLTxt = new HTMLPanel("<h1 style='text-align:left'>Farm Household Survey <br/>Data Center</h1><p style='text-align:justify;padding-left:20px;padding-right:20px;'>is a collection of farm level datasets on rice productivity, fertilizer and pesticide use, labor inputs, prices, income, demographics, farm characteristics, and other related data on rice production in farmer’s fields. It includes a lot of variables that describes not only the rice production at actual farmer’s fields but as well as the farmer and the farm family. It is a rich collection of actual farm and household level data collected through personal interviews, farm record keeping, and periodic monitoring of farm activities from various sites in different rice growing countries of Asia.</p>");
		VPanWrapper.add(HTMLTxt);
		HTMLTxt.setSize("350px", "300px");
		VPanWrapper.add(btnClose);
		VPanWrapper.setCellVerticalAlignment(btnClose, HasVerticalAlignment.ALIGN_MIDDLE);
		VPanWrapper.setCellHorizontalAlignment(btnClose, HasHorizontalAlignment.ALIGN_RIGHT);
		
	}

}
