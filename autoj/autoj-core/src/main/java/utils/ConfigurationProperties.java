package utils;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.openqa.selenium.Platform;

import driver.Browser;

public class ConfigurationProperties {
	private static Map<String, String> propertiesMap = new HashMap<>();
	
	static {
		Map<String, String> environmentMap = System.getenv();
		for (String key : environmentMap.keySet()) {
			propertiesMap.put(key, environmentMap.get(key));
		}
		Properties systemProperties = System.getProperties();
		for (String key : systemProperties.stringPropertyNames()) {
			propertiesMap.put(key,systemProperties.getProperty(key));
		}
	}
	
	private static String getProperty(String key, String defaultValue) {
		return propertiesMap.getOrDefault(key, defaultValue);
	}
	
	public static Platform getPlatform(Platform defaultPlatform) {
		String platform = getProperty("platform", null);
		if (platform == null) {
			return defaultPlatform;
		}
		return Platform.valueOf(platform.toUpperCase());
	}
	
	public static Browser getBrowser(Browser defaultBrowser) {
		String browser = getProperty("browser", null);
		if (browser == null) {
			return defaultBrowser;
		}
		return Browser.valueOf(browser);
	}
	
	public static String getVersion(String defaultVersion) {
		return getProperty("version", defaultVersion);
	}
	
	public static boolean isRemote(boolean defaultRemote) {
		String remote = getProperty("remote", null);
		if (remote == null) {
			return defaultRemote;
		}
		return Boolean.valueOf(remote.toLowerCase());
	}
}
