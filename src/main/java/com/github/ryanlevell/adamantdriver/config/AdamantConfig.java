package com.github.ryanlevell.adamantdriver.config;

import java.net.MalformedURLException;
import java.net.URL;

import org.apache.commons.lang3.NotImplementedException;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.ryanlevell.adamantdriver.capabilties.DriverCapabilities;
import com.github.ryanlevell.adamantdriver.capabilties.DriverProxy;
import com.github.ryanlevell.adamantdriver.config.AdamantProperties.Prop;

import net.lightbody.bmp.BrowserMobProxy;
import net.lightbody.bmp.BrowserMobProxyServer;
import net.lightbody.bmp.client.ClientUtil;

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
		return Boolean.valueOf(AdamantProperties.getValue(Prop.USE_GRID));
	}

	public static DesiredCapabilities getCapabilities() {

		// TODO: add tests
		DesiredCapabilities caps = getCapabilities(getBrowser());
		String className = AdamantProperties.getValue(Prop.CAPABILITIES_CLASS);

		// default caps
		if (className == null) {
			return caps;
		}

		// additional custom caps
		Class<?> clazz = null;
		try {
			clazz = AdamantConfig.class.getClassLoader().loadClass(className);
		} catch (ClassNotFoundException e) {
			throw new IllegalArgumentException("Cannot find class [" + className + "]");
		}

		if (!DriverCapabilities.class.isAssignableFrom(clazz)) {
			throw new IllegalArgumentException("Class [" + className + "] must implement DriverCapabilities");
		}

		Object customCaps = null;
		try {
			customCaps = clazz.newInstance();
		} catch (InstantiationException e) {
			throw new IllegalStateException("Class [" + className + "] must implement DriverCapabilities", e);
		} catch (IllegalAccessException e) {
			throw new IllegalStateException("Class [" + className + "] must implement DriverCapabilities", e);
		}

		caps = ((DriverCapabilities) customCaps).getCapabilties(getBrowser(), caps);

		// use xml/CLI for proxy
		if (caps.getCapability(CapabilityType.PROXY) != null) {
			throw new IllegalStateException(
					"Found proxy capability. To use a proxy, set testng.xml or command line parameter 'use_proxy' to true and optionally set 'proxy_class' to implentation of DriverProxy");
		}

		// get proxy info from xml/CLI
		if (getUseProxy()) {
			caps.setCapability(CapabilityType.PROXY, getProxy());
		}

		return caps;
	}

	private static boolean getUseProxy() {
		return Boolean.valueOf(AdamantProperties.getValue(Prop.USE_PROXY));
	}

	private static Proxy getProxy() {

		// TODO: need to shutdown server after test in test listener
		// TODO: add optional proxy object injection to test methods
		BrowserMobProxy server = new BrowserMobProxyServer();
		server.start();
		Proxy proxy = ClientUtil.createSeleniumProxy(server);

		String className = AdamantProperties.getValue(Prop.PROXY_CLASS);

		if (className == null) {
			return proxy;
		}

		Class<?> clazz = null;
		try {
			clazz = AdamantConfig.class.getClassLoader().loadClass(className);
		} catch (ClassNotFoundException e) {
			throw new IllegalArgumentException("Cannot find class [" + className + "]");
		}

		if (!DriverProxy.class.isAssignableFrom(clazz)) {
			throw new IllegalArgumentException("Class [" + className + "] must implement DriverProxy");
		}

		Object customCaps = null;
		try {
			customCaps = clazz.newInstance();
		} catch (InstantiationException e) {
			throw new IllegalStateException("Class [" + className + "] must implement DriverProxy", e);
		} catch (IllegalAccessException e) {
			throw new IllegalStateException("Class [" + className + "] must implement DriverProxy", e);
		}

		((DriverProxy) customCaps).getProxy(server);
		return proxy;
	}

	/**
	 * Helper to get default capabilities of browser.
	 * 
	 * @param browser
	 * @return
	 */
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
