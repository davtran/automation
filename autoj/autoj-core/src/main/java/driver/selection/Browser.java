package driver.selection;

import io.appium.java_client.remote.MobileBrowserType;
import io.appium.java_client.remote.MobileCapabilityType;

import org.openqa.selenium.remote.BrowserType;
import org.openqa.selenium.remote.DesiredCapabilities;

/**
 * Hierarchical enum representing web browser. Implements {@link CapabilityContributor} so given enum value can
 * contribute capabilities representing selected browser.
 */
public enum Browser implements CapabilityContributor {

	Any((capabilities, selection) -> {
	}),
	Firefox(Any, (capabilities, selection) -> {
		capabilities.setBrowserName(BrowserType.FIREFOX);
	}),
	Chrome(Any, (capabilities, selection) -> {
		if (selection.getDevice().is(Device.SmartPhone)) {
			capabilities.setCapability(MobileCapabilityType.BROWSER_NAME, MobileBrowserType.CHROME);
		} else {
			capabilities.setBrowserName(BrowserType.CHROME);
		}
	}),
	IE(Any, (capabilities, selection) -> {
		capabilities.setBrowserName(BrowserType.IE);
	}),
	IE_10(IE, (capabilities, selection) -> {
		capabilities.setVersion("10");
	}),
	Android(Any, (capabilities, selection) -> {
		capabilities.setCapability(MobileCapabilityType.BROWSER_NAME, MobileBrowserType.BROWSER);
	}),
	Safari(Any, (capabilities, selection) -> {
		if (selection.getDevice().is(Device.SmartPhone)) {
			capabilities.setCapability(MobileCapabilityType.BROWSER_NAME, MobileBrowserType.SAFARI);
		} else {
			capabilities.setBrowserName(BrowserType.SAFARI);
		}
	});

	private final Browser parent;
	private final CapabilityContributor contributor;

	private Browser(CapabilityContributor contributor) {
		this.parent = null;
		this.contributor = contributor;
	}

	private Browser(Browser parent, CapabilityContributor contributor) {
		this.parent = parent;
		this.contributor = (capabilities, selection) -> {
			parent.contributor.contribute(capabilities, selection);
			contributor.contribute(capabilities, selection);
		};
	}

	@Override
	public void contribute(DesiredCapabilities capabilities, DriverSelection selection) {
		contributor.contribute(capabilities, selection);
	}

	public boolean is(Browser browser) {
		return browser == this || (parent != null && parent.is(browser));
	}

}
