package org.irri.households.client.ui;

import org.irri.households.client.UtilsRPC;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.visualization.client.AbstractDataTable;
import com.google.gwt.visualization.client.DataTable;
import com.google.gwt.visualization.client.LegendPosition;
import com.google.gwt.visualization.client.VisualizationUtils;
import com.google.gwt.visualization.client.visualizations.corechart.AxisOptions;
import com.google.gwt.visualization.client.visualizations.corechart.ColumnChart;
import com.google.gwt.visualization.client.visualizations.corechart.Options;

public class ColumnChartPanel extends Composite {
	private VerticalPanel VisBox = new VerticalPanel();
	public String[][] mydata;

	public ColumnChartPanel(String query, String title, int w, int h) {
		
	final String ChartTitle = title;
    	final int width = w;
    	final int height = h;
    	final AsyncCallback<String[][]> DBDataTable = new AsyncCallback<String[][]>() {
        	
        	public void onSuccess(String[][] result) {
                final String[][] out = result;
                Runnable onLoadCallback = new Runnable() {

                    public void run() {
                    	mydata = out;
                    	ColumnChart column = new ColumnChart(createTable(out), createOptions(ChartTitle,width,height));
                        VisBox.add(column);
                    }
                };
                VisualizationUtils.loadVisualizationApi(onLoadCallback, ColumnChart.PACKAGE);
            }

            public void onFailure(Throwable caught) {
                throw new UnsupportedOperationException("Not supported yet.");
            }  
        };
        UtilsRPC.getService("mysqlservice").RunSELECT(query, DBDataTable);
        initWidget(VisBox);
        VisBox.setSize("320px", "260px");
        
		
	}
	
	public Options createOptions(String title, int w, int h) {
        Options options = Options.create();
        AxisOptions hAxisOptions = AxisOptions.create();
    	AxisOptions vAxisOptions = AxisOptions.create();
    	hAxisOptions.setTitle("Year");
    	vAxisOptions.setTitle("Average Income");
    	options.setHAxisOptions(hAxisOptions);
    	options.setVAxisOptions(vAxisOptions);
        options.setWidth(w);
        options.setHeight(h);
        options.setTitle(title);
        options.setLegend(LegendPosition.BOTTOM);
        return options;
    }
    
    private AbstractDataTable createTable(String[][] qdata){
        DataTable data = DataTable.create();
        	data.addColumn(AbstractDataTable.ColumnType.STRING, qdata[0][0]);
        	data.addColumn(AbstractDataTable.ColumnType.NUMBER, qdata[0][1]);
        	data.addColumn(AbstractDataTable.ColumnType.NUMBER, qdata[0][2]);
        	data.addColumn(AbstractDataTable.ColumnType.NUMBER, qdata[0][3]);
        	data.addColumn(AbstractDataTable.ColumnType.NUMBER, qdata[0][4]);
        	data.addRows(qdata.length-1);
        
        for (int i=1;i<qdata.length;i++){
        	data.setValue(i-1, 0, (qdata[i][0]));
        	for (int j=1;j<5;j++){
        		if (qdata[i][j]==null){
        			qdata[i][j]="0";
        		}
            	data.setValue(i-1, j, Double.parseDouble(qdata[i][j]));        		
        	}
        }
        return data;
    } 

}
