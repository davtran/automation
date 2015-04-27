package driver;

import java.util.List;
import java.util.stream.Collectors;

import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import utils.Wait;

public class WebElementEx implements WebElement {
	
	private static final int DEFAULT_TIMEOUT = 5;
	
	private final WebDriverExtended driver;
	private final WebElement element;
	
	public WebElementEx(WebDriverExtended driver, WebElement element) {
		
		if (driver == null) throw new IllegalArgumentException("driver cannot be null");
		if (element == null) throw new IllegalArgumentException("element cannot be null");
		this.driver = driver;
		this.element = element;
				
	}
	
	/**
	 *  Explicitly waits for a given WebElement in default timeout
	 *            amount of time to wait
	 */
	public void waitUntilVisible() {
		waitUntilVisible(DEFAULT_TIMEOUT);
	}
	
	/**
	 *  Explicitly waits for a given WebElement in a given amount of time (seconds)
	 * 
	 * @param seconds
	 *            amount of time to wait
	 */
	public void waitUntilVisible(int seconds) {
		if (seconds < 0) seconds = DEFAULT_TIMEOUT;

		WebDriverWait wait = new WebDriverWait(driver, seconds);
		wait.until(ExpectedConditions.visibilityOf(element));
	}
	
	/**
	 * Mouse click on a given element 
	 */
	public void mouseClick() {
		driver.mouseClick(element,Target.DEFAULT);
	}
	
	/**
	 * @param target
	 * Open link in new tab or window
	 * 
	 */
	public void mouseClick(Target target) {
		driver.mouseClick(element, target);
	}
	
	/**
	 * Uses the Locatable and Mouse interactions from Selenium WebDriver to take
	 * control of a system's mouse and hover over an element
	 */
	public void mouseHover() {
		driver.mouseHover(element);
	}
	
	/**
	 * Moves to the top left corner of an element then moves to the bottom right corner of an element
	 * to fully expose it
	 */
	public void expose() {
		driver.exposeElement(element);
	}
	
	@Override
	public void clear() {
		element.clear();		
	}

	@Override
	public void click() {
		element.click();
	}

	@Override
	public WebElement findElement(By by) {
		return element.findElement(by);
	}
	
	public WebElementEx findElementEx(By by) {
		return toElementEx(findElement(by));
	}
	
	private WebElementEx toElementEx(WebElement webElement) {
		return new WebElementEx(driver, webElement);
	}
 
	@Override
	public List<WebElement> findElements(By by) {
		return element.findElements(by);
	}
	
	public List<WebElementEx> findElementsEx(By by) {
		return findElements(by).stream().map(this::toElementEx).collect(Collectors.toList());
	}

	@Override
	public String getAttribute(String attribute) {
		return element.getAttribute(attribute);
	}

	@Override
	public String getCssValue(String css) {
		return element.getCssValue(css);
	}

	@Override
	public Point getLocation() {
		return element.getLocation();
	}

	@Override
	public Dimension getSize() {
		return element.getSize();
	}

	@Override
	public String getTagName() {
		return element.getTagName();
	}

	@Override
	public String getText() {
		return element.getText();
	}

	@Override
	public boolean isDisplayed() {
		return element.isDisplayed();
	}

	@Override
	public boolean isEnabled() {
		return element.isEnabled();
	}

	@Override
	public boolean isSelected() {
		return element.isSelected();
	}

	@Override
	public void sendKeys(CharSequence... keys) {
		element.sendKeys(keys);
	}

	@Override
	public void submit() {
		element.submit();
	}
	
	public Wait waitFor() {
		return new Wait(driver, element);
	}

}
