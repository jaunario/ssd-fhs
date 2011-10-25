package org.irri.households.client.ui;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.visualization.client.visualizations.Table;
import org.irri.households.client.UtilsRPC;
import org.irri.households.client.ui.charts.MultiChartPanel;
import com.google.gwt.visualization.client.AbstractDataTable;
import com.google.gwt.visualization.client.VisualizationUtils;

public class GetData extends Composite {
	public int rowcount;
	public AbstractDataTable data;
	private VerticalPanel TablePanel = new VerticalPanel();	
	private MultiChartPanel resultpanel;
	public GetData(String query, String tablename) {
		final String table = tablename;
		final AsyncCallback<String[][]> GetDataTable = new AsyncCallback<String[][]>() {

    		public void onFailure(Throwable caught) {
    			throw new UnsupportedOperationException("Not supported yet.");
    		}

    		public void onSuccess(String[][] result) {
    			final String[][] out = result;
    			rowcount = result.length-1;
    			Runnable onLoadCallback = new Runnable() {

    				public void run() {
    					resultpanel.setBaseData(out, table);
    				}
    			};    			
    			VisualizationUtils.loadVisualizationApi(onLoadCallback, Table.PACKAGE);
    		}    		
    	};
    	resultpanel =  new MultiChartPanel();
    	resultpanel.getDeckPanel().setSize("100%", "100%");
    	TablePanel.add(resultpanel);
    	resultpanel.setSize("792px", "360px");
    	UtilsRPC.getService("mysqlservice").RunSELECT(query, GetDataTable);
    	initWidget(TablePanel);
    }
}
