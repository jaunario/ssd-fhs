package org.irri.households.client;


import org.irri.households.client.ui.ContactDetails;
import org.irri.households.client.ui.SiteMap;
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
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.MenuItem;
import com.google.gwt.user.client.Command;
import com.google.gwt.view.client.SelectionChangeEvent;


public class Fhs_angelika implements EntryPoint{
	private final String ProjDetailsSql =
			"SELECT LEFT(s.site_id,2) iso2, s.country, s.project_id, p.proj_title, p.prime_researcher, "+
			"GROUP_CONCAT(DISTINCT CONCAT_WS(', ', s.province, s.district, s.village) SEPARATOR  '_'), "+
			"GROUP_CONCAT(DISTINCT CONVERT(s.survey_year, CHAR(4)) ORDER BY s.survey_year DESC SEPARATOR ',')," +
			"SUM(s.samplesize), p.purpose "+
			"FROM surveys s INNER JOIN projects p ON s.project_id = p.project_id";
	
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
			"FROM reports r, repfields f, report_fields rf, available a /*LEFT JOIN surveys ON a.site=mid(surveys.site_id,1,8)*/" +
			"WHERE a.report_id=r.report_id AND r.report_id=rf.report_id AND rf.field_id=f.field_id AND ";
	
	private Fhs_CountryList fhsCountryList;
	private Fhs_SearchBy fhsSearchBy;
	private Fhs_LandingPage fhsLandingPage;
	private Fhs_ProjectList fhsProjectList;
	private Loc_Result LocResult;
	private Proj_Result ProjResult;
	private ProjectDetails projDetails;
	private SiteMap siteMap;
	private Var_Result varResult;
	
	private DeckPanel deckPanel;
	private DockLayoutPanel dockLayoutPanel;
	private HorizontalPanel horizontalPanelLinksContainer;
	private HorizontalPanel hpAppBanner;
	private HorizontalPanel hpExternalNavigation;
	private Label label;
	private HTML htmlirriHomepage;
	private HTML htmlworldRiceStatistics;
	private Label label_1;
	private Label label_3;
	private Label lblFarmHouseholdSurvey;
	private MenuBar menuBar;
	private MenuItem mntmByLocation;
	private MenuItem mntmByProject;
	private MenuItem mntmByTable;
	private MenuItem mntmContactUs;
	private MenuItem mntmHome;
	private MenuItem mntmNewMenu;
	private RootLayoutPanel rootLayoutPanel;
	private VerticalPanel vpAppBanner;
	private VerticalPanel vpExNavFHHSDC;
	private VerticalPanel vpIRRIBanner;
	private Label label_2;
	private Label label_4;
	

	public void onModuleLoad() {
		
		/*RootPanel rootPanel = RootPanel.get("Loading-Message"); /*This is for the "loading..." message that 
		 * appears at the upper left corner of the window when the site is loading. though this part still
		 * needs to be improved because sometimes it is not that accurate. The loading message indicator
		 * part in Fhs_angelika.html is what this part refers to.*/
		
		rootLayoutPanel = RootLayoutPanel.get();
		rootLayoutPanel.setStyleName("FHS-RootHome");
		
		dockLayoutPanel = new DockLayoutPanel(Unit.PX);
		rootLayoutPanel.add(dockLayoutPanel);
		dockLayoutPanel.setSize("100%", "100%");
		dockLayoutPanel.getElement().getStyle().setPosition(Position.RELATIVE);
		
		horizontalPanelLinksContainer = new HorizontalPanel();
		horizontalPanelLinksContainer.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		dockLayoutPanel.addNorth(horizontalPanelLinksContainer, 80.0);
		horizontalPanelLinksContainer.setStyleName("banner");
		horizontalPanelLinksContainer.setSize("100%", "100%");
		
		vpIRRIBanner = new VerticalPanel();
		vpIRRIBanner.setVerticalAlignment(HasVerticalAlignment.ALIGN_BOTTOM);
		horizontalPanelLinksContainer.add(vpIRRIBanner);
		vpIRRIBanner.setSize("100%", "100%");
		
		label_2 = new Label("IRRI");
		label_2.setStyleName("gwt-Label-logo");
		vpIRRIBanner.add(label_2);
		label_2.setSize("100%", "54px");
		
		label_4 = new Label("International Rice Research Institute");
		label_4.setStyleName("gwt-Label-fullname");
		vpIRRIBanner.add(label_4);
		
		label = new Label("IRRI");
		label.setStyleName("gwt-Label-logo");
		label.setSize("100%", "54px");
		
		label_1 = new Label("International Rice Research Institute");
		label_1.setStyleName("gwt-Label-fullname");
		
		vpExNavFHHSDC = new VerticalPanel();
		vpExNavFHHSDC.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
		horizontalPanelLinksContainer.add(vpExNavFHHSDC);
		vpExNavFHHSDC.setSize("100%", "100%");
		
		hpExternalNavigation = new HorizontalPanel();
		vpExNavFHHSDC.add(hpExternalNavigation);
		vpExNavFHHSDC.setCellHorizontalAlignment(hpExternalNavigation, HasHorizontalAlignment.ALIGN_RIGHT);
		hpExternalNavigation.setSpacing(5);
		
		//link for the irri website
		htmlirriHomepage = new HTML("<a href='http://www.irri.org' target = '_blank'>IRRI Home</a>");
		htmlirriHomepage.setStyleName("gwt-HTMLLink");
		hpExternalNavigation.add(htmlirriHomepage);
		hpExternalNavigation.setCellHorizontalAlignment(htmlirriHomepage, HasHorizontalAlignment.ALIGN_CENTER);
		htmlirriHomepage.setSize("125px", "15px");
		
		//link for the wrs site
		htmlworldRiceStatistics = new HTML("<a href=\"http://ricestat.irri.org:8080/wrs_manila\">World Rice Statistics</a>");
		htmlworldRiceStatistics.setStyleName("gwt-HTML-Link");
		hpExternalNavigation.add(htmlworldRiceStatistics);
		hpExternalNavigation.setCellVerticalAlignment(htmlworldRiceStatistics, HasVerticalAlignment.ALIGN_MIDDLE);
		hpExternalNavigation.setCellHorizontalAlignment(htmlworldRiceStatistics, HasHorizontalAlignment.ALIGN_CENTER);
		htmlworldRiceStatistics.setSize("125px", "15px");
		
		vpAppBanner = new VerticalPanel();
		vpExNavFHHSDC.add(vpAppBanner);
		vpAppBanner.setHeight("100%");
		vpAppBanner.setVisible(false);
		
		hpAppBanner = new HorizontalPanel();
		vpAppBanner.add(hpAppBanner);
		
		lblFarmHouseholdSurvey = new Label("Farm Household Survey");
		lblFarmHouseholdSurvey.setStyleName("gwt-Label-title");
		lblFarmHouseholdSurvey.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
		hpAppBanner.add(lblFarmHouseholdSurvey);
		
		label_3 = new Label("[BETA]");
		label_3.setStyleName("gwt-Label-version");
		hpAppBanner.add(label_3);
		label_3.setHeight("22px");
		
		menuBar = new MenuBar(false);
		menuBar.setAutoOpen(true);
		menuBar.setAnimationEnabled(true);
		dockLayoutPanel.addNorth(menuBar, 35.0);
		MenuBar menuBar_1 = new MenuBar(true);
		
		mntmNewMenu = new MenuItem("New menu", false, menuBar_1);
		
		HorizontalPanel horizontalPanel = new HorizontalPanel();
		dockLayoutPanel.addSouth(horizontalPanel, 25.0);
		horizontalPanel.setSize("100%", "100%");
		
		Label label_5 = new Label("Ready.");
		label_5.setStyleName("status");
		horizontalPanel.add(label_5);
		label_5.setSize("50%", "14px");
		
		deckPanel = new DeckPanel();
		dockLayoutPanel.add(deckPanel);
		deckPanel.setSize("100%", "100%");
		fhsLandingPage = new Fhs_LandingPage();
		deckPanel.add(fhsLandingPage);
		fhsLandingPage.setSize("100%", "100%");
		
		fhsSearchBy = new Fhs_SearchBy();
		
		fhsProjectList = new Fhs_ProjectList();
		fhsCountryList = new Fhs_CountryList();
		varResult = new Var_Result();
		fhsSearchBy.deckPanel.add(fhsProjectList);
		fhsSearchBy.deckPanel.add(fhsCountryList);
		fhsSearchBy.deckPanel.add(varResult);
		
		siteMap = new SiteMap("SELECT DISTINCT CONCAT_WS('_', s.lat, s.long) 'LatLon', " +
				"CONCAT_WS(', ', s.province, s.country) 'Province', " +
		        "GROUP_CONCAT(DISTINCT CONCAT_WS(', ', IF(s.village IS NULL, '-',s.village), IF(s.district IS NULL,'-', s.district)) SEPARATOR '_') 'Villages', " +
		        "GROUP_CONCAT(DISTINCT CONVERT(p.proj_title,CHAR) ORDER BY 1 DESC SEPARATOR '_') Project, " +
		        "GROUP_CONCAT(DISTINCT CONVERT(s.survey_year,CHAR(4)) ORDER BY 1 DESC SEPARATOR ', '), " +
		        "GROUP_CONCAT(DISTINCT CONVERT(IF(p.key_vars IS NULL, '-', p.key_vars),CHAR) ORDER BY p.proj_title DESC SEPARATOR '_') 'Key Variables', " +
		        "s.project_id, " +
		        "SUM(s.samplesize), CEIL(SUM(s.samplesize)/ 100)*5 markersize, " +
		        "s.country id " +
		        "FROM surveys s INNER JOIN projects p ON s.project_id = p.project_id " +
		        "GROUP BY 1 ORDER BY 7", "867", "425", "yes"); /*yes means that the markers are clickable like in the search data by loacation, 
		         												when you click a marker in the map, the projects for that place are show*/
		
		deckPanel.add(fhsSearchBy);
		fhsSearchBy.setSize("100%", "100%");
		deckPanel.showWidget(0);
		
		Command projCommand = new Command(){ 
		    public void execute(){ //executed when Search Data - By Project is clicked. Shows list of projects as well as details on each project
		    	RootPanel.get("Loading-Message").setVisible(true); //setvisible=true orders the loading meassage to appear
		    	
		    	vpAppBanner.setVisible(true);
		    	
		    	deckPanel.showWidget(deckPanel.getWidgetCount()-1);
				fhsSearchBy.htmlProjSearch = new HTML("<p><b>How to Search By Project</b><p>\n\n<p>The process of retrieving data from this facility is sequential. Please follow the steps below to avoid errors.\n\n<p>\n<ol>\n<li>Select a project.</li><br>\n\n<li>Click the Browse Data button located at the bottom of the details window.</li><br>\n\n<li>Select a table and wait for the other windows to load.</li><br>\n\n<li>You may uncheck some variables and/or choose only specific years and/or countries, respectively. Just make sure to wait for the windows to refresh in between clicks.</li><br>\n\n\n\n<li>Navigate through the table by using the First, Previous, Next, and Last buttons. Click the Clear button to reset your selection.</li><br><li>Click Download Data to retrieve a csv file. It may take a few seconds for the downloaded file to appear.</li><br>\n</ol>\n\n", true);
				fhsSearchBy.verticalPanel.clear();
				fhsSearchBy.verticalPanel.add(fhsSearchBy.htmlProjSearch);
				fhsSearchBy.deckPanel.showWidget(0);
		    	fhsProjectList.verticalPanel2.clear();
				int SelectedProjID = 1;
				fhsProjectList.selectionModel.setSelected(fhsProjectList.projects.get(0), true);
				fhsProjectList.projDetails = new ProjectDetails(ProjDetailsSql + ProjDetailsSqlWhereClause(SelectedProjID) + " GROUP BY 3", SelectedProjID);
				fhsProjectList.projDetails.SetProjBrowseBtn(new ClickHandler() {
					public void onClick(ClickEvent event) { 
						//executed when the search by project's browse data button is clicked. this is for the project which is first on the list
						if (fhsSearchBy.deckPanel.getWidgetCount()>3){ //controls the number of widgets in the fhsHome.deckPanel so it does not pile up thus containing only 3 widgets (index 0:search by proj, 1:search by loc, 2:search by table) up to this point.	
							fhsSearchBy.deckPanel.remove(fhsSearchBy.deckPanel.getWidgetCount()-1); 
						}	
						int SelectedProjID = fhsProjectList.selectionModel.getSelectedObject().getId();
						String SelectedProjTitle = fhsProjectList.selectionModel.getSelectedObject().getProjTitle();
						ProjResult = new Proj_Result(SelectedProjID);
						String projvarssql = "";
						projvarssql = ProjVarsSql + ProjVarsSqlWhereProjIdClause(SelectedProjID);
						displayProjTables(projvarssql + " GROUP BY r.report_id", SelectedProjTitle);
						fhsSearchBy.deckPanel.add(ProjResult);
						fhsSearchBy.deckPanel.showWidget(fhsSearchBy.deckPanel.getWidgetCount()-1);
						deckPanel.showWidget(1);
						RootPanel.get("Loading-Message").setVisible(false); //setvisible=false orders the loading message to hide
					}
				});
				fhsProjectList.verticalPanel2.add(fhsProjectList.projDetails);
		    }
		};
		
		Command locCommand = new Command(){
		    public void execute(){ //executed when user clicked on Search Data - By Location. shows list and markers of countries.
		    	RootPanel.get("Loading-Message").setVisible(true);
		    	vpAppBanner.setVisible(true);
		    	
		    	fhsCountryList.horizontalPanelSiteMap.add(siteMap);
		    	siteMap.setLinkPanel(fhsCountryList.VPCntryDetails);
		    	fhsSearchBy.setPixelSize(deckPanel.getOffsetWidth(), deckPanel.getOffsetHeight());
		    	deckPanel.showWidget(deckPanel.getWidgetCount()-1); //show fhsHome in deckPanel
		    	fhsSearchBy.htmlCntrySearch = new HTML("<p><b>How to Search By Country</b><p>\n\n<p>The process of retrieving data from this facility is sequential. Please follow the steps below to avoid errors.\n\n<p>\n<ol>\n<li>Select a country either from the list or from the map.</li><br>\n\n<li>Click the Browse Data button located at the bottom of the details window.</li><br>\n\n<li>Select a table and wait for the other windows to load.</li><br>\n\n<li>You may uncheck some variables and/or choose only specific years, respectively. Just make sure to wait for the windows to refresh in between clicks.</li><br>\n\n<li>Navigate through the table by using the First, Previous, Next, and Last buttons. Click the Clear button to reset your selection.</li><br>\n\n<li>Click Download Data to retrieve a csv file. It may take a few seconds for the downloaded file to appear.</li><br>\n</ol>\n\n", true);
		    	fhsSearchBy.verticalPanel.clear();
		    	fhsSearchBy.verticalPanel.add(fhsSearchBy.htmlCntrySearch);
		    	fhsSearchBy.deckPanel.showWidget(1);
		    	RootPanel.get("Loading-Message").setVisible(false);
		    }
		};
		
		siteMap.SetSiteMapBrowseBtn(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {		
				//This is what happens when the search by location's site map marker's browse data button is clicked.
				RootPanel.get("Loading-Message").setVisible(true);
				if (fhsSearchBy.deckPanel.getWidgetCount()>3){ //controls the number of widgets in the fhsHome.deckPanel so it does not pile up thus containing only 3 widgets (index 0:search by proj, 1:search by loc, 2:search by table) up to this point.
					fhsSearchBy.deckPanel.remove(fhsSearchBy.deckPanel.getWidgetCount()-1);
				}
				String SelectedCountry = siteMap.id;
				LocResult = new Loc_Result(SelectedCountry);
				String projvarssql = "";
				projvarssql = ProjVarsSql2 + ProjVarsSqlWhereCountryClause(SelectedCountry);
				displayCountryTables(projvarssql + " GROUP BY r.report_id", SelectedCountry);
				fhsSearchBy.deckPanel.add(LocResult);
				fhsSearchBy.deckPanel.showWidget(fhsSearchBy.deckPanel.getWidgetCount()-1);	
				deckPanel.showWidget(1);
				RootPanel.get("Loading-Message").setVisible(false);
			}
		});
		
		Command tabCommand = new Command(){ //shows the search by table view
		    public void execute(){
		    	vpAppBanner.setVisible(true);
		    	
		    	deckPanel.showWidget(deckPanel.getWidgetCount()-1); //show fhs_home
				fhsSearchBy.htmlTableSearch = new HTML("<p><b>How to Search By Table</b><p>\n\n<p>The process of retrieving data from this facility is sequential. Please follow the steps below to avoid errors.\n\n<p>\n<ol>\n<li>Select a table and wait for the other windows to load.</li><br>\n\n<li>You may uncheck some variables and/or choose only specific years and/or countries, respectively. Just make sure to wait for the windows to refresh in between clicks.</li><br>\n\n<li>Navigate through the table by using the First, Previous, Next, and Last buttons. Click the Clear button to reset your selection.</li><br>\n\n<li>Click Download Data to retrieve a csv file. It may take a few seconds for the downloaded file to appear.</li><br>\n</ol>\n\n", true);
				fhsSearchBy.verticalPanel.clear();
				fhsSearchBy.verticalPanel.add(fhsSearchBy.htmlTableSearch);
				fhsSearchBy.deckPanel.showWidget(2);
		    }
		};
		
		Command homeCommand = new Command(){ //goes back to the home page
		    public void execute(){
		    	vpAppBanner.setVisible(false);
		    	deckPanel.showWidget(0);
		        RootPanel.get("Loading-Message").setVisible(false);
		    }
		};
		
		Command contactCommand = new Command(){ //popup of contact details of ssd.ricestat
		    public void execute(){
		    	//ContactForm contactForm = new ContactForm();
		    	ContactDetails contactDetails = new ContactDetails();
		    	//contactForm.PopupContactUs.center();
		    	contactDetails.PopupContactUs.center();
		    }
		};
		
		mntmHome = new MenuItem("Home", homeCommand);
		menuBar.addItem(mntmHome);
		
		mntmByProject = new MenuItem("By Project", projCommand);
		menuBar_1.addItem(mntmByProject);
		
		mntmByLocation = new MenuItem("By Location", locCommand);
		menuBar_1.addItem(mntmByLocation);
		
		mntmByTable = new MenuItem("By Table", tabCommand);
		menuBar_1.addItem(mntmByTable);
		mntmNewMenu.setHTML("Search Data");
		menuBar.addItem(mntmNewMenu);
		
		mntmContactUs = new MenuItem("Contact Us", contactCommand);
		menuBar.addItem(mntmContactUs);
		
		
		
		projDetails = new ProjectDetails(ProjDetailsSql + ProjDetailsSqlWhereClause(12) + " GROUP BY 3", 12); //12 is the project_id of the latest uploaded project that is featured in the landing page. change this every time there is a newly uploaded project
		
		fhsLandingPage.ProjProfileDeckPanel.add(projDetails);
		fhsLandingPage.ProjProfileDeckPanel.showWidget(0);
		
		fhsCountryList.SetLocListBrowseBtn(new ClickHandler() {
			public void onClick(ClickEvent event) {
				//This is what happens when the search by location's browse data button is clicked.
				RootPanel.get("Loading-Message").setVisible(true);
				if (fhsSearchBy.deckPanel.getWidgetCount()>3){ //controls the number of widgets in the fhsHome.deckPanel so it does not pile up thus containing only 3 widgets (index 0:search by proj, 1:search by loc, 2:search by table) up to this point.
					fhsSearchBy.deckPanel.remove(fhsSearchBy.deckPanel.getWidgetCount()-1);
				}
				String SelectedCountry = fhsCountryList.ListBoxCountry.getItemText(fhsCountryList.ListBoxCountry.getSelectedIndex());
				//String SelectedCountry2 = fhsCountryList.ListBoxCountry.getValue(fhsCountryList.ListBoxCountry.getSelectedIndex());
				LocResult = new Loc_Result(SelectedCountry);
				String projvarssql = "";
				projvarssql = ProjVarsSql2 + ProjVarsSqlWhereCountryClause(SelectedCountry);
				displayCountryTables(projvarssql + " GROUP BY r.report_id", SelectedCountry);
				fhsSearchBy.deckPanel.add(LocResult);
				fhsSearchBy.deckPanel.showWidget(fhsSearchBy.deckPanel.getWidgetCount()-1);	
				deckPanel.showWidget(1);
			}
		});
		
		
		
		fhsProjectList.SetSelectionModel(new SelectionChangeEvent.Handler() {
			public void onSelectionChange(SelectionChangeEvent event) {
				RootPanel.get("Loading-Message").setVisible(true);
				fhsProjectList.verticalPanel2.clear();
				int SelectedProjID = fhsProjectList.selectionModel.getSelectedObject().getId();
				fhsProjectList.projDetails = new ProjectDetails(ProjDetailsSql + ProjDetailsSqlWhereClause(SelectedProjID) + " GROUP BY 3", SelectedProjID);
				fhsProjectList.projDetails.SetProjBrowseBtn(new ClickHandler() {
					public void onClick(ClickEvent event) {
						//executed when the search by project's browse data button is clicked. this is for projects which are not first on the list
						if (fhsSearchBy.deckPanel.getWidgetCount()>3){ //controls the number of widgets in the fhsHome.deckPanel so it does not pile up thus containing only 3 widgets (index 0:search by proj, 1:search by loc, 2:search by table) up to this point.
							fhsSearchBy.deckPanel.remove(fhsSearchBy.deckPanel.getWidgetCount()-1);
						}	
						int SelectedProjID = fhsProjectList.selectionModel.getSelectedObject().getId();
						String SelectedProjTitle = fhsProjectList.selectionModel.getSelectedObject().getProjTitle();
						ProjResult = new Proj_Result(SelectedProjID);
						String projvarssql = "";
						projvarssql = ProjVarsSql + ProjVarsSqlWhereProjIdClause(SelectedProjID);
						displayProjTables(projvarssql + " GROUP BY r.report_id", SelectedProjTitle);
						fhsSearchBy.deckPanel.add(ProjResult);
						fhsSearchBy.deckPanel.showWidget(fhsSearchBy.deckPanel.getWidgetCount()-1);
						deckPanel.showWidget(1);
					}
				});
				fhsProjectList.verticalPanel2.add(fhsProjectList.projDetails);
			}
		});		
		
		projDetails.SetProjBrowseBtn(new ClickHandler() {
			public void onClick(ClickEvent event) { //executed when the browse button of the featured proj in the landing page is clicked
				if (fhsSearchBy.deckPanel.getWidgetCount()>3){
					fhsSearchBy.deckPanel.remove(fhsSearchBy.deckPanel.getWidgetCount()-1);
				}	
				int SelectedProjID = 12; //project id of the featured project
				String SelectedProjTitle = "Social Capital Assessment Tool (SCAT) Household Survey"; //title of the featured project
				ProjResult = new Proj_Result(SelectedProjID);
				String projvarssql = "";
				projvarssql = ProjVarsSql + ProjVarsSqlWhereProjIdClause(SelectedProjID);
				displayProjTables(projvarssql + " GROUP BY r.report_id", SelectedProjTitle);
				fhsSearchBy.deckPanel.add(ProjResult);
				fhsSearchBy.deckPanel.showWidget(fhsSearchBy.deckPanel.getWidgetCount()-1);
				deckPanel.showWidget(1);
				RootPanel.get("Loading-Message").setVisible(false);
			}
		});	
	}
	
	private String ProjVarsSqlWhereProjIdClause(int projid){
		String whereclause = "";
	    whereclause = " a.project_id = "+projid;
	    return whereclause;
	}
	
	public void displayProjTables(String sql, final String projtitle){
		ProjResult.TablesListBox.clear();
		ProjResult.FilterByYear.clear();
		ProjResult.FilterByCountry.clear();
		final AsyncCallback<String[][]> FetchDetails = new AsyncCallback<String[][]>() {
			public void onFailure(Throwable caught) {
				throw new UnsupportedOperationException("Not supported yet.");
			}
			public void onSuccess(String[][] result) { //displays the list of available tables for the selected project 
				ProjResult.TablesListBox.setTitle("Project Selected: "+projtitle);
				for (int i = 1; i < result.length; i++) {
					ProjResult.TablesListBox.addItem(result[i][0], result[i][3]);
				}
			}
		};
		UtilsRPC.getService("mysqlservice").RunSELECT(sql, FetchDetails);
	}
	
	private String ProjVarsSqlWhereCountryClause(String cntry){
        String whereclause = "";
        //whereclause = " country='"+cntry+"'";
        whereclause = " left(a.site,2) in (SELECT LEFT(site_id,2) FROM surveys where country = '"+cntry+"')";
        return whereclause;
    }
	
	public void displayCountryTables(String sql, final String cntry){
        final AsyncCallback<String[][]> FetchDetails = new AsyncCallback<String[][]>() {
            public void onFailure(Throwable caught) {
                throw new UnsupportedOperationException("Not supported yet.");
            }
            public void onSuccess(String[][] result) {
            	LocResult.TablesListBox.clear();
            	LocResult.TablesListBox.setTitle("Country Selected: "+cntry);
                for (int i = 1; i < result.length; i++) { //displays the list of available tables for the selected country
                	LocResult.TablesListBox.addItem(result[i][0],result[i][3]);
				}
                RootPanel.get("Loading-Message").setVisible(false);
            }
        };
        UtilsRPC.getService("mysqlservice").RunSELECT(sql, FetchDetails);
    }
	
	private String ProjDetailsSqlWhereClause(int projid){
        String whenclause = "";
        whenclause = " WHERE s.project_id = " +projid;
        return whenclause;
    }	
}
