package org.irri.households.client.ui;


import org.irri.households.client.ProjectDetails;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DeckPanel;
import com.google.gwt.user.client.ui.SimplePanel;


public class Slider extends Composite {		
	public SimplePanel ProjLinkPanel;
	public DeckPanel deckPanel;
	public ProjectDetails projDetails;
	
	public Slider() {
		deckPanel = new DeckPanel();
		deckPanel.setSize("500px", "700px");
		deckPanel.addStyleName("demo-panel");
		deckPanel.setAnimationEnabled(true);
		
		final Timer transition = new Timer(){
			public void run(){
				int index = deckPanel.getVisibleWidget();
				index++;
				if (index == deckPanel.getWidgetCount()) index = 0;
				deckPanel.showWidget(index);
			}
		};
		transition.scheduleRepeating(10000);
		initWidget(deckPanel);
	}
	
}
