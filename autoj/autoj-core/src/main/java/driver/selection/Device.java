package driver.selection;

import io.appium.java_client.remote.MobileCapabilityType;

import org.openqa.selenium.remote.DesiredCapabilities;

/**
 * Hierarchical enum representing hardware device. Implements {@link CapabilityContributor} so given enum value can
 * contribute capabilities representing selected device.
 */
public enum Device implements CapabilityContributor {

	Any((capabilities, selection) -> {
	}),
	PC(Any, (capabilities, selection) -> {
	}),
	SmartPhone(Any, (capabilities, selection) -> {
	}),
	SmartPhone_iPhoneSimulator(SmartPhone, (capabilities, selection) -> {
		capabilities.setCapability(MobileCapabilityType.DEVICE_NAME, "iPhone 5s");
	}),
	SmartPhone_iPhoneRetina4inch(SmartPhone, (capabilities, selection) -> {
		capabilities.setCapability(MobileCapabilityType.DEVICE_NAME, "iPhone Retina 4-inch");
	}),
	SmartPhone_AndroidEmulator(SmartPhone, (capabilities, selection) -> {
		capabilities.setCapability(MobileCapabilityType.DEVICE_NAME, "Android Emulator");
	}),
	Tablet(Any, (capabilities, selection) -> {
	}),
	Tablet_iPadSimulator(Tablet, (capabilities, selection) -> {
		capabilities.setCapability(MobileCapabilityType.DEVICE_NAME, "iPad Simulator");
	});

	private final Device parent;
	private final CapabilityContributor contributor;

	private Device(CapabilityContributor contributor) {
		this.parent = null;
		this.contributor = contributor;
	}

	private Device(Device parent, CapabilityContributor contributor) {
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

	public boolean is(Device device) {
		return device == this || (parent != null && parent.is(device));
	}

}
