/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package applicationsubmiter;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 *
 * @author rakib
 */
public class ReadProperties {

    public Properties ReadConfig() {
        
        Properties dbConfig = new Properties();
        InputStream input = null;

        try {

            String filename = "dbconfig.properties";
            input = new FileInputStream("dbconfig.properties");

            if (input == null) {
                System.out.println("Sorry, unable to find " + filename);

            }

            dbConfig.load(input);

        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {

            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {

                    e.printStackTrace();
                }
            }
        }
        return dbConfig;
    }
}
