/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.compomics.codepero.heatmap;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

/**
 *
 * @author Davy
 */
public class Main {

    private ArrayList<HashMap<Object, Integer>> datasets = new ArrayList<>();

    public static void main(String[] args) {


        JFileChooser fileChooser = new JFileChooser();

        if (fileChooser.getSelectedFiles().length > 1) {
            //get all the files in the folder
        } else if (fileChooser.getSelectedFile().isDirectory()) {
            //import all the files in the directory
        } else {
            //all situations in one file
            File singleVariableFile = fileChooser.getSelectedFile();
            int singleFile = JOptionPane.showConfirmDialog(null, "are all situations in one file?");
            
            if (singleFile == JOptionPane.YES_OPTION) {
                
            } else {
            }
        }
    }
}