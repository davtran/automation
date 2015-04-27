package driver.selection;

import org.apache.commons.lang3.SystemUtils;

/**
 * Represents target driver.
 */
public class DriverSelection {

	private final Platform platform;
	private final Browser browser;
	private final Device device;
	private final Orientation orientation;
	private final Environment environment;

	public DriverSelection(Platform platform, Browser browser, Device device, Orientation orientation,
			Environment environment) {
		this.platform = platform;
		this.browser = browser;
		this.device = device;
		this.orientation = orientation;
		this.environment = environment;
	}

	public Platform getPlatform() {
		return platform;
	}

	public Browser getBrowser() {
		return browser;
	}

	public Device getDevice() {
		return device;
	}

	public Orientation getOrientation() {
		return orientation;
	}

	public Environment getEnvironment() {
		return environment;
	}

	/**
	 * @return Current detected {@link Platform} that tests are running on.
	 */
	public static Platform getCurrentPlatform() {

		if (SystemUtils.IS_OS_MAC) return Platform.OSX;
		if (SystemUtils.IS_OS_WINDOWS) return Platform.Windows;
		if (SystemUtils.IS_OS_LINUX) return Platform.Linux;

		return Platform.Any;
	}

	/**
	 * @return Current detected {@link Device} that tests are running on.
	 */
	public static Device getCurrentDevice() {
		// Currently can only run tests on PC
		return Device.PC;
	}

	/**
	 * Matchers can be constructed to facilitate matching of {@link DriverSelection} instances.
	 */
	public static abstract class Matcher {

		public abstract boolean matches(DriverSelection target);

		public Matcher and(Matcher matcher) {
			return new Matcher() {
				@Override
				public boolean matches(DriverSelection target) {
					return Matcher.this.matches(target) && matcher.matches(target);
				}
			};
		}

		/**
		 * Matches anything
		 * 
		 * @return true
		 */
		public static Matcher any() {
			return new Matcher() {
				@Override
				public boolean matches(DriverSelection target) {
					return true;
				}
			};
		}

		/**
		 * Matches a selection based on browsers
		 * 
		 * @param browsers
		 *            a varargs of Browser enums
		 * @return true or false
		 */
		public static Matcher browsers(Browser... browsers) {
			return new Matcher() {

				@Override
				public boolean matches(DriverSelection target) {
					for (Browser browser : browsers) {
						if (browser.is(target.getBrowser())) {
							return true;
						}
					}
					return false;
				}

			};
		}

		/**
		 * Matches a selection based on platforms
		 * 
		 * @param platforms
		 *            a varargs of Platform enums
		 * @return true or false
		 */
		public static Matcher platforms(Platform... platforms) {
			return new Matcher() {

				@Override
				public boolean matches(DriverSelection target) {
					for (Platform platform : platforms) {
						if (platform.is(target.getPlatform())) {
							return true;
						}
					}
					return false;
				}

			};
		}

		/**
		 * Matches a selection based on platforms
		 * 
		 * @param platforms
		 *            a varargs of Platform enums
		 * @return true or false
		 */
		public static Matcher devices(Device... devices) {
			return new Matcher() {

				@Override
				public boolean matches(DriverSelection target) {
					for (Device device : devices) {
						if (device.is(target.getDevice())) {
							return true;
						}
					}
					return false;
				}

			};
		}

		/**
		 * Matches a selection based on environment
		 * 
		 * @param environment
		 * @return true or false
		 */
		public static Matcher environment(Environment environment) {
			return new Matcher() {
				@Override
				public boolean matches(DriverSelection target) {
					return environment.is(target.getEnvironment());
				}

			};
		}

		/**
		 * Matches a selection based on environment
		 * 
		 * @param environment
		 * @return true or false
		 */
		public static Matcher remote() {
			return new Matcher() {
				@Override
				public boolean matches(DriverSelection target) {
					return Environment.Remote.is(target.getEnvironment());
				}

			};
		}

		/**
		 * Matches a selection based on environment
		 * 
		 * @param environment
		 * @return true or false
		 */
		public static Matcher local() {
			return new Matcher() {
				@Override
				public boolean matches(DriverSelection target) {
					return Environment.Local.is(target.getEnvironment());
				}

			};
		}

	}

}
