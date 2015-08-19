package com.levell.adamantdriver;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class AdamantConfig {

	private static final Properties PROPS = new Properties();
	private static final Logger LOG = LoggerFactory.getLogger(AdamantConfig.class);
	private static final String PROPS_FILE = "adamant.properties";
	private static final String DEFAULT_BROWSERS = "firefox";

	// TODO change depending on user & OS
	private static final String DEFAULT_CHROME_PATH = "/Users/ryan/drivers/chromedriver";

	private static Properties props() {

		if (PROPS.isEmpty()) {
			InputStream inputStream = AdamantConfig.class.getClassLoader().getResourceAsStream(PROPS_FILE);
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

	private static String getValue(Prop p) {
		// sys prop overrides prop file
		String value = System.getProperty(p.name().toLowerCase());
		if (value == null) {
			value = (String) props().get(p.name().toLowerCase());
		}
		if (value == null) {
			// TODO: get defaults here
		}
		return value;
	}

	public static String getChromeDriverPath() {
		String path = getValue(Prop.CHROME_PATH);
		if (path == null) {
			path = DEFAULT_CHROME_PATH;
		}
		return path;
	}

	public static List<String> getBrowsers() {

		String browserStr = getValue(Prop.BROWSERS);

		// default firefox (easy to use - no props needed)
		if (browserStr == null) {
			LOG.info("Using default browser [firefox]");
			browserStr = DEFAULT_BROWSERS;
		}
		return Arrays.asList(browserStr.split("\\s*,\\s"));
	}

	private enum Prop {
		BROWSERS, CHROME_PATH
	}
}
