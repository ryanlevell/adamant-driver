package com.levell.wdfw;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

public class WdWrapper {

	WebDriver driver;

	public WebDriver raw() {
		if(driver == null) {
			driver = new FirefoxDriver();
		}
		return driver;
	}
}
