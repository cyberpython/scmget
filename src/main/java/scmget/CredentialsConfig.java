package scmget;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="credentials")
@XmlAccessorType(XmlAccessType.FIELD)
public class CredentialsConfig {
	
	@XmlElements({
        @XmlElement(name = "git", type = GitCredentials.class),
        @XmlElement(name = "cvs", type = CvsCredentials.class)
    })
	private List<Credentials> credentials;
	
	public List<Credentials> getCredentials() {
		return credentials;
	}

}
