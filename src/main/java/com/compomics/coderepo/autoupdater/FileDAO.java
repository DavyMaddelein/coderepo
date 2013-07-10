package com.compomics.coderepo.autoupdater;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Locale;
import java.util.zip.GZIPInputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import net.jimmc.jshortcut.JShellLink;
import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;

/**
 *
 * @author Davy
 */
public abstract class FileDAO {

    /**
     * creates a new Desktop Shortcut to the maven jar file
     *
     * @param file the maven jarfile to make a shortcut to
     * @param iconName the name of the icon file in the resources folder
     * @param deleteOldShortcut if previous shortcuts containing the maven jar
     * file artifact id should be removed
     * @throws IOException
     */
     public abstract boolean createDesktopShortcut(MavenJarFile file, String iconName, boolean deleteOldShortcut) throws IOException;
    
    
    
    public boolean addShortcutAtDeskTop(MavenJarFile mavenJarFile) {
        return addShortcutAtDeskTop(mavenJarFile, null);
    }

    public boolean addShortcutAtDeskTop(MavenJarFile mavenJarFile, String iconName) {

        JShellLink link = new JShellLink();
        link.setFolder(JShellLink.getDirectory("desktop"));
        link.setName(new StringBuilder().append(mavenJarFile.getArtifactId()).append("-").append(mavenJarFile.getVersionNumber()).toString());
        if (iconName != null) {
            link.setIconLocation(new StringBuilder().append(mavenJarFile.getAbsoluteFilePath()).append("/resources/").append(iconName).toString());
        }
        link.setPath(mavenJarFile.getAbsoluteFilePath());
        link.save();
        return true;
    }

    /**
     * 
     * @param targetDownloadFolder
     * @return
     * @throws IOException 
     */
public abstract File getLocationToDownloadOnDisk(String targetDownloadFolder) throws IOException;

    //rewrite both downloadAndUnzipFiles to use apache commons compress library?
    public File UnzipFile(ZipInputStream in, File fileLocationOnDiskToDownloadTo) throws IOException {
        BufferedWriter dest;
        InputStreamReader isr = new InputStreamReader(in);
        ZipEntry entry;
        while ((entry = in.getNextEntry()) != null) {
            int count;
            char data[] = new char[1024];
            File destFile = new File(fileLocationOnDiskToDownloadTo + "/" + entry.getName());
            destFile.getParentFile().mkdirs();
            if (!entry.isDirectory()) {
                dest = new BufferedWriter(new FileWriter(destFile), 1024);
                while ((count = isr.read(data, 0, 1024)) != -1) {
                    dest.write(data, 0, count);
                }
                dest.flush();
                dest.close();
            } else {
                destFile.mkdirs();
            }
            in.closeEntry();
        }
        isr.close();
        in.close();
        return fileLocationOnDiskToDownloadTo;
    }

    public File UnGzipAndUntarFile(GZIPInputStream in, File fileLocationOnDiskToDownloadTo) throws IOException {

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

    private boolean untar(File fileToUntar) throws FileNotFoundException, IOException {
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

    public MavenJarFile getMavenJarFileFromFolderWithArtifactId(File folder, String artifactId) throws IOException {
        MavenJarFile mainJarFile = null;
        for (File aJarFile : folder.listFiles()) {
            if (aJarFile.isDirectory()) {
                mainJarFile = getMavenJarFileFromFolderWithArtifactId(aJarFile, artifactId);
                if (mainJarFile != null) {
                    break;
                }
            } else {
                if (aJarFile.getName().toLowerCase(new Locale("en")).contains(artifactId) && aJarFile.getName().toLowerCase(new Locale("en")).contains(".jar")) {
                    mainJarFile = new MavenJarFile(aJarFile);
                    break;
                }
            }
        }
        if (mainJarFile == null) {
            //just in case
            throw new FileNotFoundException("the jar file could not be found");
        }
        return mainJarFile;
    }
}
