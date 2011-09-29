/*
 * MySQLServiceAsync.java
 *
 * Created on July 11, 2008, 3:34 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.irri.households.client;
import com.google.gwt.user.client.rpc.AsyncCallback;


/**
 *
 * @author Jorrel Khalil Aunario
 */
public interface MySQLServiceAsync {
    public void myMethod(String s, AsyncCallback<String> callback);
    public void RunSELECT(String Query, AsyncCallback<String[][]> callback);
    public void SaveCSV(String data, AsyncCallback<String> callback);
    public void downloadCSVFromQuery(String sqlquery, AsyncCallback<String> callback);
}
