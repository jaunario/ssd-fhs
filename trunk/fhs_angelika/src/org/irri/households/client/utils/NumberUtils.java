package org.irri.households.client.utils;

public class NumberUtils {

	   public static int[] createIntSeries(int start, int length, int by){
	    	int[] intarray = new int[length];
	    	for (int i = 0; i < intarray.length; i++) {
				intarray[i] = start+i*by;
			}
	    	return intarray;
	    }

}
