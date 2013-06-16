package com.compomics.coderepo.autoupdater;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import javax.swing.JOptionPane;
import javax.xml.stream.XMLStreamException;

/**
 *
 * @author Davy
 */
public class FileDAO {

    public static void createDesktopShortcut(File file) throws IOException {


        throw new UnsupportedOperationException("not yet implemented");
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

    public static boolean NewVersionReleased(URL jarPath) throws IOException, XMLStreamException {
        boolean returnValue = false;
        MavenJarFile mavenJarFile = new MavenJarFile(jarPath);
        BufferedReader remoteVersionsReader = new BufferedReader(new InputStreamReader(new URL("http", "genesis.ugent.be", "/maven2/" + mavenJarFile.getGroupId().replaceAll("\\.", "/") + "/maven-metadata.xml").openStream()));
        String latestRemoteRelease = WebDAO.getLatestVersionNumberFromRemoteRepo(remoteVersionsReader);
        if (new CompareVersionNumbers().compare(mavenJarFile.getVersionNumber(), latestRemoteRelease) == 1) {
            returnValue = true;
        }
        return returnValue;
    }
}
