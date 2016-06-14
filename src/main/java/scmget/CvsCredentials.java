package scmget;

public class CvsCredentials extends Credentials {

	@Override
	public HostType getHostType() {
		return HostType.CVS;
	}
	
}
