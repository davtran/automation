package driver.factory.impl;

import static driver.selection.DriverSelection.Matcher.devices;
import static driver.selection.DriverSelection.Matcher.remote;
import static org.apache.commons.lang3.ArrayUtils.contains;

import java.net.MalformedURLException;
import java.net.URL;

import net.lightbody.bmp.proxy.ProxyServer;

import org.openqa.selenium.Proxy;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import driver.WebDriverExtended;
import driver.factory.AbstractDriverFactory;
import driver.factory.Feature;
import driver.selection.Browser;
import driver.selection.Device;
import driver.selection.DriverSelection;
import driver.selection.DriverSelection.Matcher;
import driver.selection.Environment;
import driver.selection.Orientation;
import driver.selection.Platform;

public class RemoteDriverFactory extends AbstractDriverFactory {

	private static final Logger logger = LoggerFactory.getLogger(RemoteDriverFactory.class);

	private static final String REMOTE_HUB_URL = "http://localhost:4444/wd/hub";

	public RemoteDriverFactory(DriverSelection driverSelection) {
		super(driverSelection);
	}

	@Override
	public WebDriverExtended getDriver(Matcher driverMatcher, Feature... features) {

		// This factory only supports remote smart phone or tablet
		DriverSelection selection = new DriverSelection(Platform.Any, Browser.Any, Device.PC, Orientation.Any,
				Environment.Remote);
		if (!driverMatcher.matches(selection)) return null;

		if (!devices(Device.PC).and(remote()).matches(getDriverSelection())) return null;

		return super.getDriver(driverMatcher, features);
	}

	@Override
	protected WebDriverExtended createDriver(Feature... features) {

		DesiredCapabilities capabilities = getDesiredCapabilities();

		ProxyServer proxyServer = null;
		if (contains(features, Feature.PROXY)) {
			proxyServer = getProxyServer();

			Proxy seleniumProxy = proxyServer.seleniumProxy();
			capabilities.setCapability(CapabilityType.PROXY, seleniumProxy);
		}

		try {
			RemoteWebDriver remoteWebDriver = new RemoteWebDriver(new URL(REMOTE_HUB_URL), capabilities);
			return new WebDriverExtended(proxyServer, remoteWebDriver, getDriverSelection());
		} catch (MalformedURLException e) {
			logger.error("Incorrect Remote WebDriver URL", e.fillInStackTrace());
		}
		return null;
	}
}
