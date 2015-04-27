package driver.factory.impl;

import static org.apache.commons.lang3.ArrayUtils.contains;
import static driver.selection.DriverSelection.Matcher.devices;
import static driver.selection.DriverSelection.Matcher.browsers;
import static driver.selection.DriverSelection.Matcher.local;
import java.io.File;

import net.lightbody.bmp.proxy.ProxyServer;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.SystemUtils;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

public class ChromeDriverFactory extends AbstractDriverFactory implements DriverFactory {

	private static final Logger logger = LoggerFactory.getLogger(ChromeDriverFactory.class);

	private static final String CHROMEDRIVER_PATH = ".venus/selenium/chrome/chromedriver";
	private static volatile boolean chromeDriverInstalled = false;

	public ChromeDriverFactory(DriverSelection driverSelection) {
		super(driverSelection);
	}

	@Override
	public WebDriverExtended getDriver(Matcher driverMatcher, Feature... features) {

		// This factory only supports local chrome
		DriverSelection selection = new DriverSelection(Platform.Any, Browser.Chrome, Device.PC, Orientation.Any, Environment.Local);
		if (!driverMatcher.matches(selection)) return null;

		if (!devices(Device.PC).and(browsers(Browser.Chrome).and(local())).matches(getDriverSelection())) {
			return null;
		}

		return super.getDriver(driverMatcher, features);
	}

	@Override
	protected WebDriverExtended createDriver(Feature... features) {

		DesiredCapabilities capabilities = DesiredCapabilities.chrome();
		ChromeOptions chromeOptions = new ChromeOptions();
		capabilities.setCapability(ChromeOptions.CAPABILITY, chromeOptions);

		ProxyServer proxyServer = null;

		if (contains(features, Feature.PROXY)) {
			proxyServer = getProxyServer();

			Proxy seleniumProxy = proxyServer.seleniumProxy();
			capabilities.setCapability(CapabilityType.PROXY, seleniumProxy);
		}

		ChromeDriverService service = new ChromeDriverService.Builder().usingAnyFreePort()
				.usingDriverExecutable(getChromeDriver()).build();
		
		logger.debug(capabilities.toString());
		WebDriver chromeDriver = new ChromeDriver(service, capabilities);

		return new WebDriverExtended(proxyServer, chromeDriver, getDriverSelection());
	}

	private File getChromeDriver() {

		File executable = new File(FileUtils.getUserDirectory(), CHROMEDRIVER_PATH);

		if (chromeDriverInstalled) return executable;

		synchronized (ChromeDriverFactory.class) {
			if (chromeDriverInstalled) return executable;

			try {
				installChromeDriver(executable);
			} catch (Exception e) {
				logger.error("Error installing chrome driver to " + executable, e);
			}
		}

		return executable;
	}

	private void installChromeDriver(File executable) throws Exception {

		if (executable.exists() && executable.isFile()) return;
		
		if (SystemUtils.IS_OS_MAC_OSX) {

			String homeSubPath = executable.getParentFile().getAbsolutePath();

			exec("mkdir -p " + homeSubPath);
			exec("curl -o " + homeSubPath + "/chromedriver.zip http://chromedriver.storage.googleapis.com/2.14/chromedriver_mac32.zip");
			exec("unzip -o " + homeSubPath + "/chromedriver.zip -d " + homeSubPath);
		} else {
			throw new IllegalStateException("Dont know how to install chrome driver for os " + SystemUtils.OS_NAME);
		}
	}

	private void exec(String command) throws Exception {
		logger.debug("exec: " + command);
		Process process = Runtime.getRuntime().exec(command);
		IOUtils.copy(process.getInputStream(), System.out);
		IOUtils.copy(process.getErrorStream(), System.err);

		if (process.waitFor() != 0) {
			throw new IllegalStateException("Error executing command: " + command);
		}
	}
}
