package org.irri.households.client.ui;


import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.visualization.client.AbstractDataTable;
import com.google.gwt.visualization.client.DataTable;
import com.google.gwt.visualization.client.VisualizationUtils;
import com.google.gwt.visualization.client.visualizations.ImagePieChart;
import com.google.gwt.visualization.client.visualizations.ImagePieChart.Options;


public class PieChartImage extends Composite{
    private VerticalPanel VisBox = new VerticalPanel();

	public PieChartImage(String[][] table, String title, int w, int h){
    	final String ChartTitle = title;
    	final int width = w;
    	final int height = h;
    	final String[][] data = table;
    	Runnable onLoadCallback = new Runnable(){
    		public void run() {
                ImagePieChart pie = new ImagePieChart(createTable(data), createOptions(ChartTitle,width,height));
                VisBox.add(pie);   
            }
    	};
    	VisualizationUtils.loadVisualizationApi(onLoadCallback, ImagePieChart.PACKAGE);
    	initWidget(VisBox);
	}
    	
    public Options createOptions(String title, int w, int h) {
    	Options options = ImagePieChart.Options.create();
        options.setWidth(w);
        options.setHeight(h);
        options.setTitle(title);
        options.setIs3D(true);	
        return options;
    }
    
    private AbstractDataTable createTable(String[][] qdata){
        DataTable data = DataTable.create();
        data.addColumn(AbstractDataTable.ColumnType.STRING, qdata[0][0]);
        data.addColumn(AbstractDataTable.ColumnType.NUMBER, qdata[0][1]);
        data.addRows(qdata.length-1);
        for (int i=1;i<qdata.length;i++){
            data.setValue(i-1, 0, qdata[i][0]);
            data.setValue(i-1, 1, Double.parseDouble(qdata[i][1]));
        }
        return data;
    } 
    
    
    
}
