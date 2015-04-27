package driver.proxy;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import net.lightbody.bmp.proxy.ProxyExistsException;
import net.lightbody.bmp.proxy.ProxyPortsExhaustedException;
import net.lightbody.bmp.proxy.ProxyServer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;

/**
 * Manages proxy creation and lifecycle.
 */
public class ProxyManager {

	private static final Logger logger = LoggerFactory.getLogger(ProxyManager.class);

	private int lastPort;
	private final int minPort;
	private final int maxPort;
	private final ConcurrentMap<Integer, ProxyServer> proxies;

	@Inject
	public ProxyManager(int minPort, int maxPort) {
		this.minPort = minPort;
		this.maxPort = maxPort;
		this.lastPort = maxPort;
		this.proxies = new ConcurrentHashMap<Integer, ProxyServer>();
	}

	/**
	 * Get new {@link ProxyServer} with unique port. Every time method is called a new {@link ProxyServer} instance will
	 * be created with unique port to avoid port conflicts.
	 */
	public ProxyServer getProxyServer() {
		logger.debug("Instantiate ProxyServer...");
		ProxyServer proxy = new ProxyServer() {
			@Override
			public void stop() {
				super.stop();
				proxies.remove(getPort());
				setPort(-1);
			}
		};

		String bindAddr = null;
		if (isNotBlank(bindAddr)) {
			logger.debug("Bind ProxyServer to `{}`...", bindAddr);
			InetAddress inetAddress;
			try {
				inetAddress = InetAddress.getByName(bindAddr);
			} catch (UnknownHostException e) {
				logger.error("Unable to bind proxy to address: " + bindAddr + "; proxy will not be created.", e);

				throw new RuntimeException("Unable to bind proxy to address: ", e);
			}

			proxy.setLocalHost(inetAddress);
		}

		while (proxies.size() <= maxPort - minPort) {
			logger.debug("Use next available port for new ProxyServer...");
			int port = nextPort();
			try {
				return startProxy(proxy, port);
			} catch (ProxyExistsException ex) {
				logger.debug("Proxy already exists at port {}", port);
			}
		}
		throw new ProxyPortsExhaustedException();
	}

	private ProxyServer startProxy(ProxyServer proxy, int port) {
		proxy.setPort(port);
		ProxyServer old = proxies.putIfAbsent(port, proxy);
		if (old != null) {
			logger.info("Proxy already exists at port {}", port);
			throw new ProxyExistsException(port);
		}
		try {
			proxy.start();
			return proxy;
		} catch (Exception ex) {
			proxies.remove(port);
			try {
				proxy.stop();
			} catch (Exception ex2) {
				ex.addSuppressed(ex2);
			}
			throw ex;
		}
	}

	private synchronized int nextPort() {
		return lastPort < maxPort ? ++lastPort : (lastPort = minPort);
	}

}
