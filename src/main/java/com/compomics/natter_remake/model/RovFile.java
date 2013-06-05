package com.compomics.natter_remake.model;

import java.io.File;

/**
 *
 * @author Davy
 */
public class RovFile extends File {
    
    private byte[] fileContent;
    private RovFileData data;
    
    public RovFile(String fileName, byte[] fileContent){
        super(fileName);
        this.fileContent = fileContent;
    }
    
    public RovFile (String fileLocationOnDisk){
        super(fileLocationOnDisk);
    }
    
    
    public byte[] getFileContent(){
     return fileContent;
    }

    public void addParsedData(RovFileData parsedRovFile) {
        this.data = parsedRovFile;
    }
    
    public RovFileData getParsedData(){
        return data;
    }
}
