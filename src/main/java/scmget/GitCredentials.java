package scmget;

public class GitCredentials extends Credentials {
	
	@Override
	public HostType getHostType() {
		return HostType.GIT;
	}

}
