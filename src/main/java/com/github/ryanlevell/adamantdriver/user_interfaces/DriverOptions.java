package com.github.ryanlevell.adamantdriver.user_interfaces;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriver.Options;

import com.github.ryanlevell.adamantdriver.config.Browser;

/**
 * Allows users to set additional driver options via {@link WebDriver#manage()}.
 * <p>
 * 
 * The class that implements this interface must be set via testng.xml or
 * command line with key <b>OPTIONS_CLASS</b>.
 * 
 * @author ryan
 *
 */
public interface DriverOptions {

	/**
	 * Allows custom options to be set.
	 * 
	 * @param browser
	 *            The browser to set options specific to browser being used.
	 * @param options
	 *            The options object to add additional options to.
	 */
	void getOptions(Browser browser, Options options);
}
