/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.compomics.coderepo.autoupdater;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.XMLEvent;

/**
 *
 * @author Davy
 */
public class WebDAO {

    public static String getLatestVersionNumberFromRemoteRepo(BufferedReader remoteVersionsReader) throws XMLStreamException {
        XMLInputFactory xmlParseFactory = XMLInputFactory.newInstance();
        XMLEventReader xmlReader = xmlParseFactory.createXMLEventReader(remoteVersionsReader);
        MetaDataXMLParser xmlParser = new MetaDataXMLParser(xmlReader);
        return xmlParser.getHighestVersionNumber();
    }

    public static URL getUrlOfZippedVersion(URL repoURL, String suffix) throws MalformedURLException, IOException, XMLStreamException {
        XMLInputFactory xmlParseFactory = XMLInputFactory.newInstance();
        XMLEventReader xmlReader = xmlParseFactory.createXMLEventReader(repoURL.openStream());

        XMLEvent htmlTag;
        String toReturn = null;
        Attribute attribute;
        while (xmlReader.hasNext()) {
            htmlTag = xmlReader.nextEvent();
            if (htmlTag.isStartElement()) {
                Iterator<Attribute> attributes = htmlTag.asStartElement().getAttributes();
                while (attributes.hasNext()) {
                    attribute = attributes.next();
                    if (attribute.getName().getLocalPart().equalsIgnoreCase("href") && attribute.getValue().contains(suffix)) {
                        toReturn = attribute.getValue();
                        break;
                    }
                }
            }
        }
        return new URL(toReturn);
    }
}
