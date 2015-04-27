package driver.factory;

import net.lightbody.bmp.proxy.ProxyServer;

import org.openqa.selenium.remote.DesiredCapabilities;

import utils.ConfigurationProperties;
import driver.WebDriverExtended;
import driver.proxy.ProxyManager;
import driver.selection.Browser;
import driver.selection.Device;
import driver.selection.DriverSelection;
import driver.selection.DriverSelection.Matcher;
import driver.selection.Environment;
import driver.selection.Orientation;
import driver.selection.Platform;

/**
 * Abstract base class all driver factories should extend.
 */
public abstract class AbstractDriverFactory implements DriverFactory {

	private static final Platform DEFAULT_PLATFORM = DriverSelection.getCurrentPlatform();
	private static final Browser DEFAULT_BROWSER = Browser.Any;
	private static final Device DEFAULT_DEVICE = DriverSelection.getCurrentDevice();
	private static final Orientation DEFAULT_ORIENTATION = Orientation.Any;
	private static final Environment DEFAULT_ENVIRONMENT = Environment.Local;

	private static final ProxyManager proxyManager = new ProxyManager(8000, 10000);

	private final DriverSelection driverSelection;

	/**
	 * Create instance with default {@link DriverSelection} determined by {@link ConfigurationProperties}.
	 */
	public AbstractDriverFactory() {
		this.driverSelection = loadDriverSelection();
	}

	/**
	 * Create instance using provided {@link DriverSelection}.
	 * 
	 * @param driverSelection
	 *            {@link DriverSelection} to use.
	 */
	public AbstractDriverFactory(DriverSelection driverSelection) {
		this.driverSelection = driverSelection;
	}

	/**
	 * Load {@link DriverSelection} based on {@link ConfigurationProperties}.
	 * 
	 * @return currently configured {@link DriverSelection}
	 */
	private DriverSelection loadDriverSelection() {
		Platform platform = ConfigurationProperties.getPlatform(DEFAULT_PLATFORM);
		Browser browser = ConfigurationProperties.getBrowser(DEFAULT_BROWSER);
		Device device = ConfigurationProperties.getDevice(DEFAULT_DEVICE);
		Orientation orientation = ConfigurationProperties.getOrientation(DEFAULT_ORIENTATION);
		Environment environment = ConfigurationProperties.getEnvironment(DEFAULT_ENVIRONMENT);

		return new DriverSelection(platform, browser, device, orientation, environment);
	}

	/**
	 * @return {@link DriverSelection} representing target of {@link WebDriverExtended} instances created by this
	 *         factory.
	 */
	protected DriverSelection getDriverSelection() {
		return driverSelection;
	}

	/**
	 * @return {@link DesiredCapabilities} based on {@link DriverSelection} associated with this factory.
	 */
	protected DesiredCapabilities getDesiredCapabilities() {
		DriverSelection selection = getDriverSelection();

		DesiredCapabilities capabilities = new DesiredCapabilities();

		selection.getDevice().contribute(capabilities, selection);
		selection.getPlatform().contribute(capabilities, selection);
		selection.getBrowser().contribute(capabilities, selection);
		selection.getOrientation().contribute(capabilities, selection);

		return capabilities;
	}

	/**
	 * Get new {@link ProxyServer} with unique port. Every time method is called a new {@link ProxyServer} instance will
	 * be created with unique port to avoid port conflicts.
	 * 
	 * @return New {@link ProxyServer} with unique port.
	 */
	protected ProxyServer getProxyServer() {
		return proxyManager.getProxyServer();
	}

	@Override
	public WebDriverExtended getDriver(Matcher driverMatcher, Feature... features) {
		return driverMatcher.matches(getDriverSelection()) ? createDriver(features) : null;
	}

	protected abstract WebDriverExtended createDriver(Feature... features);

}
