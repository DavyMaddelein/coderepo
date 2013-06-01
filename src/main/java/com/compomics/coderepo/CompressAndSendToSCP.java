/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.compomics.coderepo;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import com.jcraft.jsch.*;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorOutputStream;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import javax.mail.MessagingException;
import java.io.*;
import java.util.Arrays;
import java.util.Comparator;

/**
 *
 * @author Davy
 */
public class CompressAndSendToSCP {

    
        public static void main(String[] args) throws MessagingException {

              File backupLocation = new File("/home/to_move/");
              File archiveLocation = new File("/home/server/backup/");
       
            try {
                    File oldestBackupDir = findOldestBackup(backupLocation);
                    createTarGzOfDirectory(oldestBackupDir.getAbsolutePath(),archiveLocation.getAbsolutePath()+"/"+oldestBackupDir.getName()+".tar.gz");
                    File tarredAndZippedFile = new File(archiveLocation.getAbsolutePath()+"/"+oldestBackupDir.getName()+".tar.gz");
                    if (tarredAndZippedFile.exists()){
                        tarredAndZippedFile.deleteOnExit();
                        uploadFileWithSCP(tarredAndZippedFile);
                        //TODO add sync check?
                } else {
                    //mailsender.sendMail("the tar and gzip failed");
                }
            } catch (Exception e) {
               //mailsender.sendMail(e.getMessage());
            }
        }

    /**
     * Creates a tar.gz file at the specified path with the contents of the specified directory.
     *
     * @param directoryPath The path to the directory to create an archive of
     * @param tarGzPath The path to the archive to create
     *
     * @throws IOException If anything goes wrong
     */
    public static void createTarGzOfDirectory(String directoryPath, String tarGzPath) throws IOException {
        FileOutputStream fOut = null;
        BufferedOutputStream bOut = null;
        GzipCompressorOutputStream gzOut = null;
        TarArchiveOutputStream tOut = null;

        try {
            fOut = new FileOutputStream(new File(tarGzPath));
            bOut = new BufferedOutputStream(fOut);
            gzOut = new GzipCompressorOutputStream(bOut);
            tOut = new TarArchiveOutputStream(gzOut);

            addFileToTarGz(tOut, directoryPath, "");
        } finally {
            tOut.finish();

            tOut.close();
            gzOut.close();
            bOut.close();
            fOut.close();
        }
    }

    /**
     * Creates a tar entry for the path specified with a name built from the base passed in and the file/directory
     * name. If the path is a directory, a recursive call is made such that the full directory is added to the tar.
     *
     * @param tOut The tar file's output stream
     * @param path The filesystem path of the file/directory being added
     * @param base The base prefix to for the name of the tar file entry
     *
     * @throws IOException If anything goes wrong
     */
    private static void addFileToTarGz(TarArchiveOutputStream tOut, String path, String base) throws IOException {
        File f = new File(path);
        String entryName = base + f.getName();
        TarArchiveEntry tarEntry = new TarArchiveEntry(f, entryName);
        tOut.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
        tOut.putArchiveEntry(tarEntry);

        if (f.isFile()) {
            IOUtils.copy(new FileInputStream(f), tOut);

            tOut.closeArchiveEntry();
        } else {
            tOut.closeArchiveEntry();

            File[] children = f.listFiles();

            if (children != null) {
                for (File child : children) {
                    addFileToTarGz(tOut, child.getAbsolutePath(), entryName + "/");
                }
            }
        }
    }
    
    private static File findOldestBackup(File Folder) {
        File[] files = Folder.listFiles();
        Arrays.sort(files, new Comparator<File>() {
            @Override
            public int compare(File f1, File f2) {
                return Long.valueOf(f1.lastModified()).compareTo(f2.lastModified());
            }
        });
        return  files[0];
    }
    
    private static void uploadFileWithSCP(File fileToUpload) throws SftpException, JSchException, IOException {

        String user = "user";
        String host = "host";
        int port = 22;
        String knownHostsFilename = "/home/user/.ssh/known_hosts";
        String sourcePath = fileToUpload.getAbsolutePath();
        String destPath = fileToUpload.getName();
        JSch jsch = new JSch();
        jsch.addIdentity(user, FileUtils.readFileToByteArray(new File("/home/user/.ssh/id_rsa")),null,new byte[0]);
        jsch.setKnownHosts(knownHostsFilename);
        Session session = jsch.getSession(user, host, port);
        session.connect();
        ChannelSftp sftpChannel = (ChannelSftp) session.openChannel("sftp");
        sftpChannel.connect();
        sftpChannel.get(sourcePath, destPath);
        sftpChannel.exit();
        session.disconnect();
    }
}

