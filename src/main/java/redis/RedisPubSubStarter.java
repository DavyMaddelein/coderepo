/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package redis;

/**
 *
 * @author Davy
 */
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class RedisPubSubStarter {
    public static void main(String[] args) {
        new AnnotationConfigApplicationContext( AppConfig.class );
    }
}