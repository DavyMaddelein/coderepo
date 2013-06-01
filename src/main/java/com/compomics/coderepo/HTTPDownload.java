/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.compomics.coderepo;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.MalformedInputException;

/**
 *
 * @author Davy
 */
public class HTTPDownload {

    public void download() {
        URL url = null;
        URLConnection con = null;
        int i;
        try {
            url = new URL("https://localhost:8080/AppName/FileName.txt");
            con = url.openConnection();
            File file = new File("/home/user/test.txt");
            //this can use the compressed streams in the same named package, did not do this to keep boilerplate broadly accessible
            BufferedInputStream bis = new BufferedInputStream(
                    con.getInputStream());
            BufferedOutputStream bos = new BufferedOutputStream(
                    new FileOutputStream(file.getName()));
            while ((i = bis.read()) != -1) {
                bos.write(i);
            }
            bos.flush();
            bis.close();
        } catch (MalformedInputException malformedInputException) {
            malformedInputException.printStackTrace();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }
}