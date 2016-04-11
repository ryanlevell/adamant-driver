package com.github.ryanlevell.adamantdriver.config;

import java.net.MalformedURLException;
import java.net.URL;

import org.apache.commons.lang3.NotImplementedException;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.ryanlevell.adamantdriver.capabilties.DriverCapabilities;
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
							+ " property in the testng.xml <suite> tag or from the command line.");
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

	public static URL getGridUrl() {
		String gridUrl = AdamantProperties.getValue(Prop.GRID_URL);
		URL url = null;
		try {
			url = new URL(gridUrl);
		} catch (MalformedURLException e) {
			throw new IllegalArgumentException("Malformed grid URL: " + gridUrl, e);
		}
		return url;
	}

	public static boolean getUseGrid() {
		return Boolean.valueOf(AdamantProperties.getValue(Prop.CAPABILITIES_CLASS));
	}

	public static DesiredCapabilities getCapabilities() {

		// TODO: add tests
		// TODO: throw error on proxy cap - plan on implementing differently
		String className = AdamantProperties.getValue(Prop.CAPABILITIES_CLASS);

		if (className == null) {
			return null;
		}

		Class<?> clazz = null;
		try {
			clazz = AdamantConfig.class.getClassLoader().loadClass(className);
		} catch (ClassNotFoundException e) {
			throw new IllegalArgumentException("Cannot find class [" + className + "]");
		}

		if (!DriverCapabilities.class.isAssignableFrom(clazz)) {
			throw new IllegalArgumentException("Class [" + className + "] must implement DriverCapabilities");
		}

		Object dCaps = null;
		try {
			dCaps = clazz.newInstance();
		} catch (InstantiationException e) {
			throw new IllegalStateException("Class [" + className + "] must implement DriverCapabilities", e);
		} catch (IllegalAccessException e) {
			throw new IllegalStateException("Class [" + className + "] must implement DriverCapabilities", e);
		}

		DesiredCapabilities caps = getCapabilities(getBrowser());
		return ((DriverCapabilities) dCaps).getCapabilties(getBrowser(), caps);
	}

	private static DesiredCapabilities getCapabilities(Browser browser) {
		DesiredCapabilities caps = null;
		switch (browser) {
		case FIREFOX:
			caps = DesiredCapabilities.firefox();
			break;
		case CHROME:
			caps = DesiredCapabilities.chrome();
			break;
		default:
			throw new NotImplementedException("Browser [" + browser + "] needs implemented");
		}
		return caps;
	}
}
