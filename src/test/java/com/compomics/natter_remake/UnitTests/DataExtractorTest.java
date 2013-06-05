/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.compomics.natter_remake.UnitTests;

import com.compomics.natter_remake.controllers.DataExtractor;
import com.compomics.natter_remake.controllers.DbConnectionController;
import com.compomics.natter_remake.model.Project;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.sql.SQLException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Davy
 */
public class DataExtractorTest {
   
    Project project;
    
    public DataExtractorTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() throws NullPointerException, URISyntaxException, MalformedURLException, MalformedURLException, IOException, SQLException {

        DbConnectionController.createConnection("****", "****", "****", "****");
        project = new Project(1549, "test");
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
        DataExtractor instance = null;
        instance.extractDataInMem(project);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of extractDataLowMem method, of class DataExtractor.
     */
    @Test
    public void testExtractDataLowMem() throws Exception {
        System.out.println("extractDataLowMem");
        DataExtractor instance = null;
        instance.extractDataLowMem(project);
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
        instance.extractDataToLocal(rovFileOutputLocationFolder,project);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
}