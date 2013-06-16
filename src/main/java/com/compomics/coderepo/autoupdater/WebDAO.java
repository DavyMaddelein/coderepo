/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.compomics.coderepo.autoupdater;

import java.io.BufferedReader;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;

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
    
}
