package driver.factory;

import java.util.List;

import com.google.common.collect.ImmutableList;

import driver.WebDriverExtended;
import driver.factory.impl.AppiumDriverFactory;
import driver.factory.impl.ChromeDriverFactory;
import driver.factory.impl.FirefoxDriverFactory;
import driver.factory.impl.RemoteDriverFactory;
import driver.selection.DriverSelection.Matcher;

/**
 * {@link DriverFactory} implementation that will delegate to multiple {@link DriverFactory}s to provide best match.
 */
public class WebDriverFactory extends AbstractDriverFactory implements DriverFactory {

	private final List<DriverFactory> driverFactories;

	public WebDriverFactory() {
		ImmutableList.Builder<DriverFactory> builder = ImmutableList.builder();

		builder.add(new RemoteDriverFactory(getDriverSelection()));
		builder.add(new AppiumDriverFactory(getDriverSelection()));
		builder.add(new FirefoxDriverFactory(getDriverSelection()));
		builder.add(new ChromeDriverFactory(getDriverSelection()));

		this.driverFactories = builder.build();
	}

	@Override
	public WebDriverExtended getDriver(Matcher driverMatcher, Feature... features) {

		for (DriverFactory driverFactory : driverFactories) {
			WebDriverExtended driver = driverFactory.getDriver(driverMatcher, features);
			if (driver != null) return driver;
		}

		return null;
	}

	@Override
	protected WebDriverExtended createDriver(Feature... features) {
		throw new UnsupportedOperationException("This factory only delegates to other factories to create driver");
	}

}
