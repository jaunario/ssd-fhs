package org.irri.households.client.ui;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class BrowsingPager extends Composite {

	public BrowsingPager() {
		
		
		
		DockPanel dockPanel = new DockPanel();
		initWidget(dockPanel);
		
		TabPanel tabPanel = new TabPanel();
		dockPanel.add(tabPanel, DockPanel.CENTER);
		
		DockPanel dockPanel_1 = new DockPanel();
		tabPanel.add(dockPanel_1, "New tab", false);
		dockPanel_1.setSize("5cm", "3cm");
		
		
		VerticalPanel verticalPanel = new VerticalPanel();
		tabPanel.add(verticalPanel, "New tab", false);
		verticalPanel.setSize("5cm", "3cm");
		dockPanel.add(tabPanel.getTabBar(), DockPanel.SOUTH);
		tabPanel.getTabBar().setTabHTML(1, "<object><param name='icon' value='/img/map_40.png'></object>");
		
	}

}
