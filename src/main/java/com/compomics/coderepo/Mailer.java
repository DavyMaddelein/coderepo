/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.compomics.coderepo;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

/**
 * Created by IntelliJ IDEA.
 * User: Davy
 * Date: 10/18/12
 * Time: 9:37 AM
 * To change this template use File | Settings | File Templates.
 */
public class Mailer {

    public void sendMail(String problem) throws MessagingException {

        //set up list of people to be notified
        String[] recipients = {"davy.maddelein@ugent.be"};
        this.sendMail(problem, recipients);

    }

    public void sendMail(String problem, String[] recipients) throws MessagingException {

        //Set the host smtp address
        Properties props = new Properties();
        props.put("mail.smtp.host", "vibugent.ugent.be");

        // create some properties and get the default Session
        Session session = Session.getDefaultInstance(props, null);

        // create a message
        Message msg = new MimeMessage(session);

        // set the from and to address
        InternetAddress addressFrom = new InternetAddress("noreply@ms-limsbackupsystem.com");
        msg.setFrom(addressFrom);

        InternetAddress[] addressTo = new InternetAddress[recipients.length];
        for (int i = 0; i < recipients.length; i++) {
            addressTo[i] = new InternetAddress(recipients[i]);
        }
        msg.setRecipients(Message.RecipientType.TO, addressTo);

        // Optional : You can also set your custom headers and such in the Email if you Want

        // Setting the Subject and Content Type
        msg.setSubject("problem backing up ms-lims db");
        msg.setContent(problem, "text/plain");
        Transport.send(msg);
    }
}
