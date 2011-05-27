package org.irri.households.client.ui;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;

public class toolbar extends Composite {

	public toolbar() {
		
		HorizontalPanel horizontalPanel = new HorizontalPanel();
		horizontalPanel.setSpacing(2);
		initWidget(horizontalPanel);
		horizontalPanel.setWidth("111px");
		
		PushButton pshbtnViewTable = new PushButton("View Data");
		pshbtnViewTable.setHTML("<img src=\"./images/view_40.png\" alt=\"view-data\" /><br />Get Data\r\n");
		horizontalPanel.add(pshbtnViewTable);
		horizontalPanel.setCellHeight(pshbtnViewTable, "55px");
		horizontalPanel.setCellWidth(pshbtnViewTable, "50px");
		horizontalPanel.setCellVerticalAlignment(pshbtnViewTable, HasVerticalAlignment.ALIGN_MIDDLE);
		horizontalPanel.setCellHorizontalAlignment(pshbtnViewTable, HasHorizontalAlignment.ALIGN_CENTER);
		pshbtnViewTable.setSize("50px", "55px");
		
		PushButton pshbtnChart = new PushButton("Chart");
		pshbtnChart.setHTML("<img src=\"./images/chart_40.png\" alt=\"Plot\"/><br />Plot");
		horizontalPanel.add(pshbtnChart);
		pshbtnChart.setSize("50px", "55px");
		horizontalPanel.setCellVerticalAlignment(pshbtnChart, HasVerticalAlignment.ALIGN_MIDDLE);
		horizontalPanel.setCellHorizontalAlignment(pshbtnChart, HasHorizontalAlignment.ALIGN_CENTER);
		horizontalPanel.setCellHeight(pshbtnChart, "55px");
		horizontalPanel.setCellWidth(pshbtnChart, "50px");
		
		PushButton pshbtnMap = new PushButton("Map");
		pshbtnMap.setHTML("<img src=\"./images/map_40.png\" alt=\"Download\"/><br />Map");
		horizontalPanel.add(pshbtnMap);
		horizontalPanel.setCellVerticalAlignment(pshbtnMap, HasVerticalAlignment.ALIGN_MIDDLE);
		horizontalPanel.setCellHorizontalAlignment(pshbtnMap, HasHorizontalAlignment.ALIGN_CENTER);
		pshbtnMap.setSize("50px", "55px");
		horizontalPanel.setCellHeight(pshbtnMap, "55px");
		horizontalPanel.setCellWidth(pshbtnMap, "50px");
		
		PushButton pshbtnDownload = new PushButton("Download");
		pshbtnDownload.setHTML("<img src=\"./images/Download_40.png\" alt=\"Download\"/><br />Download");
		horizontalPanel.add(pshbtnDownload);
		horizontalPanel.setCellHeight(pshbtnDownload, "55px");
		horizontalPanel.setCellWidth(pshbtnDownload, "50px");
		horizontalPanel.setCellVerticalAlignment(pshbtnDownload, HasVerticalAlignment.ALIGN_MIDDLE);
		horizontalPanel.setCellHorizontalAlignment(pshbtnDownload, HasHorizontalAlignment.ALIGN_CENTER);
		pshbtnDownload.setSize("50px", "55px");
		
		PushButton pshbtnTranspose = new PushButton("Transpose");
		horizontalPanel.add(pshbtnTranspose);
		pshbtnTranspose.setSize("50px", "55px");
		horizontalPanel.setCellHeight(pshbtnTranspose, "55px");
		horizontalPanel.setCellWidth(pshbtnTranspose, "50px");
		
	}

}
