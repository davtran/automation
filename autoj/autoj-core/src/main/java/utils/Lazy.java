package utils;

import java.util.function.Supplier;

/**
 * Lazy constructing instance supplier.
 *
 * @param <T>
 *            Type of instance that will be lazily constructed.
 */
public class Lazy<T> {

	private final Supplier<T> supplier;
	private T instance;

	/**
	 * @param supplier
	 *            {@link Supplier} that will construct instance that is returned by {@link #get()}.
	 */
	public Lazy(Supplier<T> supplier) {
		this.supplier = supplier;
	}

	/**
	 * @return Lazily constructed instance.
	 */
	public T get() {
		if (instance == null) {
			instance = supplier.get();
		}
		return instance;
	}
}