package com.github.ryanlevell.adamantdriver.stubs;

import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.NotImplementedException;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 * Used to temporarily store a {@link WebDriver} object in the data provider.
 * 
 * @author ryan
 *
 */
public class WebDriverStub implements WebDriver {

	/**
	 * Throws {@link NotImplementedException}.<br>
	 * Shouldn't appear, but leave a decent exception message if it does.
	 */
	private static void throwNotImplemented() {
		throw new NotImplementedException(
				"WebDriver was not initialized. Create a new issue in AdamantDriver Github page with code to duplicate this error.");
	}

	public void get(String url) {
		throwNotImplemented();
	}

	public String getCurrentUrl() {
		throwNotImplemented();
		return null;
	}

	public String getTitle() {
		throwNotImplemented();
		return null;
	}

	public List<WebElement> findElements(By by) {
		throwNotImplemented();
		return null;
	}

	public WebElement findElement(By by) {
		throwNotImplemented();
		return null;
	}

	public String getPageSource() {
		throwNotImplemented();
		return null;
	}

	public void close() {
		throwNotImplemented();
	}

	public void quit() {
		throwNotImplemented();
	}

	public Set<String> getWindowHandles() {
		throwNotImplemented();
		return null;
	}

	public String getWindowHandle() {
		throwNotImplemented();
		return null;
	}

	public TargetLocator switchTo() {
		throwNotImplemented();
		return null;
	}

	public Navigation navigate() {
		throwNotImplemented();
		return null;
	}

	public Options manage() {
		throwNotImplemented();
		return null;
	}
}
