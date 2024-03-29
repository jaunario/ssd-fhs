/*This is for the page that will contain the search by proj, country, table views in a deck panel.
 * Initially contains the how to for search by proj*/

package org.irri.households.client;


import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DeckPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.dom.client.Style.Unit;


public class Fhs_SearchBy extends Composite {
	public VerticalPanel verticalPanel;
	public Fhs_CountryList CountryList;
	public Var_Result VarResult;
	public DeckPanel ProjSearchDeckPanel;
	public HTML htmlProjSearch;
	public HTML htmlCntrySearch;
	public HTML htmlTableSearch;
	public DeckPanel deckPanel;


	public Fhs_SearchBy() {
		
		ScrollPanel scroll = new ScrollPanel();
		initWidget(scroll);
		
		DockLayoutPanel dockLayoutPanel = new DockLayoutPanel(Unit.PX);
		scroll.add(dockLayoutPanel);
		dockLayoutPanel.setSize("1190px", "715px");
		
		verticalPanel = new VerticalPanel();
		dockLayoutPanel.addWest(verticalPanel, 250.0);
		verticalPanel.setHeight("701px");
		verticalPanel.setStyleName("FHS-verticalPanel");
		
		HTML htmlhowToSearch = new HTML("<p><b>How to Search By Project</b><p>\n\n<p>The process of retrieving data from this facility is sequential. Please follow the steps below to avoid errors.\n\n<p>\n<ol>\n<li>Select a project.</li><br>\n\n<li>Click the Browse Data button located at the bottom of the details window.</li><br>\n\n<li>Select a table and wait for the other windows to load.</li><br>\n\n<li>You may uncheck some variables and/or choose only specific years and/or countries, respectively. Make sure to wait for the windows to refresh in between clicks.</li><br>\n\n<li>Navigate through the table by using the First, Previous, Next, and Last buttons. Click the Clear button to reset your selection.</li><br>\n\n<li>Click Download Data to retrieve a csv file. It may take a few seconds for the downloaded file to appear.</li><br>\n</ol>\n\n", true);
		verticalPanel.add(htmlhowToSearch);
		
		deckPanel = new DeckPanel();
		deckPanel.setStyleName("fhs-HomeHPanel");
		dockLayoutPanel.add(deckPanel);
		deckPanel.setSize("805px", "700px");
	}
}
