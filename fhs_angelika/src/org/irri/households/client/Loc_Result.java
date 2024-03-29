/*This part is shown when the browse data button of the search by loaction category is clicked. */
package org.irri.households.client;

import org.irri.households.client.ui.charts.MultiChartPanel;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.Label;


public class Loc_Result extends Composite {	
	private final String ProjVarsSql2 = "SELECT r.report_title, GROUP_CONCAT(DISTINCT f.repvariables ORDER BY sort SEPARATOR ';'), " +
			"GROUP_CONCAT(DISTINCT f.description ORDER BY sort SEPARATOR ';'), r.table_name " +
			"FROM reports r, repfields f, report_fields rf, available a left join surveys on site=substring(surveys.site_id,1,8)" +
			"WHERE a.report_id=r.report_id and r.report_id=rf.report_id and rf.field_id=f.field_id AND ";
	
	public ListBox TablesListBox;
	public HorizontalPanel CheckboxHPanel;
	public VerticalPanel LocResVPanel;
	public HorizontalPanel LocResHPanel;
	public SimplePanel LocResSimplePanel;
	public ListBox FilterByYear;
	private HorizontalPanel LocResHPanel3;
	private VarCheckbox varCheckbox;
	public Label lblFetchedData;
	
	private MultiChartPanel mcpResultPanel;
	private String SelectedTable;
	
	String varCheckBoxQuery;
	int[] numofnumcols; //number or numeric columns/variables selected
	
		public Loc_Result(final String CntryID) {
		varCheckbox = new VarCheckbox();
		lblFetchedData = new Label();
		
		LocResVPanel = new VerticalPanel();
		initWidget(LocResVPanel);
		LocResVPanel.setHeight("700px");
		
		HorizontalPanel LocResHPanel = new HorizontalPanel();
		LocResHPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		LocResHPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		LocResHPanel.setSpacing(2);
		LocResVPanel.add(LocResHPanel);
		LocResHPanel.setSize("835px", "235px");
		
		TablesListBox = new ListBox();
		TablesListBox.setStyleName("FHS-TablesListBox");
		LocResHPanel.add(TablesListBox);
		TablesListBox.setSize("280px", "230px");
		TablesListBox.setVisibleItemCount(5);
		TablesListBox.addChangeHandler(new ChangeHandler() {
			@Override
			public void onChange(ChangeEvent event) { //executed every time a table is chosen. this part populates the variables and years available for the table as well as the data that meets the chosen filters for the variables and years 			
				RootPanel.get("Loading-Message").setVisible(true);
				
				CheckboxHPanel.clear();
				FilterByYear.clear();
				mcpResultPanel.vpTablePage.clear();

				String SelectedCountry = CntryID;
				SelectedTable = TablesListBox.getValue(TablesListBox.getSelectedIndex());
				String site = "";
				String site2 = "";
				String select = "";
				String projvarssql = "";
				varCheckBoxQuery = "";
				
				if(TablesListBox.getSelectedIndex()>=0){ //executed whenever the user selects a table. populates the available variables and years as well as the data
					projvarssql = ProjVarsSql2 + ProjVarsSqlWhereClause4(SelectedCountry, SelectedTable);
					displayCountryVars(projvarssql + " GROUP BY r.report_id"); //calls method for dispaying the variables in checkbox format
					if (SelectedTable.equalsIgnoreCase("ot_car_partial")||SelectedTable.equalsIgnoreCase("ot_quantity_of_input")){
	                    site = "LEFT(idp_code,11)";
	                    site2 = "idp_code";
	                } else if (SelectedTable.equalsIgnoreCase("households")||SelectedTable.equalsIgnoreCase("assets") ||SelectedTable.equalsIgnoreCase("land_use") ||SelectedTable.equalsIgnoreCase("land_profile") ||SelectedTable.equalsIgnoreCase("crop_disposal") ||SelectedTable.equalsIgnoreCase("income") ||SelectedTable.equalsIgnoreCase("credit") ||SelectedTable.equalsIgnoreCase("consump_expend")){
	                    site = "LEFT(hh_code,11)";
	                    site2 = "hh_code";
	                }else {
	                    site = "LEFT(site_id,11)";
	                    site2 = "site_id";
	                }
					select = "SELECT * FROM " + SelectedTable + " WHERE " + site + " in (SELECT LEFT(site_id,11) FROM surveys s"+ CountryWhereClause(SelectedCountry) + ") ";
					if (SelectedTable.equalsIgnoreCase("surveys")){
						displayLocTabYr("SELECT DISTINCT s.survey_year FROM surveys s WHERE country='"+SelectedCountry+"'"); //calls method for displaying years of selected table
					}else{
						displayLocTabYr("SELECT DISTINCT s.survey_year FROM "+SelectedTable+" JOIN surveys s ON LEFT("+site2+",11)=LEFT(s.site_id,11) WHERE country='"+SelectedCountry+"'");
					}
					mcpResultPanel.setQuery(select, SelectedTable);	
					LocResSimplePanel.setWidget(mcpResultPanel);
				}
			}	
		});
		
		CheckboxHPanel = new HorizontalPanel();
		CheckboxHPanel.setStyleName("FHS-TablesListBox");
		LocResHPanel.add(CheckboxHPanel);
		CheckboxHPanel.setSize("421px", "230px");
		
		FilterByYear = new ListBox(true);
		FilterByYear.setStyleName("FHS-TablesListBox");
		LocResHPanel.add(FilterByYear);
		FilterByYear.setVisibleItemCount(5);
		FilterByYear.setSize("139px", "230px");
		FilterByYear.addChangeHandler(new ChangeHandler() {
			@Override
			public void onChange(ChangeEvent event) { //executed whenever a year is selected or unselected. this refreshes the table showing the data.

				mcpResultPanel.vpTablePage.clear();

				String SelectedCountry = CntryID;
				String SelectedTable = TablesListBox.getValue(TablesListBox.getSelectedIndex());
				String SelectedYear = "";
				String SelectedYear_Sql = "";
				String SelectedYearSql = "";
				String site = "";
				String select = "";
				if(TablesListBox.getSelectedIndex()>=0){
					if (SelectedTable.equalsIgnoreCase("ot_car_partial")||SelectedTable.equalsIgnoreCase("ot_quantity_of_input")){
	                    site = "LEFT(idp_code,11)";
	                } else if (SelectedTable.equalsIgnoreCase("households")||SelectedTable.equalsIgnoreCase("assets") ||SelectedTable.equalsIgnoreCase("land_use") ||SelectedTable.equalsIgnoreCase("land_profile") ||SelectedTable.equalsIgnoreCase("crop_disposal") ||SelectedTable.equalsIgnoreCase("income") ||SelectedTable.equalsIgnoreCase("credit") ||SelectedTable.equalsIgnoreCase("consump_expend")){
	                    site = "LEFT(hh_code,11)";
	                }else {
	                    site = "LEFT(site_id,11)";
	                }
					for (int i = 0; i < FilterByYear.getItemCount(); i++) { //loops through the years list box to check which years are selected
						if(FilterByYear.isItemSelected(i)){ // Those selected will be used in the query for getting the data from the database
							SelectedYear = FilterByYear.getValue(i);
							SelectedYear_Sql = "survey_year="+SelectedYear;
							SelectedYearSql = SelectedYearSql + SelectedYear_Sql + " or ";	
						}
					}
					SelectedYearSql = " AND ("+SelectedYearSql.substring(0,SelectedYearSql.length()-4)+")) ";
					
					if (!varCheckBoxQuery.equals("")){ //checks if variables checkbox have checks or not. if there are checks, the query will just get all variables
						String select1 = varCheckBoxQuery.substring(0,varCheckBoxQuery.length()-2); 
						select = select1+SelectedYearSql; 
					}else select = "SELECT * FROM " + SelectedTable + " WHERE " + site + " in (SELECT LEFT(site_id,11) FROM surveys s  where country='"+SelectedCountry+"'"+SelectedYearSql;
						
					mcpResultPanel.setQueryVarCheckBox(select, SelectedTable, numofnumcols);
					LocResSimplePanel.setWidget(mcpResultPanel);
					RootPanel.get("Loading-Message").setVisible(false);
				}
			}
		});    
		
		varCheckbox.addChangeHandler(new ChangeHandler() {
			@Override
			public void onChange(ChangeEvent event) { //executed whenever a variable is checked or unchecked in the checkbox. this refreshes the list of years and the data	
				RootPanel.get("Loading-Message").setVisible(true);

				FilterByYear.clear();
				mcpResultPanel.vpTablePage.clear();

				String SelectedCountry = CntryID;				
				String SelectedTable = TablesListBox.getValue(TablesListBox.getSelectedIndex());
				String selcols = "";
				String site2 = "";
				String numcols = "";
				numofnumcols = new int[0];
				int num = 0;
				int numcol = 0;
				if (SelectedTable.equalsIgnoreCase("ot_car_partial")||SelectedTable.equalsIgnoreCase("ot_quantity_of_input")){
                    site2 = "idp_code";
                } else if (SelectedTable.equalsIgnoreCase("households")||SelectedTable.equalsIgnoreCase("assets") ||SelectedTable.equalsIgnoreCase("land_use") ||SelectedTable.equalsIgnoreCase("land_profile") ||SelectedTable.equalsIgnoreCase("crop_disposal") ||SelectedTable.equalsIgnoreCase("income") ||SelectedTable.equalsIgnoreCase("credit") ||SelectedTable.equalsIgnoreCase("consump_expend")){
                    site2 = "hh_code";
                }else {
                    site2 = "site_id";
                }
				for (int i = 0; i < varCheckbox.getItemCount(); i++) { //takes note if the selected variable/column is numeric or not. this is needed in the multichartpanel.java's getNumericColsofTable method
					if(varCheckbox.getItem(i).getValue()){
						String colname = varCheckbox.getItem(i).getName();
						if (colname.equalsIgnoreCase("project")){
							colname = "project_id";
						}

						if(SelectedTable.equalsIgnoreCase("surveys")){
							if(colname.equalsIgnoreCase("project_id")||colname.equalsIgnoreCase("survey_year")||colname.equalsIgnoreCase("samplesize")||colname.equalsIgnoreCase("lat")||colname.equalsIgnoreCase("long")||colname.equalsIgnoreCase("uncertainty")){
								num = num+1;
								numcols = numcols + numcol + ",";
							}	
						}else if(SelectedTable.equalsIgnoreCase("households")){
							if(colname.equalsIgnoreCase("hh_id")||colname.equalsIgnoreCase("age")||colname.equalsIgnoreCase("yrs_sch")||colname.equalsIgnoreCase("yrs_farming")){
								num = num+1;
								numcols = numcols + numcol + ",";
							}
						}else if(SelectedTable.equalsIgnoreCase("assets")){
							if(colname.equalsIgnoreCase("hh_id")||colname.equalsIgnoreCase("asset_qty")||colname.equalsIgnoreCase("purchase_yr")||colname.equalsIgnoreCase("purchase_val")||colname.equalsIgnoreCase("present_val")){
								num = num+1;
								numcols = numcols + numcol + ",";
							}
						}else if(SelectedTable.equalsIgnoreCase("land_profile")){
							if(colname.equalsIgnoreCase("parcel_id")||colname.equalsIgnoreCase("plot_id")||colname.equalsIgnoreCase("area")){
								num = num+1;
								numcols = numcols + numcol + ",";
							}
						}else if(SelectedTable.equalsIgnoreCase("land_use")){
							if(colname.equalsIgnoreCase("parcel_id")||colname.equalsIgnoreCase("plot_id")||colname.equalsIgnoreCase("area")||colname.equalsIgnoreCase("kg_per_unit")||colname.equalsIgnoreCase("prod")||colname.equalsIgnoreCase("prod_val")){
								num = num+1;
								numcols = numcols + numcol + ",";
							}
						}else if(SelectedTable.equalsIgnoreCase("crop_disposal")){
							if(colname.equalsIgnoreCase("prod_kg")||colname.equalsIgnoreCase("home_consump")||colname.equalsIgnoreCase("sales")||colname.equalsIgnoreCase("give_away")||colname.equalsIgnoreCase("seed")||colname.equalsIgnoreCase("land_rent")||colname.equalsIgnoreCase("credit")||colname.equalsIgnoreCase("ht_share")||colname.equalsIgnoreCase("th_share")||colname.equalsIgnoreCase("other_labor")||colname.equalsIgnoreCase("perm_labor")){
								num = num+1;
								numcols = numcols + numcol + ",";
							}
						}else if(SelectedTable.equalsIgnoreCase("income")){
							if(colname.equalsIgnoreCase("income")||colname.equalsIgnoreCase("percent")){
								num = num+1;
								numcols = numcols + numcol + ",";
							}
						}else if(SelectedTable.equalsIgnoreCase("consump_expend")){
							if(colname.equalsIgnoreCase("qty_per_year")||colname.equalsIgnoreCase("val_exp")){
								num = num+1;
								numcols = numcols + numcol + ",";
							}
						}else if(SelectedTable.equalsIgnoreCase("credit")){
							if(colname.equalsIgnoreCase("cash_amt")||colname.equalsIgnoreCase("kind_amt")||colname.equalsIgnoreCase("interest")){
								num = num+1;
								numcols = numcols + numcol + ",";
							}
						}else if(SelectedTable.equalsIgnoreCase("ot_car_partial")){
							if(!colname.equalsIgnoreCase("idp_code")){
								num = num+1;
								numcols = numcols + numcol + ",";
							}
						}else if(SelectedTable.equalsIgnoreCase("ot_quantity_of_input")){
							if(!colname.equalsIgnoreCase("idp_code")){
								num = num+1;
								numcols = numcols + numcol + ",";
							}
						}
						numcol++;

						selcols = selcols +colname+ ",";
					}
				}

				if (numcols!=""){
					numcols = numcols.substring(0, numcols.length()-1);
					numofnumcols = new int[num];
					String[] temp;
					temp = numcols.split(",");
					for (int i=0;i<temp.length;i++ ){
						String tempval=temp[i];
						numofnumcols[i]=Integer.parseInt(tempval);
					}
				}else numofnumcols=null;
				
				if(selcols!=""){ //if the number of selected variables are greater than 0
					selcols = selcols.substring(0, selcols.length()-1);
					varCheckBoxQuery = "SELECT "+selcols+" FROM "+SelectedTable+" WHERE LEFT("+site2+",11) in (SELECT LEFT(site_id,11) FROM surveys s WHERE s.country='"+SelectedCountry+"') ";
					if (SelectedTable.equalsIgnoreCase("surveys")){
						displayLocTabYr("SELECT DISTINCT s.survey_year FROM surveys s WHERE country='"+SelectedCountry+"' ORDER BY s.survey_year");
					}else{
						displayLocTabYr("SELECT DISTINCT s.survey_year FROM "+SelectedTable+" JOIN surveys s ON LEFT("+site2+",11)=LEFT(s.site_id,11) WHERE country='"+SelectedCountry+"' ORDER BY s.survey_year");
					}
					mcpResultPanel.setQueryVarCheckBox(varCheckBoxQuery, SelectedTable, numofnumcols);
					LocResSimplePanel.setWidget(mcpResultPanel);
				}else{
					LocResSimplePanel.clear();
					FilterByYear.setEnabled(false);
				}
				
			}
		});
		
		LocResHPanel3 = new HorizontalPanel();
		LocResHPanel3.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		LocResHPanel3.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		LocResHPanel3.setStyleName("FHS-HorizontalPanelSiteMap");
		LocResVPanel.add(LocResHPanel3);
		LocResVPanel.setCellHeight(LocResHPanel3, "300px");
		LocResVPanel.setCellWidth(LocResHPanel3, "800px");
		
		LocResSimplePanel = new SimplePanel();
		LocResSimplePanel.setStyleName("FHS-TablesListBox");
		LocResHPanel3.clear();
		LocResHPanel3.add(LocResSimplePanel);
		LocResSimplePanel.setSize("841px", "465px");
		
		mcpResultPanel =  new MultiChartPanel();
		mcpResultPanel.getDeckPanel().setSize("100%", "100%");
		LocResSimplePanel.add(mcpResultPanel);
		mcpResultPanel.setSize("100%", "100%");
		
		mcpResultPanel.SetClearBtn(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) { //clears selection
				FilterByYear.clear();
				LocResSimplePanel.clear();
				TablesListBox.setSelectedIndex(-1);
				CheckboxHPanel.clear();
			}
		});
	}
	
	public void selectLocation(String location){
		
	}
	
	private String ProjVarsSqlWhereClause4(String cntry, String tablename){
        String whereclause = "";
        whereclause = " country='" +cntry+ "' AND r.table_name = '" +tablename+ "'";
        return whereclause;
    }
	
	public void displayCountryVars(String sql){ //displays the variables for the selected table in a checkbox format
		varCheckbox.CheckboxVPanel.clear();
        final AsyncCallback<String[][]> FetchDetails = new AsyncCallback<String[][]>() {

            public void onFailure(Throwable caught) {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            public void onSuccess(String[][] result) {
            	varCheckbox.populateCheckboxLoc(result);
                CheckboxHPanel.add(varCheckbox);
            }
        };
        UtilsRPC.getService("mysqlservice").RunSELECT(sql, FetchDetails);
    }

	private String CountryWhereClause(String cntry){
        String whereclause = "";
                whereclause = " WHERE country='"+cntry+"'";
        return whereclause;
    }
	
	public void displayLocTabYr(String sql){
		FilterByYear.clear();
        final AsyncCallback<String[][]> FetchDetails = new AsyncCallback<String[][]>() {
            public void onFailure(Throwable caught) {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            public void onSuccess(String[][] result) { // displays study years in a listbox
                for (int i = 1; i < result.length; i++) {
                	int col = result[0].length;
                	FilterByYear.addItem(result[i][col-1]);
                	FilterByYear.setEnabled(true);
				}
                if (FilterByYear.getItemCount()<2){
                	FilterByYear.setEnabled(false);
                }
                RootPanel.get("Loading-Message").setVisible(false);
            }
        };
        UtilsRPC.getService("mysqlservice").RunSELECT(sql, FetchDetails);
    }
}
