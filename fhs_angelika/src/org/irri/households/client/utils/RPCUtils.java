/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.irri.households.client.utils;

/**
 *
 * @author Jorrel Khalil Aunario
 */

import org.irri.households.client.MySQLService;
import org.irri.households.client.MySQLServiceAsync;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.ServiceDefTarget;

public class RPCUtils {    
    

    public static MySQLServiceAsync getService(String svc){
        // Create the client proxy. Note that although you are creating the
        // service interface proper, you cast the result to the asynchronous
        // version of
        // the interface. The cast is always safe because the generated proxy
        // implements the asynchronous interface automatically.
        MySQLServiceAsync service = (MySQLServiceAsync) GWT.create(MySQLService.class);
        // Specify the URL at which our service implementation is running.
        // Note that the target URL must reside on the same domain and port from
        // which the host page was served.
        //
        ServiceDefTarget endpoint = (ServiceDefTarget) service;
        String moduleRelativeURL = GWT.getModuleBaseURL() + svc;
        endpoint.setServiceEntryPoint(moduleRelativeURL);
        return service;
    }
    
 }
