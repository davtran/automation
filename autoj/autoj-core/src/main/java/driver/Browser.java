package driver;

import org.openqa.selenium.remote.BrowserType;

public enum Browser {
	FIREFOX(BrowserType.FIREFOX), CHROME(BrowserType.CHROME), IE(BrowserType.IE), ANDROID(
			BrowserType.ANDROID), IPHONE(BrowserType.IPHONE), IPAD(
			BrowserType.IPAD);

	private final String id;

	private Browser(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}
}
