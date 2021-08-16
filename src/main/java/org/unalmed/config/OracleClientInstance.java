package org.unalmed.config;


import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;
import oracle.jdbc.pool.OracleDataSource;


public class OracleClientInstance {
    /**
     * @return Connection
     */
    public static Connection oracleClient() {

        OracleDataSource ods = null;
        Connection conn = null;

        try {
            String username = System.getProperty("oracledb.username");
            String password = System.getProperty("oracledb.password");
            String url = System.getProperty("oracledb.url");

            // Create DataSource and connect to the local database
            ods = new OracleDataSource();
            ods.setURL("jdbc:oracle:thin:@//" + url);
            ods.setUser(username);
            ods.setPassword(password);
            conn = ods.getConnection();

            return conn;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
