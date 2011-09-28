package org.irri.households.client;

import org.irri.households.client.ui.SiteMap;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DeckPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;


public class Fhs_CountryList extends Composite {	
	private final String ProjDetailsSql = "SELECT LEFT(s.site_id,2) iso2, s.country, s.project_id, p.proj_title, p.prime_researcher, "+
			"GROUP_CONCAT(DISTINCT CONCAT_WS(', ', s.province, s.district, s.village) SEPARATOR  '_'), "+
			"GROUP_CONCAT(DISTINCT CONVERT(s.survey_year, CHAR(4)) ORDER BY s.survey_year DESC SEPARATOR ', '), SUM(s.samplesize) "+
			"FROM surveys s INNER JOIN projects p ON s.project_id = p.project_id"; 
	
	private final String ProjVarsSql2 = "SELECT r.report_title, GROUP_CONCAT(DISTINCT f.repvariables ORDER BY sort SEPARATOR ';'), " +
			"GROUP_CONCAT(DISTINCT f.description ORDER BY sort SEPARATOR ';'), r.table_name " +
			"FROM reports r, repfields f, report_fields rf, available a left join surveys on a.site=substring(surveys.site_id,1,8)" +
			"WHERE a.report_id=r.report_id and r.report_id=rf.report_id and rf.field_id=f.field_id AND ";
	
	public VerticalPanel CntrySearchVPanel;
	public ListBox ListBoxCountry;
	private Button LocListBrowseBtn;
	public VerticalPanel VPCntryDetails;
	public Label name;
	private int labci = 0;
	private ScrollPanel scrollPanel2;
	private SiteMap siteMap;
	private Loc_Result LocResult;
	private DeckPanel DeckLinkPanel;
	private HorizontalPanel horizontalPanelSiteMap;

	public Fhs_CountryList() {		
		CntrySearchVPanel = new VerticalPanel();
		CntrySearchVPanel.setStyleName("FHS-SimplePanelProjList");
		CntrySearchVPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		CntrySearchVPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		
		siteMap = new SiteMap();
		siteMap.setStyleName("FHS-TablesListBox");
		
		HorizontalPanel CntrySearchHPanel1 = new HorizontalPanel();
		CntrySearchHPanel1.setSpacing(2);
		CntrySearchVPanel.add(CntrySearchHPanel1);
		
		VPCntryDetails = new VerticalPanel();
		VPCntryDetails.setStyleName("FHS-MarkerInfoScrollMargin");
		VPCntryDetails.setSpacing(2);
		
		LocListBrowseBtn = new Button("BROWSE DATA");
		LocListBrowseBtn.setStyleName("FHS-ButtonBrowseData");
		ListBoxCountry = new ListBox();
		ListBoxCountry.setStyleName("FHS-TablesListBox");
		CntrySearchHPanel1.add(ListBoxCountry);
		ListBoxCountry.setSize("298px", "270px");
		ListBoxCountry.setVisibleItemCount(5);
		ListBoxCountry.addChangeHandler(new ChangeHandler() {
			@Override
			public void onChange(ChangeEvent event) {				
				String SelectedCountry = ListBoxCountry.getValue(ListBoxCountry.getSelectedIndex());
				String projdetailssql = "";
				projdetailssql = ProjDetailsSql + CntryDetailsSqlWhereClause(SelectedCountry);
				displayCntryDetails(projdetailssql + " GROUP BY 3");
			}
		});
		
		SimplePanel simplePanel = new SimplePanel();
		simplePanel.setStyleName("FHS-TablesListBox");
		CntrySearchHPanel1.add(simplePanel);
		
		scrollPanel2 = new ScrollPanel();
		scrollPanel2.setTouchScrollingDisabled(false);
		simplePanel.setWidget(scrollPanel2);		
		scrollPanel2.setSize("500px", "266px");
		
		scrollPanel2.setWidget(VPCntryDetails);
		siteMap.setLinkPanel(VPCntryDetails);
		
		horizontalPanelSiteMap = new HorizontalPanel();
		horizontalPanelSiteMap.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		horizontalPanelSiteMap.setStyleName("FHS-HorizontalPanelSiteMap");
		siteMap.setSize("798px", "300px");
		horizontalPanelSiteMap.add(siteMap);
		
		CntrySearchVPanel.add(horizontalPanelSiteMap);
		
		siteMap.SetSiteMapBrowseBtn(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {				
				String SelectedCountry = siteMap.id;
				LocResult = new Loc_Result(SelectedCountry);
				DeckLinkPanel.add(LocResult); //widget 4 - by location result button
				String projvarssql = "";
				projvarssql = ProjVarsSql2 + ProjVarsSqlWhereClause3(SelectedCountry);
				displayCountryTables(projvarssql + " GROUP BY r.report_id", SelectedCountry);
			}
		});
		
		initWidget(CntrySearchVPanel);
		populateListBox("SELECT s.country, LEFT(s.site_id,2) FROM surveys s GROUP BY 1 ASC;");
	}
	
	public void populateListBox(String query){
		final AsyncCallback<String[][]> populate = new AsyncCallback<String[][]>() {
			
			@Override
			public void onSuccess(String[][] result) {
                            ListBoxCountry.clear();
                            try{
                                for (int i = 1;i<result.length;i++){
                                    ListBoxCountry.addItem(result[i][labci]/*,result[i][valci]*/);
                                }
                            }
                            catch(Exception e){
                                System.err.println(e);
                            }                        
			}
			
			@Override
			public void onFailure(Throwable caught) {
				 throw new UnsupportedOperationException("Not supported yet.");
			}			
		};
		UtilsRPC.getService("mysqlservice").RunSELECT(query, populate);
	}
	
	private String CntryDetailsSqlWhereClause(String cntry){
        String whenclause = "";
        whenclause = " WHERE s.country = '"+cntry+"'";
        return whenclause;
    }
	
	public void displayCntryDetails(String sql){
		VPCntryDetails.clear();
		final HTML CntryDetails = new HTML();
        final AsyncCallback<String[][]> FetchDetails = new AsyncCallback<String[][]>() {

            public void onFailure(Throwable caught) {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            public void onSuccess(String[][] result) {
            	String html = "";
            	for (int i = 1; i < result.length; i++) {
                	html =  html + "<p><b>Project</b>: "+ result[i][3] + "</p>" +
                			"<p><b>Primary Researcher(s)</b>: " + result[i][4] + "</p>" +
                			"<p><b>Study Year(s)</b>: " + result[i][6] + "</p>" +
                            "<p><b>Study Site(s)</b>: "+ Utils.delimStringToHTMLList(result[i][5], "_", "ul") + "</p>"+
                            "<p><b>Respondents</b>: "+ result[i][7] + "</p><br>";
				}
                CntryDetails.setHTML(html);
                VPCntryDetails.add(CntryDetails);
                VPCntryDetails.add(LocListBrowseBtn);
                
            }
        };
        UtilsRPC.getService("mysqlservice").RunSELECT(sql, FetchDetails);
    }

	public void SetLocListBrowseBtn(ClickHandler click){
		LocListBrowseBtn.addClickHandler(click);
	}
	
	public void SetDeckLinkPanel(DeckPanel panel){
		DeckLinkPanel = panel;
	}
	
	private String ProjVarsSqlWhereClause3(String cntry){
        String whereclause = "";
        whereclause = " country='" +cntry+ "'";
        return whereclause;
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
                	DeckLinkPanel.showWidget(DeckLinkPanel.getWidgetCount()-1);
            }
        };
        UtilsRPC.getService("mysqlservice").RunSELECT(sql, FetchDetails);
    }
}
