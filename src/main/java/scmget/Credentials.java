package scmget;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@XmlTransient
public abstract class Credentials {
	
	@XmlAttribute
	private String host;
	
	@XmlAttribute
	private String username;
	
	@XmlAttribute
	private String password;
	
	public Credentials() {
	}
	
	public abstract HostType getHostType();
	
	public String getHost() {
		return host;
	}
	
	public String getPassword() {
		return password;
	}
	
	public String getUsername() {
		return username;
	}
	
}
