package driver.factory;

import driver.WebDriverExtended;
import driver.selection.DriverSelection.Matcher;

public interface DriverFactory {

	/**
	 * If a given configuration does not match, then it will not create a driver object. If there is a match, then a
	 * driver object is created for testing.
	 * 
	 * @param driverMatcher
	 * @param features
	 * @return null if configuration does not match, a new driver object if there is a match
	 */
	WebDriverExtended getDriver(Matcher driverMatcher, Feature... features);

}
