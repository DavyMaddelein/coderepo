package com.compomics.natter_remake;

import com.compomics.software.CompomicsWrapper;
import java.io.File;
import java.net.URISyntaxException;

/**
 *
 * @author Davy
 */
public class Startup extends CompomicsWrapper {

    private Startup(String[] args) throws URISyntaxException {
        File jarFile = new File(Startup.class.getProtectionDomain().getCodeSource().getLocation().toURI());
        // get the splash 
        String mainClass = "com.compomics.natter_remake.StartFrame";
        launchTool("natter",jarFile,null,mainClass,args);
    }

    public static void main(String[] args) throws URISyntaxException {

        new Startup(args);
    }
}
