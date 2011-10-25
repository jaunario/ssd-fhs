package org.irri.households.client.ui;

import org.irri.households.client.UtilsRPC;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DeckPanel;
import com.google.gwt.visualization.client.AbstractDataTable;
import com.google.gwt.visualization.client.DataTable;
import com.google.gwt.visualization.client.LegendPosition;
import com.google.gwt.visualization.client.VisualizationUtils;
import com.google.gwt.visualization.client.visualizations.corechart.PieChart;
import com.google.gwt.visualization.client.visualizations.corechart.Options;
import com.google.gwt.visualization.client.visualizations.corechart.PieChart.PieOptions;


/**
*
* @author jaunario
*/
public class PieChartPanel extends Composite{
    private DeckPanel VisBox = new DeckPanel();
    public String[][] mydata;
    
    public PieChartPanel(String query, String title, int w, int h){
    	final String ChartTitle = title;
    	final int width = w;
    	final int height = h;
    	final AsyncCallback<String[][]> DBDataTable = new AsyncCallback<String[][]>() {
        	
        	public void onSuccess(String[][] result) {
                final String[][] out = result;
                Runnable onLoadCallback = new Runnable() {

                    public void run() {
                    	mydata = out;
                        PieChart pie = new PieChart(createTable(out), createOptions(ChartTitle,width,height));
                        VisBox.add(pie);
                        VisBox.showWidget(0);
                    }
                };
                VisualizationUtils.loadVisualizationApi(onLoadCallback, PieChart.PACKAGE);
            }

            public void onFailure(Throwable caught) {
                throw new UnsupportedOperationException("Not supported yet.");
            }
            
        };
        UtilsRPC.getService("mysqlservice").RunSELECT(query, DBDataTable);
        initWidget(VisBox);
        VisBox.setSize("320px", "260px");
    }

    
    PieOptions pieOptions = PieChart.createPieOptions();    
    
    public Options createOptions(String title, int w, int h) {
        Options options = pieOptions;
        options.setWidth(w);
        options.setHeight(h);
        options.setTitle(title);
        options.setLegend(LegendPosition.BOTTOM);
        pieOptions.set3D(true);
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
