package org.irri.households.client.ui;

import org.irri.households.client.UtilsRPC;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.visualization.client.AbstractDataTable;
import com.google.gwt.visualization.client.DataTable;
import com.google.gwt.visualization.client.VisualizationUtils;
import com.google.gwt.visualization.client.visualizations.Table;
import com.google.gwt.visualization.client.visualizations.Table.Options;


public class VizTablePanel extends Composite {
	private VerticalPanel TablePanel = new VerticalPanel();
    public VizTablePanel(String query, String title){
    	
    	final AsyncCallback<String[][]> DBDataTable = new AsyncCallback<String[][]>() {

    		public void onFailure(Throwable caught) {
    			throw new UnsupportedOperationException("Not supported yet.");
    		}

    		public void onSuccess(String[][] result) {
    			final String[][] out = result;
    			Runnable onLoadCallback = new Runnable() {

    				public void run() {
    					Table viztab = new Table(createTable(out), createOptions());
    					TablePanel.add(viztab);
    				}
    			};
    			VisualizationUtils.loadVisualizationApi(onLoadCallback, Table.PACKAGE);
    		}
    	};

    	UtilsRPC.getService("mysqlservice").RunSELECT(query, DBDataTable);
    	initWidget(TablePanel);
    }

    private Options createOptions() {
    	Options options = Options.create();
    	return options;
    }

    private AbstractDataTable createTable(String[][] qdata){
    	DataTable data = DataTable.create();
    	for (int i=0;i<qdata[0].length;i++){
    		data.addColumn(AbstractDataTable.ColumnType.NUMBER, qdata[0][i]);
    	}
    	data.addRows(qdata.length-1);
    	for (int i=1;i<qdata.length;i++){
    		for (int j=0;j<qdata[0].length;j++){
    			data.setValue(i-1, j, Double.parseDouble(qdata[i][j]));
    		}
    	}
    	return data;
    }
}
