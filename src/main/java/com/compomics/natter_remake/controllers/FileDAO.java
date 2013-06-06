package com.compomics.natter_remake.controllers;

import com.compomics.natter_remake.model.RovFile;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.zip.GZIPInputStream;

/**
 *
 * @author Davy
 */
public class FileDAO {

    static void writeByteArrayToDisk(byte[] fileContent, String filename, File fileOutputLocation, boolean deleteOnExit) throws NullPointerException, IOException {
        StringBuilder outputString = new StringBuilder();
        if (!fileOutputLocation.exists()) {
            if (!fileOutputLocation.mkdir()) {
                throw new IOException("could not create output folder");
            }
        }

        if (fileOutputLocation.isDirectory()) {
            outputString.append(fileOutputLocation.getAbsolutePath()).append("\\");
        } else if (fileOutputLocation.getParent() == null) {
            //alteratively use homefolder
            throw new FileNotFoundException("file location is not a directory and there is no parent directory");
        } else {
            outputString.append(fileOutputLocation.getParent()).append("\\");
        }
        if (filename == null) {
            Date date = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("MM_dd_yyyy_h_mm_ss");
            outputString.append(sdf.format(date));
        } else {
            outputString.append(filename);
        }
        File outputFile = new File(outputString.toString());
        OutputStream out = new FileOutputStream(outputFile);
        try {
            out.write(fileContent);
        } finally {
            if (deleteOnExit) {
                outputFile.deleteOnExit();
            }
            out.close();
        }
    }

    static void unzipAndWriteByteArrayToDisk(byte[] zippedFileContent, File fileOutputLocation) throws IOException {
        writeByteArrayToDisk(unzipByteArray(zippedFileContent), null, fileOutputLocation, true);
    }

    static void unzipAndWriteByteArrayToDisk(byte[] zippedFileContent, String filename, File fileOutputLocation) throws IOException {
        writeByteArrayToDisk(unzipByteArray(zippedFileContent), filename, fileOutputLocation, true);
    }

    static void unzipAndWriteByteArrayToDisk(byte[] zippedFileContent, String filename, File fileOutputLocation, boolean deleteOnExit) throws IOException {
        writeByteArrayToDisk(unzipByteArray(zippedFileContent), filename, fileOutputLocation, deleteOnExit);
    }

    static byte[] unzipByteArray(byte[] fileContent) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        GZIPInputStream zis = new GZIPInputStream(new ByteArrayInputStream(fileContent));
        byte[] buffer = new byte[1024];

        while (zis.read(buffer) != -1) {
            out.write(buffer);
        }
        out.flush();
        out.close();
        return out.toByteArray();
    }

    public static void writeExtractedDataToDisk(RovFile data) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public static void writeExtractedDataToDisk(List<RovFile> rovFiles) {
        for (RovFile rovFile : rovFiles){
            writeExtractedDataToDisk(rovFile);
        }
    }
    
    public static void emptyNatterTempFolder(){
        
    }
}
