package com.github.ryanlevell.adamantdriver.driver;

import java.net.URL;
import java.util.Arrays;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ITestResult;

import com.github.ryanlevell.adamantdriver.AdamantListener;
import com.github.ryanlevell.adamantdriver.config.AdamantConfig;
import com.github.ryanlevell.adamantdriver.config.Browser;

import net.lightbody.bmp.BrowserMobProxy;

public class DriverHelper {

	private static final Logger LOG = LoggerFactory.getLogger(DriverHelper.class);

	/**
	 * Closes the AdamantDriver via the test parameters.
	 * 
	 * @param result
	 *            The test result object.
	 */
	public static void closeDriver(ITestResult result) {
		WebDriver driver = getDriver(result);
		if (driver != null) {
			LOG.info("Closing WebDriver");
			driver.quit();
		}
	}

	// TODO doesnt work - desiredcaps are lost
	public static void stopProxy(ITestResult result) {
		Object bmp = result.getAttribute(AdamantListener.ATTR_PROXY);
		if (bmp != null) {
			LOG.info("Stopping BrowserMob proxy");
			((BrowserMobProxy) bmp).stop();
		}
	}

	public static void setDriver(ITestResult result, WebDriver driver) {
		if (result.getParameters() != null || result.getParameters().length > 0) {
			Object param1 = result.getParameters()[0];
			if (param1 instanceof WebDriver) {
				result.getParameters()[0] = driver;
			}
		}
	}

	@SuppressWarnings("unchecked")
	public static <T extends WebDriver> T getDriver(ITestResult result) {
		if (result.getParameters() != null && result.getParameters().length > 0) {
			Object param1 = result.getParameters()[0];
			if (param1 instanceof WebDriver) {
				return (T) param1;
			}
		}
		return null;
	}

	/**
	 * Instantiates the {@link WebDriver} object.
	 * 
	 * @param browser
	 * @param gridUrl
	 * @param useGrid
	 * @return
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
}
