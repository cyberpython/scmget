/*
 * Copyright 2016 Georgios Migdos
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package scmget;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Georgios Migdos <cyberpython@gmail.com>
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@Parameters(separators = "=", commandDescription = "Checkout a tagged module from a CVS repository")
public class CvsCommand implements ScmCommand {

    @XmlAttribute(name="host", required = true)
    @Parameter(names = "--host", required = true, description = "The hostname or IP address of the CVS server")
    private String host;

    @XmlAttribute(name="cvsroot", required = true)
    @Parameter(names = "--cvsroot", required = true, description = "The root of the CVS repository")
    private String cvsroot;

    @XmlAttribute(name="module", required = true)
    @Parameter(names = "--module", required = true, description = "The CVS module to checkout")
    private String module;

    @XmlAttribute(name="tag", required = true)
    @Parameter(names = "--tag", required = true, description = "The tag to checkout")
    private String tag;

    @XmlAttribute(name="username", required = true)
    @Parameter(names = "--user", required = true, description = "The username to use to connect to the Git server")
    private String username;

    @XmlAttribute(name="password", required = true)
    @Parameter(names = "--pass", required = true, description = "The password to use to connect to the Git server")
    private String password;

    @XmlAttribute(name="target-dir", required = true)
    @Parameter(names = "--targetDir", required = true, description = "The relative path where code will be checked out to (should start with './' or '.\\').")
    private String targetDir;

    public String getHost() {
        return host;
    }

    public String getCvsroot() {
        return cvsroot;
    }

    public String getModule() {
        return module;
    }

    public String getTag() {
        return tag;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getTargetDir() {
        return targetDir;
    }
    
    public boolean apply(){
        return Scmget.checkoutCVSTag(this);
    }

    @Override
    public String getErrorMessage() {
        return String.format("Failed to checkout %s:/%s : %s (%s)", getHost(), getCvsroot(), getModule(), getTag());
    }

}
