package com.github.ryanlevell.adamantdriver.interfaces;

import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver.Options;

import com.github.ryanlevell.adamantdriver.config.Browser;
import com.github.ryanlevell.adamantdriver.user_interfaces.DriverOptions;

public class TestOptions implements DriverOptions {
	public void getOptions(Browser browser, Options options) {
		options.window().setSize(new Dimension(453, 301));
	}
}
