package au.com.reagroup.security.config;

import javax.ws.rs.client.Client;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mule.api.ConnectionException;
import org.mule.api.annotations.Configurable;
import org.mule.api.annotations.Connect;
import org.mule.api.annotations.ConnectionIdentifier;
import org.mule.api.annotations.Disconnect;
import org.mule.api.annotations.TestConnectivity;
import org.mule.api.annotations.ValidateConnection;
import org.mule.api.annotations.components.ConnectionManagement;
import org.mule.api.annotations.display.FriendlyName;
import org.mule.api.annotations.display.Placement;
import org.mule.api.annotations.param.ConnectionKey;
import org.mule.api.annotations.param.Default;

import au.com.reagroup.security.SecurityUtil;

@ConnectionManagement(configElementName="rea-security-config", friendlyName = "Configuration")
public class ADSecurityConfig {
	
	private static Logger LOGGER = LogManager.getLogger(ADSecurityConfig.class);
	
    /**
     * REA Group Security App URI
     */
    @Configurable
    @Placement(group = "Connection")
    @Default("https://rea-auth.rea-group.com:8092/api")
    private String serviceEndpoint;

	public String getServiceEndpoint() {
		return serviceEndpoint;
	}

	public void setServiceEndpoint(String reaSecurityServiceUri) {
		this.serviceEndpoint = reaSecurityServiceUri;
	}
	
	@Connect
	@TestConnectivity
	public void connect(@ConnectionKey @Default("https://rea-auth.rea-group.com:8092/api") @FriendlyName("AD API Connection Url") String testUrl) 
			throws ConnectionException {
		setServiceEndpoint(testUrl);
		
		LOGGER.debug("Testing REA Active Directory API Connection: "+serviceEndpoint);
		Client client = SecurityUtil.getClientConnection();
		Response response = client.target(serviceEndpoint).
				path("user").path("authenticate").request(MediaType.APPLICATION_JSON).get();
		LOGGER.info("Got: "+response.getStatus());
		
	}
	
	@Disconnect
	public void disconnect() {}
	
    @ConnectionIdentifier
	public String identifier() {
		
		return "";
	}
	
	@ValidateConnection
	public boolean connection() {
		
		return true;
	}

}
