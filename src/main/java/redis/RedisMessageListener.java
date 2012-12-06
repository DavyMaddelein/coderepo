/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.compomics.coderepo.redis;

/**
 *
 * @author Davy
 */
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;

public class RedisMessageListener implements MessageListener {
    @Override
    public void onMessage( final Message message, final byte[] pattern ) {
        System.out.println( "Message received: " + message.toString() );
    }
}