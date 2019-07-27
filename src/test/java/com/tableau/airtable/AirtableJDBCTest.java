package com.tableau.airtable;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertTrue;


import java.sql.*;
import java.util.Properties;

public class AirtableJDBCTest {

    public Connection getConnection() throws SQLException {
        DriverManager.registerDriver (new com.tableau.airtable.AirtableJDBC());
        Properties props = new Properties();
        props.put("key", System.getenv("AIRTABLE_API_KEY"));
        props.put("base", "appOQS45OT1iBFN9l");
        return DriverManager.getConnection("jdbc:airtable:https://api.airtable.com/v0", props);
    }

    public Statement createStatement() throws SQLException {
        Connection con = getConnection();
        return con.createStatement();
    }

    @Test
    public void testConnection() throws SQLException {
        getConnection();
    }


    @Test
    public void testCreateStatement() throws SQLException {
        createStatement();
    }

    @Test
    public void testExecuteQuery() throws SQLException {
        Statement stmt = createStatement();
        ResultSet rs = stmt.executeQuery("Breeding%20Plants");
        ResultSetMetaData rsm = rs.getMetaData();
        assertTrue(rsm.getColumnCount() == 17);
        while(!rs.isLast()) {
            for (int col = 1; col <= rsm.getColumnCount(); col++) {
                Object obj = rs.getObject(col);
                if (obj != null)
                    System.out.print(obj.toString() + "\t");
                else
                    System.out.print("NULL\t");
            }
            System.out.println();
            rs.next();
        }
    }

    @Test
    public void testResultSetMetadata() throws SQLException {

    }
}
