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
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.xml.stream.XMLStreamException;
import net.jimmc.jshortcut.JShellLink;
import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;

/**
 *
 * @author Davy
 */
public class FileDAO {

    /**
     * creates a new Desktop Shortcut to the maven jar file
     *
     * @param file the maven jarfile to make a shortcut to
     * @param iconName the name of the icon file in the resources folder
     * @param deleteOldShortcut if previous shortcuts containing the maven jar
     * file artifact id should be removed
     * @throws IOException
     */
    public static boolean createDesktopShortcut(MavenJarFile file, String iconName, boolean deleteOldShortcut) throws IOException {
        Properties compomicsArtifactProperties = new Properties();
        File compomicsArtifactPropertiesFile = new File(new StringBuilder().append(System.getProperty("Users.home")).append("/.compomics/").append(file.getArtifactId()).append("updatesettings.properties").toString());
        compomicsArtifactProperties.load(new FileReader(compomicsArtifactPropertiesFile));
        int selection;
        if (!compomicsArtifactProperties.contains("create_shortcut")) {
            Object[] options = new Object[]{"yes", "no", "ask me next update"};
            boolean rememberOption = false;
            selection = JOptionPane.showOptionDialog(null, "do you want to create a desktop shortcut?", "shortcut", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, JOptionPane.CANCEL_OPTION);
            //also check (as in add checkbox) to remember choice
            if (selection == JOptionPane.CANCEL_OPTION || selection == JOptionPane.CLOSED_OPTION || rememberOption) {
                compomicsArtifactProperties.setProperty("create_shortcut", String.valueOf(selection));
                compomicsArtifactProperties.store(new FileOutputStream(compomicsArtifactPropertiesFile), null);
            }
        }
        try {
            selection = Integer.parseInt(compomicsArtifactProperties.getProperty("create_shortcut"));
            if (selection == JOptionPane.YES_OPTION) {
                addShortcutAtDeskTop(file, iconName);
            }
            if (deleteOldShortcut) {
                for (String fileName : new File(System.getProperty("User.home")).list()) {
                }

            }
        } catch (NullPointerException npe) {
            throw new IOException("could not create the shortcut");
        } catch (NumberFormatException nfe) {
            throw new IOException("could not create the shortcut");
        }
        return true;
    }

    public static void addShortcutAtDeskTop(MavenJarFile mavenJarFile) {
        addShortcutAtDeskTop(mavenJarFile, null);
    }

    public static void addShortcutAtDeskTop(MavenJarFile mavenJarFile, String iconName) {

        JShellLink link = new JShellLink();
        link.setFolder(JShellLink.getDirectory("desktop"));
        link.setName(new StringBuilder().append(mavenJarFile.getArtifactId()).append("-").append(mavenJarFile.getVersionNumber()).toString());
        if (iconName != null) {
            link.setIconLocation(mavenJarFile.getAbsoluteFilePath() + "/resources/" + iconName);
        }
        link.setPath(mavenJarFile.getAbsoluteFilePath());
        link.save();
    }

    public static File getLocationToDownloadOnDisk(String targetDownloadFolder) throws IOException {
        File file = new File(targetDownloadFolder);
        if (file.exists() && !file.isDirectory()) {
            file = file.getParentFile();
        }
        if (file == null) {
            Object[] options = {"yes...", "specify other location...", "quit"};
            int choice = JOptionPane.showOptionDialog(null, "there has been a problem with finding the location of the original file\n Do you want to download the latest update to your home folder or specify another location?", "specify download location", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, JOptionPane.CANCEL_OPTION);
            if (choice == JOptionPane.YES_OPTION) {
                file = new File(System.getProperty("users.home"));
            } else if (choice == JOptionPane.NO_OPTION) {
                JFileChooser fileChooser = new JFileChooser(System.getProperty("users.home"));
                fileChooser.setMultiSelectionEnabled(false);
                fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                fileChooser.setVisible(true);
                file = fileChooser.getSelectedFile();
            } else if (choice == JOptionPane.CANCEL_OPTION || choice == JOptionPane.CLOSED_OPTION) {
                throw new IOException("no download location");
            }
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
        BufferedReader bufferedTarReader = new BufferedReader(new InputStreamReader(tarStream));
        ArchiveEntry entry;
        while ((entry = tarStream.getNextEntry()) != null) {
            char[] cbuf = new char[1024];
            int count;
            FileWriter out = new FileWriter(new File(untarLocation + "/" + entry.getName()));
            while ((count = bufferedTarReader.read(cbuf, 0, 1024)) != -1) {
                out.write(cbuf, 0, count);
            }
            out.flush();
            out.close();
        }
        bufferedTarReader.close();
        tarStream.close();
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
