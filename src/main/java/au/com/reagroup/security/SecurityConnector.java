package au.com.reagroup.security;

import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mule.api.MuleContext;
import org.mule.api.MuleEvent;
import org.mule.api.MuleMessage;
import org.mule.api.annotations.Category;
import org.mule.api.annotations.Config;
import org.mule.api.annotations.Connector;
import org.mule.api.annotations.Processor;
import org.mule.api.annotations.display.FriendlyName;
import org.mule.api.annotations.display.Placement;
import org.mule.api.annotations.param.Default;
import org.mule.api.callback.SourceCallback;
import org.mule.api.transport.PropertyScope;

import au.com.reagroup.security.config.ADSecurityConfig;



@Connector(name="rea-module-security", friendlyName="REA Group Security Module")
@Category(name = "org.mule.tooling.category.security", description = "Security")
public class SecurityConnector extends AbstractBaseModule {
	
	private static Logger LOGGER = LogManager.getLogger(SecurityConnector.class);
	
	private static String HOSTNAME;
	
    static {
        if (System.getenv("COMPUTERNAME") != null) {
            HOSTNAME = System.getenv("COMPUTERNAME");
        } else if (System.getenv("HOSTNAME") != null) {
            HOSTNAME = System.getenv("HOSTNAME");
        } else {
            try {
                HOSTNAME = Inet4Address.getLocalHost().getHostName();
            } catch (UnknownHostException e) {
                HOSTNAME = "[unknown]";
            }
        }
    }
	
    @Config
    ADSecurityConfig config;

    public ADSecurityConfig getConfig() {
		return config;
	}

	public void setConfig(ADSecurityConfig config) {
		this.config = config;
	}

	/**
     * The Mule context is injected to access metadata such as the flow name.
     */
	@Inject
    private MuleContext muleContext;

    public void setMuleContext(MuleContext muleContext) {
        this.muleContext = muleContext;
    }

	/**
     * Validate User
     *
     * @param 
     * @return The MuleEvent
     */
    @Processor(intercepting = true)
    @Category(name = "Validate User", description = "Validate username/password")
    public Object authenticateUser(
    		SourceCallback callback, 
            MuleEvent event,
            MuleMessage message,

            @FriendlyName("Basic Authentication (Base64 Format)")
            @Placement(order = 1, group = "Settings", tab = "General")
            @Default("#[message.inboundProperties['Authorization']]")
            String auth) throws Exception {
    	
    	LOGGER.debug(String.format(SecurityUtil.LOG_MESSAGE_DEBUG_FORMAT, message.getUniqueId(), "Entering authenticateUser"));
    	
    	Map<String, Object> mapa = new HashMap<String, Object>();

    	String flowName = event.getFlowConstruct().getName();
    	
    	LOGGER.info(HOSTNAME);
    	LOGGER.info("Hit Authenticate User for Flow Name: "+flowName);
    	LOGGER.info("Uri="+getConfig().getServiceEndpoint());
    	
    	try {
    	
    		String userId = authenticateUser(auth, getConfig().getServiceEndpoint());
    		mapa.put(SecurityUtil.REA_AUTHENTICATED_USER_HEADER, userId);
    		event.getMessage().addProperties(mapa, PropertyScope.OUTBOUND);
    		callback.process(event.getMessage().getPayload());
    		return event.getMessage().getPayload();
    		
    	} catch (UnauthorizedException e) {
    		mapa.put("WWW-Authenticate", "Basic realm=\"Unauthorized\"");
			mapa.put("http.status", 401);
			event.getMessage().addProperties(mapa, PropertyScope.OUTBOUND);
			event.getMessage().setPayload(String.format(SecurityUtil.JSON_RESPONSE_FORMAT, e.getMessage()));
			return event.getMessage().getPayload();
    	
    	} catch (Exception e) {
			mapa.put("http.status", 500);
			event.getMessage().addProperties(mapa, PropertyScope.OUTBOUND);
			event.getMessage().setPayload(String.format(SecurityUtil.JSON_RESPONSE_FORMAT, e.getMessage()));
			return event.getMessage().getPayload();
    	} finally {
    		LOGGER.debug(String.format(SecurityUtil.LOG_MESSAGE_DEBUG_FORMAT, message.getUniqueId(), "Leaving authenticateUser"));
    	}
    	 	

    }
    
	/**
     * Validate User with Group
     *
     * @param 
     * @return The MuleEvent
     */
    @Processor(intercepting = true)
    @Category(name = "Validate User with Group", description = "Validate username/password and also whether the user is a member of the specified group")
    public Object authenticateUserWithGroup(
    		SourceCallback callback, 
            MuleEvent event,
            MuleMessage message,
            
            @FriendlyName("Basic Authentication (Base64 Format)")
            @Placement(order = 1, group = "Settings", tab = "General")
            @Default("#[message.inboundProperties['Authorization']]")
            String auth,

            @FriendlyName("Group Name")
            @Placement(order = 1, group = "Settings", tab = "General")
            String group) {

    	LOGGER.debug(String.format(SecurityUtil.LOG_MESSAGE_DEBUG_FORMAT, message.getUniqueId(), "Entering authenticateUser"));
    	
    	Map<String, Object> mapa = new HashMap<String, Object>();

    	String flowName = event.getFlowConstruct().getName();
    	
    	LOGGER.info(HOSTNAME);
    	LOGGER.info("Hit Authenticate User for Flow Name: "+flowName);
    	LOGGER.info("Uri="+getConfig().getServiceEndpoint());
    	
    	try {
    	
    		String userId = authenticateUserWithGroup(auth, group, getConfig().getServiceEndpoint());
    		mapa.put(SecurityUtil.REA_AUTHENTICATED_USER_HEADER, userId);
    		event.getMessage().addProperties(mapa, PropertyScope.OUTBOUND);
    		callback.process(event.getMessage().getPayload());
    		return event.getMessage().getPayload();
    		
    	} catch (UnauthorizedException e) {
    		mapa.put("WWW-Authenticate", "Basic realm=\"Unauthorized\"");
			mapa.put("http.status", 401);
			event.getMessage().addProperties(mapa, PropertyScope.OUTBOUND);
			event.getMessage().setPayload(String.format(SecurityUtil.JSON_RESPONSE_FORMAT, e.getMessage()));
			return event.getMessage().getPayload();
    	} catch (ForbiddenException e) {
			mapa.put("http.status", 403);
			event.getMessage().addProperties(mapa, PropertyScope.OUTBOUND);
			event.getMessage().setPayload(String.format(SecurityUtil.JSON_RESPONSE_FORMAT, e.getMessage()));
			return event.getMessage().getPayload();
    	} catch (Exception e) {
			mapa.put("http.status", 500);
			event.getMessage().addProperties(mapa, PropertyScope.OUTBOUND);
			event.getMessage().setPayload(String.format(SecurityUtil.JSON_RESPONSE_FORMAT, e.getMessage()));
			return event.getMessage().getPayload();
    	} finally {
    		LOGGER.debug(String.format(SecurityUtil.LOG_MESSAGE_DEBUG_FORMAT, message.getUniqueId(), "Leaving authenticateUser"));
    	}
    }
    
    


}