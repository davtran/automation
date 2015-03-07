package driver;

import java.net.MalformedURLException;
import java.net.URL;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

public class WebDriverFactory {

	public WebDriver getDriver() {
		return createDriver();
	}
	
	private WebDriver createDriver() {
		if ("".equals("")) {
			return createRemoteDriver();
		} else {
			return createLocalDriver();
		}
	}

	private WebDriver createRemoteDriver() {
		DesiredCapabilities capabilities = new DesiredCapabilities();
		try {
			return new RemoteWebDriver(new URL(""), capabilities);
		} catch (MalformedURLException ex) {
			ex.printStackTrace();
		}
		return null;
	}

	private WebDriver createLocalDriver() {
		switch ("") {
		case "chrome":
			return createChromeDriver();
		case "firefox":
			return createFirefoxDriver();
		case "ie":
			return createInternetExplorerDriver();
			default:
				break;
		}
		return null;
	}
	
	private WebDriver createChromeDriver() {
		return new ChromeDriver();
	}
	
	private WebDriver createFirefoxDriver() {
		return new FirefoxDriver();
	}

	private WebDriver createInternetExplorerDriver() {
		return new InternetExplorerDriver();
	}
}
