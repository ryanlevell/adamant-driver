package com.levell.adamantdriver;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

//TODO: make fully compositional inheritance of WebDriver
public class AdamantDriver {

	private WebDriver driver;

	public WebDriver raw() {
		if(driver == null) {
			driver = new FirefoxDriver();
		}
		return driver;
	}
	
	public boolean isOpen() {
		return driver != null;
	}
	
	public void quit() {
		driver.quit();
	}
}