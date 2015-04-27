package model;

import org.openqa.selenium.By;

import utils.Scroll;
import driver.WebDriverExtended;
import driver.WebElementEx;

/**
 * Base type for modeled pages.
 */
public abstract class Page extends Component {
	
	private final WebElementEx body;
	
	public Page(WebDriverExtended driver) {
		super(driver, driver.findElementEx(By.tagName("html")));
		this.body = findElement(By.tagName("body"));
	}
	
	@Override
	public Scroll scroll() {
		return new Scroll(getDriver(), body);
	}

}
