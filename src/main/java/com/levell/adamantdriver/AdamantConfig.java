package com.levell.adamantdriver;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

class AdamantConfig {

	private static final Properties PROPS = new Properties();

	private static Properties props() {
		String propFileName = "adamant.properties";

		InputStream inputStream = AdamantConfig.class.getClassLoader().getResourceAsStream(propFileName);

		if (PROPS.isEmpty()) {
			try {
				PROPS.load(inputStream);
			} catch (IOException e) {
				throw new IllegalStateException("property file '" + propFileName + "' not found in the classpath");
			}
		}
		return PROPS;
	}

	static String getValue(Prop p) {
		return (String) props().get(p.name().toLowerCase());
	}
	
	public enum Prop {
		BROWSERS,
		CHROME_PATH
	}
}
