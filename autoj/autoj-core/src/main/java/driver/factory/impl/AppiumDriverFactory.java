package driver.factory.impl;

import static driver.selection.DriverSelection.Matcher.devices;
import static org.apache.commons.lang3.ArrayUtils.contains;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;

import java.net.MalformedURLException;
import java.net.URL;

import net.lightbody.bmp.proxy.ProxyServer;

import org.openqa.selenium.Proxy;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

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

public class AppiumDriverFactory extends AbstractDriverFactory {

	private static final String APPIUM_LOCAL_URL = "http://localhost:4723/wd/hub";
	private static final String APPIUM_REMOTE_URL = "http://localhost:4723/wd/hub";

	public AppiumDriverFactory(DriverSelection driverSelection) {
		super(driverSelection);
	}

	@Override
	public WebDriverExtended getDriver(Matcher driverMatcher, Feature... features) {

		// This factory only supports remote smart phone or tablet
		DriverSelection smartPhoneSelection = new DriverSelection(Platform.Any, Browser.Any, Device.SmartPhone,
				Orientation.Any, Environment.Any);
		DriverSelection tabletSelection = new DriverSelection(Platform.Any, Browser.Any, Device.Tablet,
				Orientation.Any, Environment.Any);
		if (!driverMatcher.matches(smartPhoneSelection) && !driverMatcher.matches(tabletSelection)) {
			return null;
		}

		if (!devices(Device.SmartPhone, Device.Tablet).matches(getDriverSelection())) return null;

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

		DriverSelection selection = getDriverSelection();
		
		// TODO: If "Any" is specified this may not work as expected. That needs to be flushed out.
		String appiumUrl = APPIUM_REMOTE_URL;
		if (Environment.Local.is(selection.getEnvironment())) {
			appiumUrl = APPIUM_LOCAL_URL;
		}

		if (selection.getPlatform().is(Platform.iOS)) {
			return createIosDriver(proxyServer, capabilities, appiumUrl);
		} else if (selection.getPlatform().is(Platform.Android)) {
			return createAndroidDriver(proxyServer, capabilities, appiumUrl);
		}

		throw new IllegalStateException("Appium driver factory does not support driver for " + capabilities);
	}

	private WebDriverExtended createIosDriver(ProxyServer proxyServer, DesiredCapabilities capabilities,
			String appiumUrl) {
		try {
			IOSDriver iosDriver = new IOSDriver(new URL(appiumUrl), capabilities);
			return new WebDriverExtended(proxyServer, iosDriver, getDriverSelection());
		} catch (MalformedURLException ex) {
			throw new IllegalStateException("Appium Server is not running or the URL is incorrect", ex);
		}
	}

	private WebDriverExtended createAndroidDriver(ProxyServer proxyServer, DesiredCapabilities capabilities,
			String appiumUrl) {
		try {
			AndroidDriver androidDriver = new AndroidDriver(new URL(appiumUrl), capabilities);
			return new WebDriverExtended(proxyServer, androidDriver, getDriverSelection());
		} catch (MalformedURLException ex) {
			throw new IllegalStateException("Appium Server is not running or the URL is incorrect", ex);
		}
	}

}
