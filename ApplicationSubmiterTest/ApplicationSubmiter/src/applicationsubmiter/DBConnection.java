/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package applicationsubmiter;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

/**
 *
 * @author rakib
 */
public class DBConnection {
    
    private final String dbDriver;
    private final String dbUrl;
    private final String dbUser;
    private final String dbPassword;
    
    private Connection dbConnection = null;
    
    public DBConnection(Properties dbConfig){
    
      dbDriver=dbConfig.getProperty("dbDriver");
      dbUrl=dbConfig.getProperty("dbUrl");
      dbUser=dbConfig.getProperty("dbUser");
      dbPassword=dbConfig.getProperty("dbPassword");
     
      
    }
    
    public Connection createDBConnection() throws ClassNotFoundException, SQLException{
        
         Class.forName(dbDriver);
         dbConnection = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
         return dbConnection;
    }
    
    public ResultSet getResultset(Connection dbCon) throws SQLException{
    
        String sql;
        ResultSet rs;
        Statement dbStmt;
                
        dbStmt = dbCon.createStatement();
        sql = "Select * from users";
        rs = dbStmt.executeQuery(sql);
        return rs;
        
    }
  
    
}
