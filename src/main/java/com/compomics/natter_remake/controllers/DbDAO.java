package com.compomics.natter_remake.controllers;

import com.compomics.natter_remake.controllers.DbConnectionController;
import com.compomics.natter_remake.model.Project;
import com.compomics.natter_remake.model.RovFile;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;

/**
 *
 * @author Davy
 */
public class DbDAO {

    public static List<RovFile> downloadRovFilesInMemoryForProject(Project project) throws SQLException {
        List<RovFile> files = new ArrayList<RovFile>();
        PreparedStatement stat = DbConnectionController.getConnection().prepareStatement("select qf.filename, qf.file from (select distinct q.l_quantitation_fileid as temp from identification as i, spectrum as f , identification_to_quantitation as t, quantitation_group as q where i.l_spectrumid = f.spectrumid and f.l_projectid = " + project.getProjectId() + " and i.identificationid = t.l_identificationid and t.l_quantitation_groupid = q.quantitation_groupid) as linker, quantitation_file as qf where linker.temp = qf.quantitation_fileid");
        ResultSet rs = stat.executeQuery();
        while (rs.next()) {
            files.add(new RovFile(rs.getString("filename"), rs.getBytes("file")));
        }
        rs.close();
        stat.close();
        return files;
    }

    public static List<RovFile> downloadRovFilesLocallyForProject(Project project) throws SQLException, NullPointerException, FileNotFoundException, IOException {
        File natterTempDir = new File(System.getProperty("java.io.tmpdir")+"/natter_rov_files");
        natterTempDir.mkdir();
        return downloadRovFilesLocallyForProject(project, natterTempDir);
    }

    public static List<RovFile> downloadRovFilesLocallyForProject(Project project, File rovFileOutputLocationFolder) throws SQLException, NullPointerException, FileNotFoundException, IOException {
        return downloadRovFilesLocallyForProject(project, rovFileOutputLocationFolder, true);
    }

    public static List<RovFile> downloadRovFilesLocallyForProject(Project project, File rovFileOutputLocationFolder, boolean deleteOnExit) throws SQLException, NullPointerException, FileNotFoundException, IOException {
        List<RovFile> files = new ArrayList<RovFile>();
        PreparedStatement stat = DbConnectionController.getConnection().prepareStatement("select qf.filename, qf.file from (select distinct q.l_quantitation_fileid as temp from identification as i, spectrum as f , identification_to_quantitation as t, quantitation_group as q where i.l_spectrumid = f.spectrumid and f.l_projectid = " + project.getProjectId() + " and i.identificationid = t.l_identificationid and t.l_quantitation_groupid = q.quantitation_groupid) as linker, quantitation_file as qf where linker.temp = qf.quantitation_fileid");
        ResultSet rs = stat.executeQuery();
        RovFile rovFile;
        while (rs.next()) {
            rovFile = new RovFile(rs.getString("filename"), rs.getBytes("file"));
            files.add(rovFile);
            FileDAO.unzipAndWriteByteArrayToDisk(rovFile.getFileContent(), rovFile.getName(), rovFileOutputLocationFolder, deleteOnExit);
        }
        rs.close();
        stat.close();
        return files;
    }

    public static RovFile getQuantitationFileForQuantitationFileId(Integer quantitation_fileid) throws SQLException {
        PreparedStatement stat = DbConnectionController.getConnection().prepareStatement("select filename,file from quantitation_file where quantitatation_fileid = " + quantitation_fileid);
        ResultSet rs = stat.executeQuery();
        rs.next();
        RovFile rovFile = new RovFile(rs.getString("filename"), rs.getBytes("file"));
        rs.close();
        stat.close();
        return rovFile;
    }

    public static List<Integer> getQuantitationFileIdsForProject(Project project) throws SQLException {
        List<Integer> quantitationFileIds = new ArrayList<Integer>();
        PreparedStatement stat = DbConnectionController.getConnection().prepareStatement("select distinct qg.l_quantitation_fileid as fileid from quantitation_group as qg, identification_to_quantitation as itq (select identification.identificationid as result from identification,spectrum where l_spectrumid = spectrumid and l_projectid = " + project.getProjectId() + ") as ident_result where ident_result.result = itq.l_identificationid and qg.quantitation_groupid = idt.l_quantitation_groupid");
        ResultSet rs = stat.executeQuery();
        while (rs.next()) {
            quantitationFileIds.add(rs.getInt("fileid"));
        }
        rs.close();
        stat.close();
        return quantitationFileIds;
    }
}
