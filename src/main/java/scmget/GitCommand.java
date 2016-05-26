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
@Parameters(separators = "=", commandDescription = "Checkout tagged a tagged project from a Git repository")
public class GitCommand implements ScmCommand {

    @XmlAttribute(name="url", required = true)
    @Parameter(names = "--url", required = true, description = "The Git repository URL")
    private String url;

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
    @Parameter(names = "--targetDir", required = true, description = "The target absolute or relative filesystem path (relative paths should start with './' or '.\\')")
    private String targetDir;

    public String getUrl() {
        return url;
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
        return Scmget.cloneAndCheckoutGitTag(this);
    }

    @Override
    public String getErrorMessage() {
        return String.format("Failed to clone %s and checkout %s", getUrl(), getTag());
    }

}
