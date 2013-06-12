package com.compomics.coderepo;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Properties;
import java.util.Scanner;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.zip.ZipInputStream;
import javax.swing.JOptionPane;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.XMLEvent;

/**
 *
 * @author Davy
 */
public class DownloadLatestZipFromRepo {

    private Properties localJarProps = new Properties();
    private String latestRemoteRelease;

    public static void main(String[] args) throws MalformedURLException, IOException, XMLStreamException {
        new DownloadLatestZipFromRepo(new File("C:\\Users\\Davy\\Desktop\\java\\thermo-msf-parser\\thermo_msf_parser_GUI\\target\\thermo_msf_parser_GUI-2.0.4\\thermo_msf_parser_GUI-2.0.4.jar").toURL());
    }

    //main [jarPath,flag overwrite oldfiles]
    public DownloadLatestZipFromRepo(URL jarPath) throws IOException, XMLStreamException {
        if (NewVersionReleased(jarPath)) {
            if (System.getProperty("os.name").contains("windows")) {
                File downloadedFile = downloadForWindows(latestRemoteRelease, new File(jarPath.getPath()));
            } else {
                File downloadedFile = downloadForUnix(latestRemoteRelease, new File(jarPath.getPath()));
            }
            //startup new version and in new version continue from here and import older settings
            //ask user if we should delete files and shortcut
            //system.properties.get(users.home)/desktop/shortcut  -> only on windows if not exists --> report

        }
        //echo $javahome?
    }

    //this could be put in a utilities webDAO class together with other classes like socket stuff
    private File downloadForWindows(String latestVersionNumber, File targetDownloadFolder) throws IOException, MalformedURLException, XMLStreamException {
        URL repoURL = new URL("http", "genesis.ugent.be", new StringBuilder().append("/maven2/").append(localJarProps.getProperty("groupId")).append("/").append(latestVersionNumber).toString());
        XMLInputFactory xmlParseFactory = XMLInputFactory.newInstance();
        XMLEventReader xmlReader = xmlParseFactory.createXMLEventReader(repoURL.openStream());
        return downloadAndUnzipFile(getUrlOfZippedVersion(xmlReader),targetDownloadFolder);
    }

    private File getLocationToDownloadOnDisk(File targetDownloadFolder) {
        File file = null;
        if (targetDownloadFolder.isDirectory()) {
            file = new File(targetDownloadFolder.toURI());
        } else if (targetDownloadFolder.getParentFile().isDirectory()) {
            file = new File(targetDownloadFolder.getParentFile().toURI());
        } else {
            Object[] options = {"yes...", "specify other location...", "quit"};
            Object choice = JOptionPane.showInputDialog(null, "there has been a problem with finding the location of the original file\n Do you want to download the latest update to your home folder or specify another location?", "Input", JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
            //if (choice == JOptionPane.YES_OPTION) {
            file = new File(System.getProperty("users.home"));
            //} else if (choice == JOptionPane_NO_option){JFilechooser fileChooser = new JFileChooser(System.getProperty("users.home"),JFileChooser.FOLDER);file = fileChooser.getFile()}
        }
        return file;
    }

    private boolean NewVersionReleased(URL jarPath) throws IOException, XMLStreamException {
        boolean returnValue = false;
        CompareVersionNumbers comparator = new CompareVersionNumbers();
        getLocalVariables(jarPath.getPath());
        BufferedReader remoteVersionsReader = new BufferedReader(new InputStreamReader(new URL("http", "genesis.ugent.be", "/maven2/" + localJarProps.getProperty("groupId").replaceAll("\\.", "/") + "/maven-metadata.xml").openStream()));
        latestRemoteRelease = getLatestVersionNumberFromRemoteRepo(remoteVersionsReader);
        if (comparator.compare(localJarProps.getProperty("versionNumber"), latestRemoteRelease) == 1) {
            returnValue = true;
        }
        return returnValue;
    }

    private void getLocalVariables(String jarPath) throws IOException, NullPointerException {
        JarFile jarFile = new JarFile(jarPath);
        Enumeration<JarEntry> entries = jarFile.entries();
        //no cleaner way to do this without asking for the group and artifact id, which defeats the point
        while (entries.hasMoreElements()) {
            JarEntry entry = entries.nextElement();
            if (entry.getName().contains("pom.properties")) {
                InputStream propertiesInput = jarFile.getInputStream(entry);
                localJarProps.load(propertiesInput);
                localJarProps.setProperty("groupId", localJarProps.getProperty("groupId").replaceAll("\\.", "/"));
            }
        }
    }

    private String getLatestVersionNumberFromRemoteRepo(BufferedReader remoteVersionsReader) throws XMLStreamException {
        XMLInputFactory xmlParseFactory = XMLInputFactory.newInstance();
        XMLEventReader xmlReader = xmlParseFactory.createXMLEventReader(remoteVersionsReader);
        MetaDataXMLParser xmlParser = new MetaDataXMLParser(xmlReader);
        return xmlParser.getHighestVersionNumber();
    }

    private URL getUrlOfZippedVersion(XMLEventReader xmlReader) throws MalformedURLException, IOException, XMLStreamException {
        XMLEvent htmlTag;
        String toReturn = null;
        Attribute attribute;
        while (xmlReader.hasNext()) {
            htmlTag = xmlReader.nextEvent();
            if (htmlTag.isStartElement()) {
                Iterator<Attribute> attributes = htmlTag.asStartElement().getAttributes();
                while (attributes.hasNext()) {
                    attribute = attributes.next();
                    if (attribute.getName().getLocalPart().equalsIgnoreCase("href") && attribute.getValue().contains(".zip")) {
                        toReturn = attribute.getValue();
                        break;
                    }
                }
            }
        }
        return new URL(toReturn);
    }

    private File downloadForUnix(String latestRemoteRelease, File targetDownloadFolder) throws MalformedURLException, XMLStreamException, IOException {
        URL repoURL = new URL("http", "genesis.ugent.be", new StringBuilder().append("/maven2/").append(localJarProps.getProperty("groupId")).append("/").append(latestRemoteRelease).toString());
        XMLInputFactory xmlParseFactory = XMLInputFactory.newInstance();
        XMLEventReader xmlReader = xmlParseFactory.createXMLEventReader(repoURL.openStream());
        return downloadAndUnzipFile(getUrlOfZippedVersion(xmlReader),targetDownloadFolder);
    }

    private File downloadAndUnzipFile(URL urlOfZippedVersion, File jarPath) throws IOException {
        File file;
        URLConnection con = urlOfZippedVersion.openConnection();
        file = getLocationToDownloadOnDisk(jarPath);
        if (file != null) {
            BufferedWriter dest = null;
            ZipInputStream in = new ZipInputStream(new BufferedInputStream(con.getInputStream()));
            InputStreamReader isr = new InputStreamReader(in);
            while (in.getNextEntry() != null) {
                int count;
                char data[] = new char[1024];
                dest = new BufferedWriter(new FileWriter(file), 1024);
                while ((count = isr.read(data, 0, 1024)) != -1) {
                    dest.write(data, 0, count);
                }
                dest.flush();
                dest.close();
            }
            isr.close();
            in.close();
        }
        return file;
    }

    private class MetaDataXMLParser {

        private String highestVersionNumber;
        private XMLEvent XMLEvent;

        public MetaDataXMLParser(XMLEventReader xmlReader) throws XMLStreamException {
            while (xmlReader.hasNext()) {
                XMLEvent = xmlReader.nextEvent();
                if (XMLEvent.isStartElement()) {
                    if (XMLEvent.asStartElement().getName().getLocalPart().equalsIgnoreCase("versions")) {
                        parseVersionNumbers(xmlReader);
                        break;
                    }
                }
            }
        }

        private String getHighestVersionNumber() {
            return highestVersionNumber;
        }

        private void parseVersionNumbers(XMLEventReader xmlReader) throws XMLStreamException {
            CompareVersionNumbers versionNumberComparator = new CompareVersionNumbers();
            while (xmlReader.hasNext()) {
                XMLEvent = xmlReader.nextEvent();
                if (XMLEvent.isStartElement()) {
                    if (XMLEvent.asStartElement().getName().getLocalPart().equalsIgnoreCase("version")) {
                        if (highestVersionNumber == null) {
                            highestVersionNumber = xmlReader.nextEvent().asCharacters().getData();
                        } else {
                            String versionNumberToCompareWith = xmlReader.nextEvent().asCharacters().getData();
                            if (versionNumberComparator.compare(highestVersionNumber, versionNumberToCompareWith) == 1) {
                                highestVersionNumber = versionNumberToCompareWith;
                            }
                        }
                    }
                } else if (XMLEvent.isEndElement()) {
                    if (XMLEvent.asEndElement().getName().getLocalPart().equalsIgnoreCase("versions")) {
                        break;
                    }
                }
            }
        }
    }

    private class CompareVersionNumbers implements Comparator<String> {

        @Override
        public int compare(String oldVersionNumber, String newVersionNumber) {
            int compareInt = -1;
            Scanner a = (new Scanner(oldVersionNumber)).useDelimiter("\\.");
            Scanner b = (new Scanner(newVersionNumber)).useDelimiter("\\.");
            int i = 0, j = 0;
            if (!newVersionNumber.contains("b") || !newVersionNumber.contains("beta")) {
                while (a.hasNext() && b.hasNext()) {
                    i = Integer.parseInt(a.next());
                    j = Integer.parseInt(b.next());
                    if (j > i) {
                        compareInt = 1;
                    }
                }
                if (b.hasNext() && !a.hasNext()) {
                    compareInt = 1;
                } else if (!b.hasNext() && !a.hasNext() && i == j) {
                    compareInt = 0;
                }
            }
            return compareInt;
        }
    }
}
