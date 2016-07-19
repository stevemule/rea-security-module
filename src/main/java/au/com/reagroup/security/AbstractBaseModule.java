package au.com.reagroup.security;

import java.net.ConnectException;

import javax.ws.rs.client.Client;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public abstract class AbstractBaseModule {
	
	private static Logger LOGGER = LogManager.getLogger(AbstractBaseModule.class);
	
	/**
	 * Function to Validate a User
	 * 
	 * @param target
	 * @return
	 */
	public String authenticateUser(String auth, String target) throws UnauthorizedException, ConnectException, Exception {
		
		Client client = null;
		Response response = null;
		
		try {
		
			client = SecurityUtil.getClientConnection();
	
			response = client.target(target).
					path("authenticate").request(MediaType.APPLICATION_JSON).
					header(SecurityUtil.AUTHORIZATION_HTTP_HEADER, auth).get();
			
		} catch (javax.ws.rs.ProcessingException e) {
			
			if (e.getCause() instanceof ConnectException) {
				throw new ConnectException(String.format("Unable to connect to REA Authentication API - %s",e.getMessage()));
			} else {
				throw e;
			}
		} 
	    
		/*
		 * Check HTTP Status Codes
		 */
	    if(response.getStatus() == 200)
	    {
	    	LOGGER.info("got 200");
	    	return response.getHeaderString(SecurityUtil.REA_AUTHENTICATED_USER_HEADER); 
	    } 
	    else if (response.getStatus() == 401)
	    {
	    	LOGGER.info("got 401");
	    	throw new UnauthorizedException("Not authorized to access resource");
	    } else {
	    	LOGGER.info("got "+response.getStatus());
	    	throw new RuntimeException(String.format("REA Authentication API returned HTTP Status Code: [%s]",response.getStatus()));
	    }
	    
	}
	
	/**
	 * Function to Validate a User against an LDAP group
	 * 
	 * @param auth
	 * @param group
	 * @param target
	 * @return
	 * @throws UnauthorizedException
	 * @throws ConnectException
	 * @throws Exception
	 */
	public String authenticateUserWithGroup(String auth, String group, String target) throws UnauthorizedException, ForbiddenException, ConnectException, Exception {
		Client client = null;
		Response response = null;
		
		try {
		
			client = SecurityUtil.getClientConnection();
	
			response = client.target(target).
					path("authenticate").path(group).request(MediaType.APPLICATION_JSON).
					header(SecurityUtil.AUTHORIZATION_HTTP_HEADER, auth).get();
			
		} catch (javax.ws.rs.ProcessingException e) {
			
			if (e.getCause() instanceof ConnectException) {
				throw new ConnectException(String.format("Unable to connect to REA Authentication API - %s",e.getMessage()));
			} else {
				throw e;
			}
		} 
	    
		/*
		 * Check HTTP Status Codes
		 */
	    if(response.getStatus() == 200)
	    {
	    	LOGGER.info("got 200");
	    	return response.getHeaderString(SecurityUtil.REA_AUTHENTICATED_USER_HEADER); 
	    } 
	    else if (response.getStatus() == 401)
	    {
	    	LOGGER.info("got 401");
	    	throw new UnauthorizedException("Not authorized to access resource");
	    }
	    else if (response.getStatus() == 403) {
	    	LOGGER.info("got 403");
	    	throw new ForbiddenException("User is not a member of the group");
	    } else {
	    	LOGGER.info("got "+response.getStatus());
	    	throw new RuntimeException(String.format("REA Authentication API returned HTTP Status Code: [%s]",response.getStatus()));
	    }
	}

}
