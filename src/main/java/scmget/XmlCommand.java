/*
 */
package scmget;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;

/**
 *
 * @author Georgios Migdos <cyberpython@gmail.com>
 */
@Parameters(separators = "=", commandDescription = "Perform multiple checkouts specified in an XML file")
public class XmlCommand {

    @Parameter(names = "--file", required = true, description = "The XML file to use as input")
    private String filePath;

    public String getFilePath() {
        return filePath;
    }
    
    
}
