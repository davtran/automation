package driver.proxy;

import net.lightbody.bmp.proxy.ProxyServer;
import driver.WebDriverExtended;

/**
 * Facade for interacting with {@link ProxyServer}.
 */
public class VenusProxy {

	private final ProxyServer proxyServer;

	public VenusProxy(ProxyServer proxyServer, WebDriverExtended driver) {
		this.proxyServer = proxyServer;

		proxyServer.setCaptureHeaders(true);
	}

	/**
	 * Starts capture of new page within current {@link Capture}.
	 */
	public void startPage(String page) {
		endPage();
		if (proxyServer.getHar() == null) {
			proxyServer.newHar(page);
		} else {
			proxyServer.newPage(page);
		}
	}

	/**
	 * End page within current {@link Capture}.
	 */
	public void endPage() {
		proxyServer.endPage();
	}

	/**
	 * @return Current {@link Capture}.
	 */
	public Capture capture() {
		return new Capture(proxyServer.getHar());
	}

	/**
	 * Inject header into all requests going through this proxy.
	 * 
	 * @param name
	 *            Header name to inject
	 * @param value
	 *            Header value to inject
	 */
	public void addHeader(String name, String value) {
		proxyServer.addHeader(name, value);
	}

}
