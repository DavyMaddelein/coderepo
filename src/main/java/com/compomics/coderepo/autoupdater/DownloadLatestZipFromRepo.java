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

    private String latestRemoteRelease;

    public static void main(String[] args) throws MalformedURLException, IOException, XMLStreamException, URISyntaxException {
        new DownloadLatestZipFromRepo(new File("C:\\Users\\Davy\\Desktop\\java\\thermo-msf-parser\\thermo_msf_parser_GUI\\target\\thermo_msf_parser_GUI-2.0.4\\thermo_msf_parser_GUI-2.0.4.jar").toURL(), false, new String[]{""});
    }

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
    public DownloadLatestZipFromRepo(URL jarPath, boolean deleteOldFiles, String[] args) throws IOException, XMLStreamException, URISyntaxException {
        MavenJarFile mavenJarFile = new MavenJarFile(jarPath.toURI());
        if (FileDAO.NewVersionReleased(mavenJarFile)) {
            URL repoURL = new URL("http", "genesis.ugent.be", new StringBuilder().append("/maven2/").append(mavenJarFile.getGroupId()).append("/").append(latestRemoteRelease).toString());
            File downloadedFile;
            if (System.getProperty("os.name").contains("windows")) {
                URL archiveURL = WebDAO.getUrlOfZippedVersion(repoURL, ".zip");
                downloadedFile = FileDAO.downloadAndUnzipFile(new ZipInputStream(new BufferedInputStream(archiveURL.openStream())), new File(FileDAO.getLocationToDownloadOnDisk(jarPath.getPath()), archiveURL.getFile()));
                try {
                    MavenJarFile newJar = FileDAO.getMavenJarFileFromFolderWithArtifactId(downloadedFile,mavenJarFile.getArtifactId());
                    FileDAO.createDesktopShortcut(newJar);
                } catch (IOException ioex) {
                    handleSilently(ioex);
                }
            } else {
                URL archiveURL = WebDAO.getUrlOfZippedVersion(repoURL, ".tar.gz");
                downloadedFile = FileDAO.downloadAndUnzipFile(new GZIPInputStream(archiveURL.openStream()), new File(FileDAO.getLocationToDownloadOnDisk(jarPath.getPath()), archiveURL.getFile()));
                //update symlinks?
            }
            
        }
        //echo $javahome?
    }

    private void handleSilently(Exception ex) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
