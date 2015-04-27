package test;

import org.junit.Assume;

import driver.WebDriverExtended;
import driver.factory.Feature;
import driver.factory.WebDriverFactory;
import driver.proxy.VenusProxy;
import driver.selection.DriverSelection.Matcher;

/**
 * Base test class all venus based tests should extend from.
 */
public abstract class BaseTest {

	private final WebDriverFactory driverFactory;

	public BaseTest() {
		this.driverFactory = new WebDriverFactory();
	}

	/**
	 * Create and return new {@link WebDriverExtended} with the given features.
	 * 
	 * @param features
	 *            Features needed for testing.
	 * @return {@link WebDriverExtended} with requested features enabled.
	 */
	protected WebDriverExtended getDriver(Feature... features) {
		return getDriver(Matcher.any(), features);
	}

	/**
	 * Create and return new {@link WebDriverExtended} with the given features and matching given driver {@link Matcher}
	 * . If driver cannot be found that satisfies {@link Matcher} then null is return.
	 * 
	 * @param driverMatcher
	 *            {@link Matcher} that needs to be satisfied.
	 * @param features
	 *            Features needed for testing.
	 * @return {@link WebDriverExtended} with requested features enabled and that matches given driver {@link Matcher}.
	 */
	protected WebDriverExtended getDriver(Matcher driverMatcher, Feature... features) {
		WebDriverExtended driver = driverFactory.getDriver(driverMatcher, features);
		Assume.assumeTrue("Skipping no driver matches for selection", driver != null);
		return driver;
	}

	/**
	 * Convenience method that will run supplied test "standard" closure against matching driver.
	 * 
	 * @param driverMatcher
	 *            {@link Matcher} that needs to be satisfied.
	 * @param standardTest
	 *            Standard test code to run with matching driver.
	 */
	protected void test(Matcher driverMatcher, StandardTest standardTest) {
		WebDriverExtended driver = getDriver(driverMatcher);
		try {
			standardTest.test(driver);
		} finally {
			driver.quit();
		}
	}

	/**
	 * Convenience method that will run supplied test "proxied" closure against matching driver.
	 * 
	 * @param driverMatcher
	 *            {@link Matcher} that needs to be satisfied.
	 * @param standardTest
	 *            Test code requiring proxy to run with matching driver.
	 */
	protected void test(Matcher driverMatcher, ProxiedTest capturingTest) {
		WebDriverExtended driver = getDriver(driverMatcher, Feature.PROXY);
		try {
			capturingTest.test(driver, driver.proxy());
		} finally {
			driver.quit();
		}
	}

	protected static interface StandardTest {
		void test(WebDriverExtended driver);
	}

	protected static interface ProxiedTest {
		void test(WebDriverExtended driver, VenusProxy proxy);
	}

}
