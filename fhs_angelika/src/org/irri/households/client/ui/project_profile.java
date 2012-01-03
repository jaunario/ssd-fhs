package org.irri.households.client.ui;

import org.irri.households.client.ui.charts.Fhs_surveysite;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.FlowPanel;

public class project_profile extends Composite {

	public project_profile() {
		
		DockPanel dpProfileWrapper = new DockPanel();
		dpProfileWrapper.setSpacing(5);
		initWidget(dpProfileWrapper);
		
		Label lblProfileName = new Label("Profile Name");
		lblProfileName.setStyleName("gwt-Label-ProfileTitle");
		dpProfileWrapper.add(lblProfileName, DockPanel.NORTH);
		
		FlowPanel flowPanel = new FlowPanel();
		dpProfileWrapper.add(flowPanel, DockPanel.CENTER);
		dpProfileWrapper.setCellHeight(flowPanel, "100%");
		dpProfileWrapper.setCellWidth(flowPanel, "100%");
		
		Fhs_surveysite ProjSites = new Fhs_surveysite();
		flowPanel.add(ProjSites);
		
		Label lblTest = new Label("TEst");
		flowPanel.add(lblTest);
	}

}
