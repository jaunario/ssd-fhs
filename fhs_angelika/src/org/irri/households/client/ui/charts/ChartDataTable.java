package org.irri.households.client.ui.charts;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import com.google.gwt.visualization.client.AbstractDataTable;
import com.google.gwt.visualization.client.AbstractDataTable.ColumnType;
import com.google.gwt.visualization.client.DataTable;
import com.google.gwt.visualization.client.formatters.NumberFormat;
import com.google.gwt.visualization.client.formatters.NumberFormat.Options;

public class ChartDataTable{
	
	public static AbstractDataTable create(String[][] data, int[] numcols){
		DataTable datatable = DataTable.create();
		boolean isnumeric = false;
		int searchcol = 0;
		Arrays.sort(numcols);
		
		Options options = Options.create();
		options.setFractionDigits(2);
		options.setNegativeColor("red");
		NumberFormat formatter = NumberFormat.create(options);
		
		for (int i = 0; i < data.length; i++) {
			if(i==1) datatable.addRows(data.length-1); 
			for (int j = 0; j < data[i].length; j++) {
				searchcol = Arrays.binarySearch(numcols, j);
				isnumeric = searchcol  >= 0 & searchcol < numcols.length;
				if (i==0){
					// Add column
					if (isnumeric) {
						datatable.addColumn(ColumnType.NUMBER,data[i][j]);
						formatter.format(datatable, i);						
					} else datatable.addColumn(ColumnType.STRING,data[i][j]);
				} else {
					// Add record row
					if (data[i][j]!= null){
						if (isnumeric) datatable.setValue(i-1, j, Float.parseFloat(data[i][j]));
						else datatable.setValue(i-1, j, data[i][j]);
					}
				}
			}
		}
		return datatable;
	}
	
	public static AbstractDataTable createPagedADT(String[][] data, int[] numcols, int limit, int offset){
		DataTable datatable = DataTable.create();
		boolean isnumeric = false;
		int searchcol = 0;
		int loopmax = 0;
		
		if (limit>(data.length-offset))	limit = data.length-offset;
		if ((offset+limit) > data.length) loopmax = data.length;
		else loopmax = (offset+limit);
		
		
		
		Arrays.sort(numcols);
		
		Options options = Options.create();
		options.setFractionDigits(2);
		options.setNegativeColor("red");
		NumberFormat formatter = NumberFormat.create(options);
		
		for (int i = 0; i < data[0].length; i++) {
			searchcol = Arrays.binarySearch(numcols, i);
			isnumeric = searchcol  >= 0 & searchcol < numcols.length;
			if (isnumeric) {
					datatable.addColumn(ColumnType.NUMBER,data[0][i]);
					formatter.format(datatable, i);						
			} else datatable.addColumn(ColumnType.STRING,data[0][i]);			
		}
		datatable.addRows(limit); 
		
		
		for (int i = offset; i < loopmax; i++) {
			for (int j = 0; j < data[i].length; j++) {
				searchcol = Arrays.binarySearch(numcols, j);
				isnumeric = searchcol  >= 0 & searchcol < numcols.length;
				// Add record row
				if (data[i][j]!= null){
					if (isnumeric) datatable.setValue(i-offset, j, Float.parseFloat(data[i][j]));
					else datatable.setValue(i-offset, j, data[i][j]);
				}
			}
		}
		return datatable;
	}

	public static AbstractDataTable numericXYSeries(AbstractDataTable data, int x, int y, int series){
		DataTable seriesdata = DataTable.create();
		seriesdata.addColumn(ColumnType.NUMBER,data.getColumnLabel(x));
		seriesdata.addColumn(ColumnType.NUMBER,data.getColumnLabel(y));
		seriesdata.addColumn(ColumnType.NUMBER,data.getColumnLabel(series));
		seriesdata.addRows(data.getNumberOfRows());
		for (int i = 0; i < data.getNumberOfRows(); i++) {		
			if(!data.isValueNull(i, x)){
				seriesdata.setValue(i, 0, data.getValueDouble(i,x));
			}
			if(!data.isValueNull(i, y)) {
				seriesdata.setValue(i, 1, data.getValueDouble(i,y));
			}
			if(!data.isValueNull(i, y)) {
				seriesdata.setValue(i, 2, data.getValueDouble(i,series));
			}

		}		
		return seriesdata;
	}

	public static AbstractDataTable filteredTable (AbstractDataTable data, int x, int y, int filtercol, String filterval){
		DataTable datatable = DataTable.create();
		datatable.addColumn(ColumnType.STRING, data.getColumnLabel(x));
		datatable.addColumn(ColumnType.NUMBER, data.getColumnLabel(y));
		int rowcount = 0;
		for (int i = 0; i < data.getNumberOfRows(); i++) {
			if(data.getValueString(i, filtercol).equalsIgnoreCase(filterval)) {
				datatable.addRow();
				datatable.setValue(rowcount, 0, data.getValueString(i, x));
				if (!data.isValueNull(i, y)) datatable.setValue(rowcount, 1, data.getValueDouble(i, y));
				rowcount++;
			}
		}
		return datatable;
	}
	
	public static AbstractDataTable getDataOfColumn(AbstractDataTable data, int colid){
		
		Options options = Options.create();
		options.setFractionDigits(2);
		
		NumberFormat formatter = NumberFormat.create(options);
		
		DataTable subset = DataTable.create();
		
		subset.addColumn(ColumnType.STRING, data.getColumnLabel(0));
		subset.addColumn(ColumnType.NUMBER, data.getColumnLabel(1));
		formatter.format(subset, 1);
		subset.addColumn(ColumnType.NUMBER, data.getColumnLabel(colid));
		formatter.format(subset, 2);
		
		subset.addRows(data.getNumberOfRows());
		for (int i = 0; i < data.getNumberOfRows(); i++) {
			subset.setValue(i, 0, data.getValueString(i, 0));
			subset.setValue(i, 1, data.getValueDouble(i, 1));
			subset.setValue(i, 2, data.getValueDouble(i, colid));
		}
		return subset;
	}
	
	public static AbstractDataTable dataIntoSeries(AbstractDataTable data, int x, int y, int series, boolean stringx){
		DataTable seriesdata = DataTable.create();
		ArrayList<String> xs = getUniqueColumnVals(data, x);
		ArrayList<String> ss = getUniqueColumnVals(data, series);
		Collections.sort(xs);
		Collections.sort(ss);
		
		if (stringx) seriesdata.addColumn(ColumnType.STRING,data.getColumnLabel(x));
			else seriesdata.addColumn(ColumnType.NUMBER,data.getColumnLabel(x));
		
		for (int i = 0; i < ss.size(); i++) {
			seriesdata.addColumn(ColumnType.NUMBER, ss.get(i));
		}
		seriesdata.addRows(xs.size());
		
		for (int i = 0; i < xs.size(); i++) {
			if (stringx) seriesdata.setValue(i,0,xs.get(i));
			else seriesdata.setValue(i,0,Double.parseDouble(xs.get(i)));
		}
		
		int r = 0; 
		int curcol  = 0;

		for (int i = 0; i < data.getNumberOfRows(); i++) {		
			curcol = Collections.binarySearch(ss, data.getValueString(i,series));
			r = Collections.binarySearch(xs, data.getValueString(i, x));
						
			if(r<xs.size() & r>=0 & !data.isValueNull(i, y)) {
				seriesdata.setValue(r, curcol+1, data.getValueDouble(i,y));
			} 
		}		
		return seriesdata;
	}
	public static String csvData(AbstractDataTable data){
		String csv = "";
		for (int i = 0; i < data.getNumberOfColumns(); i++) {
			csv = csv + data.getColumnLabel(i);
			if (i<data.getNumberOfColumns()-1) csv = csv + ",";
			else csv = csv + "\n";
		}
		for (int i = 0; i < data.getNumberOfRows(); i++) {
			for (int j = 0; j < data.getNumberOfColumns(); j++) {
				if (data.getColumnType(j)==ColumnType.STRING) csv = csv + data.getValueString(i, j);
				else if (!data.isValueNull(i, j)) csv = csv + data.getValueDouble(i, j);
				else csv = csv +"null";
				if (j<data.getNumberOfColumns()-1) csv = csv + ",";
				else csv = csv + "\n";
			}						
		}
		return csv;
	}

	public static ArrayList<String> getUniqueColumnVals(AbstractDataTable data, int col){
		
		ArrayList<String> uniquev = new ArrayList<String>();
		for (int i = 1; i < data.getNumberOfRows(); i++) {
			String item = data.getValueString(i, col);
			if(!uniquev.contains(item)) {
				uniquev.add(item);
			}			
		}		
		return uniquev;
	}
}
