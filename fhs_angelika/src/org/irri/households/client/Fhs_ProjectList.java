package org.irri.households.client;


import java.util.ArrayList;
import java.util.List;
import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.cellview.client.CellList.Style;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.cellview.client.HasKeyboardSelectionPolicy.KeyboardSelectionPolicy;
import com.google.gwt.view.client.ProvidesKey;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;


public class Fhs_ProjectList extends Composite {
	
	private final String ProjDetailsSql =
			"SELECT LEFT(s.site_id,2) iso2, s.country, s.project_id, p.proj_title, p.prime_researcher, "+
			"GROUP_CONCAT(DISTINCT CONCAT_WS(', ', s.province, s.district, s.village) SEPARATOR  '_'), "+
			"GROUP_CONCAT(DISTINCT CONVERT(s.survey_year, CHAR(4)) ORDER BY s.survey_year DESC SEPARATOR ',')," +
			"SUM(s.samplesize) "+
			"FROM surveys s INNER JOIN projects p ON s.project_id = p.project_id"; 
	
	private VerticalPanel VPProjDetails;
	public Button ProjBrowseBtn;
	public SimplePanel ProjLinkPanel;
	public SingleSelectionModel<ProjectInfo> selectionModel;
	private HorizontalPanel ProjSearchHPanel;
	private HorizontalPanel ProjSearchHPanel2;
	private ScrollPanel scrollPanel;
	private SimplePanel simplePanelProjList;
	private ScrollPanel scrollPanel2;


	public Fhs_ProjectList() {		
			
		utilsrpc("SELECT p.project_id, p.proj_title FROM projects p INNER JOIN surveys s ON p.project_id=s.project_id GROUP BY 1");
		
		ProjSearchHPanel = new HorizontalPanel();
		ProjSearchHPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		ProjSearchHPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		ProjSearchHPanel.setStyleName("FHS-SimplePanelProjList");
		
		ProjSearchHPanel2 = new HorizontalPanel();
		ProjSearchHPanel2.setSpacing(2);
		ProjSearchHPanel2.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		ProjSearchHPanel.add(ProjSearchHPanel2);
		
		
		//Left window for the Projects List -->Start
		//------------------------------------------------------------------------------
		cellList.setStyleName("FHS-ProjCellList");
		cellList.setSize("285px", "600px");
		cellList.setKeyboardSelectionPolicy(KeyboardSelectionPolicy.ENABLED);
		cellList.setKeyboardSelectionPolicy(KeyboardSelectionPolicy.BOUND_TO_SELECTION);
		
		scrollPanel = new ScrollPanel();
		scrollPanel.setStyleName("FHS-ScrollPanelCellList");
		scrollPanel.setSize("300px", "600px");
		scrollPanel.setWidget(cellList);
		ProjSearchHPanel2.add(scrollPanel);
		
		ProjSearchHPanel2.setCellVerticalAlignment(scrollPanel, HasVerticalAlignment.ALIGN_MIDDLE);
		ProjSearchHPanel2.setCellHorizontalAlignment(scrollPanel, HasHorizontalAlignment.ALIGN_CENTER);
		
		selectionModel = new SingleSelectionModel<ProjectInfo>(ProjectInfo.KEY_PROVIDER);
		cellList.setSelectionModel(selectionModel);
        selectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
			@Override
			public void onSelectionChange(SelectionChangeEvent event) {
				int SelectedProjID = selectionModel.getSelectedObject().getId();
				String projdetailssql = "";
				projdetailssql = ProjDetailsSql + ProjDetailsSqlWhereClause(SelectedProjID);
				displayProjDetails(projdetailssql + " GROUP BY 3");
			}
		});
		//------------------------------------------------------------------------------
		//Left window for the Projects List -->End
				
		
		//Right window for the details of the selected project plus the browse button below. -->Start
		//------------------------------------------------------------------------------
		simplePanelProjList = new SimplePanel();
		simplePanelProjList.setStyleName("FHS-TablesListBox");
		simplePanelProjList.setSize("500px", "600px");
		ProjSearchHPanel2.add(simplePanelProjList);
		
		ProjSearchHPanel2.setCellHeight(simplePanelProjList, "600px");
		ProjSearchHPanel2.setCellWidth(simplePanelProjList, "500px");
		
		scrollPanel2 = new ScrollPanel();
		scrollPanel2.setStyleName("FHS-ScrollPanel2");
		scrollPanel2.setSize("500px", "595px");
		simplePanelProjList.setWidget(scrollPanel2);
		
		VPProjDetails = new VerticalPanel();
		VPProjDetails.setSpacing(3);
		VPProjDetails.setStyleName("FHS-VPProjDetails");
		scrollPanel2.setWidget(VPProjDetails);
		
		ProjBrowseBtn = new Button("BROWSE DATA");
        ProjBrowseBtn.setSize("120px", "25px");
        ProjBrowseBtn.setStyleName("FHS-ButtonBrowseData");
    	//------------------------------------------------------------------------------
        //Right window for the details of the selected project plus the browse button below. -->End
     
        initWidget(ProjSearchHPanel);
	}//close brace for the Fhs_ProjectList()
	
	
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
	
	CellList<ProjectInfo> cellList = new CellList<ProjectInfo>(projectCell, GWT.<MyCellListResources> create(MyCellListResources.class), ProjectInfo.KEY_PROVIDER);
	
	final AsyncCallback<String[][]> PopulateCellList = new AsyncCallback<String[][]>() {
        public void onSuccess(String[][] result) {
            try{
            	List<ProjectInfo> projects = new ArrayList<ProjectInfo>();
                for (int i = 1;i<result.length;i++){
                	projects.add(new ProjectInfo(Integer.parseInt(result[i][0]),result[i][1]));                	
                }
                cellList.setRowCount(result.length,true);
                cellList.setRowData(projects);
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
	
	public void displayProjDetails(String sql){
		VPProjDetails.clear();
		final HTML ProjDetails = new HTML();
        final AsyncCallback<String[][]> FetchDetails = new AsyncCallback<String[][]>() {

            public void onFailure(Throwable caught) {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            public void onSuccess(String[][] result) {
            
            	String html = "";
                for (int i = 1; i < result.length; i++) {
                	html =  "<p><b>Project</b>: "+ result[i][3] + "</p>" +
                			"<p><b>Primary Researcher(s)</b>: " + result[i][4] + "</p>" +
                			"<p><b>Study Year(s)</b>: " + result[i][6] + "</p>" +
                            "<p><b>Study Site(s)</b>: "+ Utils.delimStringToHTMLList(result[i][5], "_", "ul") + "</p>"+
                            "<p><b>Respondents</b>: "+ result[i][7] + "</p>";
				}
                ProjDetails.setHTML(html);	
                VPProjDetails.add(ProjDetails);
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
