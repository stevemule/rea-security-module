package au.com.reagroup.security;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;

public class SecurityUtil {
	
	public static String AUTHORIZATION_HTTP_HEADER = "Authorization";
	
	public static String REA_AUTHENTICATED_USER_HEADER = "X-Authenticated-userid";
	
	//Log message format [messageId] message
	public static String LOG_MESSAGE_FORMAT = "[%s]: %s";
	public static String LOG_MESSAGE_DEBUG_FORMAT = "[%s]: %s";
	
	public static String JSON_RESPONSE_FORMAT = "{\"message\":\"%s\"}";
	
	public static Client getClientConnection() {

		final Client client = ClientBuilder.newClient();
		
		return client;

	}

}
