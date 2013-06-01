package com.compomics.coderepo;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.MalformedInputException;
import java.util.Properties;
import java.util.zip.ZipInputStream;
import javax.swing.JOptionPane;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;
import org.apache.log4j.Logger;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 *
 * @author Davy
 */
public class DownloadLatestZipFromRepo {

    private static final Logger logger = Logger.getLogger(DownloadLatestZipFromRepo.class);
    private String toolName;
    private String[] localVersionNumbers;

    //main [jarPath,flag overwrite oldfiles]
    public DownloadLatestZipFromRepo(URL jarPath) {
        URL repoURL;
        getLocalData(jarPath);
        if ((repoURL = NewVersionReleased(toolName)) != null) {
            if (compareVersionNumbers(localVersionNumbers,getVersionNumbers(repoURL))){
                download(repoURL,new File(jarPath.getPath()));
            }
        }
        //startup new version and in new version continue from here and import older settings
        //ask user if we should delete files and shortcut
        //system.properties.get(users.home)/desktop/shortcut  -> only on windows if not exists --> report
        //echo $javahome?
    }

    //this could be put in a utilities webDAO class together with other classes like socket stuff
    public void download(URL repoUrl, File targetDownloadFolder) {
        File file;
        try {
            URLConnection con = repoUrl.openConnection();
            if (targetDownloadFolder.isDirectory()) {
                file = new File(targetDownloadFolder.toURI() + File.separator + repoUrl.getFile());
            } else if (targetDownloadFolder.getParentFile().isDirectory()) {
                file = new File(targetDownloadFolder.getParentFile().toURI() + File.separator + repoUrl.getFile());
            } else {
                file = alternatFileName(repoUrl);
            }

            if (file != null) {
                BufferedWriter dest = null;
                ZipInputStream in = new ZipInputStream(new BufferedInputStream(con.getInputStream()));
                InputStreamReader isr = new InputStreamReader(in);
                while (in.getNextEntry() != null) {
                    int count;
                    char data[] = new char[1024];
                    dest = new BufferedWriter(new FileWriter(file), 1024);
                    while ((count = isr.read(data, 0, 1024)) != -1) {
                        dest.write(data, 0, count);
                    }
                    dest.flush();
                    dest.close();
                }
                isr.close();
                in.close();
            }

        } catch (MalformedInputException mie) {
            logger.error(mie);
            JOptionPane.showMessageDialog(null, "something went wrong with retrieving the url to the latest update. please contact");

        } catch (IOException ioe) {
            logger.error(ioe);
            JOptionPane.showMessageDialog(null, "there has been an error while fetching the latest update\n Please contact \n" + ioe.getMessage());
        }
    }

    private File alternatFileName(URL repoUrl) {
        File file = null;
        Object[] options = {"yes...", "specify other location...", "quit"};
        Object choice = JOptionPane.showInputDialog(null, "there has been a problem with finding the location of the original file\n Do you want to download the latest update to your home folder or specify another location?", "Input", JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
        if (choice == JOptionPane.YES_OPTION) {
            file = new File(System.getProperty("users.home") + File.separator + repoUrl.getFile());
        }
        return file;
    }

    /**
     * splits version numbers separated by points and assumes they are preceded
     * by a "-"
     *
     * @param localJarURL
     * @return
     */
    private String[] getVersionNumbers(URL jarURL) {
        return getVersionNumbers(jarURL, "-");
    }

    /**
     * splits version numbers separated by points and preceded by splitChar
     *
     * @param JarURL
     * @param nameSeparator
     * @return
     */
    private String[] getVersionNumbers(URL jarURL, String nameSeparator) {
        return getVersionNumbers(jarURL, nameSeparator, ".");

    }

    private String[] getVersionNumbers(URL jarURL, String nameSeperator, String versionSeparator) {
        return jarURL.getFile().substring(jarURL.getFile().lastIndexOf(nameSeperator)).split(versionSeparator);

    }

    private void getLocalData(URL jarPath) {
        try {
            String path = jarPath.getPath() + "!/META-INF/maven/groupId/artifactId/pom.properties";
            InputStream stream = getClass().getResourceAsStream(path);
            if (stream != null) {
                Properties props = new Properties();
                props.load(stream);
                setVersionNumbers(props.getProperty("").split("."));
                setToolName(props.getProperty(""));
            } else {
                setVersionNumbers(getVersionNumbers(jarPath));
                setToolName(getToolName(jarPath));
            }
        } catch (IOException ex) {
            logger.error(ex);
            JOptionPane.showMessageDialog(null, "there has been a problem retrieving the version name and number");
        }
    }

    private void setToolName(String toolName) {
        this.toolName = toolName;
    }

    private void setVersionNumbers(String[] versionNumbers) {
        this.localVersionNumbers = versionNumbers;
    }

    private URL NewVersionReleased(String projectName) {
        URL newVersion = null;
        try {
            URL releaseListURL = new URL("https://code.google.com/feeds/p/" + projectName + "/downloads/basic");
            GoogleCodeSAXHandler handler = new GoogleCodeSAXHandler();
            SAXParserFactory.newInstance().newSAXParser().parse(releaseListURL.openStream(), handler);
            newVersion = handler.getDownloadURL();
        } catch (MalformedURLException ex) {
            //go to local repository for update
            logger.error(ex);
        } catch (ParserConfigurationException | SAXException ex) {
            logger.error(ex);
        } catch (IOException ex) {
            logger.error(ex);
        }
        return newVersion;
    }

    private String getToolName(URL jarPath) {
        return (jarPath.getFile().split("-"))[0];
    }

    private boolean compareVersionNumbers(String[] localVersionNumbers, String[] versionNumbers) {
        //WILL PROBALBY BREAK
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public class GoogleCodeSAXHandler extends DefaultHandler {

        private String XMLvalue;
        private URL publicDownloadURL;

        @Override
        public void startElement(String s, String s1, String elementName, Attributes attributes) throws SAXException {
            if (elementName.equalsIgnoreCase("link")) {
                if (attributes.getValue("rel").equalsIgnoreCase("direct")) {

                    try {
                        getVersionNumbers(new URL(attributes.getValue("href")));
                    } catch (MalformedURLException ex) {
                        logger.error(ex);
                    }
                }
            }
        }
        
        public void endElement(){
        //get filesize
        }

        @Override
        public void characters(char[] ac, int i, int j) throws SAXException {
            XMLvalue = new String(ac, i, j);
        }

        public URL getDownloadURL() {
            return publicDownloadURL;
        }
    }
    
    //TODO: rudimentary gui
}
