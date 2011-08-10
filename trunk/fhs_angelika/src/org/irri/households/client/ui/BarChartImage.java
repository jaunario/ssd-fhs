package org.irri.households.client.ui;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.visualization.client.AbstractDataTable;
import com.google.gwt.visualization.client.DataTable;
import com.google.gwt.visualization.client.VisualizationUtils;
import com.google.gwt.visualization.client.visualizations.ImageBarChart;
import com.google.gwt.visualization.client.visualizations.ImageBarChart.Options;


public class BarChartImage extends Composite {
	private VerticalPanel VisBox = new VerticalPanel();
	
	public BarChartImage(String[][] table, String title, int w, int h){
		final String ChartTitle = title;
		final int width = w;
		final int height =h;
		final String[][] data = table;
        Runnable onLoadCallback = new Runnable() {

            public void run() {
            	ImageBarChart bar = new ImageBarChart(createTable(data), createOptions(ChartTitle,width,height));            
                VisBox.add(bar);
            }
        };
        VisualizationUtils.loadVisualizationApi(onLoadCallback, ImageBarChart.PACKAGE);
		initWidget(VisBox);
	}
	
	public Options createOptions(String title, int w, int h) {
        Options options = Options.create();
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
