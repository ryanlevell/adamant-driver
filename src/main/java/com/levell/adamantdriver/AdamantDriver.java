package com.levell.adamantdriver;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

public class AdamantDriver {

	WebDriver driver;

	public WebDriver raw() {
		if(driver == null) {
			driver = new FirefoxDriver();
		}
		return driver;
	}
}