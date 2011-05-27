package org.irri.households.client;

import org.irri.households.client.ui.AboutDialog;
import org.irri.households.client.ui.ContactForm;
import org.irri.households.client.ui.toolbar;
//import org.irri.households.shared.FieldVerifier;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.MenuItem;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Fhs_baysilanon implements EntryPoint {
	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {
		RootPanel rootPanel = RootPanel.get();
		
		final AboutDialog DiaAbout = new AboutDialog();		
		
		final ContactForm DiaComment = new ContactForm();
		
		DockLayoutPanel dockLayoutPanel = new DockLayoutPanel(Unit.EM);
		rootPanel.add(dockLayoutPanel, 10, 10);
		dockLayoutPanel.setSize("1004px", "748px");
		
		MenuBar MBarMain = new MenuBar(false);
		dockLayoutPanel.addNorth(MBarMain, 2.0);
		MenuBar SubMSearch = new MenuBar(true);
		
		MenuItem mntmSearch = new MenuItem("Search", false, SubMSearch);
		
		MenuItem mntmProject = new MenuItem("By Project", false, (Command) null);
		SubMSearch.addItem(mntmProject);
		
		MenuItem mntmLocation = new MenuItem("By Location", false, (Command) null);
		SubMSearch.addItem(mntmLocation);
		
		MenuItem mntmVariable = new MenuItem("By Variable", false, (Command) null);
		SubMSearch.addItem(mntmVariable);
		MBarMain.addItem(mntmSearch);
		MenuBar SubMVisual = new MenuBar(true);
		
		MenuItem mntmVisualize = new MenuItem("Help", false, SubMVisual);
		
		MenuItem mntmHowToUse = new MenuItem("How to use this facility", false, (Command) null);
		SubMVisual.addItem(mntmHowToUse);
		
		MenuItem mntmDictionary = new MenuItem("Data dictionary", false, (Command) null);
		mntmDictionary.setHTML("Data dictionary");
		SubMVisual.addItem(mntmDictionary);
		
		MenuItem mntmContactSupport = new MenuItem("Contact support", false, new Command() {
			public void execute() {
				DiaComment.center();
				DiaComment.show();
			}
		});
		mntmContactSupport.setEnabled(false);
		SubMVisual.addItem(mntmContactSupport);
		mntmVisualize.setHTML("Help");
		MBarMain.addItem(mntmVisualize);
		
		MenuItem mntmAbout = new MenuItem("About", false, new Command() {
			public void execute() {
				DiaAbout.center();
				DiaAbout.show();
			}
		});
		MBarMain.addItem(mntmAbout);
		
		toolbar mytb = new toolbar();
		dockLayoutPanel.addNorth(mytb, 6.0);
		
		DecoratorPanel decoratorPanel = new DecoratorPanel();
		dockLayoutPanel.addSouth(decoratorPanel, 3.0);
		
		Label lblStatus = new Label("Status Message goes here");
		lblStatus.setWordWrap(false);
		lblStatus.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
		decoratorPanel.setWidget(lblStatus);
		lblStatus.setSize("983px", "24px");
	}
}
