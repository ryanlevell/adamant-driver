package com.github.ryanlevell.adamantdriver.driver;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;

import org.apache.commons.lang3.tuple.Pair;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.Augmenter;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ITestResult;

import com.github.ryanlevell.adamantdriver.AdamantListener;
import com.github.ryanlevell.adamantdriver.config.AdamantConfig;
import com.github.ryanlevell.adamantdriver.config.Browser;
import com.github.ryanlevell.adamantdriver.stubs.WebDriverStub;

import net.lightbody.bmp.BrowserMobProxy;

/**
 * Class for methods to help with {@link WebDriver} actions.
 * 
 * @author ryan
 *
 */
public class DriverHelper {

	private static final Logger LOG = LoggerFactory.getLogger(DriverHelper.class);

	/**
	 * Closes the {@link WebDriver} via the test parameters.
	 * 
	 * @param result
	 *            The test result object.
	 */
	public static void closeDriver(ITestResult result) {
		WebDriver driver = getDriverFromTestParams(result);

		// if stub here, error happened on initialization - dont try to close it
		// was somewhat hide real error
		if (driver != null && !(driver instanceof WebDriverStub)) {
			LOG.info("Closing WebDriver");
			driver.quit();
		}
	}

	/**
	 * Stops the {@link BrowserMobProxy} via the test attributes.
	 * 
	 * @param result
	 *            The test result object.
	 */
	public static void stopProxy(ITestResult result) {
		Object bmp = result.getAttribute(AdamantListener.ATTR_PROXY);
		if (bmp != null) {
			LOG.info("Stopping BrowserMob proxy");
			((BrowserMobProxy) bmp).stop();
		}
	}

	/**
	 * Set the real WebDriver object to the first test parameter.
	 * 
	 * @param result
	 *            The test result object.
	 * @param driver
	 *            The real WebDriver.
	 */
	public static void setDriverTestParam(ITestResult result, WebDriver driver) {
		if (result.getParameters() != null || result.getParameters().length > 0) {
			Object param1 = result.getParameters()[0];
			if (param1 instanceof WebDriver) {
				result.getParameters()[0] = driver;
			}
		}
	}

	/**
	 * Set the real BrowserMobProxy object to the second test parameter.
	 * 
	 * @param result
	 *            The test result object.
	 * @param pair
	 *            The BrowserMobProxy object and the Selenium Proxy object.
	 * @param caps
	 *            The current capabilties.
	 */
	public static void setProxyTestParam(ITestResult result, Pair<BrowserMobProxy, Proxy> pair,
			DesiredCapabilities caps) {
		if (result.getParameters() != null || result.getParameters().length > 1) {
			Object param1 = result.getParameters()[1];
			if (param1 instanceof BrowserMobProxy) {
				result.getParameters()[1] = pair.getLeft();
				caps.setCapability(CapabilityType.PROXY, pair.getRight());
				result.setAttribute(AdamantListener.ATTR_PROXY, pair.getLeft());
			}
		}
	}

	/**
	 * Get the current driver object stored in the first test parameter.
	 * 
	 * @param <T>
	 *            A current implementation class of the WebDriver object.
	 * @param result
	 *            The current test result object.
	 * @return The current WebDriver object in the test parameters.
	 */
	@SuppressWarnings("unchecked")
	public static <T extends WebDriver> T getDriverFromTestParams(ITestResult result) {
		if (result.getParameters() != null && result.getParameters().length > 0) {
			Object param1 = result.getParameters()[0];
			if (param1 instanceof WebDriver) {
				return (T) param1;
			}
		}
		return null;
	}

	/**
	 * Get the current proxy object stored in the second test parameter.
	 * 
	 * @param <T>
	 *            A current implementation class of the BrowserMobProxy object.
	 * @param result
	 *            The current test result object.
	 * @return The current BrowserMobProxy object in the test parameters.
	 */
	@SuppressWarnings("unchecked")
	public static <T extends BrowserMobProxy> T getProxyFromTestParams(ITestResult result) {
		if (result.getParameters() != null && result.getParameters().length > 1) {
			Object param2 = result.getParameters()[1];
			if (param2 instanceof BrowserMobProxy) {
				return (T) param2;
			}
		}
		return null;
	}

	/**
	 * Instantiates the {@link WebDriver} object.
	 * 
	 * @param browser
	 *            The browser being used.
	 * @param gridUrl
	 *            The grid URL.
	 * @param useGrid
	 *            Whether to use the grid or not.
	 * @param caps
	 *            The capabilities for to use with the driver, or null.
	 * @return The WebDriver object.
	 */
	public static WebDriver createDriver(Browser browser, URL gridUrl, boolean useGrid, DesiredCapabilities caps) {

		WebDriver driver = null;

		if (useGrid) {
			driver = new RemoteWebDriver(gridUrl, caps);
		} else {
			switch (browser) {
			case FIREFOX:
				driver = caps == null ? new FirefoxDriver() : new FirefoxDriver(caps);
				break;
			case CHROME:
				System.setProperty("webdriver.chrome.driver", AdamantConfig.getChromeDriverPath());
				driver = caps == null ? new ChromeDriver() : new ChromeDriver(caps);
				break;
			default:
				throw new IllegalStateException("[" + browser + "] is not a supported browser, use one of "
						+ Arrays.toString(Browser.values()));
			}
		}
		return driver;
	}

	/**
	 * Take a screenshot.
	 * 
	 * @param driver
	 *            The WebDriver.
	 * @param path
	 *            The path to save the screenshot to.
	 * @param testNumber
	 *            The test number for identifying screenshot name.
	 */
	public static void takeScreenshot(WebDriver driver, String path, String name) {
		WebDriver augmentedDriver = driver;
		if (!(augmentedDriver instanceof TakesScreenshot)) {
			augmentedDriver = new Augmenter().augment(driver);
		}

		byte[] screenshotBytes = ((TakesScreenshot) augmentedDriver).getScreenshotAs(OutputType.BYTES);
		Path screenshotPath = Paths.get(path);
		Path screenshotName = screenshotPath.resolve(System.currentTimeMillis() + "_" + name + ".png");

		// create parent directories if needed
		if (!Files.exists(screenshotPath)) {
			try {
				Files.createDirectories(screenshotPath);
			} catch (IOException e) {
				throw new IllegalArgumentException("Could not create path [" + path + "]", e);
			}
		}

		try {
			Files.write(screenshotName, screenshotBytes, StandardOpenOption.CREATE);
		} catch (IOException e) {
			throw new IllegalArgumentException("Error saving to screenshot path [" + path + "]", e);
		}
	}
}
