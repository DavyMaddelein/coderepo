package com.compomics.natter_remake.UnitTests;

import com.compomics.natter_remake.controllers.DbConnectionController;
import com.compomics.natter_remake.controllers.DbDAO;
import com.compomics.natter_remake.model.Project;
import com.compomics.natter_remake.model.RovFile;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.util.List;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import org.junit.*;

/**
 *
 * @author Davy
 */
public class dbDAOTest {

    private static Project project;
    private static Project project2;
    private static Project project3;
    private static Project project4;
    private File tempdir;

    @BeforeClass
    public static void setUpClass() {
        project = new Project(1549, "test");
        project2 = new Project(1061, "test2");
        project3 = new Project(2220, "test3");
        project4 = new Project(996, "empty_quantitation_test");
    }

    @Before
    public void setUp() throws NullPointerException, URISyntaxException, MalformedURLException, MalformedURLException, IOException, SQLException {

        DbConnectionController.createConnection("****", "****", "****", "****");
        tempdir = new File(System.getProperty("java.io.tmpdir") + "/rovfiletest");
        if (tempdir.exists()){
            tempdir.delete();
        }
        tempdir.mkdir();
    }

    @After
    public void tearDown() throws SQLException {
        DbConnectionController.getConnection().close();
        tempdir.delete();
    }

    @Test
    public void testGetRovFilesInMem() throws SQLException {
        System.out.println("GetRovFilesInMem");
        List<RovFile> testRovFiles = DbDAO.downloadRovFilesInMemoryForProject(project);
        assertThat(testRovFiles.size(), is(40));
    }

    @Test
    public void testGetRovFilesLocally() throws SQLException, NullPointerException, FileNotFoundException, IOException {
        System.out.println("GetRovFilesLocally");
        List<RovFile> testRovFiles = DbDAO.downloadRovFilesLocallyForProject(project2, tempdir);
        assertThat(testRovFiles.size(), is(36));
        assertThat(tempdir.listFiles().length, is(36));
    }

    @Test
    public void testGetRovFilesLocallyInTempDir() throws SQLException, NullPointerException, FileNotFoundException, IOException {
        System.out.println("GetRovFilesLocallyInTempDir");
        List<RovFile> testRovFiles = DbDAO.downloadRovFilesLocallyForProject(project3);
        assertThat(testRovFiles.size(), is(20));
        File tempNatterFolder = new File(System.getProperty("java.io.tmpdir") + "/natter_rov_files");
        assertThat(tempNatterFolder.listFiles().length, is(20));
        tempNatterFolder.delete();
    }

    @Test
    public void testGetRovFilesInMemForEmptyQuantitation() throws SQLException {
        System.out.println("GetEmptyQuantInMem");
        List<RovFile> testRovFiles = DbDAO.downloadRovFilesInMemoryForProject(project4);
        assertThat(testRovFiles.size(), is(0));
    }

    @Test
    public void testGetRovFilesLocallyForEmptyQuantitation() throws SQLException, NullPointerException, FileNotFoundException, IOException {
        System.out.println("GetEmptyQuantLocally");
        List<RovFile> testRovFiles = DbDAO.downloadRovFilesLocallyForProject(project4, tempdir);
        assertThat(testRovFiles.size(), is(0));
        assertThat(tempdir.listFiles().length, is(0));
    }
}
