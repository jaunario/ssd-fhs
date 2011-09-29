package org.irri.households.client.ui.charts;

import org.irri.households.client.UtilsRPC;
import org.irri.households.client.utils.NumberUtils;
import org.irri.households.client.utils.RPCUtils;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.Frame;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.MenuItem;
import com.google.gwt.user.client.Command;
import com.google.gwt.visualization.client.AbstractDataTable;
import com.google.gwt.visualization.client.VisualizationUtils;
import com.google.gwt.visualization.client.visualizations.Table;
import com.google.gwt.user.client.ui.DeckPanel;
import com.google.gwt.user.client.ui.DecoratedTabBar;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;

public class MultiChartPanel extends Composite {
	String basequery = "";
	String table;
	int totalrows = 0;
	int lastrec = 100;
	String[][] rawdata;
	AbstractDataTable tabledata;
	private int page;
	public int[] numerics;
	private DeckPanel deckChartPager;
	private VerticalPanel vpTablePage;
	private Label lblRowCountDetails;
	private VerticalPanel verticalPanel;
	private Button btnFirst;
	private Button btnPrevious;
	private Button btnNext;
	private Button btnLast;
	private DecoratedTabBar dtbChartPageSelector;
	private HorizontalPanel horizontalPanel_2;
	private Button btnClear;
	private HorizontalPanel horizontalPanel_3;
	/**
	 * @wbp.parser.constructor
	 */
	public MultiChartPanel(){
		initPanels();
	}
	public MultiChartPanel(String[][] data) {
		numerics = NumberUtils.createIntSeries(2, data[0].length-1, 1);
		tabledata = ChartDataTable.create(data, numerics);
		initPanels();
	}
	
	private void initPanels(){
		DockPanel ChartsWrapper = new DockPanel();
		initWidget(ChartsWrapper);
		
		MenuBar mbTableOptions = new MenuBar(false);
		ChartsWrapper.add(mbTableOptions, DockPanel.NORTH);
		mbTableOptions.setAutoOpen(true);
		mbTableOptions.setAnimationEnabled(true);
		
		MenuItem mntmRedraw = new MenuItem("Refit to Panel", false, new Command() {
			public void execute() {
				drawTable();
				if (deckChartPager.getWidgetCount()>1){
					for (int i = 1; i < deckChartPager.getWidgetCount(); i++) {
						WRSChart thischart = (WRSChart) deckChartPager.getWidget(i);
						thischart.resize(deckChartPager.getOffsetWidth(), deckChartPager.getOffsetHeight());
					}
				}
			}
		});
		mntmRedraw.setTitle("Click here refit charts/table into the panel when you resize the browser");
		mbTableOptions.addItem(mntmRedraw);
		
		MenuItem mntmDownload = new MenuItem("Download", false, new Command() {
			public void execute() {
				int tab = deckChartPager.getVisibleWidget();
				switch (tab) {
				case 0:
					AsyncCallback<String> downloadAsyncCallback = new AsyncCallback<String>() {
						
						@Override
						public void onSuccess(String result) {
							Frame myframe = new Frame(result);
							deckChartPager.add(myframe);
						}
						
						@Override
						public void onFailure(Throwable caught) {
							PopupPanel msg = new PopupPanel();
							msg.add(new HTML("Operation failed. Please try Again. If the problem persists please contact us. (Indicate the variables chosen)"));
							msg.setGlassEnabled(true);
							msg.center();
							msg.show();
						}
					};
					RPCUtils.getService("mysqlservice").downloadCSVFromQuery(basequery, downloadAsyncCallback);
	                                	
					break;

				default:
					break;
				}
			}
		});
		mntmDownload.setHTML("Download Data");
		mbTableOptions.addItem(mntmDownload);

		deckChartPager = new DeckPanel();
		ChartsWrapper.add(deckChartPager, DockPanel.CENTER);
		ChartsWrapper.setCellHeight(deckChartPager, "100%");
		ChartsWrapper.setCellWidth(deckChartPager, "65%");
		deckChartPager.setSize("100%", "100%");
		deckChartPager.setAnimationEnabled(true);
		
		vpTablePage = new VerticalPanel();
		vpTablePage.setStyleName("gwt-Label");
		deckChartPager.add(vpTablePage);
		vpTablePage.setSize("100%", "100%");
		
		HorizontalPanel horizontalPanel_1 = new HorizontalPanel();
		horizontalPanel_1.setVerticalAlignment(HasVerticalAlignment.ALIGN_BOTTOM);
		horizontalPanel_1.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
		ChartsWrapper.add(horizontalPanel_1, DockPanel.SOUTH);
		horizontalPanel_1.setSize("100%", "23px");

		dtbChartPageSelector = new DecoratedTabBar();
		dtbChartPageSelector.addTab("Table");
		dtbChartPageSelector.setWidth("100%");
		horizontalPanel_1.add(dtbChartPageSelector);
		dtbChartPageSelector.addSelectionHandler(new SelectionHandler<Integer>() {
			public void onSelection(SelectionEvent<Integer> event) {
				deckChartPager.showWidget(event.getSelectedItem());				
			}
		});
		
		verticalPanel = new VerticalPanel();
		verticalPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		verticalPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
		horizontalPanel_1.add(verticalPanel);
		
		HorizontalPanel horizontalPanel = new HorizontalPanel();
		horizontalPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_BOTTOM);
		horizontalPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
		horizontalPanel.setWidth("100%");
		verticalPanel.add(horizontalPanel);
		
		btnFirst = new Button("First");
		btnFirst.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				page = 0;
				runQuery(basequery);
			}
		});
		
		horizontalPanel_2 = new HorizontalPanel();
		horizontalPanel.add(horizontalPanel_2);
		
		lblRowCountDetails = new Label("Display row count details here.");
		horizontalPanel_2.add(lblRowCountDetails);
		lblRowCountDetails.setVisible(false);
		horizontalPanel.add(btnFirst);
		
		btnPrevious = new Button("Previous");
		btnPrevious.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				page--;
				runQuery(basequery);
			}
		});
		horizontalPanel.add(btnPrevious);
		
		btnNext = new Button("Next");
		btnNext.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				page++;
				runQuery(basequery);
			}
		});
		horizontalPanel.add(btnNext);
		
		btnLast = new Button("Last");
		btnLast.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				page = totalrows/100;
				runQuery(basequery);
			}
		});
		
		horizontalPanel.add(btnLast);
		
		horizontalPanel_3 = new HorizontalPanel();
		horizontalPanel_3.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
		horizontalPanel.add(horizontalPanel_3);
		horizontalPanel_3.setWidth("60px");
		
		btnClear = new Button("Clear");
		horizontalPanel_3.add(btnClear);
		
		
		
	}
	
	public void checkPaginOptions(){
		
	}
	public DeckPanel getDeckPanel() {
		return deckChartPager;
	}
	
	public int[] getNumericColsofTable(String SelectedTable){
		int[] numcols = new int[0];
		if (SelectedTable.equalsIgnoreCase("surveys")){
			numcols = new int[6];
			numcols[0]= 1;
			numcols[1]= 2;
			numcols[2]= 7;
			numcols[3]= 8;
			numcols[4]= 9;
			numcols[5]= 10;
		}else if(SelectedTable.equalsIgnoreCase("households")){
			numcols = new int[4];
			numcols[0]= 1;
			numcols[1]= 5;
			numcols[2]= 7;
			numcols[3]= 8;
		}else if(SelectedTable.equalsIgnoreCase("assets")){
			numcols = new int[5];
			numcols[0]= 1;
			numcols[1]= 4;
			numcols[2]= 5;
			numcols[3]= 6;
			numcols[4]= 7;	
		}else if(SelectedTable.equalsIgnoreCase("land_profile")){
			numcols = new int[3];
			numcols[0]= 1;
			numcols[1]= 2;
			numcols[2]= 7;
		}else if(SelectedTable.equalsIgnoreCase("land_use")){
			numcols = new int[6];
			numcols[0]= 2;
			numcols[1]= 3;
			numcols[2]= 4;
			numcols[3]= 10;
			numcols[4]= 11;
			numcols[5]= 12;
		}else if(SelectedTable.equalsIgnoreCase("crop_disposal")){
			numcols = new int[12];
			numcols[0]= 3;
			numcols[1]= 4;
			numcols[2]= 5;
			numcols[3]= 6;
			numcols[4]= 7;
			numcols[5]= 8;
			numcols[6]= 9;
			numcols[7]= 10;
			numcols[8]= 11;
			numcols[9]= 12;
			numcols[10]= 13;
			numcols[11]= 14;
		}else if(SelectedTable.equalsIgnoreCase("income")){
			numcols = new int[2];
			numcols[0]= 3;
			numcols[1]= 4;
		}else if(SelectedTable.equalsIgnoreCase("consump_expend")){
			numcols = new int[2];
			numcols[0]= 3;
			numcols[1]= 5;
		}else if(SelectedTable.equalsIgnoreCase("credit")){
			numcols = new int[3];
			numcols[0]= 3;
			numcols[1]= 4;
			numcols[2]= 5;
		}else if(SelectedTable.equalsIgnoreCase("ot_car_partial")){
			numcols = new int[13];
			numcols[0]= 1;
			numcols[1]= 2;
			numcols[2]= 3;
			numcols[3]= 4;
			numcols[4]= 5;
			numcols[5]= 6;
			numcols[6]= 7;
			numcols[7]= 8;
			numcols[8]= 9;
			numcols[9]= 10;
			numcols[10]= 11;
			numcols[11]= 12;
			numcols[12]= 13;
		}else if(SelectedTable.equalsIgnoreCase("ot_quantity_of_input")){
			numcols = new int[9];
			numcols[0]= 1;
			numcols[1]= 2;
			numcols[2]= 3;
			numcols[3]= 4;
			numcols[4]= 5;
			numcols[5]= 6;
			numcols[6]= 7;
			numcols[7]= 8;
			numcols[8]= 9;
		}
		return numcols;
		
	}
	
	public void setBaseData(String[][] data, String table){
		//rawdata = data;
		//numerics = getNumericColsofTable(table);
		//numerics = NumberUtils.createIntSeries(2, data[0].length-1, 1);
		/*switch (key) {
		case 0:
			break;
		case 0:
			break;
		default:
			break;
		}*/
		//runQuery(query);
	}
	
	public void setQuery(String query, String table){
		page=0;
		basequery = query;
		numerics = getNumericColsofTable(table);
		getFullQueryRecordCount();
		//numerics = NumberUtils.createIntSeries(2, data[0].length-1, 1);
		/*switch (key) {
		case 0:
			break;
		case 0:
			break;
		default:
			break;
		}*/
		runQuery(query);
	}
	
	public void setQueryVarCheckBox(String query, String table, int[] numcols){
		page=0;
		basequery = query;
		//numerics = getNumericColsofTable(table);
		if (numcols==null){
			//numerics = getNumericColsofTable(table);
			numerics[0] = 999;
		}else numerics = numcols;
		getFullQueryRecordCount();
		//numerics = NumberUtils.createIntSeries(2, data[0].length-1, 1);
		/*switch (key) {
		case 0:
			break;
		case 0:
			break;
		default:
			break;
		}*/
		runQuery(query);
	}
	
	
	public void getFullQueryRecordCount(){
		//String cntquery = basequery.replace("*", "count(*)");
		int indexoffrom = basequery.indexOf("FROM");
		String cntquery = "SELECT COUNT(*) " + basequery.substring(indexoffrom);
		
		final AsyncCallback<String[][]> RecCountCallback = new AsyncCallback<String[][]>() {
			
			@Override
			public void onSuccess(String[][] result) {
				totalrows = Integer.parseInt(result[1][0]);
			}
			
			@Override
			public void onFailure(Throwable caught) {
				throw new UnsupportedOperationException("Not supported yet.");
			}
		};
		
		UtilsRPC.getService("mysqlservice").RunSELECT(cntquery, RecCountCallback);
	}
	
	public void drawTable(){
		if(vpTablePage.getWidgetCount()>0) vpTablePage.clear();
		Runnable onLoadCallback = new Runnable() {
			@Override
			public void run() {
				Table table = new Table(tabledata, setTableSize(deckChartPager.getOffsetWidth(), deckChartPager.getOffsetHeight()));
				table.setSize("800px", "310px");
				vpTablePage.clear();
				deckChartPager.clear();
				vpTablePage.add(table);
				deckChartPager.add(vpTablePage);
				deckChartPager.showWidget(0);
				updatePagerButtons();
			}
		};
		
		VisualizationUtils.loadVisualizationApi(onLoadCallback, Table.PACKAGE);
	};
	

	public Table.Options setTableSize(int w, int h) {
    	Table.Options options = Table.Options.create();
    	options.setHeight((h-5)+"px");
    	options.setWidth((w-5)+"px");
    	options.setShowRowNumber(true);
    	return options;
    }


	private void runQuery(String query){
		final AsyncCallback<String[][]> GetDataTable = new AsyncCallback<String[][]>() {

			public void onFailure(Throwable caught) {
				throw new UnsupportedOperationException("Not supported yet.");
			}

			public void onSuccess(String[][] result) {
				final String[][] out = result;
				Runnable onLoadCallback = new Runnable() {
					public void run() {
						tabledata = ChartDataTable.create(out, numerics);						
						lastrec = page*100+tabledata.getNumberOfRows();
						drawTable();
					}
				};    			
				VisualizationUtils.loadVisualizationApi(onLoadCallback, Table.PACKAGE);				
			}    		
		};
		UtilsRPC.getService("mysqlservice").RunSELECT(basequery+ "LIMIT 100 OFFSET " + page*100/*+1*/, GetDataTable);
	}
	
	public void updatePagerButtons(){
		int startrow = page*100+1;
		
		btnFirst.setEnabled(true);
		btnPrevious.setEnabled(true);
		btnNext.setEnabled(true);
		btnLast.setEnabled(true);

		lblRowCountDetails.setText("Displaying rows " +startrow+ " to "+lastrec+" of "+totalrows+" rows.     ");
		lblRowCountDetails.setVisible(true);

		if (lastrec>=totalrows) {btnNext.setEnabled(false); btnLast.setEnabled(false);}
		if (page==0) {btnFirst.setEnabled(false); btnPrevious.setEnabled(false);}
		RootPanel.get("Loading-Message").setVisible(false);
	}
	public Label getLblRowCountDetails() {
		return lblRowCountDetails;
	}
	
	public void SetClearBtn(ClickHandler click){
		btnClear.addClickHandler(click);
	}
}
