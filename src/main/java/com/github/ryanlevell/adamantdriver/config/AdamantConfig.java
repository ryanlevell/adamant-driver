package com.github.ryanlevell.adamantdriver.config;

import java.net.MalformedURLException;
import java.net.URL;

import org.apache.commons.lang3.NotImplementedException;
import org.apache.commons.lang3.tuple.Pair;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.ryanlevell.adamantdriver.config.AdamantProperties.Prop;
import com.github.ryanlevell.adamantdriver.user_interfaces.DriverCapabilities;
import com.github.ryanlevell.adamantdriver.user_interfaces.DriverProxy;

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
	 * Get the chrome driver path property via {@link Prop#CHROME_PATH}.
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
	 * Get the browser property via {@link Prop#BROWSER}.
	 * 
	 * @return The browser. Defaults to {@link #DEFAULT_BROWSER}.
	 */
	public static Browser getBrowser() {

		String browserStr = AdamantProperties.getValue(Prop.BROWSER);
		if (browserStr != null) {
			LOG.debug("Using browser [" + browserStr + "]");
			return Browser.valueOf(browserStr.trim().toUpperCase());
		}

		// default
		LOG.debug("Using default browser [" + DEFAULT_BROWSER + "]");
		return DEFAULT_BROWSER;
	}

	/**
	 * Get the selenium grid URL via {@link Prop#GRID_URL}.
	 * 
	 * @return The {@link URL} object.
	 */
	public static URL getGridUrl() {
		String gridUrl = AdamantProperties.getValue(Prop.GRID_URL);

		// if param not being used
		if (gridUrl == null) {
			return null;
		}

		try {
			return new URL(gridUrl);
		} catch (MalformedURLException e) {
			throw new IllegalArgumentException("Malformed grid URL: " + gridUrl, e);
		}
	}

	/**
	 * If Selenium grid should be used via {@link Prop#USE_GRID}.
	 * 
	 * @return
	 */
	public static boolean getUseGrid() {
		String useGridStr = AdamantProperties.getValue(Prop.USE_GRID);
		if (useGridStr == null) {
			return false;
		}
		return Boolean.valueOf(useGridStr);
	}

	/**
	 * Get default {@link DesiredCapabilities}.<br>
	 * Allows additional custom capabilities by calling
	 * {@link DriverCapabilities} implementation via
	 * {@link Prop#CAPABILITIES_CLASS}.
	 * 
	 * @return
	 */
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

		return caps;
	}

	/**
	 * Initializes and starts {@link BrowserMobProxy}.<br>
	 * The {@link BrowserMobProxy} object can be enhanced with interceptors,
	 * header editing, etc by calling a {@link DriverProxy} implementation via
	 * {@value Prop#PROXY_CLASS}.
	 * 
	 * @return The {@link BrowserMobProxy} and {@link Proxy} tuple.
	 */
	public static Pair<BrowserMobProxy, Proxy> getProxy() {

		LOG.info("Starting BrowserMob proxy");
		BrowserMobProxy server = new BrowserMobProxyServer();
		server.start();
		Proxy proxy = ClientUtil.createSeleniumProxy(server);

		String className = AdamantProperties.getValue(Prop.PROXY_CLASS);

		if (className == null) {
			return Pair.of(server, proxy);
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
		return Pair.of(server, proxy);
	}

	/**
	 * Helper to get default capabilities of browser.
	 * 
	 * @param browser
	 *            The {@link Browser} being used.
	 * @return The {@link DesiredCapabilities} object.
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
