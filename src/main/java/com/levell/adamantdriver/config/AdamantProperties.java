package com.levell.adamantdriver.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class to obtain properties.
 * 
 * @author ryan
 *
 */
class AdamantProperties {

	private static final Logger LOG = LoggerFactory.getLogger(AdamantProperties.class);

	/**
	 * Optional properties file. Looks by default in root classpath. Use
	 * adamantProps property to change path.
	 */
	private static final String PROPS_FILE_NAME = "adamant.properties";
	private static final String PROPS_FILE = System.getProperty("adamantProps", PROPS_FILE_NAME);
	private static final Properties PROPS = new Properties();
	
	/**
	 * Get the single instance of the Properties.
	 * 
	 * @return The Properties object.
	 */
	static Properties properties() {

		if (PROPS.isEmpty()) {
			InputStream inputStream = AdamantProperties.class.getClassLoader().getResourceAsStream(PROPS_FILE);
			if (inputStream == null) {
				LOG.info("No properties file - will use defaults");
			} else {
				try {
					PROPS.load(inputStream);
				} catch (IOException e) {
					throw new IllegalStateException("Error loading properties file", e);
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
	static String getValue(Prop p) {
		// sys prop overrides prop file
		String value = System.getProperty(p.name().toLowerCase());
		if (value == null) {
			value = (String) properties().get(p.name().toLowerCase());
		}
		return value;
	}

	/**
	 * Supported properties.
	 * 
	 * @author ryan
	 *
	 */
	enum Prop {
		BROWSER, CHROME_PATH
	}
}
