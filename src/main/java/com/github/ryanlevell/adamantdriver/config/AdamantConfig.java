package com.github.ryanlevell.adamantdriver.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.ryanlevell.adamantdriver.config.AdamantProperties.Prop;

/**
 * Configuration for the AdamantDriver features.
 * 
 * @author ryan
 *
 */
public class AdamantConfig {

	private static final Logger LOG = LoggerFactory.getLogger(AdamantConfig.class);

	private static final Browser DEFAULT_BROWSER = Browser.FIREFOX;
	
	/**
	 * Get the chrome driver path property.
	 * 
	 * @return The path.
	 */
	public static String getChromeDriverPath() {
		String path = AdamantProperties.getValue(Prop.CHROME_PATH);
		if (path == null) {
			throw new IllegalStateException(
					"Chromedriver path was not specified. Use the " + Prop.CHROME_PATH.name().toLowerCase()
							+ " property in adamant.properties or from the command line.");
		}
		return path;
	}

	/**
	 * Get the browser(s) property.
	 * 
	 * @return The browser(s). Defaults to {@link #DEFAULT_BROWSER}.
	 */
	public static Browser getBrowser() {

		String browserStr = AdamantProperties.getValue(Prop.BROWSER);
		if (browserStr != null) {
			LOG.info("Using browser [" + browserStr + "]");
			return Browser.valueOf(browserStr.trim().toUpperCase());
		}

		// default
		LOG.info("Using default browser [" + DEFAULT_BROWSER + "]");
		return DEFAULT_BROWSER;
	}
}
