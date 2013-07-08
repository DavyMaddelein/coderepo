package com.compomics.coderepo.autoupdater;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.zip.GZIPInputStream;
import java.util.zip.ZipInputStream;
import javax.xml.stream.XMLStreamException;

/**
 *
 * @author Davy
 */
public class DownloadLatestZipFromRepo {

    /**
     *
     * @param jarPath the URL of the location of the jar that needs to be
     * updated on the file system
     * @param deleteOldFiles should the old installation be removed or not
     * @param args the args that were passed to the original program that was
     * started
     * @throws IOException should there be problems with reading or writing
     * files during the updating
     * @throws XMLStreamException if there was a problem reading the meta data
     * from the remote maven repository
     */
    public static void downloadLatestZipFromRepo(URL jarPath, boolean deleteOldFiles, String[] args) throws IOException, XMLStreamException, URISyntaxException {
        MavenJarFile mavenJarFile = new MavenJarFile(jarPath.toURI());
        downloadLatestZipFromRepo(jarPath, deleteOldFiles, args, new URL("http", "genesis.ugent.be", new StringBuilder().append("/maven2/").append(mavenJarFile.getGroupId()).append("/").toString()));
        //echo $javahome?
    }

    public static void downloadLatestZipFromRepo(URL jarPath, boolean deleteOldFiles, String[] args, URL jarRepository) throws IOException, XMLStreamException, URISyntaxException {
        MavenJarFile mavenJarFile = new MavenJarFile(jarPath.toURI());
        if (FileDAO.NewVersionReleased(mavenJarFile)) {
            File downloadedFile = null;
            if (System.getProperty("os.name").toLowerCase().contains("windows")) {
                URL archiveURL = WebDAO.getUrlOfZippedVersion(jarRepository, ".zip", false);
                downloadedFile = FileDAO.downloadAndUnzipFile(new ZipInputStream(new BufferedInputStream(archiveURL.openStream())), new File(FileDAO.getLocationToDownloadOnDisk(jarPath.getPath()), archiveURL.getFile()));
                try {
                    MavenJarFile newJar = FileDAO.getMavenJarFileFromFolderWithArtifactId(downloadedFile, mavenJarFile.getArtifactId());
                    FileDAO.createDesktopShortcut(newJar,deleteOldFiles);
                } catch (IOException ioex) {
                    handleSilently(ioex);
                }
            } else {
                URL archiveURL = WebDAO.getUrlOfZippedVersion(jarRepository, ".tar.gz", true);
                if (archiveURL != null) {
                    downloadedFile = FileDAO.downloadAndUnzipFile(new GZIPInputStream(archiveURL.openStream()), new File(FileDAO.getLocationToDownloadOnDisk(jarPath.getPath()), archiveURL.getFile()));
                }
                //update symlinks?
            }
            StringBuilder builder = new StringBuilder();
            builder.append("java -jar ");
            try {
                builder.append(downloadedFile.getAbsolutePath());
                ProcessBuilder p = new ProcessBuilder();
                p.start();
            } catch (NullPointerException npe) {
                throw new IOException("could not start the jar");
            }
            if (deleteOldFiles) {
                
            }
        }
    }

    private static void handleSilently(Exception ex) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
