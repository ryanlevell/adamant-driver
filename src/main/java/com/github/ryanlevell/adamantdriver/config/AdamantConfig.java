package com.github.ryanlevell.adamantdriver.config;

import java.net.MalformedURLException;
import java.net.URL;

import org.apache.commons.lang3.NotImplementedException;
import org.apache.commons.lang3.tuple.Pair;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.ryanlevell.adamantdriver.config.AdamantProperties.Prop;
import com.github.ryanlevell.adamantdriver.user_interfaces.DriverCapabilities;
import com.github.ryanlevell.adamantdriver.user_interfaces.DriverOptions;
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
	 * @return True if the grid should be used for tests.
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
	 * @return The capabilities with possibly added user provided capabilities.
	 */
	public static DesiredCapabilities getCapabilities() {

		DesiredCapabilities caps = getCapabilities(getBrowser());
		String className = AdamantProperties.getValue(Prop.CAPABILITIES_CLASS);

		// default caps
		if (className == null) {
			return caps;
		}

		// additional custom caps
		DriverCapabilities driverCapabilities = newInstance(className, DriverCapabilities.class);
		driverCapabilities.getCapabilties(getBrowser(), caps);

		return caps;
	}

	/**
	 * Get default {@link DesiredCapabilities}.<br>
	 * Allows additional custom capabilities by calling
	 * {@link DriverCapabilities} implementation via
	 * {@link Prop#CAPABILITIES_CLASS}.
	 * 
	 * @param driver
	 *            The WebDriver object.
	 */
	public static void getOptions(WebDriver driver) {

		String className = AdamantProperties.getValue(Prop.OPTIONS_CLASS);

		// do nothing
		if (className == null) {
			return;
		}

		// additional custom caps
		DriverOptions driverOptions = newInstance(className, DriverOptions.class);
		driverOptions.getOptions(getBrowser(), driver.manage());
	}

	/**
	 * Initializes and starts {@link BrowserMobProxy}.<br>
	 * The {@link BrowserMobProxy} object can be enhanced with interceptors,
	 * header editing, etc by calling a {@link DriverProxy} implementation via
	 * {@link Prop#PROXY_CLASS}.
	 * 
	 * @return The {@link BrowserMobProxy} and {@link Proxy} tuple.
	 */
	public static Pair<BrowserMobProxy, Proxy> getProxy() {

		LOG.info("Starting BrowserMob proxy");
		BrowserMobProxy server = new BrowserMobProxyServer();
		server.start();
		Proxy proxy = ClientUtil.createSeleniumProxy(server);

		String className = AdamantProperties.getValue(Prop.PROXY_CLASS);

		// return vanilla proxy
		if (className == null) {
			return Pair.of(server, proxy);
		}

		// additional proxy config
		DriverProxy driverProxy = newInstance(className, DriverProxy.class);
		driverProxy.getProxy(server);
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

	/**
	 * Construct an object and set the type as the interface.
	 * 
	 * @param className
	 *            The class name to instantiate.
	 * @param classInterface
	 *            The interface for the object's data type.
	 * @return The new object.
	 */
	private static <T> T newInstance(String className, Class<T> classInterface) {
		Class<?> clazz = null;
		try {
			clazz = AdamantConfig.class.getClassLoader().loadClass(className);
		} catch (ClassNotFoundException e) {
			throw new IllegalArgumentException("Cannot find class [" + className + "]");
		}

		if (!classInterface.isAssignableFrom(clazz)) {
			throw new IllegalArgumentException("Class [" + className + "] must implement " + classInterface);
		}

		Object customClass = null;
		try {
			customClass = clazz.newInstance();
		} catch (InstantiationException e) {
			throw new IllegalStateException("Class [" + customClass + "] could not be instantiated", e);
		} catch (IllegalAccessException e) {
			throw new IllegalStateException("Class [" + customClass + "] or constrcutor could not be accessed", e);
		}

		return classInterface.cast(customClass);
	}
}
