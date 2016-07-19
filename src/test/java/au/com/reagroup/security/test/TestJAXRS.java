package au.com.reagroup.security.test;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;

public class TestJAXRS {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		//HttpAuthenticationFeature feature = HttpAuthenticationFeature.basic("username", "password");
		 
		final Client client = ClientBuilder.newClient();
		//client.register(feature);

		Response response = client.target("http://localhost:8081/api").
				path("user").path("authenticate").request(MediaType.APPLICATION_JSON).header("Authorization", "blah").get();
		
		System.out.println(response.getStatus());
	    System.out.println(response.getStatusInfo());
	    
	     
	    if(response.getStatus() == 200)
	    {
	    	System.out.println(response.getHeaderString("X-Authenticated-userid"));
	        
	    }

	    //UserResource.GetUserAuthenticateResponse.accepted().
    	
		

	}

}
