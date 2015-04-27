package model;

import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebElement;

import utils.Lazy;
import utils.Scroll;
import utils.Wait;
import driver.WebDriverExtended;
import driver.WebElementEx;

/**
 * Base type for modeled components.
 */
public abstract class Component {

	private final WebElementEx element;
	private final WebDriverExtended driver;

	public Component(WebDriverExtended driver, WebElementEx element) {
		this.driver = driver;
		this.element = element;
	}

	/**
	 * @return {@link WebElement} linked to this component
	 */
	protected WebElementEx getElement() {
		return element;
	}

	/**
	 * @return {@link WebDriverExtended} linked to this component
	 */
	protected WebDriverExtended getDriver() {
		return driver;
	}

	/**
	 * Convenience method to construct {@link Lazy} instances for given {@link Supplier}.
	 * 
	 * @param supplier
	 *            {@link Supplier} that will produce lazily constructed object.
	 * @return {@link Lazy} instance for given {@link Supplier}
	 */
	protected <T> Lazy<T> lazy(Supplier<T> supplier) {
		return new Lazy<T>(supplier);
	}

	/**
	 * @see WebElement#findElement(By)
	 */
	protected WebElementEx findElement(By by) {
		return element.findElementEx(by);
	}
	
	protected List<WebElementEx> findElements(By by){
		return element.findElementsEx(by);
	}
	
	protected <T> List<T> findElements(By by, BiFunction<WebDriverExtended, WebElementEx, T> function){
		List<WebElementEx> results = findElements(by);
		
		return results.stream().map((webElement) -> function.apply(driver, webElement)).collect(Collectors.toList());
	}

	public Scroll scroll() throws Exception{
		throw new UnsupportedOperationException("Component doesn't support scrolling");
		}
	
	public Dimension getSize() {
		return element.getSize();
	}
	
	public Point getLocation() {
		return element.getLocation();
	}
	
	public Wait waitFor(){
		return getElement().waitFor();
	}
}
