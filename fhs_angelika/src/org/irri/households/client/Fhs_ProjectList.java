package org.irri.households.client;


import java.util.ArrayList;
import java.util.List;
import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.core.client.GWT;
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
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;


public class Fhs_ProjectList extends Composite {	
	private final String ProjDetailsSql =
			"SELECT LEFT(s.site_id,2) iso2, s.country, s.project_id, p.proj_title, p.prime_researcher, "+
			"GROUP_CONCAT(DISTINCT CONCAT_WS(', ', s.province, s.district, s.village) SEPARATOR  '_'), "+
			"GROUP_CONCAT(DISTINCT CONVERT(s.survey_year, CHAR(4)) ORDER BY s.survey_year DESC SEPARATOR ',')," +
			"SUM(s.samplesize), p.purpose "+
			"FROM surveys s INNER JOIN projects p ON s.project_id = p.project_id";
	
	public Button ProjBrowseBtn;
	public SimplePanel ProjLinkPanel;
	public SingleSelectionModel<ProjectInfo> selectionModel;
	private HorizontalPanel ProjSearchHPanel;
	private HorizontalPanel ProjSearchHPanel2;
	private ScrollPanel scrollPanel;
	public VerticalPanel verticalPanel2;
	public ProjectDetails projDetails;
	private VerticalPanel verticalPanel;


	public Fhs_ProjectList() {		
			
		utilsrpc("SELECT p.project_id, p.proj_title FROM projects p INNER JOIN surveys s ON p.project_id=s.project_id GROUP BY 1");
		
		ProjSearchHPanel = new HorizontalPanel();
		
		ProjSearchHPanel2 = new HorizontalPanel();
		ProjSearchHPanel2.setSpacing(1);
		ProjSearchHPanel.add(ProjSearchHPanel2);
		
		verticalPanel2 = new VerticalPanel();
		
		selectionModel = new SingleSelectionModel<ProjectInfo>(ProjectInfo.KEY_PROVIDER);
		
		verticalPanel = new VerticalPanel();
		verticalPanel.setStyleName("FHS-TablesListBox");
		ProjSearchHPanel2.add(verticalPanel);
		
		cellList.setStyleName("FHS-ProjCellList");
		cellList.setSize("300px", "698px");
		cellList.setKeyboardSelectionPolicy(KeyboardSelectionPolicy.ENABLED);
		cellList.setKeyboardSelectionPolicy(KeyboardSelectionPolicy.BOUND_TO_SELECTION);
		
		scrollPanel = new ScrollPanel();
		verticalPanel.add(scrollPanel);
		
		scrollPanel.setWidget(cellList);
				
		ProjSearchHPanel2.setCellVerticalAlignment(scrollPanel, HasVerticalAlignment.ALIGN_MIDDLE);
		ProjSearchHPanel2.setCellHorizontalAlignment(scrollPanel, HasHorizontalAlignment.ALIGN_CENTER);
		cellList.setSelectionModel(selectionModel);
		
		ProjSearchHPanel2.add(verticalPanel2);
		
		ProjSearchHPanel2.setCellHeight(verticalPanel2, "600px");
		ProjSearchHPanel2.setCellWidth(verticalPanel2, "500px");
		
		projDetails = new ProjectDetails(ProjDetailsSql + ProjDetailsSqlWhereClause(5) + " GROUP BY 3", 5);
		verticalPanel2.add(projDetails);
	
		ProjBrowseBtn = new Button("BROWSE DATA");
        ProjBrowseBtn.setSize("120px", "25px");
        ProjBrowseBtn.setStyleName("FHS-ButtonBrowseData");
     
        initWidget(ProjSearchHPanel);
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
	
	public void SetSelectionModel(SelectionChangeEvent.Handler handler){
		selectionModel.addSelectionChangeHandler(handler);
	}
	
}