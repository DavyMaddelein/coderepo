/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.compomics.coderepo.linux;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Davy
 */
public class SymAndHardLinks {

    public void createLink() {

        try {
            Path file1 = Paths.get("test1");
            System.out.println(file1.toRealPath());
            Path hLink = Paths.get("test1.hLink");
            Path sLink = Paths.get("test1.symLink");

            try {
                //only works on supporting OSs.
                Files.createSymbolicLink(sLink, file1);

            } catch (UnsupportedOperationException ex) {
                System.out.println("This OS doesn't support creating Sym links");
            }

            try {
                //this actually creates a hard link
                Files.createLink(hLink, file1);
                System.out.println(hLink.toRealPath());
            } catch (UnsupportedOperationException ex) {
                System.out.println("This OS doesn't support creating hard links");
            }

        } catch (IOException ex) {
            Logger.getLogger(SymAndHardLinks.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
