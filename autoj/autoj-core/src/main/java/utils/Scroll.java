package utils;

import org.openqa.selenium.WebElement;

import driver.WebDriverExtended;

public class Scroll {
	WebDriverExtended driver;
	WebElement element;

	public Scroll(WebDriverExtended driver, WebElement element) {
		this.driver = driver;
		this.element = element;
	}

	/**
	 * Executes JavaScript to scroll up by a pixel amount
	 * 
	 * @param pixelAmount
	 */
	public void top(int pixelAmount) {
		driver.executeJS("$(arguments[0]).scrollTop(" + pixelAmount + ");", element);
	}

	/**
	 * Executes JavaScript to scroll up (pixelAmount =< 0) or down (pixelAmount > 0) with a given animation speed
	 * 
	 * @param pixelAmount
	 * @param animationSpeed
	 *            based on milliseconds. higher values are slower
	 */
	public void top(int pixelAmount, int animationSpeed) {
		driver.executeJS("$(arguments[0]).animate({scrollTop: " + pixelAmount + "}, " + animationSpeed + ");", element);
	}

	/**
	 * Executes JavaScript to scroll up (pixelAmount =< 0) or down (pixelAmount > 0) with a given animation speed
	 * 
	 * @param pixelAmount
	 * @param speed
	 *            DEFAULT, FAST, SLOW
	 */
	public void top(int pixelAmount, Speed speed) {
		driver.executeJS("$(arguments[0].animate({scrollTop: " + pixelAmount + "}," + speed.getMilliseconds() + "",
				element);
	}

	/**
	 * Executes JavaScript to scroll left by a pixel amount
	 * 
	 * @param pixelAmount
	 */
	public void left(int pixelAmount) {
		driver.executeJS("$(arguments[0]).scrollLeft(" + pixelAmount + ");", element);
	}

	/**
	 * Executes JavaScript to scroll left (pixelAmount =< 0) or right (pixelAmount > 0) with a given animation speed
	 * 
	 * @param pixelAmount
	 * @param animationSpeed
	 *            based on milliseconds. higher values are slower
	 */
	public void left(int pixelAmount, int animationSpeed) {
		driver.executeJS("$(arguments[0]).animate({scrollLeft: " + pixelAmount + "}, " + animationSpeed + ");", element);
	}

	/**
	 * Executes JavaScript to scroll left (pixelAmount =< 0) or right (pixelAmount > 0) with a given animation speed
	 * 
	 * @param pixelAmount
	 * @param speed
	 *            DEFAULT, FAST, SLOW
	 */
	public void left(int pixelAmount, Speed speed) {
		driver.executeJS(
				"$(arguments[0]).animate({scrollLeft: " + pixelAmount + "}, " + speed.getMilliseconds() + ");", element);
	}

	/**
	 * Executes JavaScript to scroll to the very top of a page
	 */
	public void top() {
		top(0);
	}

	/**
	 * Executes JavaScript to scroll to the very left
	 */
	public void left() {
		left(0);
	}

	public static enum Speed {
		DEFAULT(400),
		SLOW(600),
		FAST(200);

		private final int milliseconds;

		private Speed(int milliseconds) {
			this.milliseconds = milliseconds;
		}

		public int getMilliseconds() {
			return milliseconds;
		}
	}
}
