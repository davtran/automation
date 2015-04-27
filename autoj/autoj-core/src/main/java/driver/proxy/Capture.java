package driver.proxy;

import java.util.Collection;
import java.util.Map;

import net.lightbody.bmp.core.har.Har;
import net.lightbody.bmp.core.har.HarEntry;
import net.lightbody.bmp.core.har.HarLog;
import net.lightbody.bmp.core.har.HarPage;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ImmutableMap;

/**
 * Represents request/response details of all pages captured by {@link VenusProxy}.
 */
public class Capture {

	private final Map<String, CapturePage> pages;

	public Capture(Har har) {

		HarLog harLog = har.getLog();

		ArrayListMultimap<String, HarEntry> entriesByPage = ArrayListMultimap.create();
		for (HarEntry harEntry : harLog.getEntries()) {
			entriesByPage.put(harEntry.getPageref(), harEntry);
		}

		ImmutableMap.Builder<String, CapturePage> builder = ImmutableMap.builder();
		for (HarPage harPage : harLog.getPages()) {
			builder.put(harPage.getId(), new CapturePage(harPage, entriesByPage.get(harPage.getId())));
		}

		this.pages = builder.build();
	}

	public CapturePage getPage(String id) {
		return pages.get(id);
	}
	
	public Collection<CapturePage> getPages() {
		return pages.values();
	}
	
}
