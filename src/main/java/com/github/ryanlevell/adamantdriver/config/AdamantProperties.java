package com.github.ryanlevell.adamantdriver.config;

import java.util.Map;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.ryanlevell.adamantdriver.driver.AdamantDriver;

/**
 * Class to store properties.
 * 
 * @author ryan
 *
 */
public class AdamantProperties {

	private static final Logger LOG = LoggerFactory.getLogger(AdamantProperties.class);

	private static final Properties PROPS = new Properties();

	private AdamantProperties() {
		// static only
	}

	/**
	 * Set properties for {@link AdamantDriver}.
	 * 
	 * @param params
	 */
	public static void setProperties(Map<String, String> params) {
		// xml parameters
		for (Prop prop : Prop.values()) {
			for (Map.Entry<String, String> param : params.entrySet()) {
				if (prop.name().toLowerCase().equals(param.getKey())) {
					LOG.debug("Adding XML param: [" + prop.name() + "=" + param.getValue() + "]");
					PROPS.setProperty(prop.name(), param.getValue());
					break;
				}
			}
		}

		// cli parameters override xml
		for (Prop prop : Prop.values()) {
			String param = System.getProperty(prop.name().toLowerCase());
			if (param != null) {
				LOG.debug("Overwrite XML param: [" + prop.name() + "=" + param + "]");
				PROPS.setProperty(prop.name(), param);
			}
		}
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
		return (String) PROPS.get(p.name());
	}

	/**
	 * Supported properties.
	 * 
	 * @author ryan
	 *
	 */
	enum Prop {
		BROWSER, CHROME_PATH, CAPABILITIES_CLASS, GRID_URL, USE_GRID, PROXY_CLASS
	}
}
