/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.irri.households.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.TreeItem;

/**
 *
 * @author jaunario
 */
public class Utils {
	public static dbConnectAsync getService() {
        // Create the client proxy. Note that although you are creating the
        // service interface proper, you cast the result to the asynchronous
        // version of the interface. The cast is always safe because the
        // generated proxy implements the asynchronous interface automatically.

        return GWT.create(dbConnect.class);
    }  

    public static String delimStringToHTMLList(String dstring, String delimiter, String listtype){
        String html = "";
        String[] split = dstring.split(delimiter);
        for (int i=0;i<split.length;i++){
            html += placeInTag(split[i].trim(), "li");
        }
        return placeInTag(html,listtype);
    }

    public static String placeInTag(String str, String tag){
        return "<"+tag+">"+str+"</"+tag+">";
    }

    public static String summarizeYears(String yrstr){
        String[] arrayyrs = yrstr.split(",");
        String newyrs = "";
        int hi = 0;
        int low = 0;
        int now = 0;
        for (int i=0;i<arrayyrs.length;i++){
            now = Integer.parseInt(arrayyrs[i]);
            if (now>hi){
                hi = now;
                low = now;
            }
            else if (low-now==1){
                low = now;
            }
            else if (low-now>1){
                if (hi==low){
                    newyrs = String.valueOf(hi)+", " + newyrs ;
                } else{
                    newyrs = String.valueOf(low)+"-"+String.valueOf(hi)+", " + newyrs;
                }
                hi = now;
                low =now;
            }
        }


        return newyrs;
    }

    public static int maxStringLength(String[] StrArray){
        int maxlength = 0;
        for (String s: StrArray){
            if (s.length()>maxlength){
                maxlength = s.length();
            }
        }
        return maxlength;
    }
    
    public static int getTreeItemIndex(Tree thistree, String ItemID, int TreeLevels){
    	int itemindex = -1;
    	boolean notfound = true;
    	boolean notend = true;
    	boolean notvalid = true;
    	
    	int index = 0;
    	TreeItem parent = thistree.getItem(0);
    	int[] lastlevels = new int[TreeLevels];
    	int curlevel = 0;
    	// find TreeItem with matching ID 
    	while (notfound && notend) {
    		TreeItem curitem = parent.getChild(index);
			String curid = curitem.getElement().getId();
			if(ItemID.equalsIgnoreCase(curid)){
				notfound = false;
				//ProjectDetailsBox.clear();
                //SiteDetailsBox.clear();
                //ReportBox.clear();
				//String sql = countrysql + parseTreeIdToWhereClause(treeitem);                
                //displayRecord(sql+ " GROUP BY 1");
                curitem.setSelected(true);
			} else {
				int children = curitem.getChildCount();
				if (children>0){
					lastlevels[curlevel]=index;
					curlevel++;
					parent = curitem;
					index = 0;
		    	} else {
					index++;
					notvalid = true;
					do {						
						if(index>=parent.getChildCount() && curlevel>0){
							lastlevels[curlevel]=0;
							parent = parent.getParentItem();
							curlevel--;
							index = lastlevels[curlevel]+1;
						} else if (index>=parent.getChildCount() && curlevel==0){
							notend = false;
						} else {
							notvalid = false;
						}
					} while (notvalid);										
		    	}				
			}								
		}
    	return itemindex;
    }

   
}
