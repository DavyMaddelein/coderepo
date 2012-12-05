/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.compomics.FTP;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.io.FileUtils;
import java.io.IOException;

/**
 *
 * @author Davy
 */
public class FtpMethods {

    FTPClient client = new FTPClient();

    private void deleteFileServerSide() {
        try {
            client.connect("ftp.asite.com");
            client.login("username", "password");
            // Set a string with the file you want to delete	 
            String filename = "/coomons/footer.jsp";
            // Delete file
            boolean exist = client.deleteFile(filename);
            // Notify user for deletion
            if (exist) {
                System.out.println("File '" + filename + "' deleted...");
            } // Notify user that file doesn't exist
            else {
                System.out.println("File '" + filename + "' doesn't exist...");
            }

            client.logout();
        } catch (IOException e) {
            e.printStackTrace();

        } finally {
            try {
                client.disconnect();

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    private void listFiles() {
        FTPClient client = new FTPClient();
        try {
            client.connect("ftp.site.com");
            client.login("username", "password");
            FTPFile[] files = client.listFiles();
            for (FTPFile ftpFile : files) {
                if (ftpFile.getType() == FTPFile.FILE_TYPE) {
                    System.out.println("File: " + ftpFile.getName()
                            + "size-> " + FileUtils.byteCountToDisplaySize(
                            ftpFile.getSize()));
                }
            }
            client.logout();

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                client.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void connectToFtpServer() {
        FTPClient client = new FTPClient();
        try {
            client.connect("ftp.site.com");
            boolean login = client.login("username", "password");
            if (login) {
                System.out.println("Connection established...");
                // Try to logout and return the respective boolean value
                boolean logout = client.logout();
                // If logout is true notify user
                if (logout) {
                    System.out.println("Connection close...");
                }
            } else {
                System.out.println("Connection fail...");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                client.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void downloadFile() {
        FTPClient client = new FTPClient();
        FileOutputStream fos = null;
        try {
            client.connect("ftp.site.com");
            client.login("username", "password");
// Create an OutputStream for the file
            String filename = "test.txt";
            fos = new FileOutputStream(filename);
// Fetch file from server 
            client.retrieveFile("/" + filename, fos);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fos != null) {
                    fos.close();
                }
                client.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void uploadFile() {
            FTPClient client = new FTPClient();
            FileInputStream fis = null;
            try {
                client.connect("ftp.site.com");
                client.login("username", "password");
                // Create an InputStream of the file to be uploaded
                String filename = "test.txt";
                fis = new FileInputStream(filename);
                // Store file on server and logout
                client.storeFile(filename, fis);
                client.logout();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (fis != null) {
                        fis.close();
                    }
                    client.disconnect();
                } catch (IOException e) {
                   e.printStackTrace();
                }

            }
        }
    }