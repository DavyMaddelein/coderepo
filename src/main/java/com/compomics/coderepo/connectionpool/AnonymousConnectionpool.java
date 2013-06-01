/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.compomics.coderepo.connectionpool;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author Davy
 */
public class AnonymousConnectionpool {
    
    //get from property file
    private static final int MAX_CONNECTIONS = 10;
    private static final String username = "";
    private static final String password = "";
    private static final String url = "";
    private static final String database = "";
    
    private static Set<Connection> connectionPool = new HashSet<>(MAX_CONNECTIONS);
    private static MysqlDataSource dbSource = new MysqlDataSource();
    
    public static Connection createConnection() throws SQLException, NoConnectionsAvailableException{
        Connection connection;
        if (connectionPool.size() <= MAX_CONNECTIONS){
            dbSource.setUser(username);
            dbSource.setPassword(password);
            dbSource.setURL(url);
            dbSource.setDatabaseName(database);
            connection = dbSource.getConnection();
            connectionPool.add(connection);
            return connection;
        } else {
            return getConnection();
        }
    }

    public static Connection getConnection() throws NoConnectionsAvailableException {
        if (!connectionPool.isEmpty()){
        return connectionPool.iterator().next();
        } else {
            throw new NoConnectionsAvailableException("no connections are available at the moment");
        }
    }
    
    public static void returnConnection(Connection usedConnection){
        connectionPool.add(usedConnection);
    }
    
    public static class NoConnectionsAvailableException extends Exception{
        public NoConnectionsAvailableException(String message){
            super(message);
        }
    }   
}
