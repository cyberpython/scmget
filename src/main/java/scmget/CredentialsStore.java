package scmget;

import java.util.HashMap;
import java.util.Map;

public class CredentialsStore {

	private Map<String, Credentials> credentialsMap;
	
	
	public CredentialsStore() {
		this.credentialsMap = new HashMap<String, Credentials>();
	}
	
	public void put(CredentialsConfig cs){
		for(Credentials c : cs.getCredentials()){
			credentialsMap.put(c.getHost(), c);
		}
	}
	
	public void put(Credentials c){
		credentialsMap.put(c.getHost(), c);
	}
	
	public Credentials get(String host){
		return credentialsMap.get(host);
	}
	
	public void clear(){
		credentialsMap.clear();
	}
	
}
