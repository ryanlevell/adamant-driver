package com.github.ryanlevell.adamantdriver.config;

import java.util.List;
import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class WebDriverStub implements WebDriver {

	public void get(String url) {
	}

	public String getCurrentUrl() {
		return null;
	}

	public String getTitle() {
		return null;
	}

	public List<WebElement> findElements(By by) {
		return null;
	}

	public WebElement findElement(By by) {
		return null;
	}

	public String getPageSource() {
		return null;
	}

	public void close() {
	}

	public void quit() {
	}

	public Set<String> getWindowHandles() {
		return null;
	}

	public String getWindowHandle() {
		return null;
	}

	public TargetLocator switchTo() {
		return null;
	}

	public Navigation navigate() {
		return null;
	}

	public Options manage() {
		return null;
	}
}
