/*
 * MySQLServiceImpl.java
 *
 * Created on July 11, 2008, 3:34 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.irri.households.server;

import org.irri.households.client.MySQLService;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import java.sql.*;
import java.io.*;
import java.util.Random;
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 *
 * @author jaunario
 */
public class MySQLServiceImpl extends RemoteServiceServlet implements
    MySQLService {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public String myMethod(String s) {
        // Do something interesting with 's' here on the server.
        return "Server says: " + s;
    }
    
    public String[][] RunSELECT(String Query){
        String[][] out;
        
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            Connection connection = DriverManager.getConnection(
            		"jdbc:mysql://172.29.31.182/fhh_survey", "jaunario", "Ragnarok09");
            		//"jdbc:mysql://localhost/rice_stats", "root", "Ragnarok09");
            Statement select = connection.createStatement();

            ResultSet result = select.executeQuery(Query);
            result.last();
            int rows = result.getRow();
            int cols = result.getMetaData().getColumnCount();
            
            out = new String[rows+1][cols];
            
            for (int j=0;j<cols;j++){ 
                    out[0][j] = result.getMetaData().getColumnLabel(j+1);
                }
            
            result.beforeFirst();
            int i=1;

            while (!result.isLast()){
                result.next();
                for (int j=0;j<cols;j++){                    
                    out[i][j] = result.getString(j+1);
                    /*if (out[i][j]==null){
                       out[i][j] = "No Data";
                    }*/
                }
                i++;
            }
            connection.close();
        }
        catch (Exception e){
            System.out.println("Error: " + e);
            return null;
        }                
        return out;
    }
    
    public String SaveCSV(String data){
       String filename = createFilename();
       String url = "http://geo.irri.org/csvs/"+filename;
       File csvFile = new File("/www/csvs/"+filename);
       //String url = "http://localhost/csvs/"+filename;
       //File csvFile = new File("C:/Program Files/Apache Software Foundation/Apache2.2/htdocs/csvs/"+filename);
              
       try{
           csvFile.createNewFile();
           OutputStreamWriter csvOSW = new OutputStreamWriter(new FileOutputStream(csvFile));
           csvOSW.write(data);
           csvOSW.close();
       }
       catch (Exception e){
           System.out.println("Error: " + e);
           return null;
       }
                      
       return url;
    }
    
    private String createFilename(){
        Date today = new Date();
        DateFormat simpleDate = new SimpleDateFormat("yyyy-MM-dd");
        Random r = new Random();
        int i = absolute(r.nextInt());
        String fname=simpleDate.format(today)+"_"+Integer.toString(i)+".csv";
        File file = new File("/www/csvs/"+fname);
        while (file.exists()){
            i=absolute(r.nextInt());
            fname=today.toString()+"_"+Integer.toString(i)+".csv";
        }
        return fname;
    }
    
    private int absolute(int x){
        if (x<0){
            x = -x;
        }
        return x;
    }
        
}
