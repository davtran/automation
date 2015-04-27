package driver.factory.impl;

import static driver.selection.DriverSelection.Matcher.browsers;
import static driver.selection.DriverSelection.Matcher.devices;
import static driver.selection.DriverSelection.Matcher.local;
import static org.apache.commons.lang3.ArrayUtils.contains;
import net.lightbody.bmp.proxy.ProxyServer;

import org.openqa.selenium.Proxy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

import driver.WebDriverExtended;
import driver.factory.AbstractDriverFactory;
import driver.factory.DriverFactory;
import driver.factory.Feature;
import driver.selection.Browser;
import driver.selection.Device;
import driver.selection.DriverSelection;
import driver.selection.DriverSelection.Matcher;
import driver.selection.Environment;
import driver.selection.Orientation;
import driver.selection.Platform;

public class FirefoxDriverFactory extends AbstractDriverFactory implements DriverFactory {

	public FirefoxDriverFactory(DriverSelection driverSelection) {
		super(driverSelection);
	}

	@Override
	public WebDriverExtended getDriver(Matcher driverMatcher, Feature... features) {

		// This factory only supports local firefox
		DriverSelection selection = new DriverSelection(Platform.Any, Browser.Firefox, Device.PC, Orientation.Any,
				Environment.Local);
		if (!driverMatcher.matches(selection)) return null;

		if (!devices(Device.PC).and(browsers(Browser.Firefox).and(local())).matches(getDriverSelection())) {
			return null;
		}

		return super.getDriver(driverMatcher, features);
	}

	@Override
	protected WebDriverExtended createDriver(Feature... features) {

		DesiredCapabilities capabilities = DesiredCapabilities.firefox();

		ProxyServer proxyServer = null;

		if (contains(features, Feature.PROXY)) {
			proxyServer = getProxyServer();

			Proxy seleniumProxy = proxyServer.seleniumProxy();
			capabilities.setCapability(CapabilityType.PROXY, seleniumProxy);
		}

		WebDriver firefoxDriver = new FirefoxDriver(capabilities);

		return new WebDriverExtended(proxyServer, firefoxDriver, getDriverSelection());
	}

}
