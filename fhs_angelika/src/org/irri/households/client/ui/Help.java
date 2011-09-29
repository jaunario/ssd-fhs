package org.irri.households.client.ui;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.HTML;

public class Help extends Composite {
	public DialogBox helpBox;

	public Help() {
		
		helpBox = new DialogBox(true);
		helpBox.setGlassStyleName("FHS-AboutUsGlass");
		helpBox.setGlassEnabled(true);
		helpBox.setAnimationEnabled(true);
		helpBox.setStyleName("FHS-AboutUsPopup");
		
		VerticalPanel verticalPanel = new VerticalPanel();
		helpBox.add(verticalPanel);
		
		HTML helphtml = new HTML("<p><b>How to Use this Facility</b><p>\n\n<p>The process of retrieving data from this facility is sequential. Please follow the steps below to avoid errors.\n\n<p>\n<ol>\n<li>Select a search type (Search By Project, Country or Table).</li>\n\n<li>For Search By Project:<br>\n   a. Select a project.<br>\n   b. Click the Browse Data button located below the details window.<br>\n   c. Select a table and wait for the other windows to load. When the loading indicator in the top left part of the page is gone, you can proceed.<br>\n   d. You may uncheck some variables and/or choose only specific years and countries. Just make sure to wait for the windows to load in between clicks.<br>\n   e. Click Download Data to retrieve a csv file.<br><br>\n</li>\n\n   For Search By Country:<br>\n   a. Select a country from the list or from the map.<br>\n   b. Click the Browse Data button located below the details window.<br>\n   c. Select a table and wait for the other windows to load. When the loading indicator in the top left part of the page is gone, you can proceed.<br>\n   d. You may uncheck some variables and/or choose only specific years. Just make sure to wait for the windows to load in between clicks.<br>\n   e. Click Download Data to retrieve a csv file.<br><br>\n\n   For Search By Table:<br>\n   a. Select a table and wait for the other windows to load. When the loading indicator in the top left part of the page is gone, you can proceed.<br>\n   b. You may uncheck some variables and/or choose only specific years and countries. Just make sure to wait for the windows to load in between clicks.<br>\n   c. Click Download Data to retrieve a csv file.", true);
		helphtml.setStyleName("FHS-Help");
		verticalPanel.add(helphtml);
		helphtml.setWidth("500px");
	}

}
