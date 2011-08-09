package org.irri.households.client.ui;

import org.irri.households.client.UtilsRPC;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.visualization.client.AbstractDataTable;
import com.google.gwt.visualization.client.DataTable;
import com.google.gwt.visualization.client.VisualizationUtils;
import com.google.gwt.visualization.client.visualizations.corechart.AxisOptions;
import com.google.gwt.visualization.client.visualizations.corechart.BarChart;
import com.google.gwt.visualization.client.visualizations.corechart.Options;

public class BarChartPanel extends Composite {
	private VerticalPanel VisBox = new VerticalPanel();
	public String[][] mydata;

	public BarChartPanel(String query, String title, int w, int h) {
		
		final String ChartTitle = title;
    	final int width = w;
    	final int height = h;
    	final AsyncCallback<String[][]> DBDataTable = new AsyncCallback<String[][]>() {
        	
        	public void onSuccess(String[][] result) {
                final String[][] out = result;
                Runnable onLoadCallback = new Runnable() {

                    public void run() {
                    	mydata = out;
                    	BarChart bar = new BarChart(createTable(out), createOptions(ChartTitle,width,height));
                        VisBox.add(bar);
                    }
                };
                VisualizationUtils.loadVisualizationApi(onLoadCallback, BarChart.PACKAGE);
            }

            public void onFailure(Throwable caught) {
                throw new UnsupportedOperationException("Not supported yet.");
            }  
        };
        UtilsRPC.getService("mysqlservice").RunSELECT(query, DBDataTable);
        initWidget(VisBox);
        
		
	}
	
	public Options createOptions(String title, int w, int h) {
        Options options = Options.create();
        AxisOptions hAxisOptions = AxisOptions.create();
    	AxisOptions vAxisOptions = AxisOptions.create();
    	hAxisOptions.setTitle("x-axis");
    	vAxisOptions.setTitle("Year");
    	options.setHAxisOptions(hAxisOptions);
    	options.setVAxisOptions(vAxisOptions);
        options.setWidth(w);
        options.setHeight(h);
        options.setTitle(title);
        return options;
    }
    
    private AbstractDataTable createTable(String[][] qdata){
        DataTable data = DataTable.create();
        data.addColumn(AbstractDataTable.ColumnType.STRING, qdata[0][0]);
        data.addColumn(AbstractDataTable.ColumnType.NUMBER, qdata[0][1]);
        data.addColumn(AbstractDataTable.ColumnType.NUMBER, qdata[0][2]);
        data.addRows(qdata.length-1);
        for (int i=1;i<qdata.length;i++){ 
            data.setValue(i-1, 0, (qdata[i][0]));
            data.setValue(i-1, 1, Double.parseDouble(qdata[i][1]));
            data.setValue(i-1, 2, Double.parseDouble(qdata[i][2]));
        }
        return data;
    } 

}
