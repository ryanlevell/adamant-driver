package com.github.ryanlevell.adamantdriver.driver;

import java.net.URL;
import java.util.List;
import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import com.github.ryanlevell.adamantdriver.config.AdamantConfig;
import com.github.ryanlevell.adamantdriver.config.Browser;

/**
 * A wrapper around WebDriver. The only difference is when the driver is opened.
 * Unlike the normal WebDriver implementations, this does not open the browser
 * on initialization. This is needed to prevent tests that have many iterations
 * via a DataProvider from opening all WebDriver browsers for each iteration all
 * at once.
 * 
 * @author ryan
 *
 */
public class AdamantDriver implements WebDriver {

	private WebDriver driver;
	private final Browser browser;
	private final DesiredCapabilities caps;
	private final boolean useGrid;
	private final URL gridUrl;

	public AdamantDriver(Browser browser, DesiredCapabilities caps, URL gridUrl, boolean useGrid) {
		this.browser = browser;
		this.caps = caps;
		this.useGrid = useGrid;
		this.gridUrl = gridUrl;
	}

	/**
	 * Gets the original {@link WebDriver}.
	 * 
	 * @return The WebDriver object.
	 */
	public WebDriver raw() {
		if (driver == null) {
			driver = createDriver(browser, caps, gridUrl, useGrid);
		}
		return driver;
	}

	/**
	 * Determines if the driver has been "opened" or initialized.
	 * 
	 * @return Whether the driver has been opened yet.
	 */
	public boolean isOpen() {
		return driver != null;
	}

	/**
	 * {@inheritDoc}
	 */
	public void quit() {
		raw().quit();
	}

	/**
	 * {@inheritDoc}
	 */
	public void close() {
		raw().close();
	}

	/**
	 * {@inheritDoc}
	 */
	public WebElement findElement(By arg0) {
		return raw().findElement(arg0);
	}

	/**
	 * {@inheritDoc}
	 */
	public List<WebElement> findElements(By arg0) {
		return raw().findElements(arg0);
	}

	/**
	 * {@inheritDoc}
	 */
	public void get(String arg0) {
		raw().get(arg0);
	}

	public String getCurrentUrl() {
		return raw().getCurrentUrl();
	}

	/**
	 * {@inheritDoc}
	 */
	public String getPageSource() {
		return raw().getPageSource();
	}

	/**
	 * {@inheritDoc}
	 */
	public String getTitle() {
		return raw().getTitle();
	}

	/**
	 * {@inheritDoc}
	 */
	public String getWindowHandle() {
		return raw().getWindowHandle();
	}

	/**
	 * {@inheritDoc}
	 */
	public Set<String> getWindowHandles() {
		return raw().getWindowHandles();
	}

	/**
	 * {@inheritDoc}
	 */
	public Options manage() {
		return raw().manage();
	}

	/**
	 * {@inheritDoc}
	 */
	public Navigation navigate() {
		return raw().navigate();
	}

	/**
	 * {@inheritDoc}
	 */
	public TargetLocator switchTo() {
		return raw().switchTo();
	}

	/**
	 * {@inheritDoc}
	 */
	public String toString() {
		return this.getClass().getSimpleName() + "{browser=" + browser + "}";
	}

	private WebDriver createDriver(Browser browser, DesiredCapabilities caps, URL gridUrl, boolean useGrid) {
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
				throw new IllegalStateException("[" + browser + "] is not a supported browser");
			}
		}
		return driver;
	}
}