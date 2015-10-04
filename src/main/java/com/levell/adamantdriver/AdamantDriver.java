package com.levell.adamantdriver;

import java.util.List;
import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

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
class AdamantDriver implements WebDriver {

	private WebDriver driver;
	private Browser browser;

	public AdamantDriver(String browser) {
		this.browser = Browser.valueOf(browser.toUpperCase());
	}

	/**
	 * Gets the original {@link WebDriver}.
	 * @return The WebDriver object.
	 */
	public WebDriver raw() {
		if (driver == null) {
			switch (browser) {
			case FIREFOX:
				driver = new FirefoxDriver();
				break;
			case CHROME:
				driver = new ChromeDriver();
				break;
			default:
				throw new IllegalStateException("[" + browser + "] is not a browser");
			}
		}
		return driver;
	}

	/**
	 * Determines if the driver has been "opened" or initialized.
	 * @return Whether the driver has been opened yet.
	 */
	public boolean isOpen() {
		return driver != null;
	}

	/**
	 * @see WebDriver#quit().
	 */
	public void quit() {
		raw().quit();
	}

	/**
	 * @see WebDriver#close().
	 */
	public void close() {
		raw().close();
	}

	/**
	 * @see WebDriver#findElement(By).
	 */
	public WebElement findElement(By arg0) {
		return raw().findElement(arg0);
	}

	/**
	 * @see WebDriver#findElements(By).
	 */
	public List<WebElement> findElements(By arg0) {
		return raw().findElements(arg0);
	}

	/**
	 * @see WebDriver#get(String).
	 */
	public void get(String arg0) {
		raw().get(arg0);
	}

	public String getCurrentUrl() {
		return raw().getCurrentUrl();
	}

	/**
	 * @see WebDriver#getPageSource().
	 */
	public String getPageSource() {
		return raw().getPageSource();
	}

	/**
	 * @see WebDriver#getTitle().
	 */
	public String getTitle() {
		return raw().getTitle();
	}

	/**
	 * @see WebDriver#getWindowHandle().
	 */
	public String getWindowHandle() {
		return raw().getWindowHandle();
	}

	/**
	 * @see WebDriver#getWindowHandles().
	 */
	public Set<String> getWindowHandles() {
		return raw().getWindowHandles();
	}

	/**
	 * @see WebDriver#manage().
	 */
	public Options manage() {
		return raw().manage();
	}

	/**
	 * @see WebDriver#navigate().
	 */
	public Navigation navigate() {
		return raw().navigate();
	}

	/**
	 * @see WebDriver#switchTo().
	 */
	public TargetLocator switchTo() {
		return raw().switchTo();
	}
}