package driver.selection;

public enum Environment {

	Any,
	Local(Any),
	Remote(Any);

	private final Environment parent;

	private Environment() {
		this.parent = null;
	}

	private Environment(Environment parent) {
		this.parent = parent;
	}

	public boolean is(Environment environment) {
		return environment == this || (parent != null && parent.is(environment));
	}

}
