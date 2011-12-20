/* Displays the details of the selected project. Details include the project title, location map, primary
 * researchers, purpose of the study, study years, number of households */

package org.irri.households.client;


import org.irri.households.client.ui.SiteMap;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.SimplePanel;


public class ProjectDetails extends Composite {		
	public VerticalPanel VPProjDetails;
	public Button ProjBrowseBtn;
	public SimplePanel ProjLinkPanel;
	public int num;

	public ProjectDetails(String sql, final int projID) {		
		
		VPProjDetails = new VerticalPanel();
		VPProjDetails.setSpacing(3);
		VPProjDetails.setStyleName("FHS-VPProjDetails");
		
		ProjBrowseBtn = new Button("BROWSE DATA");
        ProjBrowseBtn.setSize("120px", "25px");
        ProjBrowseBtn.setStyleName("FHS-ButtonBrowseData");
        
        VPProjDetails.clear();
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

                //draws a map with unclickable markers pointing on the locations where the study is conducted
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
        		        "WHERE s.project_id=" +projID+
        		        " GROUP BY 1 ORDER BY 7", "470", "300", "no");
                VPProjDetails.add(siteMap);
                siteMap.map.checkResizeAndCenter();
                
                
                String html2 = "";
                for (int i = 1; i < result.length; i++) { //displays the project details below the map
                	html2 = "<p><b>Primary Researcher(s)</b>: " + result[i][4] + "</p>" +
                			"<p><b>Purpose</b>: "+result[i][8]+"</p>"+
                			"<p><b>Study Year(s)</b>: " + result[i][6] + "</p>" +
                            "<p><b>Households</b>: "+ result[i][7] + "</p>";
				}
                ProjDetails2.setHTML(html2);	
                VPProjDetails.add(ProjDetails2);
            
                VPProjDetails.add(ProjBrowseBtn);
                
            }
        };
        UtilsRPC.getService("mysqlservice").RunSELECT(sql, FetchDetails);
     
        initWidget(VPProjDetails);
	}
	
	
	//---------------------------------------------------------------------------------------------------
	//---------------------------------------------------------------------------------------------------
	
	public void SetProjBrowseBtn(ClickHandler click){
		ProjBrowseBtn.addClickHandler(click);
	}
	
}
