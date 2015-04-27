package driver.selection;

import org.openqa.selenium.remote.DesiredCapabilities;

/**
 * Hierarchical enum representing device orientation. Implements {@link CapabilityContributor} so given enum value can
 * contribute capabilities representing selected orientation.
 */
public enum Orientation implements CapabilityContributor {

	Any((capabilities, selection) -> {
	}),
	Portrait(Any, (capabilities, selection) -> {
		capabilities.setCapability("orientation", "PORTRAIT");
	}),
	Landscape(Any, (capabilities, selection) -> {
		capabilities.setCapability("orientation", "LANDSCAPE");
	});

	private final Orientation parent;
	private final CapabilityContributor contributor;

	private Orientation(CapabilityContributor contributor) {
		this.parent = null;
		this.contributor = contributor;
	}

	private Orientation(Orientation parent, CapabilityContributor contributor) {
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

	public boolean is(Orientation orientation) {
		return orientation == this || (parent != null && parent.is(orientation));
	}
}
