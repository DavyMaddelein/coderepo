package com.compomics.coderepo.autoupdater;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Properties;
import java.util.zip.GZIPInputStream;
import java.util.zip.ZipInputStream;
import javax.swing.JOptionPane;
import javax.xml.stream.XMLStreamException;
import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;

/**
 *
 * @author Davy
 */
public class FileDAO {

    public static void createDesktopShortcut(MavenJarFile file) throws IOException {
        Properties compomicsArtifactProperties = new Properties();
        File compomicsArtifactPropertiesFile = new File(new StringBuilder().append(System.getProperty("Users.home")).append("/.compomics/").append(file.getArtifactId()).append("updatesettings.properties").toString());
        compomicsArtifactProperties.load(new FileReader(compomicsArtifactPropertiesFile));
        if (compomicsArtifactProperties.getProperty("create_shortcut").equalsIgnoreCase(String.valueOf(JOptionPane.YES_OPTION))) {
            //steal code from peptideshaker
        } else if (compomicsArtifactProperties.getProperty("create_shortcut").equalsIgnoreCase(String.valueOf(JOptionPane.CANCEL_OPTION)) || compomicsArtifactProperties.getProperty("create_shortcut").equalsIgnoreCase(String.valueOf(JOptionPane.CLOSED_OPTION))) {
            Object[] options = new Object[]{"yes", "no", "ask me next update"};
            boolean rememberOption = false;
            int selection = JOptionPane.showOptionDialog(null, "do you want to create a desktop shortcut?", "shortcut", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, JOptionPane.CANCEL_OPTION);
            //also check to remember choice
            if (selection == JOptionPane.YES_OPTION) {
            } else if (selection == JOptionPane.CANCEL_OPTION || selection == JOptionPane.CLOSED_OPTION || rememberOption) {
                compomicsArtifactProperties.setProperty("create_shortcut", String.valueOf(selection));
                compomicsArtifactProperties.store(new FileOutputStream(compomicsArtifactPropertiesFile), null);
            }
        }
    }

    public static File getLocationToDownloadOnDisk(String targetDownloadFolder) {
        File file = new File(targetDownloadFolder);
        if (file.exists() && !file.isDirectory()) {
            file = file.getParentFile();
        }
        if (file == null) {
            Object[] options = {"yes...", "specify other location...", "quit"};
            Object choice = JOptionPane.showInputDialog(null, "there has been a problem with finding the location of the original file\n Do you want to download the latest update to your home folder or specify another location?", "Input", JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
            //if (choice == JOptionPane.YES_OPTION) {
            file = new File(System.getProperty("users.home"));
            //} else if (choice == JOptionPane_NO_option){JFilechooser fileChooser = new JFileChooser(System.getProperty("users.home"),JFileChooser.FOLDER);file = fileChooser.getFile()}
        }
        return file;
    }

    public static boolean NewVersionReleased(MavenJarFile jarFile) throws IOException, XMLStreamException {
        boolean newVersion = false;
        BufferedReader remoteVersionsReader = new BufferedReader(new InputStreamReader(new URL("http", "genesis.ugent.be", "/maven2/" + jarFile.getGroupId().replaceAll("\\.", "/") + "/maven-metadata.xml").openStream()));
        String latestRemoteRelease = WebDAO.getLatestVersionNumberFromRemoteRepo(remoteVersionsReader);
        if (new CompareVersionNumbers().compare(jarFile.getVersionNumber(), latestRemoteRelease) == 1) {
            newVersion = true;
        }
        return newVersion;
    }

    //rewrite both downloadAndUnzipFiles to use apache commons compress library?
    public static File downloadAndUnzipFile(ZipInputStream in, File fileLocationOnDiskToDownloadTo) throws IOException {
        BufferedWriter dest;
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

    public static File downloadAndUnzipFile(GZIPInputStream in, File fileLocationOnDiskToDownloadTo) throws IOException {

        InputStreamReader isr = new InputStreamReader(in);
        int count;
        char data[] = new char[1024];
        BufferedWriter dest = new BufferedWriter(new FileWriter(fileLocationOnDiskToDownloadTo), 1024);
        while ((count = isr.read(data, 0, 1024)) != -1) {
            dest.write(data, 0, count);
        }
        dest.flush();
        dest.close();
        isr.close();
        in.close();
        untar(fileLocationOnDiskToDownloadTo);
        return fileLocationOnDiskToDownloadTo;
    }

    private static boolean untar(File fileToUntar) throws FileNotFoundException, IOException {
        boolean fileUntarred = false;
        String untarLocation = fileToUntar.getAbsolutePath();
        TarArchiveInputStream tarStream = new TarArchiveInputStream(new FileInputStream(fileToUntar));
        BufferedReader buf = new BufferedReader(new InputStreamReader(tarStream));
        ArchiveEntry entry;
        while ((entry = tarStream.getNextEntry()) != null) {
            char[] cbuf = new char[1024];
            int count;
            FileWriter out = new FileWriter(new File(untarLocation + "/" + entry.getName()));
            while ((count = buf.read(cbuf, 0, 1024)) != -1) {
                out.write(cbuf, 0, count);
            }
        }
        return fileUntarred;
    }

    static MavenJarFile getMavenJarFileFromFolderWithArtifactId(File folder, String artifactId) throws IOException {
        MavenJarFile mainJarFile = null;
        for (File aJarFile : folder.listFiles(new JarFileFilter())) {
            mainJarFile = new MavenJarFile(aJarFile);
            if (mainJarFile.getArtifactId().equalsIgnoreCase(artifactId)) {
                break;
            }
        }
        if (mainJarFile == null) {
            //just in case
            throw new FileNotFoundException("the jar file could not be found");
        }
        return mainJarFile;
    }
}
