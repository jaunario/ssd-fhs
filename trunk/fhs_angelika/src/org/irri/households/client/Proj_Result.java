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


public class Proj_Result extends Composite {
	private final String ProjVarsSql =
			"SELECT r.report_title," +
			"GROUP_CONCAT(DISTINCT f.repvariables ORDER BY sort SEPARATOR ';'), " +
			"GROUP_CONCAT(DISTINCT f.description ORDER BY sort SEPARATOR ';')," +
			"r.table_name " +
			"FROM reports r, repfields f, report_fields rf, available a " +
			"WHERE a.report_id=r.report_id AND r.report_id=rf.report_id AND rf.field_id=f.field_id AND ";	
	
	public ListBox TablesListBox;
	public HorizontalPanel CheckboxHPanel;
	public VerticalPanel ProjResVPanel;
	public HorizontalPanel ProjResHPanel3;
	public SimplePanel ProjResSimplePanel;
	public ListBox FilterByYear;
	public ListBox FilterByCountry;
	private VarCheckbox varCheckbox;
	private VerticalPanel verticalPanel;
	private MultiChartPanel mcpResultPanel;
	private VerticalPanel FilterByYearVPanel;
	private VerticalPanel FilterByCountryVPanel;
	
	String varCheckBoxQuery;
	int[] numofnumcols;

	
	public Proj_Result(final int ProjID) {
		varCheckbox = new VarCheckbox();
		
		ProjResVPanel = new VerticalPanel();
		ProjResVPanel.setStyleName("FHS-SimplePanelProjList");
		ProjResVPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		ProjResVPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		initWidget(ProjResVPanel);
		
		HorizontalPanel ProjResHPanel = new HorizontalPanel();
		ProjResHPanel.setSpacing(2);
		ProjResHPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		ProjResHPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		ProjResVPanel.add(ProjResHPanel);
		ProjResHPanel.setSize("810px", "230px");
		
		TablesListBox = new ListBox();
		TablesListBox.setVisibleItemCount(5);
		TablesListBox.setStyleName("FHS-TablesListBox2");
		ProjResHPanel.add(TablesListBox);
		TablesListBox.setSize("250px", "230px");
		TablesListBox.addChangeHandler(new ChangeHandler() {
			@Override
			public void onChange(ChangeEvent event) {
				RootPanel.get("Loading-Message").setVisible(true);
				
				int SelectedProjID = ProjID;
				String SelectedTable = TablesListBox.getValue(TablesListBox.getSelectedIndex());
				String site = "";
				String site2 = "";
				String select = "";
				String projvarssql = "";
				varCheckBoxQuery = "";
				
				if(TablesListBox.getSelectedIndex()>=0){
					projvarssql = ProjVarsSql + " a.project_id = " +SelectedProjID+ " AND r.table_name = '" +SelectedTable+ "'";
					displayProjVars(projvarssql + " GROUP BY r.report_id");
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
					select = "SELECT * FROM fhh_survey."+SelectedTable+" WHERE "+site+" in (SELECT site_id FROM surveys s WHERE s.project_id="+SelectedProjID+") ";
					if (SelectedTable.equalsIgnoreCase("surveys")){
						displayProjTabYr("SELECT surveys.survey_year FROM "+SelectedTable+" WHERE SUBSTRING_INDEX("+site2+", '-', 2) in (SELECT site_id FROM surveys s WHERE s.project_id="+SelectedProjID+") GROUP BY survey_year");
						displayProjTabCntry("SELECT country FROM "+SelectedTable+" WHERE SUBSTRING_INDEX("+site2+", '-', 2) in (SELECT site_id FROM surveys s WHERE s.project_id="+SelectedProjID+") GROUP BY country");
					}else{
						displayProjTabYr("SELECT surveys.survey_year FROM "+SelectedTable+" left join surveys on substring("+SelectedTable+"."+site2+",1,11)=substring(surveys.site_id,1,11) WHERE SUBSTRING_INDEX("+site2+", '-', 2) in (SELECT site_id FROM surveys s WHERE s.project_id="+SelectedProjID+") GROUP BY survey_year");
						displayProjTabCntry("SELECT country FROM "+SelectedTable+" left join surveys on substring("+site2+",1,11)=substring(surveys.site_id,1,11) WHERE SUBSTRING_INDEX("+site2+", '-', 2) in (SELECT site_id FROM surveys s WHERE s.project_id="+SelectedProjID+") GROUP BY country");
					}
					mcpResultPanel.setQuery(select, SelectedTable);
					ProjResSimplePanel.setWidget(mcpResultPanel);
				}
			}
		});
		
		CheckboxHPanel = new HorizontalPanel();
		CheckboxHPanel.setStyleName("FHS-TablesListBox");
		ProjResHPanel.add(CheckboxHPanel);
		CheckboxHPanel.setSize("416px", "230px");
			
		verticalPanel = new VerticalPanel();
		verticalPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		verticalPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		ProjResHPanel.add(verticalPanel);
		verticalPanel.setSize("129px", "230px");
		
		FilterByYearVPanel = new VerticalPanel();
		FilterByYearVPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		verticalPanel.add(FilterByYearVPanel);
		FilterByYearVPanel.setSize("100%", "100%");
		
		FilterByYear = new ListBox(true);
		FilterByYearVPanel.add(FilterByYear);
		FilterByYear.setStyleName("FHS-TablesListBox");
		FilterByYear.setVisibleItemCount(5);
		FilterByYear.setSize("129px", "114px");
		
		FilterByCountryVPanel = new VerticalPanel();
		FilterByCountryVPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		FilterByCountryVPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_BOTTOM);
		verticalPanel.add(FilterByCountryVPanel);
		FilterByCountryVPanel.setSize("100%", "100%");
		
		FilterByCountry = new ListBox(true);
		FilterByCountryVPanel.add(FilterByCountry);
		FilterByCountry.setStyleName("FHS-TablesListBox");
		FilterByCountry.setVisibleItemCount(5);
		FilterByCountry.setSize("129px", "113px");
		FilterByCountry.addChangeHandler(new ChangeHandler() {
			@Override
			public void onChange(ChangeEvent event) {	
				RootPanel.get("Loading-Message").setVisible(true);
				
				int SelectedProjID = ProjID;
				String SelectedTable = TablesListBox.getValue(TablesListBox.getSelectedIndex());
				//String Selected_Year = FilterByYear.getValue(FilterByYear.getSelectedIndex());
				//String SelectedYear = Selected_Year.substring(2,4);
				String Selected_Year = "";
				String SelectedYear = "";
				String SelectedYear_Sql = "";
				String SelectedYearSql = "";
				//String SelectedCountry = FilterByCountry.getValue(FilterByCountry.getSelectedIndex());
				String Selected_Country = "";
				String SelectedCountry = "";
				String SelectedCountrySql = "";
				String site = "";
				String select = "";
				if(TablesListBox.getSelectedIndex()>=0){
					if (SelectedTable.equalsIgnoreCase("ot_car_partial")||SelectedTable.equalsIgnoreCase("ot_quantity_of_input")){
	                    site = "SUBSTRING_INDEX(idp_code, '-', 2)";
	                } else if (SelectedTable.equalsIgnoreCase("households")||SelectedTable.equalsIgnoreCase("assets") ||SelectedTable.equalsIgnoreCase("land_use") ||SelectedTable.equalsIgnoreCase("land_profile") ||SelectedTable.equalsIgnoreCase("crop_disposal") ||SelectedTable.equalsIgnoreCase("income") ||SelectedTable.equalsIgnoreCase("credit") ||SelectedTable.equalsIgnoreCase("consump_expend")){
	                    site = "SUBSTRING_INDEX(hh_code, '-', 2)";
	                }else {
	                    site = "SUBSTRING_INDEX(site_id, '-', 2)";
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
					
					if (!varCheckBoxQuery.equals("")){
						select = varCheckBoxQuery.substring(0, varCheckBoxQuery.length()-1)+ SelectedYearSql+" AND ("+SelectedCountrySql+"))"; 
					}else select = "SELECT * FROM " + SelectedTable + " WHERE " + site + " in (SELECT site_id FROM surveys s WHERE s.project_id="+SelectedProjID + SelectedYearSql + " and ("+SelectedCountrySql+"))";
				
					mcpResultPanel.setQueryVarCheckBox(select, SelectedTable, numofnumcols);
					ProjResSimplePanel.setWidget(mcpResultPanel);
				}
			}
		});
		FilterByYear.addChangeHandler(new ChangeHandler() {
			@Override
			public void onChange(ChangeEvent event) {	
				RootPanel.get("Loading-Message").setVisible(true);
				
				int SelectedProjID = ProjID;
				String SelectedTable = TablesListBox.getValue(TablesListBox.getSelectedIndex());
				//String Selected_Year = FilterByYear.getValue(FilterByYear.getSelectedIndex());
				//String SelectedYear = Selected_Year.substring(2,4);
				String Selected_Year = "";
				String SelectedYear = "";
				String SelectedYear_Sql = "";
				String SelectedYearSql = "";
				String site = "";
				String site2 = "";
				String select = "";
				//String projvarssql = "";
				if(TablesListBox.getSelectedIndex()>=0){
					//projvarssql = ProjVarsSql + ProjVarsSqlWhereClause2(SelectedProjID, SelectedTable);
					//displayProjVars(projvarssql + " GROUP BY r.report_id");
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
						select = varCheckBoxQuery.substring(0, varCheckBoxQuery.length()-1)+" AND ("+SelectedYearSql+"))"; 
					}else select = "SELECT * FROM " + SelectedTable + " WHERE " + site + " in (SELECT site_id FROM surveys s WHERE  s.project_id="+SelectedProjID+" and ("+SelectedYearSql+"))" ;
					      
					if (SelectedTable.equalsIgnoreCase("surveys")){
						displayProjTabCntry("SELECT country FROM "+SelectedTable+" WHERE SUBSTRING_INDEX("+site2+", '-', 2) in (SELECT site_id FROM surveys s WHERE s.project_id="+SelectedProjID+" and ("+SelectedYearSql+")) GROUP BY country");
					}else{
						displayProjTabCntry("SELECT country FROM "+SelectedTable+" left join surveys on substring("+site2+",1,11)=substring(surveys.site_id,1,11) WHERE SUBSTRING_INDEX("+site2+", '-', 2) in (SELECT site_id FROM surveys s WHERE s.project_id="+SelectedProjID+" and ("+SelectedYearSql+")) GROUP BY country");
					}
					
					mcpResultPanel.setQueryVarCheckBox(select, SelectedTable, numofnumcols);
					ProjResSimplePanel.setWidget(mcpResultPanel);
				}	
			}
		});
		
		varCheckbox.addChangeHandler(new ChangeHandler() {
			
			@Override
			public void onChange(ChangeEvent event) {	
				RootPanel.get("Loading-Message").setVisible(true);
				int SelectedProjID = ProjID;
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
				for (int i = 0; i < varCheckbox.getItemCount(); i++) {
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
						/**/
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
					varCheckBoxQuery = "SELECT "+selcols+" FROM "+SelectedTable+" WHERE SUBSTRING_INDEX("+site2+",'-',2) in (SELECT site_id FROM surveys s WHERE project_id="+SelectedProjID+")";
					if (SelectedTable.equalsIgnoreCase("surveys")){
						displayProjTabYr("SELECT surveys.survey_year FROM "+SelectedTable+" WHERE SUBSTRING_INDEX("+site2+", '-', 2) in (SELECT site_id FROM surveys s WHERE s.project_id="+SelectedProjID+") GROUP BY survey_year");
						displayProjTabCntry("SELECT country FROM "+SelectedTable+" WHERE SUBSTRING_INDEX("+site2+", '-', 2) in (SELECT site_id FROM surveys s WHERE s.project_id="+SelectedProjID+") GROUP BY country");
					}else{
						displayProjTabYr("SELECT surveys.survey_year FROM "+SelectedTable+" left join surveys on substring("+SelectedTable+"."+site2+",1,11)=substring(surveys.site_id,1,11) WHERE SUBSTRING_INDEX("+site2+", '-', 2) in (SELECT site_id FROM surveys s WHERE s.project_id="+SelectedProjID+") GROUP BY survey_year");
						displayProjTabCntry("SELECT country FROM "+SelectedTable+" left join surveys on substring("+site2+",1,11)=substring(surveys.site_id,1,11) WHERE SUBSTRING_INDEX("+site2+", '-', 2) in (SELECT site_id FROM surveys s WHERE s.project_id="+SelectedProjID+") GROUP BY country");
					}
					mcpResultPanel.setQueryVarCheckBox(varCheckBoxQuery, SelectedTable, numofnumcols);
					ProjResSimplePanel.setWidget(mcpResultPanel);
				}else{
					ProjResSimplePanel.clear();
					FilterByYear.setEnabled(false);
					FilterByCountry.setEnabled(false);
					RootPanel.get("Loading-Message").setVisible(false);
				}
				
			}
		});

		ProjResHPanel3 = new HorizontalPanel();
		ProjResHPanel3.setStyleName("FHS-HorizontalPanelSiteMap");
		ProjResHPanel3.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		ProjResHPanel3.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		ProjResVPanel.add(ProjResHPanel3);
		ProjResVPanel.setCellHeight(ProjResHPanel3, "300px");
		ProjResVPanel.setCellWidth(ProjResHPanel3, "800px");
		
		ProjResSimplePanel = new SimplePanel();
		ProjResSimplePanel.setStyleName("FHS-TablesListBox");
		ProjResHPanel3.clear();
		ProjResHPanel3.add(ProjResSimplePanel);
		ProjResSimplePanel.setSize("800px", "355px");
		
		mcpResultPanel =  new MultiChartPanel();
		mcpResultPanel.getDeckPanel().setSize("100%", "100%");
		ProjResSimplePanel.add(mcpResultPanel);
		mcpResultPanel.setSize("100%", "100%");
		
		mcpResultPanel.SetClearBtn(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				FilterByCountry.clear();
				FilterByYear.clear();
				ProjResSimplePanel.clear();
				TablesListBox.setSelectedIndex(-1);
				CheckboxHPanel.clear();
			}
		});
	}
	
	
	//-------------------------------------------------------------------------------------------
	//-------------------------------------------------------------------------------------------
	
	
	public void displayProjVars(String sql){
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

	public void displayProjTabYr(String sql){
		FilterByYear.clear();
        final AsyncCallback<String[][]> FetchDetails = new AsyncCallback<String[][]>() {

            public void onFailure(Throwable caught) {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            public void onSuccess(String[][] result) {
                for (int i = 1; i < result.length; i++) {
                	int col = result[0].length;
                	String surveyyr = result[i][col-1];
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
	
	public void displayProjTabCntry(String sql){
		FilterByCountry.clear();
        final AsyncCallback<String[][]> FetchDetails = new AsyncCallback<String[][]>() {
            public void onFailure(Throwable caught) {
                throw new UnsupportedOperationException("Not supported yet.");
            }
            public void onSuccess(String[][] result) {
                for (int i = 1; i < result.length; i++) {
                	int col = result[0].length;
                	FilterByCountry.addItem(result[i][col-1]);
                	FilterByCountry.setEnabled(true);
				}
                if (FilterByCountry.getItemCount()<2){
            		FilterByCountry.setEnabled(false);
            	}
            }
        };
        UtilsRPC.getService("mysqlservice").RunSELECT(sql, FetchDetails);
    }
}
