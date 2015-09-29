package com.perfmath.spring.soba.aop.proxy;
import java.sql.*;

import com.perfmath.spring.soba.util.PerfBasicUtil;
public class JDBCConnectionTest
{
    public static void main (String[] args)
    {
        Connection conn = null;
        PerfBasicUtil.setProfilingStatus("enabled");
        try {
        	IJDBCConnection mySQLJDBCConnection = new MySQLJDBCConnection ();
        	IJDBCConnection jdbcConnection = 
        			(IJDBCConnection) PerfBasicLoggingHandler.createProxy(mySQLJDBCConnection);
			String connectionDescriptor = "com.mysql.jdbc.Driver+"
					+ "jdbc:mysql://localhost:3306/soba32+" + "soba32admin+"
					+ "soba32admin";
        	conn = jdbcConnection.checkConnection(connectionDescriptor);
        	if (conn != null) {
            System.out.println ("   --> calling by proxy completed with " + 
            		" database connection established");
        	}
        } catch (Exception e) {
            System.err.println ("Cannot connect to database server: " + e.getMessage());
        } finally {
            if (conn != null) {
                try
                {
                    conn.close ();
                    System.out.println ("   --> Database connection terminated");
                }
                catch (Exception e) { /* ignore close errors */ }
            }
        }
        PerfBasicUtil.setProfilingStatus("disabled");
    }
}
