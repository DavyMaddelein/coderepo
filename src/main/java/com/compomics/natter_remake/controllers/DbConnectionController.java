package com.compomics.natter_remake.controllers;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 *
 * @author Davy
 */
public class DbConnectionController {

    private static Connection connection;

    public static Connection createConnection(String username, String password, String url, String database) throws SQLException {
        DbConnectionController instance = new DbConnectionController(username, password, url, database);
        return DbConnectionController.getConnection();
    }

    private DbConnectionController(String username, String password, String url, String database) throws SQLException {
        MysqlDataSource dbSource = new MysqlDataSource();
        dbSource.setServerName(url);
        dbSource.setDatabaseName(database);
        dbSource.setUser(username);
        dbSource.setPassword(password);
        DbConnectionController.connection = dbSource.getConnection();
    }

    public static Connection getConnection() throws SQLException {
        if (connection != null) {
            return connection;
        } else {
            throw new SQLException("connection not yet established");
        }
    }
}
