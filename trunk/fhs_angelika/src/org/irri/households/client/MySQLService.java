/*
 * MySQLService.java
 *
 * Created on July 11, 2008, 3:34 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.irri.households.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 *
 * @author Jorrel Khalil Aunario
 */
@RemoteServiceRelativePath("mysqlservice")
public interface MySQLService extends RemoteService{
    public String myMethod(String s);
    public String[][] RunSELECT(String Query);
    public String SaveCSV(String data);
    public String downloadCSVFromQuery(String sqlquery);
    public void SendMail(String table, String email, String resultURL);
}
