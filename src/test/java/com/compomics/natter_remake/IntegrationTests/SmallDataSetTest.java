/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.compomics.natter_remake.IntegrationTests;

import com.compomics.natter_remake.controllers.DbConnectionController;
import com.compomics.natter_remake.model.Project;
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
public class SmallDataSetTest {

    Project project;

    public SmallDataSetTest() {
        project = new Project(2220, "test");

    }

    @BeforeClass
    public static void setUpClass() throws SQLException {
        DbConnectionController.createConnection("Davy", "aerodynamic", "muppet03.ugent.be", "projects");
        
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }
    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    // @Test
    // public void hello() {}
}