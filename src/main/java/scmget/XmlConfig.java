/*
 */

package scmget;

import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Georgios Migdos <cyberpython@gmail.com>
 */
@XmlRootElement(name="configuration")
@XmlAccessorType(XmlAccessType.FIELD)
public class XmlConfig {
    
    @XmlElements({
        @XmlElement(name = "git", type = GitCommand.class),
        @XmlElement(name = "cvs", type = CvsCommand.class)
    })
    private List<ScmCommand> commands;

    public List<ScmCommand> getCommands() {
        return commands;
    }
    
    
}
