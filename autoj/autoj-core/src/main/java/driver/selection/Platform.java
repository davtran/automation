package driver.selection;

import static org.openqa.selenium.Platform.*;
import io.appium.java_client.remote.MobileCapabilityType;
import io.appium.java_client.remote.MobilePlatform;

import org.openqa.selenium.remote.DesiredCapabilities;

/**
 * Hierarchical enum representing platform OS. Implements {@link CapabilityContributor} so given enum value can
 * contribute capabilities representing selected platform.
 */
public enum Platform implements CapabilityContributor {

	Any((capabilities, selection) -> {
		capabilities.setPlatform(ANY);
	}),
	Windows(Any, (capabilities, selection) -> {
		capabilities.setPlatform(WINDOWS);
	}),
	Windows_XP(Windows, (capabilities, selection) -> {
		capabilities.setPlatform(XP);
	}),
	Windows_Vista(Windows, (capabilities, selection) -> {
		capabilities.setPlatform(VISTA);
	}),
	OSX(Any, (capabilities, selection) -> {
		capabilities.setPlatform(MAC);
	}),
	OSX_Yosemite(OSX, (capabilities, selection) -> {
		capabilities.setPlatform(MAC);
	}),
	Linux(Any, (capabilities, selection) -> {
		capabilities.setPlatform(LINUX);
	}),
	iOS(Any, (capabilities, selection) -> {
		capabilities.setPlatform(MAC);
		capabilities.setCapability(MobileCapabilityType.PLATFORM_NAME, MobilePlatform.IOS);
	}),
	iOS_704(iOS, (capabilities, selection) -> {
		capabilities.setCapability(MobileCapabilityType.PLATFORM_VERSION, "7.0.4");
	}),
	Android(Any, (capabilities, selection) -> {
		capabilities.setPlatform(ANDROID);
		capabilities.setCapability(MobileCapabilityType.PLATFORM_NAME, MobilePlatform.ANDROID);
	}),
	Android_44(Android, (capabilities, selection) -> {
		capabilities.setCapability(MobileCapabilityType.PLATFORM_VERSION, "4.4");
	});

	private final Platform parent;
	private final CapabilityContributor contributor;

	private Platform(CapabilityContributor contributor) {
		this.parent = null;
		this.contributor = contributor;
	}

	private Platform(Platform parent, CapabilityContributor contributor) {
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

	public boolean is(Platform platform) {
		return platform == this || (parent != null && parent.is(platform));
	}

}
