package org.irri.households.client;


import java.util.ArrayList;
import java.util.List;
import org.irri.households.client.ui.SiteMap;
import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.cellview.client.CellList.Style;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.view.client.ProvidesKey;
import com.google.gwt.view.client.SingleSelectionModel;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DeckPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.SimplePanel;


public class Fhs_SlidingProjectList extends Composite {
	
	private final String ProjDetailsSql =
			"SELECT LEFT(s.site_id,2) iso2, s.country, s.project_id, p.proj_title, p.prime_researcher, "+
			"GROUP_CONCAT(DISTINCT CONCAT_WS(', ', s.province, s.district, s.village) SEPARATOR  '_'), "+
			"GROUP_CONCAT(DISTINCT CONVERT(s.survey_year, CHAR(4)) ORDER BY s.survey_year DESC SEPARATOR ',')," +
			"SUM(s.samplesize), p.purpose "+
			"FROM surveys s INNER JOIN projects p ON s.project_id = p.project_id"; 
	
	public VerticalPanel VPProjDetails;
	public Button ProjBrowseBtn;
	public SimplePanel ProjLinkPanel;
	public SingleSelectionModel<ProjectInfo> selectionModel;
	private HorizontalPanel ProjSearchHPanel;
	private DeckPanel deckPanel;
	private ScrollPanel scrollPanel2;
	public int SelectedProjID;


	public Fhs_SlidingProjectList() {
		SelectedProjID = 5;	
		utilsrpc("SELECT p.project_id, p.proj_title FROM projects p INNER JOIN surveys s ON p.project_id=s.project_id GROUP BY 1");
		
		ProjSearchHPanel = new HorizontalPanel();
		
		displayProjDetails(ProjDetailsSql + ProjDetailsSqlWhereClause(SelectedProjID) + " GROUP BY 3", SelectedProjID);
		
		/*selectionModel = new SingleSelectionModel<ProjectInfo>(ProjectInfo.KEY_PROVIDER);
        selectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
			@Override
			public void onSelectionChange(SelectionChangeEvent event) {
				int SelectedProjID = selectionModel.getSelectedObject().getId();
				displayProjDetails(ProjDetailsSql + ProjDetailsSqlWhereClause(SelectedProjID) + " GROUP BY 3", SelectedProjID);
			}
		});*/
		
		ProjBrowseBtn = new Button("BROWSE DATA");
        ProjBrowseBtn.setSize("120px", "25px");
        ProjBrowseBtn.setStyleName("FHS-ButtonBrowseData");
    	//------------------------------------------------------------------------------
        //Right window for the details of the selected project plus the browse button below. -->End
     
        initWidget(ProjSearchHPanel);
        
        scrollPanel2 = new ScrollPanel();
        ProjSearchHPanel.add(scrollPanel2);
        scrollPanel2.setStyleName("FHS-ScrollPanel2");
        
        deckPanel = new DeckPanel();
        scrollPanel2.setWidget(deckPanel);
        
        VPProjDetails = new VerticalPanel();
        //deckPanel.add(VPProjDetails);
        VPProjDetails.setSpacing(3);
        VPProjDetails.setStyleName("FHS-VPProjDetails");
	}
	
	
	//---------------------------------------------------------------------------------------------------
	//---------------------------------------------------------------------------------------------------
	
	
	static class ProjectInfo implements Comparable<ProjectInfo>{
		@Override
		public int compareTo(ProjectInfo arg0) {
			return 0;
		}
		
		public static final ProvidesKey<ProjectInfo> KEY_PROVIDER = new ProvidesKey<ProjectInfo>() {
			public Object getKey(ProjectInfo item) {
				return item == null ? null : item.getId();
			}
		};

	    private int id;
		private String ProjTitle;
		
		public ProjectInfo(int id1, String projtitle){
			id = id1;
			ProjTitle = projtitle;
		}
		
		public int getId(){
			return id;
		}
		
		public String getProjTitle() {
		      return ProjTitle;
		}
	}
	
	static class ProjectCell extends AbstractCell<ProjectInfo>{
		@Override
		public void render(com.google.gwt.cell.client.Cell.Context context, ProjectInfo value, SafeHtmlBuilder sb) {
			sb.appendHtmlConstant("<html><head>");
			sb.appendHtmlConstant("<style type='text/css'>");
			sb.appendHtmlConstant("table.proj {table-layout:fixed}");
			sb.appendHtmlConstant("</style>");
			sb.appendHtmlConstant("</head>");
			sb.appendHtmlConstant("<body>");
			sb.appendHtmlConstant("<table class=proj; width='100%' border=0 cellpadding=5 cellspacing=0>");
			if (/*value.getId()*/context.getIndex()%2==0){
				sb.appendHtmlConstant("<td width=550 style='background-color:#F0F0F0; font-size:12px; font-color:#000000'>");  
			}else{
				sb.appendHtmlConstant("<td width=550 style='background-color:#FFFFFF; font-size:12px; font-color:#000000'>");
			}
			sb.appendEscaped(value.getProjTitle());
			sb.appendHtmlConstant("</td></tr></table>");
			sb.appendHtmlConstant("</body></html>");	
		}		
	}
	
	ProjectCell projectCell = new ProjectCell();
	
	interface MyCellListResources extends CellList.Resources { 
	    @Source({"CellList.css"}) 
	    @Override 
	    public Style cellListStyle(); 
	} 
	
	final AsyncCallback<String[][]> PopulateCellList = new AsyncCallback<String[][]>() {
        public void onSuccess(String[][] result) {
            try{
            	List<ProjectInfo> projects = new ArrayList<ProjectInfo>();
                for (int i = 1;i<result.length;i++){
                	projects.add(new ProjectInfo(Integer.parseInt(result[i][0]),result[i][1]));                	
                }
              
                selectionModel.setSelected(projects.get(0), true);
            }
            catch(Exception e){
                System.err.println(e);
            }
        }
        
        public void onFailure(Throwable caught) {
             System.out.println("Communication failed (RDV.InitRegionBox)");
        }
        
    };
    
	public void utilsrpc(String query){
    	UtilsRPC.getService("mysqlservice").RunSELECT(query, PopulateCellList);    	
    }
    

	private String ProjDetailsSqlWhereClause(int projid){
        String whenclause = "";
        whenclause = " WHERE s.project_id = " +projid;
        return whenclause;
    }
	
	public void displayProjDetails(String sql, final int SelectedProjID){
		final HTML ProjDetails = new HTML();
		final HTML ProjDetails2 = new HTML();
        final AsyncCallback<String[][]> FetchDetails = new AsyncCallback<String[][]>() {

            public void onFailure(Throwable caught) {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            public void onSuccess(String[][] result) {
            
            	String html = "";
                for (int i = 1; i < result.length; i++) {
                	html =  "<p><b>Project</b>: "+ result[i][3] + "</p>";
				}
                ProjDetails.setHTML(html);	
                VPProjDetails.add(ProjDetails);

                SiteMap siteMap = new SiteMap("SELECT DISTINCT CONCAT_WS('_', s.lat, s.long) 'LatLon', " +
        				"CONCAT_WS(', ', s.province, s.country) 'Province', " +
        		        "GROUP_CONCAT(DISTINCT CONCAT_WS(', ', IF(s.village IS NULL, '-',s.village), IF(s.district IS NULL,'-', s.district)) SEPARATOR '_') 'Villages', " +
        		        "GROUP_CONCAT(DISTINCT CONVERT(p.proj_title,CHAR) ORDER BY 1 DESC SEPARATOR '_') Project, " +
        		        "GROUP_CONCAT(DISTINCT CONVERT(s.survey_year,CHAR(4)) ORDER BY 1 DESC SEPARATOR ', '), " +
        		        "GROUP_CONCAT(DISTINCT CONVERT(IF(p.key_vars IS NULL, '-', p.key_vars),CHAR) ORDER BY p.proj_title DESC SEPARATOR '_') 'Key Variables', " +
        		        "s.project_id, " +
        		        "SUM(s.samplesize), CEIL(SUM(s.samplesize)/ 100)*5 markersize, " +
        		        "s.country id " +
        		        "FROM surveys s INNER JOIN projects p ON s.project_id = p.project_id " +
        		        "WHERE s.project_id=" +SelectedProjID+
        		        " GROUP BY 1 ORDER BY 7", "470", "300", "no");
                VPProjDetails.add(siteMap);
                
                String html2 = "";
                for (int i = 1; i < result.length; i++) {
                	html2 = "<p><b>Primary Researcher(s)</b>: " + result[i][4] + "</p>" +
                			"<p><b>Purpose</b>: "+result[i][8]+"</p>"+
                			"<p><b>Study Year(s)</b>: " + result[i][6] + "</p>" +
                            "<p><b>Households</b>: "+ result[i][7] + "</p>";
				}
                ProjDetails2.setHTML(html2);	
                VPProjDetails.add(ProjDetails2);
           
                VPProjDetails.add(ProjBrowseBtn);     
        		RootPanel.get("Loading-Message").setVisible(false);
                
            }
        };
        UtilsRPC.getService("mysqlservice").RunSELECT(sql, FetchDetails);
    }
	
	public void SetProjBrowseBtn(ClickHandler click){
		ProjBrowseBtn.addClickHandler(click);
	}
	
}
