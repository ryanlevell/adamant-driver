package com.github.ryanlevell.adamantdriver.driver;

import java.net.URL;
import java.util.Arrays;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.ITestResult;

import com.github.ryanlevell.adamantdriver.config.AdamantConfig;
import com.github.ryanlevell.adamantdriver.config.Browser;

public class DriverHelper {

	/**
	 * Closes the AdamantDriver via the test parameters.
	 * 
	 * @param result
	 *            The test result object.
	 */
	public static void closeWebDriver(ITestResult result) {
		if (result.getParameters() == null || result.getParameters().length == 0) {
			return;
		}
		Object param1 = result.getParameters()[0];
		if (param1 instanceof AdamantDriver) {
			AdamantDriver driver = (AdamantDriver) param1;
			if (driver.isOpen()) {
				driver.quit();
			}
		}
	}

	/**
	 * Instantiates the {@link WebDriver} object.
	 * 
	 * @param browser
	 * @param gridUrl
	 * @param useGrid
	 * @return
	 */
	static WebDriver createDriver(Browser browser, URL gridUrl, boolean useGrid) {

		DesiredCapabilities caps = AdamantConfig.getCapabilities();
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
