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
    public void SendMail(String table, String email, String resultURL, AsyncCallback<Void> callback);
    /*table-table to be downloaded. this willbe included in the subject of the email to the user*/
    /*email-email address of user entered in popup*/
    /*resultURL-url of data which is included in the body of the email*/
}
