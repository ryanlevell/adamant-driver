package com.levell.adamantdriver;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import org.openqa.selenium.Capabilities;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Configuration for the AdamantDriver features.
 * 
 * @author ryan
 *
 */
class AdamantConfig {

	private static final Properties PROPS = new Properties();
	private static final Logger LOG = LoggerFactory.getLogger(AdamantConfig.class);
	private static final String PROPS_FILE_NAME = "adamant.properties";

	/**
	 * Optional properties file. Looks by default in root classpath. Use
	 * adamantProps property to change path.
	 */
	private static final String PROPS_FILE = System.getProperty("adamantProps", PROPS_FILE_NAME);
	private static final String DEFAULT_BROWSERS = "firefox";

	/**
	 * Get the single instance of the Properties.
	 * 
	 * @return The Properties object.
	 */
	private static Properties props() {

		if (PROPS.isEmpty()) {
			InputStream inputStream = AdamantConfig.class.getClassLoader().getResourceAsStream(PROPS_FILE);
			// Thread.currentThread().getContextClassLoader().getResourceAsStream("adamant.properties");
			if (inputStream == null) {
				LOG.info("No properties file - will use defaults");
			} else {
				try {
					PROPS.load(inputStream);
				} catch (IOException e) {
					throw new IllegalStateException(e);
				}
			}
		}
		return PROPS;
	}

	/**
	 * Get a config value. It checks system properties first, then the
	 * properties file.
	 * 
	 * @param p
	 *            The property to find.
	 * @return The property value or the default or null.
	 */
	private static String getValue(Prop p) {
		// sys prop overrides prop file
		String value = System.getProperty(p.name().toLowerCase());
		if (value == null) {
			value = (String) props().get(p.name().toLowerCase());
		}
		return value;
	}

	/**
	 * Get the chrome driver path property.
	 * 
	 * @return The path. Defaults to {@link #DEFAULT_CHROME_PATH}.
	 */
	public static String getChromeDriverPath() {
		String path = getValue(Prop.CHROME_PATH);
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
	 * @return The browser(s). Defaults to {@link #DEFAULT_BROWSERS}.
	 */
	public static List<String> getBrowsers() {

		String browserStr = getValue(Prop.BROWSERS);

		// default firefox (easy to use - no props needed)
		if (browserStr == null) {
			LOG.info("Using default browser [" + DEFAULT_BROWSERS + "]");
			browserStr = DEFAULT_BROWSERS;
		}
		return Arrays.asList(browserStr.split("\\s*,\\s"));
	}

	public static DesiredCapabilities getCaps(Browser b) {
		DesiredCapabilities caps = null;
		switch (b) {
		case CHROME:
			caps = DesiredCapabilities.chrome();
			break;
		case FIREFOX:
			caps = DesiredCapabilities.firefox();
			break;
		default:
			break;
		}

		Map<String, Object> capMap = getCapMap(b);
		System.out.println(capMap);
		for (Map.Entry<String, Object> entry : capMap.entrySet()) {
			caps.setCapability(entry.getKey(), entry.getValue());
		}

		return caps;
	}

	private static Map<String, Object> getCapMap(Browser b) {
		Map<String, Object> forBrowser = new HashMap<String, Object>();
		String find = b.name().toLowerCase() + ".cap";
		for (Entry<Object, Object> e : props().entrySet()) {
			if (e.getKey().toString().startsWith(find)) {

				// remove "browser.cap." piece of String
				String key = e.getKey().toString().replace(find + ".", "");
				forBrowser.put(key, e.getValue());
			}
		}
		return forBrowser;
	}

	/**
	 * Supported properties.
	 * 
	 * @author ryan
	 *
	 */
	private enum Prop {
		BROWSERS, CHROME_PATH
	}
}
