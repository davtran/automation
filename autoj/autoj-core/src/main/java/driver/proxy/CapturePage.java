package driver.proxy;

import java.util.Date;
import java.util.List;

import com.google.common.collect.ImmutableList;

import net.lightbody.bmp.core.har.HarEntry;
import net.lightbody.bmp.core.har.HarPage;
import net.lightbody.bmp.core.har.HarPageTimings;

/**
 * Represents request/response details for a single page captured by {@link VenusProxy}.
 */
public class CapturePage {

	// TODO: Need to see if we can extract out the page request/response from the harentries and put it at this level
	private final String id;
	private final Date startedDateTime;
	private final HarPageTimings pageTimings;
	private final List<HarEntry> entries;

	public CapturePage(HarPage harPage, List<HarEntry> harEntries) {
		this.id = harPage.getId();
		this.startedDateTime = harPage.getStartedDateTime();
		this.pageTimings = harPage.getPageTimings();
		this.entries = ImmutableList.copyOf(harEntries);
	}

	public String getId() {
		return id;
	}

	public Date getStartedDateTime() {
		return startedDateTime;
	}

	public HarPageTimings getPageTimings() {
		return pageTimings;
	}

	public List<HarEntry> getEntries() {
		return entries;
	}

}
