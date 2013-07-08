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

    /**
     * gets the first zip file from an url, in case of a maven repo deploy this
     * should be the only zip in the folder
     *
     * @param repoURL the URL to get the zip from
     * @param suffix what file extension should be looked for
     * @param returnAlternateArchives if the requested file extension isn't
     * found, return the first .zip/tar.gz found
     * @return URL to the archive file
     * @throws MalformedURLException
     * @throws IOException
     * @throws XMLStreamException
     */
    public static URL getUrlOfZippedVersion(URL repoURL, String suffix, boolean returnAlternateArchives) throws MalformedURLException, IOException, XMLStreamException,NullPointerException {
        XMLInputFactory xmlParseFactory = XMLInputFactory.newInstance();
        XMLEventReader xmlReader = xmlParseFactory.createXMLEventReader(repoURL.openStream());
//probably cleaner with dom parser or jaxb
        XMLEvent htmlTag;
        String toReturn = null;
        String alternativeReturn = null;
        Attribute attribute;
        while (xmlReader.hasNext()) {
            htmlTag = xmlReader.nextEvent();
            if (htmlTag.isStartElement()) {
                Iterator<Attribute> attributes = htmlTag.asStartElement().getAttributes();
                while (attributes.hasNext()) {
                    attribute = attributes.next();
                    toReturn = attribute.getValue();
                    if (attribute.getName().getLocalPart().equalsIgnoreCase("href") && attribute.getValue().toLowerCase().contains(suffix)) {
                        break;
                    } else if (toReturn.contains(".zip") || toReturn.contains(".tar.gz") || toReturn.contains(".bz") && returnAlternateArchives) {
                        alternativeReturn = toReturn;
                    }
                }
            }
        }
        if (returnAlternateArchives && toReturn == null) {
            toReturn = alternativeReturn;
        }
        return new URL(toReturn);
    }
}
