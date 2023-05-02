/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.oi.mailingfxapp.dao;

import com.oi.mailingfxapp.enums.DatabaseVendor;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author mmouraam
 */
public class MailingFXAppDao {

    public static PreparedStatement getInsertStatement(String tabela, boolean replaceData, int columns, Connection connection, DatabaseVendor database) throws SQLException {
        StringBuilder sb;
        String sql;

        if (replaceData) {
            sb = new StringBuilder("INSERT INTO ");
            sb.append(tabela).append(" VALUES (");
            for (int j = 0; j < columns; j++) {
                sb.append("?,");
            }
            sb.append(")");
            sql = sb.toString().replace(",)", ")");
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            return preparedStatement;
        } else {
            switch (database) {
                case ORACLE:
                    sb = new StringBuilder("DECLARE BEGIN INSERT INTO ");
                    sb.append(tabela).append(" VALUES (");
                    for (int j = 0; j < columns; j++) {
                        sb.append("?,");
                    }
                    sb.append("); EXCEPTION WHEN DUP_VAL_ON_INDEX THEN NULL; END;");
                    sql = sb.toString().replace(",)", ")");
                    CallableStatement callableStatement = connection.prepareCall(sql);

                    return callableStatement;

                case MYSQL:
                    sb = new StringBuilder("INSERT IGNORE INTO ");
                    sb.append(tabela).append(" VALUES (");
                    for (int j = 0; j < columns; j++) {
                        sb.append("?,");
                    }
                    sb.append(")");
                    sql = sb.toString().replace(",)", ")");
                    PreparedStatement psMysql = connection.prepareStatement(sql);

                    return psMysql;

                case POSTGRESQL:
                    sb = new StringBuilder("INSERT INTO ");
                    sb.append(tabela).append(" VALUES (");
                    for (int j = 0; j < columns; j++) {
                        sb.append("?,");
                    }
                    sb.append(") ON CONFLICT DO NOTHING");
                    sql = sb.toString().replace(",)", ")");
                    PreparedStatement psPostgre = connection.prepareStatement(sql);

                    return psPostgre;

                case SQLSERVER:
                    sb = new StringBuilder("INSERT INTO ");
                    sb.append(tabela).append(" VALUES (");
                    for (int j = 0; j < columns; j++) {
                        sb.append("?,");
                    }
                    sb.append(")");
                    sql = sb.toString().replace(",)", ")");
                    PreparedStatement psSqlServer = connection.prepareStatement(sql);

                    return psSqlServer;
            }
        }

        return null;
    }

    public static Connection getConnection(String host, String port, String schema, String user, String password, DatabaseVendor database, String classe) throws SQLException, ClassNotFoundException {
        String connectionString;
        Connection connection = null;
        try {
            switch (database) {
                case ORACLE:
                    if(classe != null && !"".equals(classe))
                        Class.forName(classe);
                    else
                        Class.forName("oracle.jdbc.driver.OracleDriver");
                    
                    DriverManager.setLoginTimeout(3);

                    connectionString = "jdbc:oracle:thin:@" + host + ":" + port + "/" + schema;
                    System.out.println("connect:" + connectionString);

                    //connection = DriverManager.getConnection("jdbc:oracle:thin:@10.3.29.30:1521:xe", "PROMOCOESWEB", "Everis@2020");
                    //connection = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "MMOURAAM", "everis@2019");
                    try {
                        connection = DriverManager.getConnection(connectionString, user, password);
                        
                    } catch (Exception e) {
                        connectionString = "jdbc:oracle:thin:@" + host + ":" + port + ":" + schema;
                        connection = DriverManager.getConnection(connectionString, user, password);
                    }
                    break;

                case MYSQL:
                    if(classe != null && !"".equals(classe))
                        Class.forName(classe);
                    else
                        Class.forName("com.mysql.cj.jdbc.Driver");
                    
                    DriverManager.setLoginTimeout(3);

                    connectionString = "jdbc:mysql://" + host + ":" + port + "/" + schema + "?useTimezone=true&serverTimezone=UTC";
                    System.out.println("connect:" + connectionString);

                    connection = DriverManager.getConnection(connectionString, user, password);
                    break;

                case POSTGRESQL:
                    if(classe != null && !"".equals(classe))
                        Class.forName(classe);
                    else
                        Class.forName("org.postgresql.Driver");
                    
                    DriverManager.setLoginTimeout(3);

                    connectionString = "jdbc:postgresql://" + host + ":" + port + "/" + schema;
                    System.out.println("connect:" + connectionString);

                    connection = DriverManager.getConnection(connectionString, user, password);
                    break;

                case SQLSERVER:
                    if(classe != null && !"".equals(classe))
                        Class.forName(classe);
                    else
                        Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
                    
                    DriverManager.setLoginTimeout(3);

                    connectionString = "jdbc:sqlserver://" + host + ":" + port + ";DatabaseName=" + schema;
                    System.out.println("connect:" + connectionString);

                    connection = DriverManager.getConnection(connectionString, user, password);
                    break;
            }
        } catch (SQLException | RuntimeException | ClassNotFoundException e) {
            throw e;
        }
        return connection;
    }

    public static void returnConnection(Connection connection) throws SQLException {
        if (connection != null) {
            try {
                connection.clearWarnings();
                connection.setAutoCommit(true);
            } catch (SQLException | RuntimeException e) {
                throw e;
            }
            try {
                connection.close();
            } catch (SQLException | RuntimeException e) {
                throw e;
            }
        }
    }

    public static void closePreparedStatement(Statement statement) throws SQLException {
        try {
            if (statement != null) {
                statement.close();
            }
        } catch (SQLException | RuntimeException e) {
            throw e;
        }
    }
}
