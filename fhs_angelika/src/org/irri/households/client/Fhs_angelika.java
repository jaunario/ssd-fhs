package org.irri.households.client;


import org.irri.households.client.ui.AboutDialog;
import org.irri.households.client.ui.ContactForm;
import org.irri.households.client.ui.Help;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.DeckPanel;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.MenuItem;
import com.google.gwt.user.client.Command;


public class Fhs_angelika implements EntryPoint {
	private final String ProjVarsSql = 
			"SELECT r.report_title," +
			"GROUP_CONCAT(DISTINCT f.repvariables ORDER BY sort SEPARATOR ';'), " +
			"GROUP_CONCAT(DISTINCT f.description ORDER BY sort SEPARATOR ';')," +
			"r.table_name " +
			"FROM reports r, repfields f, report_fields rf, available a " +
			"WHERE a.report_id=r.report_id AND r.report_id=rf.report_id AND rf.field_id=f.field_id AND ";
	
	private final String ProjVarsSql2 = 
			"SELECT r.report_title," +
			"GROUP_CONCAT(DISTINCT f.repvariables ORDER BY sort SEPARATOR ';'), " +
			"GROUP_CONCAT(DISTINCT f.description ORDER BY sort SEPARATOR ';')," +
			"r.table_name " +
			"FROM reports r, repfields f, report_fields rf, available a LEFT JOIN surveys ON a.site=substring(surveys.site_id,1,8)" +
			"WHERE a.report_id=r.report_id AND r.report_id=rf.report_id AND rf.field_id=f.field_id AND ";
	
	private ScrollPanel mainScrollPanel;
	private VerticalPanel verticalPanel;
	private Fhs_ProjectList ProjCellList;
	private Proj_Result ProjResult;
	private Fhs_CountryList CountryList;
	private Loc_Result LocResult;
	private Var_Result VarResult;
	private DeckPanel ProjSearchDeckPanel;
	private HorizontalPanel horizontalPanel;
	private PushButton pshbtnSearchByProject;
	private PushButton pshbtnSearchByLocation;
	private PushButton pshbtnSearchByVariable;
	private HorizontalPanel horizontalPanelLinksContainer;
	private PushButton pshbtnNewButton;
	private PushButton pshbtnContactUs;
	private PushButton pshbtnHelp;
	private DockLayoutPanel dockLayoutPanel;
	private VerticalPanel verticalPanel_2;
	private Label label;
	private Label label_1;
	private HorizontalPanel horizontalPanel_1;
	private HorizontalPanel horizontalPanel_2;
	private Label lblFarmHouseholdSurvey;
	private Label label_3;
	private HorizontalPanel horizontalPanel_3;
	private HTML html;
	private HTML htmlworldRiceStatistics;
	private MenuBar menuBar;
	private MenuItem mntmNewMenu;
	private MenuItem mntmByProject;
	private MenuItem mntmByLocation;
	private MenuItem mntmByTable;
	private MenuItem mntmHelp;
	private MenuItem mntmHowTo;
	private MenuItem mntmContactUs;
	private MenuItem mntmAbout;
	
	
	public void onModuleLoad() {
		//RootPanel.get("Loading-Message").setVisible(true);
		
		RootLayoutPanel rootLayoutPanel = RootLayoutPanel.get();
		rootLayoutPanel.setSize("100%", "100%");
		CountryList = new Fhs_CountryList();
		VarResult = new Var_Result();
		
		dockLayoutPanel = new DockLayoutPanel(Unit.PX);
		dockLayoutPanel.getElement().getStyle().setPosition(Position.RELATIVE);
		rootLayoutPanel.add(dockLayoutPanel);
		rootLayoutPanel.setWidgetLeftRight(dockLayoutPanel, 90.0, Unit.PX, 90.0, Unit.PX);
		
		
		//Links located at the top right corner of the site -->Start
		//----------------------------------------------------------
		horizontalPanelLinksContainer = new HorizontalPanel();
		horizontalPanelLinksContainer.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
		dockLayoutPanel.addNorth(horizontalPanelLinksContainer, 80.0);
		horizontalPanelLinksContainer.setStyleName("banner");
		horizontalPanelLinksContainer.setSize("100%", "100%");
		
		verticalPanel_2 = new VerticalPanel();
		horizontalPanelLinksContainer.add(verticalPanel_2);
		verticalPanel_2.setSize("100%", "100%");
		
		label = new Label("IRRI");
		label.setStyleName("gwt-Label-logo");
		verticalPanel_2.add(label);
		label.setSize("100%", "54px");
		
		label_1 = new Label("International Rice Research Institute");
		label_1.setStyleName("gwt-Label-fullname");
		verticalPanel_2.add(label_1);
		
		horizontalPanel_3 = new HorizontalPanel();
		horizontalPanelLinksContainer.add(horizontalPanel_3);
		horizontalPanel_3.setSpacing(5);
		
		html = new HTML("<a href=\"http://www.irri.org\">IRRI Home</a>", true);
		html.setStyleName("gwt-HTML-Link");
		horizontalPanel_3.add(html);
		horizontalPanel_3.setCellHorizontalAlignment(html, HasHorizontalAlignment.ALIGN_CENTER);
		html.setSize("73px", "15px");
		
		htmlworldRiceStatistics = new HTML("<a href=\"http://50.19.190.186:8080/wrs\">World Rice Statistics</a>", true);
		htmlworldRiceStatistics.setStyleName("gwt-HTML-Link");
		horizontalPanel_3.add(htmlworldRiceStatistics);
		horizontalPanel_3.setCellHorizontalAlignment(htmlworldRiceStatistics, HasHorizontalAlignment.ALIGN_CENTER);
		htmlworldRiceStatistics.setSize("150px", "15px");
		
		horizontalPanel_1 = new HorizontalPanel();
		dockLayoutPanel.addNorth(horizontalPanel_1, 40.0);
		horizontalPanel_1.setSize("100%", "100%");
		
		horizontalPanel_2 = new HorizontalPanel();
		horizontalPanel_1.add(horizontalPanel_2);
		
		lblFarmHouseholdSurvey = new Label("Farm Household Survey Data Center");
		lblFarmHouseholdSurvey.setStyleName("gwt-Label-title");
		lblFarmHouseholdSurvey.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
		horizontalPanel_2.add(lblFarmHouseholdSurvey);
		
		label_3 = new Label("[BETA]");
		label_3.setStyleName("gwt-Label-version");
		horizontalPanel_2.add(label_3);
		label_3.setHeight("22px");
		
		menuBar = new MenuBar(false);
		menuBar.setAutoOpen(true);
		menuBar.setAnimationEnabled(true);
		dockLayoutPanel.addNorth(menuBar, 35.0);
		MenuBar menuBar_1 = new MenuBar(true);
		
		mntmNewMenu = new MenuItem("New menu", false, menuBar_1);
		
		mntmByProject = new MenuItem("By Project", false, (Command) null);
		menuBar_1.addItem(mntmByProject);
		
		mntmByLocation = new MenuItem("By Location", false, (Command) null);
		menuBar_1.addItem(mntmByLocation);
		
		mntmByTable = new MenuItem("By Table", false, (Command) null);
		menuBar_1.addItem(mntmByTable);
		mntmNewMenu.setHTML("Search Data");
		menuBar.addItem(mntmNewMenu);
		MenuBar menuBar_2 = new MenuBar(true);
		
		mntmHelp = new MenuItem("Help", false, menuBar_2);
		
		mntmHowTo = new MenuItem("How To", false, (Command) null);
		menuBar_2.addItem(mntmHowTo);
		
		mntmContactUs = new MenuItem("Contact Us", false, (Command) null);
		menuBar_2.addItem(mntmContactUs);
		menuBar.addItem(mntmHelp);
		
		mntmAbout = new MenuItem("About", false, (Command) null);
		menuBar.addItem(mntmAbout);
		
		mainScrollPanel = new ScrollPanel();
		dockLayoutPanel.add(mainScrollPanel);
		
		ProjCellList = new Fhs_ProjectList();
		
		verticalPanel = new VerticalPanel();
		verticalPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		mainScrollPanel.setWidget(verticalPanel);
		verticalPanel.setWidth("100%");
		verticalPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		//---------------------------------------------------------
		//Static site labels/title --> End
		
		
		//Search Menu -->Start
		//---------------------------------------------------------
		horizontalPanel = new HorizontalPanel();
		horizontalPanel.setSpacing(3);
		horizontalPanel.setStyleName("FHS-HorizontalPanel");
		verticalPanel.add(horizontalPanel);
		horizontalPanel.setWidth("350px");
		
		pshbtnSearchByProject = new PushButton("SEARCH BY PROJECT");
		pshbtnSearchByProject.setStyleName("FHS-PshBtnAboutUs");
		horizontalPanel.add(pshbtnSearchByProject);
		pshbtnSearchByProject.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				pshbtnSearchByProject.setStyleName("FHS-HighlightText");
				pshbtnSearchByLocation.setStyleName("FHS-PshBtnAboutUs");
				pshbtnSearchByVariable.setStyleName("FHS-PshBtnAboutUs");
				ProjSearchDeckPanel.clear();
				ProjSearchDeckPanel.add(ProjCellList);
				ProjSearchDeckPanel.showWidget(0);
			}
		});
		
		pshbtnSearchByLocation = new PushButton("SEARCH BY COUNTRY");
		pshbtnSearchByLocation.setStyleName("FHS-PshBtnAboutUs");
		horizontalPanel.add(pshbtnSearchByLocation);
		pshbtnSearchByLocation.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				pshbtnSearchByProject.setStyleName("FHS-PshBtnAboutUs");
				pshbtnSearchByLocation.setStyleName("FHS-HighlightText");
				pshbtnSearchByVariable.setStyleName("FHS-PshBtnAboutUs");
				ProjSearchDeckPanel.clear();
				ProjSearchDeckPanel.add(CountryList);
				ProjSearchDeckPanel.showWidget(0);
			}
		});
		
		pshbtnSearchByVariable = new PushButton("SEARCH BY TABLE");
		pshbtnSearchByVariable.setStyleName("FHS-PshBtnAboutUs");
		horizontalPanel.add(pshbtnSearchByVariable);
		pshbtnSearchByVariable.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				pshbtnSearchByProject.setStyleName("FHS-PshBtnAboutUs");
				pshbtnSearchByLocation.setStyleName("FHS-PshBtnAboutUs");
				pshbtnSearchByVariable.setStyleName("FHS-HighlightText");
				ProjSearchDeckPanel.clear();
				ProjSearchDeckPanel.add(VarResult);
				ProjSearchDeckPanel.showWidget(0);
			}
		});
		//---------------------------------------------------------
		//Search Menu -->End
		
		
		//This panel is where the action happens. It will contain the lists, filters and results available for each search type.
		//Initially, the Search By Project Button is selected, displaying the Projects List. -->Start
		//------------------------------------------------------------
		ProjSearchDeckPanel = new DeckPanel();
		ProjSearchDeckPanel.setStyleName("FHS-SimplePanelSearch");
		verticalPanel.add(ProjSearchDeckPanel);
		ProjSearchDeckPanel.setSize("814px", "602px");
		
		pshbtnSearchByProject.setStyleName("FHS-HighlightText");
		
		pshbtnHelp = new PushButton("Help");
		horizontalPanel.add(pshbtnHelp);
		pshbtnHelp.setStyleName("FHS-PshBtnAboutUs");
		pshbtnHelp.setWidth("23px");
		
		pshbtnContactUs = new PushButton("Contact Us");
		horizontalPanel.add(pshbtnContactUs);
		pshbtnContactUs.setStyleName("FHS-PshBtnAboutUs");
		pshbtnContactUs.setWidth("53px");
		
		pshbtnNewButton = new PushButton("About Us");
		horizontalPanel.add(pshbtnNewButton);
		pshbtnNewButton.setStyleName("FHS-PshBtnAboutUs");
		pshbtnNewButton.setWidth("45px");
		pshbtnNewButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				AboutDialog aboutDialog = new AboutDialog();
				aboutDialog.aboutUsPopup.center();
			}
		});
		pshbtnContactUs.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				ContactForm contactForm = new ContactForm();
				contactForm.PopupContactUs.center();
			}
		});
		pshbtnHelp.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				Help help = new Help();
				help.helpBox.center();
			}
		});
		ProjSearchDeckPanel.add(ProjCellList);
		CountryList.SetDeckLinkPanel(ProjSearchDeckPanel);
		ProjSearchDeckPanel.showWidget(0);
		//------------------------------------------------------------
		//This panel is where the action happens. It will contain the lists, filters and results available for each search type.
		//Initially, the Search By Project Button is selected, displaying the Projects List. -->Start
		
		
		//Add click handlers for Browse Data buttons of Project and Country Search.
		//No Browse Data button for Table Search -->Start
		//----------------------------------------------------
		ProjCellList.SetProjBrowseBtn(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				RootPanel.get("Loading-Message").setVisible(true);
				
				int SelectedProjID = ProjCellList.selectionModel.getSelectedObject().getId();
				String SelectedProjTitle = ProjCellList.selectionModel.getSelectedObject().getProjTitle();
				ProjResult = new Proj_Result(SelectedProjID);
				ProjSearchDeckPanel.insert(ProjResult, 1);
				String projvarssql = "";
				projvarssql = ProjVarsSql + ProjVarsSqlWhereProjIdClause(SelectedProjID);
				displayProjTables(projvarssql + " GROUP BY r.report_id", SelectedProjTitle);
			}
		});
		
		CountryList.SetLocListBrowseBtn(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				String SelectedCountry = CountryList.ListBoxCountry.getValue(CountryList.ListBoxCountry.getSelectedIndex());
				LocResult = new Loc_Result(SelectedCountry);
				ProjSearchDeckPanel.add(LocResult); //widget 4 - by location result button
				String projvarssql = "";
				projvarssql = ProjVarsSql2 + ProjVarsSqlWhereCountryClause(SelectedCountry);
				displayCountryTables(projvarssql + " GROUP BY r.report_id", SelectedCountry);
			}
		});
		//----------------------------------------------------
		//Add click handlers for Browse Data buttons of Project and Country Search.
		//No Browse Data button for Table Search -->End
	}// <-- Close brace for onModuleLoad
	
	
//-----------------------------------------------------------------------------------
//-----------------------------------------------------------------------------------
	
	
	private String ProjVarsSqlWhereProjIdClause(int projid){
        String whereclause = "";
        whereclause = " a.project_id = "+projid;
        return whereclause;
    }
	
	private String ProjVarsSqlWhereCountryClause(String cntry){
        String whereclause = "";
        whereclause = " country='"+cntry+"'";
        return whereclause;
    }
	
	public void displayProjTables(String sql, final String projtitle){
		ProjResult.TablesListBox.clear();
		ProjResult.ProjResSimplePanel.clear();
		ProjResult.FilterByYear.clear();
		ProjResult.FilterByCountry.clear();
        final AsyncCallback<String[][]> FetchDetails = new AsyncCallback<String[][]>() {
            public void onFailure(Throwable caught) {
                throw new UnsupportedOperationException("Not supported yet.");
            }
            public void onSuccess(String[][] result) {
            	ProjResult.TablesListBox.setTitle("Project Selected: "+projtitle);
                for (int i = 1; i < result.length; i++) {
                	ProjResult.TablesListBox.addItem(result[i][0], result[i][3]);
				}
                ProjSearchDeckPanel.showWidget(1);
                RootPanel.get("Loading-Message").setVisible(false);
            }
        };
        UtilsRPC.getService("mysqlservice").RunSELECT(sql, FetchDetails);
    }
	
	public void displayCountryTables(String sql, final String cntry){
		LocResult.TablesListBox.clear();
        final AsyncCallback<String[][]> FetchDetails = new AsyncCallback<String[][]>() {
            public void onFailure(Throwable caught) {
                throw new UnsupportedOperationException("Not supported yet.");
            }
            public void onSuccess(String[][] result) {
            	LocResult.TablesListBox.setTitle("Country Selected: "+cntry);
                for (int i = 1; i < result.length; i++) {
                	LocResult.TablesListBox.addItem(result[i][0], result[i][3]);
				}
                	ProjSearchDeckPanel.showWidget(ProjSearchDeckPanel.getWidgetCount()-1);
            }
        };
        UtilsRPC.getService("mysqlservice").RunSELECT(sql, FetchDetails);
    }
}
