package com.compomics.coderepo.autoupdater;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.zip.GZIPInputStream;
import java.util.zip.ZipInputStream;
import javax.xml.stream.XMLStreamException;
import org.apache.commons.io.FileUtils;

/**
 *
 * @author Davy
 */
public class DownloadLatestZipFromRepo {

    /**
     * downloads the latest deploy from the genesis maven repository of the
     * artifact of the jarPath,starts it without arguments and removes the old
     * jar if there was an update
     *
     * @param jarPath the path to the jarfile
     * @throws IOException should there be problems with reading or writing
     * files during the updating
     * @throws XMLStreamException if there was a problem reading the meta data
     * from the remote maven repository
     * @throws URISyntaxException
     */
    public static void downloadLatestZipFromRepo(URL jarPath) throws IOException, XMLStreamException, URISyntaxException {
        downloadLatestZipFromRepo(jarPath, true, true);
    }

    /**
     * downloads the latest deploy from the genesis maven repository of the
     * artifact and starts it without arguments
     *
     * @param jarPath the path to the jarfile
     * @param deleteOldFiles if the jar who starts the update should be deleted
     * @param startDownloadedVersion if the newly downloaded jar should be
     * started after download
     * @throws IOException should there be problems with reading or writing
     * files during the updating
     * @throws XMLStreamException if there was a problem reading the meta data
     * from the remote maven repository
     * @throws URISyntaxException
     */
    public static void downloadLatestZipFromRepo(URL jarPath, boolean deleteOldFiles, boolean startDownloadedVersion) throws IOException, XMLStreamException, URISyntaxException {
        downloadLatestZipFromRepo(jarPath, deleteOldFiles, new String[0], startDownloadedVersion);
    }

    /**
     * downloads the latest zip archive of the jar in the url from the genesis
     * maven repo
     *
     * @param jarPath the path to the jarfile to update
     * @param deleteOldFiles if the original jar file should be deleted
     * @param args the args for the newly downloaded jar when it starts
     * @throws IOException should there be problems with reading or writing
     * files during the updating
     * @throws XMLStreamException if there was a problem reading the meta data
     * from the remote maven repository
     * @throws URISyntaxException
     */
    public static void downloadLatestZipFromRepo(URL jarPath, boolean deleteOldFiles, String[] args, boolean startDownloadedVersion) throws IOException, XMLStreamException, URISyntaxException {
        MavenJarFile mavenJarFile = new MavenJarFile(jarPath.toURI());
        downloadLatestZipFromRepo(jarPath, deleteOldFiles, null, args, new URL("http", "genesis.ugent.be", new StringBuilder().append("/maven2/").append(mavenJarFile.getGroupId()).append("/").toString()), startDownloadedVersion);
        //echo $javahome?
    }

    /**
     * downloads the latest zip archive of the jar in the url from a given
     * jarRepository
     *
     * @param jarPath the path to the jarfile to update, cannot be {@code null}
     * @param deleteOldFiles if the original jar folder should be deleted,
     * cannot be {@code null}
     * @param args the args for the newly downloaded jar when it starts
     * @param jarRepository the repository to look for the latest deploy of the
     * jar file, cannot be {@code null}
     * @throws IOException should there be problems with reading or writing
     * files during the updating
     * @throws XMLStreamException if there was a problem reading the meta data
     * from the remote maven repository
     * @throws URISyntaxException
     */
    public static void downloadLatestZipFromRepo(URL jarPath, boolean deleteOldFiles, String[] args, URL jarRepository) throws IOException, XMLStreamException, URISyntaxException {
        downloadLatestZipFromRepo(jarPath, deleteOldFiles, null, args, jarRepository, true);
    }

    /**
     * retrieves the latest version of a maven jar file from a maven repository
     *
     * @param jarPath the URL of the location of the jar that needs to be
     * updated on the file system. cannot be {@code null}
     * @param deleteOldFiles should the old installation be removed or not
     * cannot be {@code null}
     * @param args the args that will be passed to the newly downloaded program
     * when started, cannot be {@code null}
     * @param jarRepository the maven repository to go look in, cannot be
     * {@code null}
     * @param startDownloadedVersion if the newly downloaded version should be
     * started automatically or not
     * @throws IOException should there be problems with reading or writing
     * files during the updating
     * @throws XMLStreamException if there was a problem reading the meta data
     * from the remote maven repository
     * @throws URISyntaxException
     */
    public static void downloadLatestZipFromRepo(final URL jarPath, boolean deleteOldFiles, String iconName, String[] args, URL jarRepository, boolean startDownloadedVersion) throws IOException, XMLStreamException, URISyntaxException {
        MavenJarFile mavenJarFile = new MavenJarFile(jarPath.toURI());
        if (FileDAO.NewVersionReleased(mavenJarFile)) {
            File downloadedFile = null;
            if (System.getProperty("os.name").toLowerCase().contains("windows")) {
                URL archiveURL = WebDAO.getUrlOfZippedVersion(jarRepository, ".zip", false);
                downloadedFile = FileDAO.downloadAndUnzipFile(new ZipInputStream(new BufferedInputStream(archiveURL.openStream())), new File(FileDAO.getLocationToDownloadOnDisk(jarPath.getPath()), archiveURL.getFile()));
                try {
                    MavenJarFile newJar = FileDAO.getMavenJarFileFromFolderWithArtifactId(downloadedFile, mavenJarFile.getArtifactId());
                    FileDAO.createDesktopShortcut(newJar, iconName, deleteOldFiles);
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
            try {
                launchJar(downloadedFile, args);
                if (deleteOldFiles) {
                    Runtime.getRuntime().addShutdownHook(new Thread() {
                        @Override
                        public void run() {
                            try {
                                File jarParent = new File(jarPath.toURI()).getParentFile();
                                if (jarParent.exists()) {
                                    FileUtils.deleteDirectory(jarParent);
                                }
                            } catch (URISyntaxException ex) {
                                //todo handle file location does not exist
                            } catch (IOException ex) {
                                //todo handle old files could not be downloaded
                            }
                        }
                    });

                }
            } catch (IOException ioe) {
                throw new IOException("could not start the downloaded jar, old files have not been deleted");
            }
        }
    }

    private static void handleSilently(Exception ex) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * simple jar launch through a {@code processBuilder}
     * 
     * @param downloadedFile the downloaded jar file to start
     * @param args the args to give to the jar file
     * @return true if the launch succeeded
     * @throws IOException if the process could not start
     */
    private static boolean launchJar(File downloadedFile, String[] args) throws NullPointerException, IOException {

        List<String> processToRun = new ArrayList<String>();
        try {
            processToRun.add("java -jar");
            processToRun.add(downloadedFile.getAbsolutePath());
            processToRun.addAll(Arrays.asList(args));
            ProcessBuilder p = new ProcessBuilder(processToRun);
            p.start();
        } catch (NullPointerException npe) {
            throw new IOException("could not start the jar");
        }
        return true;
    }
}
