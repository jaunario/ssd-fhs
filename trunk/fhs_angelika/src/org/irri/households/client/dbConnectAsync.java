/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.irri.households.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 *
 * @author jaunario
 */
public interface dbConnectAsync {
    public void RunSELECT(String Query, AsyncCallback<String[][]> callback);
    public void SaveCSV(String data, AsyncCallback<String> callback);
}
