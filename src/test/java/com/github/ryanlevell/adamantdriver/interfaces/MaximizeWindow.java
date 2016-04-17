package com.github.ryanlevell.adamantdriver.interfaces;

import org.openqa.selenium.WebDriver.Options;

import com.github.ryanlevell.adamantdriver.config.Browser;
import com.github.ryanlevell.adamantdriver.user_interfaces.DriverOptions;

public class MaximizeWindow implements DriverOptions {

	public void getOptions(Browser browser, Options options) {
		options.window().maximize();
	}
}
