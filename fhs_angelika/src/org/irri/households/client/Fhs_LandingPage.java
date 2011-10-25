package org.irri.households.client;


import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.DeckPanel;
import com.google.gwt.user.client.ui.HTML;
import org.irri.households.client.ui.PicsSlider;
import org.irri.households.client.ui.Slider;
import org.irri.households.client.ui.ColumnChartPanel;
import org.irri.households.client.ui.PieChartPanel;
import org.irri.households.client.ui.ScatterPlotPanel;


public class Fhs_LandingPage extends Composite {
	public Slider slider;
	public DeckPanel ProjProfileDeckPanel;
	private VerticalPanel verticalPanel;
	private HorizontalPanel horizontalPanel;
	private ScrollPanel scrollPanel;
	private HTML htmlfarmHouseholdSurvey;
	private VerticalPanel verticalPanel_1;
	private HorizontalPanel horizontalPanel_1;
	private PieChartPanel pieChartPanel;

	public Fhs_LandingPage() {
		
		scrollPanel = new ScrollPanel();
		initWidget(scrollPanel);
		
		horizontalPanel = new HorizontalPanel();
		horizontalPanel.setStyleName("fhs-HomeHPanel2");
		scrollPanel.setWidget(horizontalPanel);
		horizontalPanel.setSize("100%", "100%");
		
		verticalPanel = new VerticalPanel();
		verticalPanel.setStyleName("FHS-verticalPanel");
		verticalPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		verticalPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		horizontalPanel.add(verticalPanel);
		
		ProjProfileDeckPanel = new DeckPanel();
		ProjProfileDeckPanel.setStyleName("FHS-verticalPanel_1");
		verticalPanel.add(ProjProfileDeckPanel);
		ProjProfileDeckPanel.setSize("500px", "710px");
		
		verticalPanel_1 = new VerticalPanel();
		verticalPanel_1.setStyleName("FHS-HomeAbout");
		horizontalPanel.add(verticalPanel_1);
		verticalPanel_1.setHeight("700px");
		
		htmlfarmHouseholdSurvey = new HTML("<h1>Farm Household Survey Database</h1>\n\n<p align=\"justify\">The farm household survey database is a collection of farm level data sets on rice productivity, fertilizer and pesticide use, labor inputs, prices, income, demographics, farm characteristics, and other related data on rice production in farmer’s fields. It is a rich collection of actual farm and household level data collected through personal farmer interviews, farm record keeping, and periodic monitoring of farm activities from various sites in different rice growing countries of Asia. The data collection was done by the SSD staff in collaboration with NARES partners in various parts of Asia. These data sets are part of the output of the various research projects undertaken by SSD staff and hence made available to interested users.</p>", true);
		verticalPanel_1.add(htmlfarmHouseholdSurvey);
		
		horizontalPanel_1 = new HorizontalPanel();
		horizontalPanel_1.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		horizontalPanel_1.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		verticalPanel_1.add(horizontalPanel_1);
		
		pieChartPanel = new PieChartPanel("SELECT rice_varname,count(*) FROM land_use l WHERE rice_varname is not null GROUP BY rice_varname ORDER BY 2 DESC LIMIT 5;", "Top Rice Varieties", 320, 260);
		horizontalPanel_1.add(pieChartPanel);
		
		PicsSlider picsSlider = new PicsSlider();
		horizontalPanel_1.add(picsSlider);
		
		HorizontalPanel horizontalPanel_2 = new HorizontalPanel();
		horizontalPanel_2.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		horizontalPanel_2.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		verticalPanel_1.add(horizontalPanel_2);
		
		ScatterPlotPanel scatterPlotPanel = new ScatterPlotPanel("SELECT survey_year, round(avg(if(left(idp_code, 2)='BD',Yield, null)),2) as 'BD',round(avg(if(left(idp_code, 2)='CN',Yield, null)),2) as 'CN',round(avg(if(left(idp_code, 2)='ID',Yield, null)),2) as 'ID',round(avg(if(left(idp_code, 2)='IN',Yield, null)),2) as 'IN',round(avg(if(left(idp_code, 2)='KH',Yield, null)),2) as 'KH',round(avg(if(left(idp_code, 2)='LA',Yield, null)),2) as 'LA',round(avg(if(left(idp_code, 2)='PH',Yield, null)),2) as 'PH',round(avg(if(left(idp_code, 2)='TH',Yield, null)),2) as 'TH',round(avg(if(left(idp_code, 2)='VN',Yield, null)),2) as 'VN' FROM ot_quantity_of_input join surveys on site_id = substring_index(idp_code, '-',2)GROUP BY 1;", "Average Yield", 320, 260);
		horizontalPanel_2.add(scatterPlotPanel);
		
		/*ColumnChartPanel columnChartPanel = new ColumnChartPanel("SELECT mid(hh_code,1,2) as country, " +
				"AVG(IF(src_income='Non-farm' or src_income='Non-farm activities' or src_income='Non-farm income',income,null)) as 'Non-farm', " +
				"AVG(IF(src_income='Non-rice',income,null)) as 'Non-rice', " +
				"AVG(IF(src_income='Off-farm income',income,null)) as 'Off-farm', " +
				"AVG(IF(spec_src='Rice',income,null)) as 'Rice' " +
				"FROM income " +
				"WHERE income>0 " +
				"GROUP BY 1;", "Average Income Per Country", 320, 260);*/
		ColumnChartPanel columnChartPanel = new ColumnChartPanel("SELECT mid(hh_code,10,2) as year, " +
		"AVG(IF(src_income='Non-farm' or src_income='Non-farm activities' or src_income='Non-farm income',income,null)) as 'Non-farm', " +
		"AVG(IF(src_income='Non-rice',income,null)) as 'Non-rice', " +
		"AVG(IF(src_income='Off-farm income',income,null)) as 'Off-farm', " +
		"AVG(IF(spec_src='Rice',income,null)) as 'Rice' " +
		"FROM income " +
		"WHERE income>0 and mid(hh_code,1,2)='PH' and (mid(hh_code,10,2)='04' or mid(hh_code,10,2)='07' or mid(hh_code,10,2)='08') " +
		"GROUP BY 1;", "Average Income Per Year in the Philippines", 320, 260);
		horizontalPanel_2.add(columnChartPanel);		
	}
}
