package driver.selection;

import org.openqa.selenium.remote.DesiredCapabilities;

/**
 * Contributes capabilities.
 */
public interface CapabilityContributor {

	void contribute(DesiredCapabilities capabilities, DriverSelection selection);

}
