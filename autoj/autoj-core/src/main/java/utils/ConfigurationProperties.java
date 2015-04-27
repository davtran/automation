package utils;

import static org.apache.commons.lang3.StringUtils.defaultIfEmpty;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import driver.selection.Browser;
import driver.selection.Device;
import driver.selection.Environment;
import driver.selection.Orientation;
import driver.selection.Platform;

/**
 * Provides access to all venus configuration properties.
 */
public class ConfigurationProperties {

	private static final String PLATFORM_PROP = "platform";
	private static final String BROWSER_PROP = "browser";
	private static final String DEVICE_PROP = "device";
	private static final String ENVIRONMENT_PROP = "environment";
	private static final String ORIENTATION_PROP = "orientation";
	private static final String URL_ENVIRONMENT_PROP = "urlEnvironment";
	private static final String URL_PORT_PROP = "urlPort";

	private static Map<String, String> propertiesMap = new HashMap<>();

	/**
	 * Places all environment variables and system properties into a properties map
	 */
	static {
		Map<String, String> environmentMap = System.getenv();
		for (String key : environmentMap.keySet()) {
			propertiesMap.put(key, environmentMap.get(key));
		}
		Properties systemProperties = System.getProperties();
		for (String key : systemProperties.stringPropertyNames()) {
			propertiesMap.put(key, systemProperties.getProperty(key));
		}
	}

	/**
	 * Gets a property based on a key or a default value
	 * 
	 * @param key
	 * @param defaultValue
	 * @return the property of the key or the default value
	 */
	private static <E extends Enum<E>> E getProperty(String key, E defaultValue, Class<E> enumType) {
		String string = propertiesMap.get(key);
		if (string == null) return defaultValue;

		return Enum.valueOf(enumType, string);
	}

	private static String getProperty(String key, String defaultValue) {
		return defaultIfEmpty(propertiesMap.get(key), defaultValue);
	}

	/**
	 * Gets the Venus Platform and returns that value or the default value
	 * 
	 * @param defaultPlatform
	 * @return
	 */
	public static Platform getPlatform(Platform defaultPlatform) {
		return getProperty(PLATFORM_PROP, defaultPlatform, Platform.class);
	}

	/**
	 * Gets the Venus Browser property and returns that value or the default value
	 * 
	 * @param defaultBrowser
	 * @return
	 */
	public static Browser getBrowser(Browser defaultBrowser) {
		return getProperty(BROWSER_PROP, defaultBrowser, Browser.class);
	}

	/**
	 * Gets the Venus Device property and returns that value or the default value
	 * 
	 * @param defaultDevice
	 * @return
	 */
	public static Device getDevice(Device defaultDevice) {
		return getProperty(DEVICE_PROP, defaultDevice, Device.class);
	}

	/**
	 * Gets the Venus Orientation property and returns that value or the default value
	 * 
	 * @param defaultDevice
	 * @return
	 */
	public static Orientation getOrientation(Orientation defaultOrientation) {
		return getProperty(ORIENTATION_PROP, defaultOrientation, Orientation.class);
	}

	/**
	 * Gets the Venus Environment property and returns that value or the default value
	 * 
	 * @param defaultEnvironment
	 * @return the property's value or the default environment
	 */
	public static Environment getEnvironment(Environment defaultEnvironment) {
		return getProperty(ENVIRONMENT_PROP, defaultEnvironment, Environment.class);
	}

	public static String getUrlEnvironment(String defaultUrlEnvironment) {
		return getProperty(URL_ENVIRONMENT_PROP, defaultUrlEnvironment);
	}
	
	public static String getUrlPort(String defaultUrlPort) {
		return getProperty(URL_PORT_PROP, defaultUrlPort);
	}

}
