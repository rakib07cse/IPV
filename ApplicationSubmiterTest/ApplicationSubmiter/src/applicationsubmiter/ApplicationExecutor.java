/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package applicationsubmiter;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Stack;

/**
 *
 * @author rakib
 */
public class ApplicationExecutor implements Runnable {

    private String command;
    private int id;

    public ApplicationExecutor(String command, int id) {
        this.command = command;
        this.id = id;
    }

    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName() + "(Start) message =" + command);
        processCommand(command, id);
        System.out.println(Thread.currentThread().getName() + "(End)");
    }

    private void processCommand(String cmd, int id) {

        Stack<String> msgStack = new Stack<String>();
        String[] finalStatus = null;
        String[] trackingUrl = null;
        
        String line = "";
        String msgLine = "";
        int statusCode = 255;

        boolean trackingUrlFound = false;
        boolean finalStatusFound = false;

        StringBuffer output = new StringBuffer();
        Process p;
        try {
            p = Runtime.getRuntime().exec(cmd);
            statusCode = p.waitFor();
            BufferedReader reader = new BufferedReader(new InputStreamReader(p.getErrorStream()));

            while ((line = reader.readLine()) != null) {
                msgStack.push(line);
            }
            while (!msgStack.empty() && (!trackingUrlFound || !finalStatusFound)) {
                msgLine = msgStack.pop();
                if (!trackingUrlFound && msgLine.contains("tracking URL")) {

                    trackingUrl = msgLine.trim().split(" ");
                    System.out.println(trackingUrl[2]);

                    trackingUrlFound = true;

                }
                if (!finalStatusFound && msgLine.contains("final status")) {

                    finalStatus = msgLine.trim().split(" ");
                    if (finalStatus[2].equals("SUCCEEDED")) {
                        finalStatusFound = true;
                        System.out.println(finalStatus[2]);
                    }

                }
                System.out.println(output.toString());
            }
            if (finalStatusFound) {
                String sql;
                int value=0;
                Statement dbStmt = ApplicationSubmiter.dbConn.createStatement();

                sql = "update users set status="+value+", trackingUrl="+"'"+trackingUrl[2]+"'"+" where id="+id;
                System.out.println(sql);
                dbStmt.executeUpdate(sql);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
