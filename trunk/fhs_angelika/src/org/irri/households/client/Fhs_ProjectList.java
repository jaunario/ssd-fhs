package org.irri.households.client;

import java.util.ArrayList;
import java.util.List;
import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.cellview.client.HasKeyboardSelectionPolicy.KeyboardSelectionPolicy;
import com.google.gwt.view.client.ProvidesKey;
import com.google.gwt.view.client.SingleSelectionModel;


public class Fhs_ProjectList extends Composite {

	private static class ProjectInfo implements Comparable<ProjectInfo>{

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
		private String PrimeResearcher;
		private String Year;
		
		public ProjectInfo(int id1, String projtitle, String primresearcher, String year){
			id = id1;
			ProjTitle = projtitle;
			PrimeResearcher = primresearcher;
			if (year == null){
				Year = "Year not available";
			}else Year = year;
		}
		
		public int getId(){
			return id;
		}
		
		public String getProjTitle() {
		      return ProjTitle;
		}
		
		public String getPrimeResearcher() {
		      return PrimeResearcher;
		}

		public String getYear() {
		      return Year;
		}
	}
	

	static class ProjectCell extends AbstractCell<ProjectInfo>{

		private final String imageHtml;	
		
		public ProjectCell(String imgurl) {
		      this.imageHtml = imgurl;
		}

		@Override
		public void render(com.google.gwt.cell.client.Cell.Context context,
							ProjectInfo value, SafeHtmlBuilder sb) {
			sb.appendHtmlConstant("<html><head>");
			sb.appendHtmlConstant("<style type='text/css'>");
			sb.appendHtmlConstant("table.proj {table-layout:fixed}");
			sb.appendHtmlConstant("</style>");
			sb.appendHtmlConstant("</head>");
			sb.appendHtmlConstant("<body>");
				
			sb.appendHtmlConstant("<table class=proj; width='100%' border=0 cellpadding=5 cellspacing=0>");

			// Add the contact image.
			sb.appendHtmlConstant("<tr><td width = 100>");
			sb.appendHtmlConstant(imageHtml);
			sb.appendHtmlConstant("</td>");
				
			// Add the name and address.
			if (value.getId()%2==0){
				sb.appendHtmlConstant("<td width=550 style='background-color:#D3D3D3'><b>");  
			}else{
				sb.appendHtmlConstant("<td width=550 style='background-color:#EDEDED'><b>");
				}
			sb.appendEscaped(value.getProjTitle());
			sb.appendHtmlConstant("</b><br>");  
			sb.appendEscaped(value.getPrimeResearcher());
			sb.appendHtmlConstant("<br>");
			sb.appendEscaped(value.getYear());
			sb.appendHtmlConstant("</td></tr></table>");
			sb.appendHtmlConstant("</body></html>");	
		}		
	}
	
	
	ProjectCell projectCell = new ProjectCell("<img src='http://127.0.0.1:8888/images/view_40.png'>");	
	CellList<ProjectInfo> cellList = new CellList<ProjectInfo>(projectCell, ProjectInfo.KEY_PROVIDER);
	

	final AsyncCallback<String[][]> PopulateCellList = new AsyncCallback<String[][]>() {
        public void onSuccess(String[][] result) {
            try{
            	List<ProjectInfo> projects = new ArrayList<ProjectInfo>();
                for (int i = 1;i<result.length;i++){
                	projects.add(new ProjectInfo(Integer.parseInt(result[i][0]),result[i][1], result[i][2], result[i][3]));                	
                }
                cellList.setRowCount(result.length,true);
                cellList.setRowData(projects);
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
    
	
	public Fhs_ProjectList() {
		
		initWidget(cellList);
		
		utilsrpc("SELECT project_id, proj_title, prime_researcher, study_yr FROM projects");
		
		cellList.setKeyboardSelectionPolicy(KeyboardSelectionPolicy.ENABLED);
		cellList.setKeyboardSelectionPolicy(KeyboardSelectionPolicy.BOUND_TO_SELECTION);
		
		final SingleSelectionModel<ProjectInfo> selectionModel = new SingleSelectionModel<ProjectInfo>(ProjectInfo.KEY_PROVIDER);
	    cellList.setSelectionModel(selectionModel);	    
	}
}
