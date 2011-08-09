package org.irri.households.client.ui;

import org.irri.households.client.UtilsRPC;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.visualization.client.AbstractDataTable;
import com.google.gwt.visualization.client.DataTable;
import com.google.gwt.visualization.client.VisualizationUtils;
import com.google.gwt.visualization.client.visualizations.corechart.AxisOptions;
import com.google.gwt.visualization.client.visualizations.corechart.ScatterChart;
import com.google.gwt.visualization.client.visualizations.corechart.Options;


public class ScatterPlotPanel extends Composite {
	private VerticalPanel ScapPanel = new VerticalPanel();
	
	public ScatterPlotPanel(String query,String title, int w, int h) {
		final String ChartTitle = title;
		final int width = w;
		final int height = h;
		final AsyncCallback<String[][]> DBDataTable = new AsyncCallback<String[][]>() {

			@Override
			public void onFailure(Throwable caught) {
				throw new UnsupportedOperationException("Not supported yet.");
			}

			@Override
			public void onSuccess(String[][] result) {
				final String[][] out = result;
				Runnable onLoadCallback = new Runnable(){
				
					public void run(){
						ScatterChart scaplot = new ScatterChart(createTable(out), createOptions(ChartTitle,width,height));
						ScapPanel.add(scaplot);
						
					}
				};
				VisualizationUtils.loadVisualizationApi(onLoadCallback, ScatterChart.PACKAGE);
				
			}
		};
		UtilsRPC.getService("mysqlservice").RunSELECT(query, DBDataTable);
		initWidget(ScapPanel);	
	}
	

	private AbstractDataTable createTable(String[][] qdata) {
		DataTable data = DataTable.create();	
    	for (int i=0;i<qdata[0].length;i++){
    		data.addColumn(AbstractDataTable.ColumnType.NUMBER, qdata[0][i]);
    	}
    	data.addRows(qdata.length-1);
		for (int i = 1; i < qdata.length; i++) {
			for (int j = 0; j < qdata[i].length; j++) {
				if (qdata[i][j]!=null){
					if(j==0){
						data.setValue(i-1, j, Integer.parseInt(qdata[i][j]));
					} else {
						data.setValue(i-1, j, Double.parseDouble(qdata[i][j]));
					}
				}
			}
		}
		return data;
	}
	
	private Options createOptions(String title, int w, int h){
		Options options = Options.create();
		options.setWidth(w);
    	options.setHeight(h);
    	options.setTitle(title);
    	options.setPointSize(2); //increase number to increase point size
    	AxisOptions hAxisOptions = AxisOptions.create();
    	AxisOptions vAxisOptions = AxisOptions.create();
    	hAxisOptions.setTitle("Year");
    	hAxisOptions.set("format", "####");
    	
    	vAxisOptions.setTitle("Yield");
    	options.setHAxisOptions(hAxisOptions);
    	options.setVAxisOptions(vAxisOptions);
		return options;
	}
	
}
