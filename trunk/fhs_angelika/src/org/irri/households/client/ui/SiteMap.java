package org.irri.households.client.ui;


import org.irri.households.client.Utils;
import org.irri.households.client.UtilsRPC;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.maps.client.InfoWindowContent;
import com.google.gwt.maps.client.MapUIOptions;
import com.google.gwt.maps.client.MapWidget;
import com.google.gwt.maps.client.Maps;
import com.google.gwt.maps.client.event.MarkerClickHandler;
import com.google.gwt.maps.client.geom.LatLng;
import com.google.gwt.maps.client.geom.Size;
import com.google.gwt.maps.client.overlay.Marker;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.VerticalPanel;


public class SiteMap extends Composite {
	private VerticalPanel verticalPanel = new VerticalPanel();
	
	public TabPanel ParentPanel;
	
	final AsyncCallback<String[][]> siteMapCallback = new AsyncCallback<String[][]>() {

		@Override
		public void onFailure(Throwable caught) {
			throw new UnsupportedOperationException("Not supported yet.");			
		}
		@Override
		public void onSuccess(String[][] result) {
			Size mapSize = Size.newInstance(490, 375);
			
			final MapWidget map = new MapWidget(LatLng.newInstance(14, 103), 3);
			map.clearOverlays();
			map.setPixelSize(490, 375);
			
			MapUIOptions options = MapUIOptions.newInstance(mapSize);
			options.setScrollwheel(true);
			options.setKeyboard(true);
			options.setScaleControl(false);
			options.setSmallZoomControl3d(true);
			options.setLargeMapControl3d(false);
			options.setMapTypeControl(true);
		    
		    map.setUI(options);
		    
		    String lastgroup = "";
		    int group = 0;
		    
			for (int i=1;i<result.length;i++){
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
				VerticalPanel InfoWidget = new VerticalPanel();
				final String id = result[i][9];
                HTML htmlWidget = new HTML(htmlstring);
                Button BtBrowse = new Button("Browse Data");

                BtBrowse.addClickHandler(new ClickHandler() {

                    public void onClick(ClickEvent event) {
                        //ParentPanel.selectTab(1);
                        //FiguresSlider.showControls(false);
                        //BrowserPanel.setLocationItem(id);
                    }
                });
				
				InfoWidget.add(htmlWidget);
                InfoWidget.add(BtBrowse);
                String curgroup = result[i][6];
                if (!lastgroup.equalsIgnoreCase(curgroup)){
                    group += 1;
                    lastgroup = curgroup;
                }
				final ScrollPanel infoscroll = new ScrollPanel(InfoWidget);
                infoscroll.setStyleName("whitebox");
                infoscroll.setSize("300px", "200px");
                
				
				String[] latlng = result[i][0].split("_");
				double lat = Double.parseDouble(latlng[0]);
                double lon = Double.parseDouble(latlng[1]);
                final Marker marker = new Marker(LatLng.newInstance(lat, lon));
                marker.addMarkerClickHandler(new MarkerClickHandler() {
                    public void onClick(MarkerClickEvent event) {
                    	map.getInfoWindow().open(marker,new InfoWindowContent(infoscroll));
                    }
                });
                
                map.addOverlay(marker); // Add a marker
			}
		    
		    
		    verticalPanel.add(map);
		}
	};
	
	public SiteMap() {
		
		Maps.loadMapsApi("", "2", false, new Runnable() {
		      public void run() {
		    	  //buildUi();
		    	  utilsRPC("SELECT DISTINCT CONCAT_WS('_', s.lat, s.long) 'LatLon', " +
		                  "CONCAT_WS(', ', s.province, s.country) 'Province', " +
		                  "GROUP_CONCAT(DISTINCT CONCAT_WS(', ', IF(s.village IS NULL, '-',s.village), IF(s.district IS NULL,'-', s.district)) SEPARATOR '_') 'Villages', " +
		                  "GROUP_CONCAT(DISTINCT CONVERT(p.proj_title,CHAR) ORDER BY 1 DESC SEPARATOR '_') Project, " +
		                  "GROUP_CONCAT(DISTINCT CONVERT(s.survey_year,CHAR(4)) ORDER BY 1 DESC SEPARATOR ', '), " +
		                  "GROUP_CONCAT(DISTINCT CONVERT(IF(p.key_vars IS NULL, '-', p.key_vars),CHAR) ORDER BY p.proj_title DESC SEPARATOR '_') 'Key Variables', " +
		                  "s.project_id, " +
		                  "SUM(s.samplesize), CEIL(SUM(s.samplesize)/ 100)*5 markersize, " +
		                  "LEFT(s.site_id,4) id " +
		                  "FROM surveys s INNER JOIN projects p ON s.project_id = p.project_id " +
		                  "GROUP BY 1 ORDER BY 7");
		    	 
		      }
		});
		initWidget(verticalPanel);
	}
	
	public void utilsRPC(String query){
		UtilsRPC.getService("mysqlservice").RunSELECT(query, siteMapCallback);
	}
	
	
}

