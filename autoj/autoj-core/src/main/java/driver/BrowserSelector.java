package driver;

import org.openqa.selenium.Platform;

public class BrowserSelector {

	public static interface BrowserSelectionMatcher {
		
		boolean matches(BrowserSelection target);
		
	}
	
	public static class BrowserSelection {
		private final Platform platform;
		private final Browser browser;
		private final String version;
		private final boolean remote;
		
		public BrowserSelection(Platform platform, Browser browser, String version, boolean remote) {
			this.platform = platform;
			this.browser = browser;
			this.version = version;
			this.remote = remote;
		}
		
		public Platform getPlatform() {
			return platform;
		}
		
		public Browser getBrowser() {
			return browser;
		}
		
		public String getVersion() {
			return version;			
		}
		
		public boolean isRemote() {
			return remote;
		}
	}
}

