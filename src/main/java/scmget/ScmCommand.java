/*
 */

package scmget;

import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Georgios Migdos <cyberpython@gmail.com>
 */
@XmlTransient
public interface ScmCommand {
    public boolean apply();
    public String getErrorMessage();
}
