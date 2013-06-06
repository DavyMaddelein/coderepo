/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.compomics.natter_remake.UnitTests;

import com.compomics.natter_remake.controllers.DataExtractor;
import com.compomics.natter_remake.controllers.DbConnectionController;
import com.compomics.natter_remake.model.Project;
import com.compomics.natter_remake.model.RovFile;
import com.compomics.natter_remake.model.RovFileData;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

import static org.hamcrest.CoreMatchers.*;

/**
 *
 * @author Davy
 */
public class DataExtractorTest {

    static Project project;
    static Project project2;
    static Project project3;

    public DataExtractorTest() {
    }

    @BeforeClass
    public static void setUpClass() {
        project = new Project(1549, "test");
        project2 = new Project(2220, "test2");
        project3 = new Project(1200, "test3");
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() throws NullPointerException, URISyntaxException, MalformedURLException, MalformedURLException, IOException, SQLException {

        DbConnectionController.createConnection("Davy", "aerodynamic", "muppet03.ugent.be", "projects");
    }

    @After
    public void tearDown() throws SQLException {
        DbConnectionController.getConnection().close();
    }

    /**
     * Test of extractDataInMem method, of class DataExtractor.
     */
    @Test
    public void testExtractDataInMem() throws Exception {
        System.out.println("extractDataInMem");
        List<RovFile> rovFiles = DataExtractor.extractDataInMem(project);
        assertThat(rovFiles.size(),is(not(0)));
        assertThat(rovFiles.size(),is(20));
        RovFile rovFile = rovFiles.get(7);
        RovFileData rovFileData = rovFile.getParsedData();
        assertThat(rovFileData.getDistillerVersion(), is("2.3.0"));
        assertThat(rovFileData.getFileName(), is(""));
        assertThat(rovFileData.getProteaseUsed(), is("Trypsin"));
        assertThat(rovFileData.getQuantitationMethod(), is(""));
        assertThat(rovFileData.getQuantitationMethod(),is(""));
    }

    /**
     * Test of extractDataLowMem method, of class DataExtractor.
     */
    @Test
    public void testExtractDataLowMem() throws Exception {
        System.out.println("extractDataLowMem");
        DataExtractor instance = null;
        instance.extractDataLowMem(project2);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of extractDataToLocal method, of class DataExtractor.
     */
    @Test
    public void testExtractDataToLocal_0args() throws Exception {
        System.out.println("extractDataToLocal");
        DataExtractor instance = null;
        instance.extractDataToLocal(project);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of extractDataToLocal method, of class DataExtractor.
     */
    @Test
    public void testExtractDataToLocal_File() throws Exception {
        System.out.println("extractDataToLocal");
        File rovFileOutputLocationFolder = null;
        DataExtractor instance = null;
        instance.extractDataToLocal(rovFileOutputLocationFolder, project);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
}