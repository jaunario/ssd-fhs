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
	private HorizontalPanel horizontalPanelLinks;
	private PushButton pshbtnNewButton;
	private HTML htmlIRRIOrg;
	private HTML htmlWRSSite;
	private PushButton pshbtnContactUs;
	private PushButton pshbtnHelp;
	private Label lblIRRI;
	private Label lblInternationalRiceResearchInstitute;
	private Label lblFHSDCCSS;
	
	
	public void onModuleLoad() {
		RootPanel.get("Loading-Message").setVisible(true);
		
		RootLayoutPanel rootLayoutPanel = RootLayoutPanel.get();
		rootLayoutPanel.setSize("100%", "100%");
		
		mainScrollPanel = new ScrollPanel();
		rootLayoutPanel.add(mainScrollPanel);
		
		ProjCellList = new Fhs_ProjectList();
		CountryList = new Fhs_CountryList();
		VarResult = new Var_Result();
		
		verticalPanel = new VerticalPanel();
		verticalPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		mainScrollPanel.setWidget(verticalPanel);
		verticalPanel.setWidth("100%");
		verticalPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		
		
		//Links located at the top right corner of the site -->Start
		//----------------------------------------------------------
		horizontalPanelLinksContainer = new HorizontalPanel();
		horizontalPanelLinksContainer.setStyleName("FHS-HorizontalPanelContainer");
		horizontalPanelLinksContainer.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
		verticalPanel.add(horizontalPanelLinksContainer);
		horizontalPanelLinksContainer.setWidth("100%");
		
		horizontalPanelLinks = new HorizontalPanel();
		horizontalPanelLinks.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		horizontalPanelLinksContainer.add(horizontalPanelLinks);
		horizontalPanelLinks.setWidth("400px");
				
		pshbtnNewButton = new PushButton("About Us");
		pshbtnNewButton.setStyleName("FHS-AboutUsPshBtn");
		horizontalPanelLinks.add(pshbtnNewButton);
		pshbtnNewButton.setWidth("45px");
		pshbtnNewButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				AboutDialog aboutDialog = new AboutDialog();
				aboutDialog.aboutUsPopup.center();
			}
		});
		
		htmlIRRIOrg = new HTML("<a href='http://www.irri.org' target = '_blank' style=\"color:#000000;\">IRRI Homepage</a>");
		htmlIRRIOrg.setStyleName("FHS-HTMLButton");
		horizontalPanelLinks.add(htmlIRRIOrg);
		htmlIRRIOrg.setWidth("76px");
		
		htmlWRSSite = new HTML("<a href='http://50.19.190.186:8080/wrs' target = '_blank' style=\"color:#000000;\">WRS Site</a>");
		htmlWRSSite.setStyleName("FHS-HTMLButton");
		horizontalPanelLinks.add(htmlWRSSite);
		htmlWRSSite.setWidth("46px");
		
		pshbtnContactUs = new PushButton("Contact Us");
		pshbtnContactUs.setStyleName("FHS-AboutUsPshBtn");
		horizontalPanelLinks.add(pshbtnContactUs);
		pshbtnContactUs.setWidth("53px");
		pshbtnContactUs.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				ContactForm contactForm = new ContactForm();
				contactForm.PopupContactUs.center();
			}
		});
		
		pshbtnHelp = new PushButton("Help");
		horizontalPanelLinks.add(pshbtnHelp);
		pshbtnHelp.setStyleName("FHS-AboutUsPshBtn");
		pshbtnHelp.setWidth("23px");
		pshbtnHelp.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				Help help = new Help();
				help.helpBox.center();
			}
		});
		//--------------------------------------------------------
		//Links located at the top right corner of the site -->End
		
		
		//Static site labels/title --> Start
		//---------------------------------------------------------
		lblIRRI = new Label("IRRI");
		lblIRRI.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		lblIRRI.setStyleName("FHS-LblIrri");
		verticalPanel.add(lblIRRI);
		
		lblInternationalRiceResearchInstitute = new Label("International Rice Research Institute");
		lblInternationalRiceResearchInstitute.setStyleName("FHS-LblInternationalRiceResearchInstitute");
		verticalPanel.add(lblInternationalRiceResearchInstitute);
		lblInternationalRiceResearchInstitute.setSize("100%", "100%");
		
		lblFHSDCCSS = new Label("Farm Household Survey Data Center");
		lblFHSDCCSS.setStyleName("FHS-FHSDCCSS");
		verticalPanel.add(lblFHSDCCSS);
		//---------------------------------------------------------
		//Static site labels/title --> End
		
		
		//Search Menu -->Start
		//---------------------------------------------------------
		horizontalPanel = new HorizontalPanel();
		horizontalPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
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
