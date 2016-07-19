package au.com.reagroup.security.test.automation.runner;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;
import au.com.reagroup.security.test.automation.functional.AuthenticateUserTestCases;
import au.com.reagroup.security.test.automation.functional.AuthenticateUserWithGroupTestCases;
import au.com.reagroup.security.SecurityConnector;
import org.mule.tools.devkit.ctf.mockup.ConnectorTestContext;

@RunWith(Suite.class)
@SuiteClasses({ AuthenticateUserTestCases.class, AuthenticateUserWithGroupTestCases.class })

public class FunctionalTestSuite {

	@BeforeClass
	public static void initialiseSuite() {
		ConnectorTestContext.initialize(SecurityConnector.class);
	}

	@AfterClass
	public static void shutdownSuite() {
		ConnectorTestContext.shutDown();
	}

}