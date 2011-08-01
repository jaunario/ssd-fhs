package org.irri.households.client;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.CheckboxCell;
import com.google.gwt.cell.client.CompositeCell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.cell.client.HasCell;
import com.google.gwt.dom.client.Element;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.cellview.client.CellTree;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.ProvidesKey;
import com.google.gwt.view.client.TreeViewModel;


public class Fhs_CountryTree extends Composite {
	
	public static class CountryInfo implements Comparable<CountryInfo>{
		@Override
		public int compareTo(CountryInfo arg0) {
			return 0;
		}
		
		public static final ProvidesKey<CountryInfo> KEY_PROVIDER = new ProvidesKey<CountryInfo>() {
		      public Object getKey(CountryInfo item) {
		        return item == null ? null : item.getId();
		      }
		};
		
		private String Site_ID;
		private String Country;
		private String Province;
		private String District;
		private String Village;
		
		public CountryInfo(String sid, String country, String province, String district, String village){
			Site_ID = sid;
			Country = country;
			Province = province;
			if(district==null){
				District = "-";
			}else District = district;
			if(village==null){
				Village = "-";
			}else Village = village;
		}
		
		public String getId(){
			return Site_ID;
		}
		
		public String getCountry() {
		      return Country;
		}
		
		public String getProvince() {
		      return Province;
		}
		
		public String getDistrict() {
		      return District;
		}
		
		public String getVillage() {
		      return Village;
		}
	}

	/*Cell used to render Country Names*/
	static class CountryCell extends AbstractCell<Country>{
		@Override
		public void render(Context context, Country value, SafeHtmlBuilder sb) {
			sb.appendEscaped(value.getCountry());
		}		
	}

	public static class Country implements Comparable<Country>{
		private String Country;
		
		@Override
		public int compareTo(Country arg0) {
			return 0;
		}
		
		public static final ProvidesKey<Country> KEY_PROVIDER = new ProvidesKey<Country>() {
		      public Object getKey(Country item) {
		        return item == null ? null : item.getCountry();
		      }
		};

		public Country(String country){
			this.Country = country;			
		}
		
		public String getCountry(){
			return this.Country;
		}
	}
	
	public static class ProvinceInfo implements Comparable<ProvinceInfo>{
		private String Country;
		private String Province;
		
		@Override
		public int compareTo(ProvinceInfo arg0) {
			return 0;
		}
		
		public static final ProvidesKey<ProvinceInfo> KEY_PROVIDER = new ProvidesKey<ProvinceInfo>() {
		      public Object getKey(ProvinceInfo item) {
		        return item == null ? null : item.getProvince();
		      }
		};
			
		public ProvinceInfo(String country, String province){
			this.Country = country;
			this.Province = province;
		}
		
		public String getCountry(){
			return this.Country;
		}
		
		public String getProvince() {
		      return this.Province;
		}
	}

	public static class DistrictInfo implements Comparable<DistrictInfo>{
		private String Province;
		private String District;
		
		@Override
		public int compareTo(DistrictInfo arg0) {
			return 0;
		}
		
		public static final ProvidesKey<DistrictInfo> KEY_PROVIDER = new ProvidesKey<DistrictInfo>() {
		      public Object getKey(DistrictInfo item) {
		        return item == null ? null : item.getDistrict();
		      }
		};
		
		
		public DistrictInfo(String province, String district){
			this.Province = province;
			if(district==null){
				this.District="-";
			}else this.District = district;
		}
		
		public String getProvince(){
			return this.Province;
		}
		
		public String getDistrict() {
		      return this.District;
		}
	}
	
	public static class VillageInfo implements Comparable<VillageInfo>{
		private String District;
		private String Village;
		
		@Override
		public int compareTo(VillageInfo arg0) {
			return 0;
		}
		
		public static final ProvidesKey<VillageInfo> KEY_PROVIDER = new ProvidesKey<VillageInfo>() {
		      public Object getKey(VillageInfo item) {
		        return item == null ? null : item.getVillage();
		      }
		};
		
		
		public VillageInfo(String district, String village){
			if (district==null){
				this.District="-";
			}else this.District = district;
			if (village==null){
				this.Village="-";
			}else this.Village = village;
		}
		
		public String getDistrict(){
			return this.District;
		}
		
		public String getVillage() {
		      return this.Village;
		}
	}
	
	/*Cell used to render Province Names*/
	static class ProvinceCell extends AbstractCell<ProvinceInfo>{
		@Override
		public void render(Context context,	ProvinceInfo value, SafeHtmlBuilder sb) {
			sb.appendEscaped(value.getProvince());
		}		
	}
	
	/*Cell used to render District Names*/
	static class DistrictCell extends AbstractCell<DistrictInfo>{
		@Override
		public void render(com.google.gwt.cell.client.Cell.Context context,
				DistrictInfo value, SafeHtmlBuilder sb) {
			sb.appendEscaped(value.getDistrict());
		}		
	}

	/*Cell used to render Village Names*/
	static class VillageCell extends AbstractCell<VillageInfo>{
		@Override
		public void render(com.google.gwt.cell.client.Cell.Context context,
				VillageInfo value, SafeHtmlBuilder sb) {
			sb.appendEscaped(value.getVillage());
		}		
	}
	private static class SiteTreeModel implements TreeViewModel {
		
		ListDataProvider<CountryInfo> dataprovider = new ListDataProvider<Fhs_CountryTree.CountryInfo>();
		ListDataProvider<Country> dataprovider1 = new ListDataProvider<Country>();
		List<HasCell<VillageInfo, ?>> hasCells = new ArrayList<HasCell<VillageInfo, ?>>();
		
		private Cell<VillageInfo> countryCell;
		
		final AsyncCallback<String[][]> PopulateTreeModel = new AsyncCallback<String[][]>() {
			@Override
			public void onSuccess(String[][] result) {
				String lastcountry = "";
			
				for (int i=1;i<result.length;i++){
					dataprovider.getList().add(new CountryInfo(result[i][0], result[i][1], result[i][2], result[i][3], result[i][4]));
						if(!lastcountry.equalsIgnoreCase(result[i][1])){
							dataprovider1.getList().add(new Country(result[i][1]));
							lastcountry = result[i][1];
						}
				
				}
				
			    hasCells.add(new HasCell<VillageInfo, Boolean>() {

			      private CheckboxCell cell = new CheckboxCell(true, false);

			      public Cell<Boolean> getCell() {
			        return cell;
			      }

			      public FieldUpdater<VillageInfo, Boolean> getFieldUpdater() {
			        return null;
			      }

			      public Boolean getValue(VillageInfo object) {
			        //return selectionModel.isSelected(object);
			    	  return null;
			      }
			    });
			    hasCells.add(new HasCell<VillageInfo, VillageInfo>() {

			      private Cell<VillageInfo> cell = new VillageCell();

			      public Cell<VillageInfo> getCell() {
			        return cell;
			      }

			      public FieldUpdater<VillageInfo, VillageInfo> getFieldUpdater() {
			        return null;
			      }

			      public VillageInfo getValue(VillageInfo object) {
			        return object;
			      }
			    });
					
				countryCell = new CompositeCell<VillageInfo>(hasCells){
					@Override
				      public void render(Context context, VillageInfo value, SafeHtmlBuilder sb) {
				        sb.appendHtmlConstant("<table><tbody><tr>");
				        super.render(context, value, sb);
				        sb.appendHtmlConstant("</tr></tbody></table>");
				      }

				      @Override
				      protected Element getContainerElement(Element parent) {
				        // Return the first TR element in the table.
				        return parent.getFirstChildElement().getFirstChildElement().getFirstChildElement();
				      }

				      @Override
				      protected <X> void render(Context context, VillageInfo value,
				          SafeHtmlBuilder sb, HasCell<VillageInfo, X> hasCell) {
				        Cell<X> cell = hasCell.getCell();
				        sb.appendHtmlConstant("<td>");
				        cell.render(context, hasCell.getValue(value), sb);
				        sb.appendHtmlConstant("</td>");
				      }
				};
				
			}	
			
			@Override
			public void onFailure(Throwable caught) {
				System.out.println("Communication failed (PopulateTreeModel)");
			}
		};
		public SiteTreeModel(String query){
			UtilsRPC.getService("mysqlservice").RunSELECT(query, PopulateTreeModel);
			
		}
		
	    // Get the NodeInfo that provides the children of the specified value.
		@Override
		public <T> NodeInfo<?> getNodeInfo(T value) {
			if (value == null){
				return new DefaultNodeInfo<Country>(dataprovider1, new CountryCell());
			}else if (value instanceof Country){
				Country country = (Country) value;
				
				List<CountryInfo> countries = new ArrayList<CountryInfo>();				
				for (int i=0;i<dataprovider.getList().size();i++){
					if (dataprovider.getList().get(i).getCountry().equalsIgnoreCase(country.getCountry())){
						countries.add(dataprovider.getList().get(i));
					}
				}
				
				List<ProvinceInfo> provinces = new ArrayList<ProvinceInfo>();
				String lastprovince = "";
				for (int i=0;i<countries.size();i++){
					if (!lastprovince.equalsIgnoreCase(countries.get(i).getProvince())) {
						provinces.add(new ProvinceInfo(countries.get(i).getCountry(),countries.get(i).getProvince()));
						lastprovince = countries.get(i).getProvince();
					}	
				}
					
				return new DefaultNodeInfo<ProvinceInfo>(new ListDataProvider<ProvinceInfo>(provinces), new ProvinceCell());
			}else if (value instanceof ProvinceInfo){
				ProvinceInfo province = (ProvinceInfo) value;
			
				List<CountryInfo> provinces = new ArrayList<CountryInfo>();
				for (int i=0;i<dataprovider.getList().size();i++){
					if (dataprovider.getList().get(i).getProvince().equalsIgnoreCase(province.getProvince())){
						provinces.add(dataprovider.getList().get(i));
						
					}
				}
				List<DistrictInfo> districts = new ArrayList<DistrictInfo>();
				String lastdistrict = "";
				for (int i=0;i<provinces.size();i++){
					if (!lastdistrict.equalsIgnoreCase(provinces.get(i).getDistrict())){
						districts.add(new DistrictInfo(provinces.get(i).getProvince(),provinces.get(i).getDistrict()));
						lastdistrict = provinces.get(i).getDistrict();
					}
				}
				return new DefaultNodeInfo<DistrictInfo>(new ListDataProvider<DistrictInfo>(districts), new DistrictCell());
				//return new DefaultNodeInfo<DistrictInfo>(dataprovider2, new DistrictCell());
			}else if (value instanceof DistrictInfo){
				DistrictInfo district = (DistrictInfo) value;
				
				List<CountryInfo> districts = new ArrayList<CountryInfo>();				
				for (int i=0;i<dataprovider.getList().size();i++){
					CountryInfo thisvill = dataprovider.getList().get(i);
					if (thisvill.getDistrict().equalsIgnoreCase(district.getDistrict())){
						districts.add(thisvill);
					}
				}
				List<VillageInfo> villages = new ArrayList<VillageInfo>();
				String lastvillage = "";
				for (int i=0;i<districts.size();i++){
					if(!lastvillage.equalsIgnoreCase(districts.get(i).getVillage())){
						villages.add(new VillageInfo(districts.get(i).getDistrict(),districts.get(i).getVillage()));
					lastvillage = districts.get(i).getVillage();
					}
				}
				
				return new DefaultNodeInfo<VillageInfo>(new ListDataProvider<VillageInfo>(villages), new VillageCell());
			}
			return new DefaultNodeInfo<Country>(dataprovider1, new CountryCell());
			
		}
		
		// Check if the specified value represents a leaf node. Leaf nodes cannot be opened.
	    public boolean isLeaf(Object value) {
	    	return value instanceof VillageInfo;
	    }	
	  }
	
   
	TreeViewModel model = new SiteTreeModel("SELECT substring_index(site_id,'-', 1) ssite_id, country, province, district, village FROM fhh_survey.surveys GROUP BY 2,3,4,5 ORDER BY 2,3,4,5");
   
	
	public Fhs_CountryTree() {
	    // Add the tree to the root layout panel.
		CellTree cellTree = new CellTree(model, null);
		cellTree.setDefaultNodeSize(10);
	    initWidget(cellTree);
	}

}