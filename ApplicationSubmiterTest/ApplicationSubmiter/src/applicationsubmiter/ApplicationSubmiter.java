/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package applicationsubmiter;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author rakib
 */
public class ApplicationSubmiter {

    /**
     * @param args the command line arguments
     */
    final static ExecutorService AppExecutor = Executors.newFixedThreadPool(2);
    public static DBConnection dbConobj = null;
    public static  Connection dbConn =null;
    
    static {
        ReadProperties readConfig = new ReadProperties();
        Properties dbConfig = readConfig.ReadConfig();

         dbConobj = new DBConnection(dbConfig);
        try {
            dbConn = dbConobj.createDBConnection();
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(ApplicationSubmiter.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        // TODO code application logic here

        //Connection dbConn = null;
        //ResultSet dbRS = null;

        //dbRS = dbConobj.getResultset(dbConn);
        Timer timer = new Timer();

        timer.scheduleAtFixedRate(new TimerTask() {

            public void run() {
                try {
                    ResultSet dbRS = dbConobj.getResultset(dbConn);
                    while (dbRS.next()) {
                        int status = dbRS.getInt("status");
                        int id= dbRS.getInt("id");
                        String command = dbRS.getString("submit_value");
                        System.out.println(command);

                        if (status == 1) {
                            Runnable worker = new ApplicationExecutor(command,id);
                            AppExecutor.execute(worker);

                        }

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, 0, 5 * 60 * 1000);

        // AppExecutor.shutdown();
        while (!AppExecutor.isTerminated()) {
        }
        System.out.println("Finished all threads");
    }

}
