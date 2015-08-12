package com.levell.adamantdriver;

import java.util.List;
import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;

/**
 * A wrapper around WebDriver. The only difference is when the driver is opened.
 * Unlike the normal WebDriver implementations, this does not open the browser on
 * initialization. This is needed to prevent tests that have many iterations via a
 * DataProvider from opening all WebDriver browsers for each iteration all at once.
 * 
 * @author ryan
 *
 */
public class AdamantDriver implements WebDriver {

	private WebDriver driver;

	public WebDriver raw() {
		if (driver == null) {
			// TODO: support other drivers
			driver = new FirefoxDriver();
		}
		return driver;
	}

	public boolean isOpen() {
		return driver != null;
	}

	public void quit() {
		raw().quit();
	}

	public void close() {
		raw().close();
	}

	public WebElement findElement(By arg0) {
		return raw().findElement(arg0);
	}

	public List<WebElement> findElements(By arg0) {
		return raw().findElements(arg0);
	}

	public void get(String arg0) {
		raw().get(arg0);
	}

	public String getCurrentUrl() {
		return raw().getCurrentUrl();
	}

	public String getPageSource() {
		return raw().getPageSource();
	}

	public String getTitle() {
		return raw().getTitle();
	}

	public String getWindowHandle() {
		return raw().getWindowHandle();
	}

	public Set<String> getWindowHandles() {
		return raw().getWindowHandles();
	}

	public Options manage() {
		return raw().manage();
	}

	public Navigation navigate() {
		return raw().navigate();
	}

	public TargetLocator switchTo() {
		return raw().switchTo();
	}
}