package driver;

import java.io.File;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import net.lightbody.bmp.proxy.ProxyServer;

import org.apache.http.annotation.NotThreadSafe;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.interactions.HasInputDevices;
import org.openqa.selenium.interactions.Mouse;
import org.openqa.selenium.internal.Locatable;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import utils.OutputTypes;
import utils.Url;

import com.google.common.collect.Sets;

import driver.factory.Feature;
import driver.proxy.VenusProxy;
import driver.selection.DriverSelection;
import driver.selection.Platform;

/**
 * Implementation of {@link WebDriver} with added functionality and convenience methods.
 */
@NotThreadSafe
public class WebDriverExtended implements WebDriver {

	private static final String SCREENSHOT_DIRECTORY = "change me";
	private static final int DEFAULT_TIMEOUT = 5;

	private final WebDriver driver;
	private final ProxyServer proxyServer;
	private final DriverSelection driverSelection;

	public WebDriverExtended(ProxyServer proxyServer, WebDriver driver, DriverSelection driverSelection) {

		if (driver == null) throw new IllegalArgumentException("driver cannot be null");

		this.driver = driver;
		this.proxyServer = proxyServer;
		this.driverSelection = driverSelection;

		// Set initial state
		manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
	}

	public DriverSelection getDriverSelection() {
		return driverSelection;
	}

	/**
	 * Explicitly waits for a given WebElement in a given amount of time (seconds)
	 * 
	 * @param targetElement
	 *            a Selenium WebDriver WebElement
	 * @param seconds
	 *            amount of time to wait
	 */
	public void waitForElement(WebElement targetElement, int seconds) {
		if (seconds < 0) seconds = 5;

		WebDriverWait wait = new WebDriverWait(driver, seconds);
		wait.until(ExpectedConditions.visibilityOf(targetElement));
	}

	/**
	 * Use <strong>Selenium TakesScreenshot</strong> class to capture screen shot of a provided page and store it to a
	 * provided directory in the workspace.
	 * 
	 * @return imageFile Captured screenshot image file in PNG format
	 */
	public File captureScreenshot(String filename) {

		String currentDate = new SimpleDateFormat(" MM-dd-yyyy (HH-mm-ss)").format(new Date());
		File file = new File(SCREENSHOT_DIRECTORY + filename + currentDate + ".png");

		return ((TakesScreenshot) driver).getScreenshotAs(OutputTypes.file(file));
	}

	/**
	 * Execute provided javascript snippet inline.
	 * 
	 * @param jSScript
	 *            JavaScript snippet
	 */
	public void executeJS(String jsSnippet, Object... objects) {
		if (driver instanceof JavascriptExecutor) {
			((JavascriptExecutor) driver).executeScript(jsSnippet, objects);
		}
	}

	@Override
	public void get(String url) {
		driver.get(new Url(url).toString());
	}

	@Override
	public String getCurrentUrl() {
		return driver.getCurrentUrl();
	}

	@Override
	public String getTitle() {
		return driver.getTitle();
	}

	@Override
	public List<WebElement> findElements(By by) {
		return driver.findElements(by);
	}

	@Override
	public WebElement findElement(By by) {
		return driver.findElement(by);
	}

	public WebElementEx findElementEx(By by) {
		return toElementEx(findElement(by));
	}
	
	private WebElementEx toElementEx(WebElement webElement) {
		return new WebElementEx(this, webElement);
	}
 
	public List<WebElementEx> findElementsEx(By by) {
		return findElements(by).stream().map(this::toElementEx).collect(Collectors.toList());
	}
	
	@Override
	public String getPageSource() {
		return driver.getPageSource();
	}

	@Override
	public void close() {
		driver.close();
	}

	@Override
	public void quit() {
		driver.quit();
		if (proxyServer != null) proxyServer.stop();
	}

	@Override
	public Set<String> getWindowHandles() {
		return driver.getWindowHandles();
	}

	@Override
	public String getWindowHandle() {
		return driver.getWindowHandle();
	}

	public String getWindowHandle(Runnable action) {
		Set<String> windowHandles = getWindowHandles();
		
		action.run();
		
		Set<String> difference = Sets.symmetricDifference(windowHandles, getWindowHandles());
		if (difference.isEmpty()) return null;
		
		return difference.iterator().next();
	}

	@Override
	public TargetLocator switchTo() {
		return driver.switchTo();
	}

	@Override
	public NavigationEx navigate() {
		return new NavigationEx(driver.navigate());
	}

	@Override
	public Options manage() {
		return driver.manage();
	}

	/**
	 * Get {@link VenusProxy} associated with this driver. The lifecycle of the proxy is linked to this driver, when
	 * driver is quit the proxy will be stopped.
	 * 
	 * @return {@link VenusProxy} associated with this driver
	 */
	public VenusProxy proxy() {
		if (proxyServer == null) {
			throw new IllegalStateException(Feature.PROXY + " feature not enabled");
		}
		return new VenusProxy(proxyServer, this);
	}

	/**
	 * @param element
	 *            Mouse click on a given element
	 */
	public void mouseClick(WebElement element) {
		mouseClick(element, Target.DEFAULT);
	}

	/**
	 * @param element
	 * @param target
	 *            Open link in new tab or window
	 * 
	 */
	public void mouseClick(WebElement element, Target target) {
		Actions builder = new Actions(driver);
		switch (target) {
		case NEW_WINDOW:
			builder.keyDown(Keys.SHIFT).click(element).keyUp(Keys.SHIFT).perform();
			break;
		case NEW_TAB:
			Keys key = Keys.CONTROL;
			if (DriverSelection.getCurrentPlatform().is(Platform.OSX)) key = Keys.COMMAND;
			builder.keyDown(key).click(element).keyUp(key).perform();
			break;
		case DEFAULT:
			Locatable elementLocation = (Locatable) element;
			Mouse mouse = ((HasInputDevices) driver).getMouse();
			mouse.click(elementLocation.getCoordinates());
		}
	}

	/**
	 * @param element
	 *            Uses the Locatable and Mouse interactions from Selenium WebDriver to take control of a system's mouse
	 *            and hover over an element
	 */
	public void mouseHover(WebElement element) {
		Locatable hoverItem = (Locatable) element;
		Mouse mouse = ((HasInputDevices) driver).getMouse();
		mouse.mouseMove(hoverItem.getCoordinates());
	}

	/**
	 * @param element
	 *            Moves to the top left corner of an element then moves to the bottom right corner of an element to
	 *            fully expose it
	 */
	public void exposeElement(WebElement element) {
		int yAxisLocation = element.getLocation().getY();
		int xAxisLocation = element.getLocation().getX();
		int elementWidth = element.getSize().getWidth();
		int elementHeight = element.getSize().getHeight();
		int offsetY = yAxisLocation + elementHeight;
		int offsetX = xAxisLocation + elementWidth;
		Locatable hoverItem = (Locatable) element;
		Mouse mouse = ((HasInputDevices) driver).getMouse();
		mouse.mouseMove(hoverItem.getCoordinates());
		mouse.mouseMove(hoverItem.getCoordinates(), offsetX, offsetY + (elementHeight * 3));
	}

	/**
	 * Wait until all active ajax calls are done
	 * 
	 * @throws Exception
	 */
	public void waitForAjax() throws Exception {
		boolean ajaxActive = (boolean) ((JavascriptExecutor) driver).executeScript("return jQuery.active === 0");
		while (ajaxActive != true) {
			Thread.sleep(3000);
			ajaxActive = (boolean) ((JavascriptExecutor) driver).executeScript("return jQuery.active === 0");
			if (ajaxActive) {
				break;
			}
		}
	}

	/**
	 * Wait for an expected condition to match within default timeout
	 * 
	 * @param condition
	 */
	public <T> T waitFor(ExpectedCondition<T> condition) {
		return waitFor(condition, DEFAULT_TIMEOUT);
	}

	/**
	 * Wait for an expected condition to match within given timeout
	 * 
	 * @param condition
	 */
	public <T> T waitFor(ExpectedCondition<T> condition, int timeOut) {
		if (timeOut < 0) timeOut = DEFAULT_TIMEOUT;
		return new WebDriverWait(driver, timeOut).until(condition);
	}

	public class NavigationEx implements Navigation {

		private final Navigation navigation;

		public NavigationEx(Navigation navigation) {
			this.navigation = navigation;
		}

		@Override
		public void back() {
			navigation.back();
		}

		@Override
		public void forward() {
			navigation.forward();
		}

		@Override
		public void to(String url) {
			navigation.to(new Url(url).toString());
		}

		@Override
		public void to(URL url) {
			to(url.toString());
		}

		@Override
		public void refresh() {
			navigation.refresh();
		}

	}

}
