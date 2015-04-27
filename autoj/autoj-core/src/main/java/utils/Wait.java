package utils;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import driver.WebDriverExtended;

public class Wait {

	WebDriverExtended driver;
	WebElement element;
	
	public Wait(WebDriverExtended driver, WebElement element) {
		this.driver = driver;
		this.element = element;
	}
	
	public void visibility() {
		driver.waitFor(ExpectedConditions.visibilityOf(element));
	}
	
	public void staleness() {
		driver.waitFor(ExpectedConditions.stalenessOf(element));
	}
}
