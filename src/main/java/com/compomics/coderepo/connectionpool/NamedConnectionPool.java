/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.compomics.coderepo.connectionpool;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Davy
 */
public class NamedConnectionPool {
    
    //get from property file
    private static final int MAX_CONNECTIONS = 10;
    
    private static Map<String,Connection> connectionPool = new HashMap<>(MAX_CONNECTIONS);
    private static MysqlDataSource dbSource = new MysqlDataSource();
    
    public static Connection createConnection(String username,String password, String url, String database) throws SQLException, NoConnectionsAvailableException{
        Connection connection;
        if (connectionPool.size() <= MAX_CONNECTIONS){
            dbSource.setUser(username);
            dbSource.setPassword(password);
            dbSource.setURL(url);
            dbSource.setDatabaseName(database);
            connection = dbSource.getConnection();
            connectionPool.put(username,connection);
            return connection;
        } else {
            throw new NoConnectionsAvailableException("All connections are taken");
        }
    }

    //least secure implementation
    public static Connection getConnectionForUser(String username) throws NoConnectionsAvailableException {
        if (!connectionPool.isEmpty()){
        return connectionPool.get(username);
        } else {
            throw new NoConnectionsAvailableException("no connections are available at the moment");
        }
    }
    
    public static void returnConnection(String username,Connection usedConnection){
        connectionPool.put(username,usedConnection);
    }
    
    public static class NoConnectionsAvailableException extends Exception{
        public NoConnectionsAvailableException(String message){
            super(message);
        }
    }   
    
}
