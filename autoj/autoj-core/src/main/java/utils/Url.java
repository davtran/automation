package utils;

import static org.apache.commons.lang3.StringUtils.replaceOnce;
import static org.apache.commons.lang3.StringUtils.trimToNull;

import java.nio.charset.Charset;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;

public class Url {

	private static final String URL_ENVIRONMENT = trimToNull(ConfigurationProperties.getUrlEnvironment(null));
	private static final String URL_PORT = trimToNull(ConfigurationProperties.getUrlPort(null));

	private final String url;
	private final String query;
	private ListMultimap<String, String> parameters;

	public Url(String url) {
		this(url, URL_ENVIRONMENT, URL_PORT);
	}

	Url(String url, String environment, String port) {
		int indexQuestion = url.indexOf('?');
		String urlWithoutQueryParams = indexQuestion != -1 ? url.substring(0, indexQuestion) : url;

		String urlReplacement = getEnvironmentAppend(environment, urlWithoutQueryParams) + ".about.com"
				+ getPortAppend(port, urlWithoutQueryParams);
		this.url = replaceOnce(urlWithoutQueryParams, ".about.com", urlReplacement);

		if (indexQuestion == -1) {
			this.query = null;
		} else {
			this.query = url.substring(indexQuestion + 1);
		}
	}

	private static String getEnvironmentAppend(String environment, String url) {
		if (StringUtils.isEmpty(environment) || url.contains(environment)) return "";
		return "." + environment;
	}

	private static String getPortAppend(String port, String url) {
		if (StringUtils.isEmpty(port) || "80".equals(port) || url.contains(":"+port)) return "";
		return ":" + port;
	}

	public ListMultimap<String, String> getParameters() {
		if (parameters != null) return parameters;

		ListMultimap<String, String> map = ArrayListMultimap.create();
		List<NameValuePair> params = URLEncodedUtils.parse(query, Charset.forName("UTF-8"));
		for (NameValuePair param : params) {
			map.put(param.getName(), param.getValue());
		}

		return parameters = map;
	}

	@Override
	public String toString() {
		return url + (query == null ? "" : "?" + query);
	}
}
