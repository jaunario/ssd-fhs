//slide of pictures as seen on the landing page

package org.irri.households.client.ui;


import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DeckPanel;
import com.google.gwt.user.client.ui.Image;


public class PicsSlider extends Composite {
	private DeckPanel deckPanel;
	
	public PicsSlider() {
		deckPanel = new DeckPanel();
		deckPanel.setSize("220px", "160px");
		deckPanel.addStyleName("demo-panel");
		deckPanel.setAnimationEnabled(true);
		
		Image image1 = new Image("/images/picslider1.jpg");
		Image image2 = new Image("/images/picslider2.jpg");
		Image image3 = new Image("/images/picslider3.jpg");
		
		deckPanel.add(image1);
		deckPanel.add(image2);
		deckPanel.add(image3);
		
		deckPanel.showWidget(0);
		
		final Timer transition = new Timer(){
			public void run(){
				int index = deckPanel.getVisibleWidget();
				index++;
				if (index == deckPanel.getWidgetCount()) index = 0;
				deckPanel.showWidget(index);
			}
		};	
		
		transition.scheduleRepeating(5000);
		
		initWidget(deckPanel);
	}
}
