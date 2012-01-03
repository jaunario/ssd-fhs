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
import java.util.Properties;
import java.util.Random;
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;



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
            		"jdbc:mysql://127.0.0.1/fhh_survey", "ssd.webview", "Vi3wOn1y"); // for amazon and dev           		
        			//"jdbc:mysql://172.29.31.182/fhh_survey", "ssd.webview", "Vi3wOn1y"); // for geo
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
            // Loop through dataset put in string matrix. out is Used for displaying data to table. sent through asyncallback
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
        //String htdocs = System.getenv("HTDOCS") + "/csvs";
        String htdocs = "/data/gisadmin/html/csvs";
        //String hostname = System.getenv("DOMAIN");
        String hostname = "ricestat.irri.org";
        String url = "http://"+ hostname +"/csvs/"+filename;
        File csvFile = new File(htdocs+"/"+filename);
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
        //File file = new File("/www/csvs/"+fname);
        File file = new File("/data/gisadmin/html/csvs/"+fname);
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
    
    public String downloadCSVFromQuery(String sqlquery){
    	String urladd = "";
    	String csvdata = "";
    	String[] type;
    	try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            Connection connection = DriverManager.getConnection(
            		"jdbc:mysql://127.0.0.1/fhh_survey", "ssd.webview", "Vi3wOn1y"); // for amazon and dev           		
        			//"jdbc:mysql://172.29.31.182/fhh_survey", "ssd.webview", "Vi3wOn1y"); // for geo
            Statement select = connection.createStatement();

            ResultSet result = select.executeQuery(sqlquery);
            int cols = result.getMetaData().getColumnCount();
            type = new String[cols];
            
            for (int j=0;j<cols;j++){ 
                    csvdata += result.getMetaData().getColumnLabel(j+1);
                    type[j] = result.getMetaData().getColumnTypeName(j+1);                    
                    if (j+1<cols) csvdata+=",";
                    else csvdata += "\n"; 
            }
            
            result.beforeFirst();
            while (!result.isLast()){
                result.next();
                for (int j=0;j<cols;j++){                    
                    if (type[j]=="VARCHAR"){
                    	csvdata += "\""+result.getString(j+1)+"\"";
                    } else {
                    	csvdata += result.getString(j+1);
                    }
                	
                    
                    /*if (out[i][j]==null){
                       out[i][j] = "No Data";
                    }*/
                    if (j+1<cols) csvdata+=",";
                    else csvdata += "\n"; 
                }
            }
            connection.close();
        }
        catch (Exception e){
            System.out.println("Error: " + e);
            return null;
        }                
    	urladd = SaveCSV(csvdata);
    	
    	return urladd;
    }

    /*method for sending an email to the user who entered his/her email address at the popup message.
    *the email's body will contain the link of the data. Sending of mail is only applicable whe the user
    *is downloading data with >=2000 rows.*/ 
	public void SendMail(String table, String email, String query){
		String SMTP_HOST_NAME = "smtp.gmail.com";
		int SMTP_HOST_PORT = 465;
		String SMTP_AUTH_USER = "ssd.ricestat@gmail.com"; //email address that will be used as the sender of the mail
		/**/
		File file = new File("email.txt"); //file containing ssd.ricestat password
		int ch;
		StringBuffer strContent = new StringBuffer("");
		FileInputStream fin = null;
		 try
		    {
		      fin = new FileInputStream(file);
		      while( (ch = fin.read()) != -1)
		        strContent.append((char)ch);
		      fin.close();
		    }
		    catch(FileNotFoundException e)
		    {
		      System.out.println("File " + file.getAbsolutePath() +
		                             " could not be found on filesystem");
		    }
		    catch(IOException ioe)
		    {
		      System.out.println("Exception while reading the file" + ioe);
		    }
		/**/
		String SMTP_AUTH_PWD  = strContent.toString(); //ssd.ricestat@gmail.com password converted from stringbuffer to string
		String messagebody = downloadCSVFromQuery(query); //link/url of the data
		String emailadd = email;

        // Get system properties 
		Properties properties = new Properties();

        // Setup mail server 
       //properties.setProperty("mail.smtp.host", host); 
		properties.put("mail.transport.protocol", "smtps");
        properties.put("mail.smtps.host", SMTP_HOST_NAME);
        properties.put("mail.smtps.auth", "true");
        // props.put("mail.smtps.quitwait", "false");

        // Get the default Session object. 
        Session mailSession = Session.getDefaultInstance(properties);
        mailSession.setDebug(true);
        
        Transport transport = null;
		try {
			transport = mailSession.getTransport();
		} catch (NoSuchProviderException e) {
			e.printStackTrace();
		}

        // Create a default MimeMessage object. 
        MimeMessage message = new MimeMessage(mailSession);
        try { //this will be the subject of the mail to be sent to user. table is the name of the table that the user downloaded
			message.setSubject("Farm Household Survey Data-"+table+" table"); 
		} catch (MessagingException e) {
			e.printStackTrace();
		}
        try {
			message.setContent(messagebody, "text/plain");
		} catch (MessagingException e) {
			e.printStackTrace();
		}

        try {
			message.addRecipient(Message.RecipientType.TO,
			     new InternetAddress(emailadd));
		} catch (AddressException e) {
			e.printStackTrace();
		} catch (MessagingException e) {
			e.printStackTrace();
		}

        try {
			transport.connect
			  (SMTP_HOST_NAME, SMTP_HOST_PORT, SMTP_AUTH_USER, SMTP_AUTH_PWD);
		} catch (MessagingException e) {
			e.printStackTrace();
		}

        try {
			transport.sendMessage(message,
			    message.getRecipients(Message.RecipientType.TO));
		} catch (MessagingException e) {
			e.printStackTrace();
		}
        try {
			transport.close();
		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}
}


