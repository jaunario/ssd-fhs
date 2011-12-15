package org.irri.households.client.ui;


import org.irri.households.client.Utils;
import org.irri.households.client.UtilsRPC;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.maps.client.MapType;
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
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;


public class SiteMap extends Composite {
	private VerticalPanel LinkPanel;
	private Button SiteMapBrowseBtn;
	public MapWidget map;
	public VerticalPanel verticalPanel = new VerticalPanel();
	public HTML htmlWidget;
	public String id;
	private ScrollPanel infoscroll;
	public Marker marker;
	public MarkerClickHandler click;
	private Size mapSize;


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
                
                marker = new Marker(LatLng.newInstance(lat, lon));

                
                click = new MarkerClickHandler(){
                	public void onClick(MarkerClickEvent event) {
                    	LinkPanel.clear();
                    	LinkPanel.add(InfoWidget);
                    	LinkPanel.add(SiteMapBrowseBtn);
                    	Label lbl = (Label) InfoWidget.getWidget(0);
                    	id = lbl.getText(); 	
                    }
                };                
                marker.addMarkerClickHandler(click);
                map.addOverlay(marker); // Add a marker
                
			}
		}
	};
	
	final AsyncCallback<String[][]> siteMapCallback1 = new AsyncCallback<String[][]>() {

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
                
                marker = new Marker(LatLng.newInstance(lat, lon));
                map.addOverlay(marker);
                RootPanel.get("Loading-Message").setVisible(false);    
			}
		}
	};
	
	public SiteMap(String query, String w, String h, String clickable) {	
		map = new MapWidget(LatLng.newInstance(14, 103), 3);
		mapSize = Size.newInstance(Integer.parseInt(w), Integer.parseInt(h));
		
		SiteMapBrowseBtn = new Button("BROWSE DATA");
		SiteMapBrowseBtn.setEnabled(true);
		SiteMapBrowseBtn.setStyleName("FHS-ButtonBrowseData");
		
		MapUIOptions options = MapUIOptions.newInstance(mapSize);
		options.setScrollwheel(false);
		options.setKeyboard(false);
		options.setSmallZoomControl3d(false);
		options.setMapTypeControl(false);
		options.setLargeMapControl3d(false);
		options.setMenuMapTypeControl(false);	
		options.setSatelliteMapType(false);
		options.setHybridMapType(false);
		options.setNormalMapType(false);
		options.setPhysicalMapType(false);
		map.setUI(options);
		map.setDraggable(true);
		map.setCurrentMapType(MapType.getPhysicalMap());
		verticalPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		verticalPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		verticalPanel.add(map);
		map.setSize(w+"px", h+"px");
		map.checkResizeAndCenter();
		
		if (clickable.equals("yes")){ //yes means that the markers should me clickable like those in the sitemap of the search by location
			UtilsRPC.getService("mysqlservice").RunSELECT(query, siteMapCallback); //markers are clickable
		}else UtilsRPC.getService("mysqlservice").RunSELECT(query, siteMapCallback1); //markers are not clickable
		
		initWidget(verticalPanel);
	}
	
	public void SetSiteMapBrowseBtn(ClickHandler click){
		SiteMapBrowseBtn.addClickHandler(click);
	}
	
	public void setLinkPanel(VerticalPanel panel){
		LinkPanel = panel;
	}
}

