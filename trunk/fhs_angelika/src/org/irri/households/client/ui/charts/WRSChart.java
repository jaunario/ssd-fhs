package org.irri.households.client.ui.charts;


import java.util.ArrayList;
import java.util.Collections;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.visualization.client.AbstractDataTable;
import com.google.gwt.visualization.client.LegendPosition;
import com.google.gwt.visualization.client.AbstractDataTable.ColumnType;
import com.google.gwt.visualization.client.VisualizationUtils;
import com.google.gwt.visualization.client.visualizations.GeoMap;
import com.google.gwt.visualization.client.visualizations.GeoMap.DataMode;
import com.google.gwt.visualization.client.visualizations.ImageAreaChart;
import com.google.gwt.visualization.client.visualizations.ImageBarChart;
import com.google.gwt.visualization.client.visualizations.ImageLineChart;
import com.google.gwt.visualization.client.visualizations.ImagePieChart;
import com.google.gwt.visualization.client.visualizations.corechart.AreaChart;
import com.google.gwt.visualization.client.visualizations.corechart.BarChart;
import com.google.gwt.visualization.client.visualizations.corechart.ColumnChart;
import com.google.gwt.visualization.client.visualizations.corechart.LineChart;
import com.google.gwt.visualization.client.visualizations.corechart.Options;
import com.google.gwt.visualization.client.visualizations.corechart.PieChart;
import com.google.gwt.visualization.client.visualizations.corechart.ScatterChart;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.HTML;

public class WRSChart extends Composite {
	private Button btnOk;
	AbstractDataTable BaseData;
	AbstractDataTable ChartData;
	private ListBox cbbChartType;
	private TextBox txtbxChartTitle;
	private ListBox cbbLPosition;
	private ListBox cbbX;
	private ListBox cbbY;
	private ListBox cbbSeries;
	private Image image;
	private HTML htmlChartDesc;
	private CheckBox chckbxInteractive;
	
	public int itemid;
	private int width;
	private int height;
	
	private SimplePanel chart;
	
	private ArrayList<String> catcols;
	private ArrayList<String> regions;
	private ArrayList<String> variables;
	private ArrayList<String> years;
	private Label lblChartType;
	private Label lblSeries;
	private Label lblY;
	private Label lblX;
	
	private DialogBox chartoptions;
	
	public WRSChart(AbstractDataTable basedata, int w, int h,int index) {
		width = w;
		height = h;
		itemid = index;
		BaseData = basedata;
		
		chart = new SimplePanel();
		initWidget(chart);

		getYears(basedata);
		getRegions(basedata);
		getVariables(basedata);
		getCategorizeColumns(basedata);
		initDialog();
		lblSeries.setText("year");
		populateListBox(cbbSeries, years);		
		populateListBox(cbbY, variables);
		lblX.setText("Parts");
		cbbX.clear();
		cbbX.addItem("region");
		cbbX.setEnabled(false);

	}

	public void initDialog(){
		chartoptions = new DialogBox();
		chartoptions.setGlassEnabled(true);
		chartoptions.setHTML("Chart Options");
		
		DockPanel dockPanel = new DockPanel();
		chartoptions.setWidget(dockPanel);
		dockPanel.setSize("510px", "310px");
				
		HorizontalPanel hpChartSubmit = new HorizontalPanel();
		hpChartSubmit.setSpacing(5);
		dockPanel.add(hpChartSubmit, DockPanel.SOUTH);
		dockPanel.setCellHorizontalAlignment(hpChartSubmit, HasHorizontalAlignment.ALIGN_RIGHT);
		
		btnOk = new Button("Ok");
		btnOk.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				int selchart = cbbChartType.getSelectedIndex();
				switch (selchart) {
				case 1:
					ChartData = ChartDataTable.numericXYSeries(BaseData, cbbX.getSelectedIndex()+2, cbbY.getSelectedIndex()+2, cbbSeries.getSelectedIndex()+2);
					if (chckbxInteractive.getValue()){
						Runnable onLoadCallback = new Runnable() {
							
							@Override
							public void run() {
								ScatterChart scatter = new ScatterChart(ChartData, createCoreChartOptions(txtbxChartTitle.getText(), getLegendPosition(), width, height));
								chart.setWidget(scatter);
							}
						};
						VisualizationUtils.loadVisualizationApi(onLoadCallback, ScatterChart.PACKAGE);
					}
					break;
				case 2:
					ChartData = ChartDataTable.dataIntoSeries(BaseData, cbbX.getSelectedIndex(), cbbY.getSelectedIndex()+2, cbbSeries.getSelectedIndex(),true);
					if (chckbxInteractive.getValue()){
						Runnable onLoadCallback = new Runnable() {
							
							@Override
							public void run() {
								LineChart line = new LineChart(ChartData, createCoreChartOptions(txtbxChartTitle.getText(), getLegendPosition(), width, height));
								chart.setWidget(line);
							}
						};
						VisualizationUtils.loadVisualizationApi(onLoadCallback, LineChart.PACKAGE);
					} else {
						Runnable onLoadCallback = new Runnable() {
							
							@Override
							public void run() {
								ImageLineChart.Options ipcOptions = ImageLineChart.Options.create();
								ipcOptions.setTitle(txtbxChartTitle.getText());
								ipcOptions.setWidth(width);
								ipcOptions.setHeight(height);
								ipcOptions.setLegend(getLegendPosition());
								ImageLineChart line = new ImageLineChart(ChartData, ipcOptions);
								chart.setWidget(line);
							}
						};
						VisualizationUtils.loadVisualizationApi(onLoadCallback, ImageLineChart.PACKAGE);
					}
					
					break;
				case 3:
					ChartData = ChartDataTable.dataIntoSeries(BaseData, cbbX.getSelectedIndex(), cbbY.getSelectedIndex()+2, cbbSeries.getSelectedIndex(),true);
					if (chckbxInteractive.getValue()){
						Runnable onLoadCallback = new Runnable() {
							
							@Override
							public void run() {
								ColumnChart line = new ColumnChart(ChartData, createCoreChartOptions(txtbxChartTitle.getText(), getLegendPosition(), width, height));
								chart.setWidget(line);
							}
						};
						VisualizationUtils.loadVisualizationApi(onLoadCallback, ColumnChart.PACKAGE);
					} 
					break;
				case 4:
					ChartData = ChartDataTable.dataIntoSeries(BaseData, cbbX.getSelectedIndex(), cbbY.getSelectedIndex()+2, cbbSeries.getSelectedIndex(),true);
					if (chckbxInteractive.getValue()){
						Runnable onLoadCallback = new Runnable() {
							
							@Override
							public void run() {
								BarChart bar = new BarChart(ChartData, createCoreChartOptions(txtbxChartTitle.getText(), getLegendPosition(), width, height));
								chart.setWidget(bar);
							}
						};
						VisualizationUtils.loadVisualizationApi(onLoadCallback, BarChart.PACKAGE);
					} else {
						Runnable onLoadCallback = new Runnable() {
							
							@Override
							public void run() {
								ImageBarChart.Options ipcOptions = ImageBarChart.Options.create();
								ipcOptions.setTitle(txtbxChartTitle.getText());
								ipcOptions.setWidth(width);
								ipcOptions.setHeight(height);
								ipcOptions.setLegend(getLegendPosition());
								ImageBarChart line = new ImageBarChart(ChartData, ipcOptions);
								chart.setWidget(line);
							}
						};
						VisualizationUtils.loadVisualizationApi(onLoadCallback, ImageBarChart.PACKAGE);
					}
					
					break;
				case 5:
					ChartData = ChartDataTable.dataIntoSeries(BaseData, cbbX.getSelectedIndex(), cbbY.getSelectedIndex()+2, cbbSeries.getSelectedIndex(),true);
					if (chckbxInteractive.getValue()){
						Runnable onLoadCallback = new Runnable() {
							
							@Override
							public void run() {
								AreaChart area = new AreaChart(ChartData, createCoreChartOptions(txtbxChartTitle.getText(), getLegendPosition(), width, height));
								chart.setWidget(area);
							}
						};
						VisualizationUtils.loadVisualizationApi(onLoadCallback, AreaChart.PACKAGE);
					} else {
						Runnable onLoadCallback = new Runnable() {
							
							@Override
							public void run() {
								ImageAreaChart.Options ipcOptions = ImageAreaChart.Options.create();
								ipcOptions.setTitle(txtbxChartTitle.getText());
								ipcOptions.setWidth(width);
								ipcOptions.setHeight(height);
								ipcOptions.setLegend(getLegendPosition());
								ImageAreaChart area = new ImageAreaChart(ChartData, ipcOptions);
								chart.setWidget(area);
							}
						};
						VisualizationUtils.loadVisualizationApi(onLoadCallback, ImageAreaChart.PACKAGE);
					}

					break;
				default:
							
					ChartData = ChartDataTable.filteredTable(BaseData, 0, cbbY.getSelectedIndex()+2, 1, cbbSeries.getItemText(cbbSeries.getSelectedIndex()));
					if (chckbxInteractive.getValue()){
						Runnable onLoadCallback = new Runnable() {
							
							@Override
							public void run() {
								PieChart pie = new PieChart(ChartData, createCoreChartOptions(txtbxChartTitle.getText(), getLegendPosition(), width, height));
								chart.setWidget(pie);
							}
						};
						VisualizationUtils.loadVisualizationApi(onLoadCallback, PieChart.PACKAGE);
					} else {
						Runnable onLoadCallback = new Runnable() {
							
							@Override
							public void run() {
								ImagePieChart.Options ipcOptions = ImagePieChart.Options.create();
								ipcOptions.setTitle(txtbxChartTitle.getText());
								ipcOptions.setWidth(width);
								ipcOptions.setHeight(height);
								ipcOptions.setLegend(getLegendPosition());
								ImagePieChart pie = new ImagePieChart(ChartData, ipcOptions);
								chart.setWidget(pie);
							}
						};
						VisualizationUtils.loadVisualizationApi(onLoadCallback, ImagePieChart.PACKAGE);
					}
					
					break;
				}
				chartoptions.hide();
			}
		});
		hpChartSubmit.add(btnOk);
		hpChartSubmit.setCellHorizontalAlignment(btnOk, HasHorizontalAlignment.ALIGN_RIGHT);
		
		Button btnCancel = new Button("Cancel");
		btnCancel.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				chartoptions.hide();
			}
		});
		hpChartSubmit.add(btnCancel);
		hpChartSubmit.setCellHorizontalAlignment(btnCancel, HasHorizontalAlignment.ALIGN_RIGHT);
		
		VerticalPanel verticalPanel = new VerticalPanel();
		verticalPanel.setStyleName("notes");
		verticalPanel.setSpacing(2);
		verticalPanel.setBorderWidth(0);
		dockPanel.add(verticalPanel, DockPanel.WEST);
		verticalPanel.setSize("204px", "100%");
		
		image = new Image("images/pie.png");
		verticalPanel.add(image);
		image.setSize("188px", "112px");
		
		htmlChartDesc = new HTML("Shows percentage values as a slice of a pie", true);
		verticalPanel.add(htmlChartDesc);
		htmlChartDesc.setSize("178px", "47px");
		
		Grid gridChartComponents = new Grid(7, 2);
		gridChartComponents.setCellPadding(5);
		dockPanel.add(gridChartComponents, DockPanel.CENTER);
		gridChartComponents.setSize("100%", "100%");
		
		lblChartType = new Label("Chart Type");
		gridChartComponents.setWidget(0, 0, lblChartType);
		
		cbbChartType = new ListBox();
		cbbChartType.addChangeHandler(new ChangeHandler() {
			public void onChange(ChangeEvent event) {
				int selitem = cbbChartType.getSelectedIndex();
				switch (selitem) {
				case 1:
					image.setUrl("images/scatter.png");
					htmlChartDesc.setHTML("Scatter Plot");
					chckbxInteractive.setEnabled(false);
					chckbxInteractive.setValue(true);
					lblX.setText("X");
					populateListBox(cbbX, variables);
					cbbX.setEnabled(true);
					lblSeries.setText("Anoher Variable");
					populateListBox(cbbSeries, variables);
					cbbSeries.setEnabled(true);
					break;

				case 2:
					image.setUrl("images/line.png");
					htmlChartDesc.setHTML("Line Chart");
					chckbxInteractive.setEnabled(true);
					chckbxInteractive.setValue(true);
					commonChartOptions();
					break;
				case 3:
					image.setUrl("images/column.png");
					htmlChartDesc.setHTML("Column Chart");
					chckbxInteractive.setEnabled(false);
					chckbxInteractive.setValue(true);
					commonChartOptions();
					break;
				case 4:
					image.setUrl("images/bar.png");
					htmlChartDesc.setHTML("Bar Chart");
					chckbxInteractive.setEnabled(true);
					chckbxInteractive.setValue(true);
					commonChartOptions();
					break;
				case 5:
					image.setUrl("images/area.png");
					htmlChartDesc.setHTML("Area Chart");
					chckbxInteractive.setEnabled(true);
					chckbxInteractive.setValue(true);
					commonChartOptions();
					break;
				default:
					image.setUrl("images/pie.png");
					htmlChartDesc.setHTML("Shows percentage values as a slice of a pie");
					chckbxInteractive.setEnabled(true);
					chckbxInteractive.setValue(true);
					lblSeries.setText("year");
					populateListBox(cbbSeries, years);
					cbbSeries.setEnabled(true);
					lblX.setText("Parts");
					cbbX.clear();
					cbbX.addItem("region");
					cbbX.setEnabled(false);
					break;
				}
			}
		});
		gridChartComponents.setWidget(0, 1, cbbChartType);
		cbbChartType.addItem("Pie Chart");
		cbbChartType.addItem("Scatter Plot");
		cbbChartType.addItem("Line Graph");
		cbbChartType.addItem("Column Chart");
		cbbChartType.addItem("Bar Chart");
		cbbChartType.addItem("Area Chart");
//		cbbChartType.addItem("Map");
		cbbChartType.addItem("");
		
		chckbxInteractive = new CheckBox("Interactive");
		gridChartComponents.setWidget(1, 1, chckbxInteractive);
		chckbxInteractive.setValue(true);
		
		Label lblTitle = new Label("Title");
		gridChartComponents.setWidget(2, 0, lblTitle);
		
		txtbxChartTitle = new TextBox();
		txtbxChartTitle.setText("Chart Title");
		gridChartComponents.setWidget(2, 1, txtbxChartTitle);
		txtbxChartTitle.setWidth("274px");
		
		Label lblLegendPosition = new Label("Legend Position");
		lblLegendPosition.setWordWrap(false);
		gridChartComponents.setWidget(3, 0, lblLegendPosition);
		
		cbbLPosition = new ListBox();
		cbbLPosition.addItem("Top");
		cbbLPosition.addItem("Right");
		cbbLPosition.addItem("Bottom");
		cbbLPosition.addItem("Left");
		cbbLPosition.addItem("None");
		gridChartComponents.setWidget(3, 1, cbbLPosition);
		
		lblX = new Label("X");
		gridChartComponents.setWidget(4, 0, lblX);
		
		cbbX = new ListBox();
		gridChartComponents.setWidget(4, 1, cbbX);
		
		lblY = new Label("Value");
		gridChartComponents.setWidget(5, 0, lblY);
		
		cbbY = new ListBox();
		gridChartComponents.setWidget(5, 1, cbbY);
		
		lblSeries = new Label("Series");
		gridChartComponents.setWidget(6, 0, lblSeries);
		
		cbbSeries = new ListBox();
		gridChartComponents.setWidget(6, 1, cbbSeries);
		
		chartoptions.center();
		chartoptions.show();
		
	}
	
	private void commonChartOptions(){
		lblX.setText("X");
		populateListBox(cbbX, catcols);
		cbbX.setEnabled(true);
		lblSeries.setText("Series");
		populateListBox(cbbSeries, catcols);
		cbbSeries.setEnabled(true);
	}
	
	private LegendPosition getLegendPosition(){
		int lp = cbbLPosition.getSelectedIndex();
		LegendPosition lgpos;
		switch (lp) {
		case 1:
			lgpos = LegendPosition.RIGHT; 
			break;
		case 2:
			lgpos = LegendPosition.BOTTOM; 
			break;
		case 3:
			lgpos = LegendPosition.LEFT; 
			break;
		case 4:
			lgpos = LegendPosition.NONE; 
			break;

		default:
			lgpos = LegendPosition.TOP;
			break;
		}
		return lgpos;
	}
	public void setOkButtonClickHandler(ClickHandler handler){
		btnOk.addClickHandler(handler);
	}
	
	public int getItemID(){
		return itemid;
	}
	
	public SimplePanel getChart() {
		return chart;
	}
	
	private void getYears(AbstractDataTable basedata){
		years = ChartDataTable.getUniqueColumnVals(basedata, 1);
		Collections.sort(years);
	}
	
	private void getRegions(AbstractDataTable basedata){
		regions = ChartDataTable.getUniqueColumnVals(basedata, 0);
		Collections.sort(regions);
	}
	
	private void getVariables(AbstractDataTable basedata){
		variables = new ArrayList<String>();
		for (int i = 2; i < basedata.getNumberOfColumns(); i++) {
			variables.add(basedata.getColumnLabel(i));
		}
	}
	
	private void getCategorizeColumns(AbstractDataTable basedata){
		catcols = new ArrayList<String>();
		for (int i = 0; i < basedata.getNumberOfColumns(); i++) {
			if(basedata.getColumnType(i)!=ColumnType.NUMBER) catcols.add(basedata.getColumnLabel(i));
		}
	}

	private void populateListBox(ListBox listbox, ArrayList<String> items){
		listbox.clear();
		for (int i = 0; i < items.size(); i++) {
			listbox.addItem(items.get(i));
		}
	}
	
	public Options createCoreChartOptions(String title, LegendPosition legendpos, int w, int h){
		Options options = Options.create();
		options.setWidth(w);
		options.setHeight(h);
		options.setTitle(title);
		options.setLegend(legendpos);
		options.set("is3D", "true");
		return options;
		
	}
	
	public GeoMap.Options createMapOptions(int w, int h){
		GeoMap.Options options = GeoMap.Options.create();
		options.setWidth(w);
        options.setHeight(h);
        options.setShowLegend(true);
        options.setShowZoomOut(true);
        options.setDataMode(DataMode.REGIONS);
        options.setColors(0xDDDDDD,0xCE0000,0xFF9E00,0xF7D708,0x9CCF31);
        return options;
	}

	

	public Label getLblChartType() {
		return lblChartType;
	}
	public Label getLblSeries() {
		return lblSeries;
	}
	public Label getLblY() {
		return lblY;
	}
	public Label getLblX() {
		return lblX;
	}
	
	public void resize(int w, int h){
		width = w;
		height =h;
		btnOk.click();
	}
}
