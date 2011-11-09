package org.irri.households.client;

import org.irri.households.client.ui.charts.MultiChartPanel;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;


public class Var_Result extends Composite {
	private final String ProjVarsSql2 = "SELECT r.report_title, GROUP_CONCAT(DISTINCT f.repvariables ORDER BY sort SEPARATOR ';'), " +
			"GROUP_CONCAT(DISTINCT f.description ORDER BY sort SEPARATOR ';'), r.table_name " +
			"FROM reports r, repfields f, report_fields rf, available a left join surveys on site=substring(surveys.site_id,1,8)" +
			"WHERE a.report_id=r.report_id and r.report_id=rf.report_id and rf.field_id=f.field_id and f.repvariables<>'powercosts' ";
	
	public ListBox TablesListBox;
	public CheckBox VarsCheckBox;
	public VerticalPanel VarResVPanel;
	public HorizontalPanel VarResHPanel3;
	public SimplePanel VarResSimplePanel;
	public ListBox FilterByYear;
	public ListBox FilterByCountry;
	private HorizontalPanel CheckboxHPanel;
	private VarCheckbox varCheckbox;
	private VerticalPanel verticalPanel;
	private MultiChartPanel mcpResultPanel;
	private VerticalPanel FilterByYearVPanel;
	private VerticalPanel FilterByCountryVPanel;
	
	String varCheckBoxQuery;
	int[] numofnumcols;

	public Var_Result() {			
		varCheckbox = new VarCheckbox();
		
		VarResVPanel = new VerticalPanel();
		initWidget(VarResVPanel);
		VarResVPanel.setHeight("700px");
		
		HorizontalPanel VarResHPanel = new HorizontalPanel();
		VarResHPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		VarResHPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		VarResHPanel.setSpacing(2);
		VarResVPanel.add(VarResHPanel);
		VarResHPanel.setSize("835px", "230px");
		
		TablesListBox = new ListBox();
		TablesListBox.setStyleName("FHS-TablesListBox");
		VarResHPanel.add(TablesListBox);
		TablesListBox.setSize("280px", "230px");
		TablesListBox.setVisibleItemCount(5);
		TablesListBox.addChangeHandler(new ChangeHandler() {
			@Override
			public void onChange(ChangeEvent event) {	
				RootPanel.get("Loading-Message").setVisible(true);
				
				String SelectedTable = TablesListBox.getValue(TablesListBox.getSelectedIndex());
				String site2 = "";
				String select = "";
				String projvarssql = "";
				varCheckBoxQuery = "";
				
				if(TablesListBox.getSelectedIndex()>=0){
					//displayVars("SELECT f.repvariables, f.description FROM repfields f INNER JOIN (report_fields rf LEFT JOIN reports r ON rf.report_id=r.report_id) ON rf.field_id=f.field_id WHERE r.table_name = '"+SelectedTable+"'");
					projvarssql = ProjVarsSql2 + ProjVarsSqlWhereClause4(SelectedTable);
					displayVars(projvarssql + " GROUP BY r.report_id");
					if (SelectedTable.equalsIgnoreCase("ot_car_partial")||SelectedTable.equalsIgnoreCase("ot_quantity_of_input")){
	                    site2 = "idp_code";
	                } else if (SelectedTable.equalsIgnoreCase("households")||SelectedTable.equalsIgnoreCase("assets") ||SelectedTable.equalsIgnoreCase("land_use") ||SelectedTable.equalsIgnoreCase("land_profile") ||SelectedTable.equalsIgnoreCase("crop_disposal") ||SelectedTable.equalsIgnoreCase("income") ||SelectedTable.equalsIgnoreCase("credit") ||SelectedTable.equalsIgnoreCase("consump_expend")){
	                    site2 = "hh_code";
	                }else {
	                    site2 = "site_id";
	                }
					//select = "SELECT * FROM " + SelectedTable + " WHERE SUBSTRING_INDEX("+site2+",'-',2) in (SELECT site_id FROM surveys s) ";
					select = "SELECT * FROM " + SelectedTable + " WHERE mid("+site2+",1,11) in (SELECT mid(site_id,1,11) FROM surveys s) ";
					if (SelectedTable.equalsIgnoreCase("surveys")){
						displayVarTabYr("SELECT surveys.survey_year FROM "+SelectedTable+" WHERE SUBSTRING_INDEX("+site2+", '-', 2) in (SELECT site_id FROM surveys s) GROUP BY survey_year");
						displayVarTabCntry("SELECT country FROM "+SelectedTable+" WHERE SUBSTRING_INDEX("+site2+", '-', 2) in (SELECT site_id FROM surveys s) GROUP BY country");
					}else{
						displayVarTabYr("SELECT surveys.survey_year FROM "+SelectedTable+" left join surveys on substring("+SelectedTable+"."+site2+",1,11)=substring(surveys.site_id,1,11) WHERE SUBSTRING_INDEX("+site2+", '-', 2) in (SELECT site_id FROM surveys s) GROUP BY survey_year");
						displayVarTabCntry("SELECT country FROM "+SelectedTable+" left join surveys on substring("+site2+",1,11)=substring(surveys.site_id,1,11) WHERE SUBSTRING_INDEX("+site2+", '-', 2) in (SELECT site_id FROM surveys s) GROUP BY country");
					}
					mcpResultPanel.setQuery(select, SelectedTable);
					VarResSimplePanel.setWidget(mcpResultPanel);
				}
			}
		});
		
		CheckboxHPanel = new HorizontalPanel();
		CheckboxHPanel.setStyleName("FHS-TablesListBox");
		VarResHPanel.add(CheckboxHPanel);
		CheckboxHPanel.setSize("421px", "230px");
		
		verticalPanel = new VerticalPanel();
		verticalPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		VarResHPanel.add(verticalPanel);
		verticalPanel.setSize("129px", "230px");
		
		FilterByYearVPanel = new VerticalPanel();
		FilterByYearVPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		verticalPanel.add(FilterByYearVPanel);
		FilterByYearVPanel.setSize("100%", "100%");
		
		FilterByYear = new ListBox(true);
		FilterByYearVPanel.add(FilterByYear);
		FilterByYear.setStyleName("FHS-TablesListBox");
		FilterByYear.setVisibleItemCount(5);
		FilterByYear.setSize("139px", "114px");
		FilterByYear.addChangeHandler(new ChangeHandler() {
			@Override
			public void onChange(ChangeEvent event) {	
				RootPanel.get("Loading-Message").setVisible(true);
				
				String SelectedTable = TablesListBox.getValue(TablesListBox.getSelectedIndex());
				String Selected_Year = "";
				String SelectedYear = "";
				String SelectedYear_Sql = "";
				String SelectedYearSql = "";
				String site = "";
				String site2 = "";
				String select = "";
				if(TablesListBox.getSelectedIndex()>=0){
					if (SelectedTable.equalsIgnoreCase("ot_car_partial")||SelectedTable.equalsIgnoreCase("ot_quantity_of_input")){
	                    site = "SUBSTRING_INDEX(idp_code, '-', 2)";
	                    site2 = "idp_code";
	                } else if (SelectedTable.equalsIgnoreCase("households")||SelectedTable.equalsIgnoreCase("assets") ||SelectedTable.equalsIgnoreCase("land_use") ||SelectedTable.equalsIgnoreCase("land_profile") ||SelectedTable.equalsIgnoreCase("crop_disposal") ||SelectedTable.equalsIgnoreCase("income") ||SelectedTable.equalsIgnoreCase("credit") ||SelectedTable.equalsIgnoreCase("consump_expend")){
	                    site = "SUBSTRING_INDEX(hh_code, '-', 2)";
	                    site2 = "hh_code";
	                }else {
	                    site = "SUBSTRING_INDEX(site_id, '-', 2)";
	                    site2 = "site_id";
	                }
					for (int i = 0; i < FilterByYear.getItemCount(); i++) {
						if(FilterByYear.isItemSelected(i)){
							Selected_Year = FilterByYear.getValue(i);
							SelectedYear = SelectedYear + Selected_Year.substring(2,4) + ",";
							SelectedYear_Sql = "SUBSTRING(site_id,10,2)="+Selected_Year.substring(2,4);
							SelectedYearSql = SelectedYearSql + SelectedYear_Sql + " or ";	
						}
					}
					if (SelectedYear.equals("")){
						SelectedYearSql = "";
					}else{
					SelectedYear = SelectedYear.substring(0, SelectedYear.length()-1);
					SelectedYearSql = SelectedYearSql.substring(0,SelectedYearSql.length()-4);
					}
					
					if (!varCheckBoxQuery.equals("")){
						//select = varCheckBoxQuery.substring(0, varCheckBoxQuery.length()-1) + " AND "+SelectedYearSql+" ";
						select = varCheckBoxQuery.substring(0, varCheckBoxQuery.length()-2) + " WHERE "+SelectedYearSql+") ";
					}else select = "SELECT * FROM " + SelectedTable + " WHERE " + site + " in (SELECT site_id FROM surveys s WHERE "+SelectedYearSql+") ";
					
					if (SelectedTable.equalsIgnoreCase("surveys")){
						displayVarTabCntry("SELECT country FROM "+SelectedTable+" WHERE SUBSTRING_INDEX("+site2+", '-', 2) in (SELECT site_id FROM surveys s WHERE "+SelectedYearSql+") GROUP BY country");
					}else{
						displayVarTabCntry("SELECT country FROM "+SelectedTable+" left join surveys on substring("+site2+",1,11)=substring(surveys.site_id,1,11) WHERE SUBSTRING_INDEX("+site2+", '-', 2) in (SELECT site_id FROM surveys s WHERE "+SelectedYearSql+") GROUP BY country");
					}
					
					mcpResultPanel.setQueryVarCheckBox(select, SelectedTable, numofnumcols);
					VarResSimplePanel.setWidget(mcpResultPanel);
				}	
			}
		});
		
		FilterByCountryVPanel = new VerticalPanel();
		FilterByCountryVPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		FilterByCountryVPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_BOTTOM);
		verticalPanel.add(FilterByCountryVPanel);
		FilterByCountryVPanel.setSize("100%", "100%");
		
		FilterByCountry = new ListBox(true);
		FilterByCountryVPanel.add(FilterByCountry);
		FilterByCountry.setVisibleItemCount(5);
		FilterByCountry.setStyleName("FHS-TablesListBox");
		FilterByCountry.setSize("139px", "114px");
		FilterByCountry.addChangeHandler(new ChangeHandler() {
			@Override
			public void onChange(ChangeEvent event) {
				RootPanel.get("Loading-Message").setVisible(true);
				
				String SelectedTable = TablesListBox.getValue(TablesListBox.getSelectedIndex());
				String Selected_Year = "";
				String SelectedYear = "";
				String SelectedYear_Sql = "";
				String SelectedYearSql = "";
				String Selected_Country = "";
				String SelectedCountry = "";
				String SelectedCountrySql = "";
				String site2 = "";
				String select = "";
				if(TablesListBox.getSelectedIndex()>=0){
					if (SelectedTable.equalsIgnoreCase("ot_car_partial")||SelectedTable.equalsIgnoreCase("ot_quantity_of_input")){
	                    site2 = "idp_code";
	                } else if (SelectedTable.equalsIgnoreCase("households")||SelectedTable.equalsIgnoreCase("assets") ||SelectedTable.equalsIgnoreCase("land_use") ||SelectedTable.equalsIgnoreCase("land_profile") ||SelectedTable.equalsIgnoreCase("crop_disposal") ||SelectedTable.equalsIgnoreCase("income") ||SelectedTable.equalsIgnoreCase("credit") ||SelectedTable.equalsIgnoreCase("consump_expend")){
	                    site2 = "hh_code";
	                }else {site2 = "site_id";}
					
					for (int i = 0; i < FilterByYear.getItemCount(); i++) {
						if(FilterByYear.isItemSelected(i)){
							Selected_Year = FilterByYear.getValue(i);
							SelectedYear = SelectedYear + Selected_Year.substring(2,4) + ",";
							SelectedYear_Sql = "SUBSTRING(site_id,10,2)="+Selected_Year.substring(2,4);
							SelectedYearSql = SelectedYearSql + SelectedYear_Sql + " or ";	
						}
					}
					if (SelectedYear.equals("")){
						SelectedYearSql = "";
					}else{
					SelectedYear = SelectedYear.substring(0, SelectedYear.length()-1);
					SelectedYearSql = " AND (" +SelectedYearSql.substring(0,SelectedYearSql.length()-4)+")";
					}
					
					for (int i = 0; i < FilterByCountry.getItemCount(); i++) {
						if(FilterByCountry.isItemSelected(i)){
							Selected_Country = FilterByCountry.getValue(i);	
							SelectedCountry = "'" +Selected_Country+"'";
							SelectedCountrySql = SelectedCountrySql + "country="+SelectedCountry+" or ";	
						}
					}
					SelectedCountrySql = SelectedCountrySql.substring(0,SelectedCountrySql.length()-4);
					
					if (!varCheckBoxQuery.equals("") && (!SelectedYearSql.equals(""))){
						select = varCheckBoxQuery.substring(0, varCheckBoxQuery.length()-2)+" WHERE ("+SelectedYearSql.substring(6)+" AND ("+SelectedCountrySql+"))";
					}else if (varCheckBoxQuery.equals("") && (!SelectedYearSql.equals(""))){
						select = "SELECT * FROM " + SelectedTable + " WHERE SUBSTRING_INDEX("+site2+",'-',2) in (SELECT site_id FROM surveys s WHERE "+SelectedYearSql.substring(5)+" AND " +SelectedCountrySql+")";
					}else if (!varCheckBoxQuery.equals("") && (SelectedYearSql.equals(""))){
						select = varCheckBoxQuery.substring(0, varCheckBoxQuery.length()-2)+" WHERE ("+SelectedCountrySql+"))";
					}else if (varCheckBoxQuery.equals("") && (SelectedYearSql.equals(""))){
						select = "SELECT * FROM " + SelectedTable + " WHERE SUBSTRING_INDEX("+site2+",'-',2) in (SELECT site_id FROM surveys s WHERE "+SelectedCountrySql+")";
					} 
					mcpResultPanel.setQueryVarCheckBox(select, SelectedTable, numofnumcols);
					VarResSimplePanel.setWidget(mcpResultPanel);
				}
			}
		});
		
		varCheckbox.addChangeHandler(new ChangeHandler() {
			
			@Override
			public void onChange(ChangeEvent event) {	
				RootPanel.get("Loading-Message").setVisible(true);
				
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
				for (int i = /*1*/0; i < varCheckbox.getItemCount(); i++) {
					if(varCheckbox.getItem(i).getValue()){
						String colname = varCheckbox.getItem(i).getName();
						if (colname.equalsIgnoreCase("project")){
							colname = "project_id";
						}
						/**/
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
				/**/
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
				/**/
				if(selcols!=""){
					selcols = selcols.substring(0, selcols.length()-1);
					varCheckBoxQuery = "SELECT "+selcols+" FROM "+SelectedTable+" WHERE SUBSTRING_INDEX("+site2+",'-',2) in (SELECT site_id FROM surveys s) ";
					if (SelectedTable.equalsIgnoreCase("surveys")){
						displayVarTabYr("SELECT surveys.survey_year FROM "+SelectedTable+" WHERE SUBSTRING_INDEX("+site2+", '-', 2) in (SELECT site_id FROM surveys s) GROUP BY survey_year");
						displayVarTabCntry("SELECT country FROM "+SelectedTable+" WHERE SUBSTRING_INDEX("+site2+", '-', 2) in (SELECT site_id FROM surveys s) GROUP BY country");
					}else{
						displayVarTabYr("SELECT surveys.survey_year FROM "+SelectedTable+" left join surveys on substring("+SelectedTable+"."+site2+",1,11)=substring(surveys.site_id,1,11) WHERE SUBSTRING_INDEX("+site2+", '-', 2) in (SELECT site_id FROM surveys s) GROUP BY survey_year");
						displayVarTabCntry("SELECT country FROM "+SelectedTable+" left join surveys on substring("+site2+",1,11)=substring(surveys.site_id,1,11) WHERE SUBSTRING_INDEX("+site2+", '-', 2) in (SELECT site_id FROM surveys s) GROUP BY country");
					}
					mcpResultPanel.setQueryVarCheckBox(varCheckBoxQuery, SelectedTable, numofnumcols);
					VarResSimplePanel.setWidget(mcpResultPanel);
				}else{
					VarResSimplePanel.clear();
					FilterByYear.setEnabled(false);
					FilterByCountry.setEnabled(false);
					//RootPanel.get("Loading-Message").setVisible(false);
				}
			}
		});
		
		VarResHPanel3 = new HorizontalPanel();
		VarResHPanel3.setStyleName("FHS-HorizontalPanelSiteMap");
		VarResHPanel3.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		VarResVPanel.add(VarResHPanel3);
		VarResVPanel.setCellHeight(VarResHPanel3, "300px");
		VarResVPanel.setCellWidth(VarResHPanel3, "800px");
		
		VarResSimplePanel = new SimplePanel();
		VarResSimplePanel.setStyleName("FHS-TablesListBox");
		VarResHPanel3.clear();
		VarResHPanel3.add(VarResSimplePanel);
		VarResSimplePanel.setSize("841px", "465px");
		populateListBox("SELECT * FROM reports");
		
		mcpResultPanel =  new MultiChartPanel();
		mcpResultPanel.getDeckPanel().setSize("100%", "100%");
		VarResSimplePanel.add(mcpResultPanel);
		mcpResultPanel.setSize("100%", "100%");
		
		mcpResultPanel.SetClearBtn(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				FilterByCountry.clear();
				FilterByYear.clear();
				VarResSimplePanel.clear();
				TablesListBox.setSelectedIndex(-1);
				CheckboxHPanel.clear();
			}
		});
	}
	public void populateListBox(String query){
		final AsyncCallback<String[][]> populate = new AsyncCallback<String[][]>() {
			
			@Override
			public void onSuccess(String[][] result) {
                            TablesListBox.clear();
                            try{
                                for (int i = 1;i<result.length;i++){
                                    TablesListBox.addItem(result[i][1],result[i][2]);
                                }
                            }
                            catch(Exception e){
                                System.err.println(e);
                            }                          
			}
			
			@Override
			public void onFailure(Throwable caught) {
				 throw new UnsupportedOperationException("Not supported yet.");
			}			
		};
		UtilsRPC.getService("mysqlservice").RunSELECT(query, populate);
	}
	
	public void displayVars(String sql){
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

	public void displayVarTabYr(String sql){
		FilterByYear.clear();
        final AsyncCallback<String[][]> FetchDetails = new AsyncCallback<String[][]>() {

            public void onFailure(Throwable caught) {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            public void onSuccess(String[][] result) {
                for (int i = 1; i < result.length; i++) {
                	int col = result[0].length;
                	String surveyyr = result[i][col-1];
                	int surveyyrlength = surveyyr.length();
                	if (surveyyrlength==2){
                		if (surveyyr.substring(0,1).equals("0")){
                			surveyyr = "20"+surveyyr;
                		}else{
                			surveyyr = "19"+surveyyr;
                		}
                	}
                	FilterByYear.addItem(surveyyr);
                	FilterByYear.setEnabled(true);
				}
                if (FilterByYear.getItemCount()<2){
            		FilterByYear.setEnabled(false);
            	}
            }
        };
        UtilsRPC.getService("mysqlservice").RunSELECT(sql, FetchDetails);
    }
	
	public void displayVarTabCntry(String sql){
		FilterByCountry.clear();
        final AsyncCallback<String[][]> FetchDetails = new AsyncCallback<String[][]>() {
            public void onFailure(Throwable caught) {
                throw new UnsupportedOperationException("Not supported yet.");
            }
            public void onSuccess(String[][] result) {
                for (int i = 1; i < result.length; i++) {
                	int col = result[0].length;
                	String surveyyr = result[i][col-1];
                	FilterByCountry.addItem(surveyyr);
                	FilterByCountry.setEnabled(true);
				}
            	if (FilterByCountry.getItemCount()<2){
            		FilterByCountry.setEnabled(false);
            	}
            	RootPanel.get("Loading-Message").setVisible(false);
            }
        };
        UtilsRPC.getService("mysqlservice").RunSELECT(sql, FetchDetails);
    }
	
	private String ProjVarsSqlWhereClause4(String tablename){
        String whereclause = "";
        whereclause = " AND r.table_name = '" +tablename+ "'";
        return whereclause;
    }
}