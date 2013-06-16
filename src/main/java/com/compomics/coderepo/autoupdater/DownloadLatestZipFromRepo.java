package com.compomics.coderepo.autoupdater;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;
import java.util.Properties;
import java.util.zip.ZipInputStream;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.XMLEvent;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream;
import org.apache.log4j.Logger;

/**
 *
 * @author Davy
 */
public class DownloadLatestZipFromRepo {

    private static final Logger logger = Logger.getLogger(DownloadLatestZipFromRepo.class);
    private Properties localJarProps = new Properties();
    private String latestRemoteRelease;

    public static void main(String[] args) throws MalformedURLException, IOException, XMLStreamException {
        new DownloadLatestZipFromRepo(new File("C:\\Users\\Davy\\Desktop\\java\\thermo-msf-parser\\thermo_msf_parser_GUI\\target\\thermo_msf_parser_GUI-2.0.4\\thermo_msf_parser_GUI-2.0.4.jar").toURL(), false, new String[]{""});
    }

    //main [jarPath,flag overwrite oldfiles]
    public DownloadLatestZipFromRepo(URL jarPath, boolean deleteOldFiles, String[] args) throws IOException, XMLStreamException {
        if (FileDAO.NewVersionReleased(jarPath)) {
            URL repoURL = new URL("http", "genesis.ugent.be", new StringBuilder().append("/maven2/").append(localJarProps.getProperty("groupId")).append("/").append(latestRemoteRelease).toString());
            File downloadedFile;
            if (System.getProperty("os.name").contains("windows")) {
                URL archiveURL = getUrlOfZippedVersion(repoURL, ".zip");
                downloadedFile = downloadAndUnzipFile(new ZipInputStream(new BufferedInputStream(archiveURL.openStream())), new File(FileDAO.getLocationToDownloadOnDisk(jarPath.getPath()), archiveURL.getFile()));
                try {
                    Properties compomicsArtifactProperties = new Properties();
                    compomicsArtifactProperties.load(new FileReader(new File(new StringBuilder().append(System.getProperty("Users.home")).append("/.compomics/").append(localJarProps.getProperty("artifcatId")).append("updatesettings.properties").toString())));
                    if (compomicsArtifactProperties.getProperty("create_shortcut").equalsIgnoreCase("yes")) {
                        FileDAO.createDesktopShortcut(downloadedFile);
                    }
                } catch (IOException ioex) {
                    handleSilently(ioex);
                }
            } else {
                URL archiveURL = getUrlOfZippedVersion(repoURL, ".tar.gz");
                downloadedFile = downloadAndUnzipFile(new BufferedInputStream(archiveURL.openStream()), new File(FileDAO.getLocationToDownloadOnDisk(jarPath.getPath()), archiveURL.getFile()));
                //update symlinks?
            }
            //startup new version and in new version continue from here and import older settings

        }
        //echo $javahome?
    }

    private URL getUrlOfZippedVersion(URL repoURL, String suffix) throws MalformedURLException, IOException, XMLStreamException {
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

    private File downloadAndUnzipFile(InputStream in, File fileLocationOnDiskToDownloadTo) throws IOException {
        BufferedWriter dest = null;
        InputStreamReader isr = new InputStreamReader(in);
        while (in.getNextEntry() != null) {
            int count;
            char data[] = new char[1024];
            dest = new BufferedWriter(new FileWriter(fileLocationOnDiskToDownloadTo), 1024);
            while ((count = isr.read(data, 0, 1024)) != -1) {
                dest.write(data, 0, count);
            }
            dest.flush();
            dest.close();
        }
        isr.close();
        in.close();
        return fileLocationOnDiskToDownloadTo;
    }

    private void handleSilently(Exception ex) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
