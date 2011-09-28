package org.irri.households.client.ui;

import org.irri.households.client.Utils;
import org.irri.households.client.UtilsRPC;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.maps.client.InfoWindowContent;
import com.google.gwt.maps.client.MapUIOptions;
import com.google.gwt.maps.client.MapWidget;
import com.google.gwt.maps.client.event.MarkerClickHandler;
import com.google.gwt.maps.client.geom.LatLng;
import com.google.gwt.maps.client.geom.Size;
import com.google.gwt.maps.client.overlay.Marker;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;

public class SiteMap extends Composite {
	private VerticalPanel LinkPanel;
	private Button SiteMapBrowseBtn;
	private MapWidget map = new MapWidget(LatLng.newInstance(14, 103), 3);
	public VerticalPanel verticalPanel = new VerticalPanel();
	public HTML htmlWidget;
	public String id;
	private ScrollPanel infoscroll;
	
	Size mapSize = Size.newInstance(797, 323);
	

	final AsyncCallback<String[][]> siteMapCallback = new AsyncCallback<String[][]>() {

		@Override
		public void onFailure(Throwable caught) {
			throw new UnsupportedOperationException("Not supported yet.");			
		}
		@Override
		public void onSuccess(final String[][] result) {
			map.clearOverlays();
		    
			for (int i=1;i<result.length;i++){
				if (i==result.length-1){
					System.out.println(i);
				}
				String keyvars = result[i][5];
				String htmlstring = Utils.placeInTag(result[i][1], "h4") +
				Utils.placeInTag("<b>Project: </b><big>"+result[i][3]+ "</big><br/>" +
						"<br/><b>Key Variables: </b>" + "<i>" + keyvars + " </i><br/>" +
	                    "<br/><b>Respondents: </b>"+ result[i][7] + "<br/>" +
	                    "<br/><b>Village, District:</b>", "p")+ "<ul>";
				String[] villages = result[i][2].split("_");
				for (int j=0; j<villages.length;j++){
                    htmlstring += Utils.placeInTag(villages[j],"li");
                }
				htmlstring += "</ul>";
				final VerticalPanel InfoWidget = new VerticalPanel();				
                htmlWidget = new HTML(htmlstring);
                Label markercountry = new Label(result[i][9]/*.substring(0, 2)*/);
                markercountry.setVisible(false);
                InfoWidget.add(markercountry);
				InfoWidget.add(htmlWidget);
				infoscroll = new ScrollPanel(InfoWidget);
                infoscroll.setStyleName("whitebox");
                infoscroll.setSize("300px", "200px");
                
				String[] latlng = result[i][0].split("_");
				double lat = Double.parseDouble(latlng[0]);
                double lon = Double.parseDouble(latlng[1]);
                
                final Marker marker = new Marker(LatLng.newInstance(lat, lon));
                marker.addMarkerClickHandler(new MarkerClickHandler() {
                    public void onClick(MarkerClickEvent event) {
                    	map.getInfoWindow().open(marker,new InfoWindowContent(infoscroll));
                    	LinkPanel.clear();
                    	LinkPanel.add(InfoWidget);
                    	LinkPanel.add(SiteMapBrowseBtn);
                    	Label lbl = (Label) InfoWidget.getWidget(0);
                    	id = lbl.getText(); 	
                    }
                });
                map.addOverlay(marker); // Add a marker
			}
		}
	};
	
	public SiteMap() {	
		SiteMapBrowseBtn = new Button("BROWSE DATA");
		SiteMapBrowseBtn.setEnabled(true);
		SiteMapBrowseBtn.setStyleName("fhs-ButtonBrowseData");
		
		MapUIOptions options = MapUIOptions.newInstance(mapSize);
		options.setScrollwheel(true);
		options.setKeyboard(true);
		options.setSmallZoomControl3d(true);
		options.setMapTypeControl(true);
		map.setUI(options);
		verticalPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		verticalPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		verticalPanel.add(map);
		map.setSize("800px", "325px");
		map.checkResizeAndCenter();
		
		
		utilsRPC("SELECT DISTINCT CONCAT_WS('_', s.lat, s.long) 'LatLon', " +
				"CONCAT_WS(', ', s.province, s.country) 'Province', " +
		        "GROUP_CONCAT(DISTINCT CONCAT_WS(', ', IF(s.village IS NULL, '-',s.village), IF(s.district IS NULL,'-', s.district)) SEPARATOR '_') 'Villages', " +
		        "GROUP_CONCAT(DISTINCT CONVERT(p.proj_title,CHAR) ORDER BY 1 DESC SEPARATOR '_') Project, " +
		        "GROUP_CONCAT(DISTINCT CONVERT(s.survey_year,CHAR(4)) ORDER BY 1 DESC SEPARATOR ', '), " +
		        "GROUP_CONCAT(DISTINCT CONVERT(IF(p.key_vars IS NULL, '-', p.key_vars),CHAR) ORDER BY p.proj_title DESC SEPARATOR '_') 'Key Variables', " +
		        "s.project_id, " +
		        "SUM(s.samplesize), CEIL(SUM(s.samplesize)/ 100)*5 markersize, " +
		        "s.country id " +
		        "FROM surveys s INNER JOIN projects p ON s.project_id = p.project_id " +
		        "GROUP BY 1 ORDER BY 7");
		initWidget(verticalPanel);
		
	}
	
	public void utilsRPC(String query){
		UtilsRPC.getService("mysqlservice").RunSELECT(query, siteMapCallback);
	}
	
	public void SetSiteMapBrowseBtn(ClickHandler click){
		SiteMapBrowseBtn.addClickHandler(click);
	}
	
	public void setLinkPanel(VerticalPanel panel){
		LinkPanel = panel;
	}
}

