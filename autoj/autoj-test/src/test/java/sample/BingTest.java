package sample;

import static driver.selection.DriverSelection.Matcher.browsers;

import org.junit.Test;

import test.BaseTest;
import driver.selection.Browser;

public class BingTest extends BaseTest {

	@Test
	public void searchBingTest() {
		test(browsers(Browser.IE), driver -> {
			driver.get("www.bing.com");
		});
	}
}
