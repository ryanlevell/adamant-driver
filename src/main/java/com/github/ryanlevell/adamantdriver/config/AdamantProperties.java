package com.github.ryanlevell.adamantdriver.config;

import java.util.Map;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class to store properties.
 * 
 * @author ryan
 *
 */
public class AdamantProperties {

	private static final Logger LOG = LoggerFactory.getLogger(AdamantProperties.class);
	private static final Properties PROPS = new Properties();

	/**
	 * Private constructor - static access only.
	 */
	private AdamantProperties() {
	}

	/**
	 * Set properties for AdamantDriver configuration.<br>
	 * It will check testng.xml for parameters first. It will then check command
	 * line parameters and overwrite any xml parameters.
	 * 
	 * @param params
	 *            {@link Map} of parameters.
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
	 * Get a config value. It checks command line parameters first, then the
	 * testng.xml.
	 * 
	 * @param p
	 *            The {@link Prop} to find.
	 * @return The parameter value or the default or null.
	 */
	static String getValue(Prop p) {
		return (String) PROPS.get(p.name());
	}

	/**
	 * Supported properties.
	 */
	enum Prop {
		BROWSER, CHROME_PATH, CAPABILITIES_CLASS, GRID_URL, USE_GRID, PROXY_CLASS, OPTIONS_CLASS, TAKE_SCREENSHOT, SCREENSHOT_PATH, RETRY_LIMIT, DELETE_RETRIED_RESULTS
	}
}
