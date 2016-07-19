package au.com.reagroup.security.test.automation.functional;

import static org.junit.Assert.*;
import au.com.reagroup.security.SecurityConnector;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mule.tools.devkit.ctf.junit.AbstractTestCase;

public class AuthenticateUserTestCases extends AbstractTestCase<SecurityConnector> {

	public AuthenticateUserTestCases() {
		super(SecurityConnector.class);
	}

	@Before
	public void setup() {
		// TODO
	}

	@After
	public void tearDown() {
		// TODO
	}

	@Test
	public void verify() throws Exception {
		java.lang.Object expected = null;
		org.mule.api.callback.SourceCallback callback = null;
		org.mule.api.MuleEvent event = null;
		org.mule.api.MuleMessage message = null;
		java.lang.String auth = null;
		assertEquals(getConnector().authenticateUser(callback, event, message, auth), expected);
	}

}