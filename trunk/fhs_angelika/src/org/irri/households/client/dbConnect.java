/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.irri.households.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 *
 * @author jaunario
 */
@RemoteServiceRelativePath("dbconnect")
public interface dbConnect extends RemoteService {
    public String[][] RunSELECT(String Query);
    public String SaveCSV(String data);
}
