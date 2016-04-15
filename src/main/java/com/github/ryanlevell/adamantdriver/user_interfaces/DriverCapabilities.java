package com.github.ryanlevell.adamantdriver.user_interfaces;

import org.openqa.selenium.remote.DesiredCapabilities;

import com.github.ryanlevell.adamantdriver.config.Browser;

/**
 * Allows users to set additional browser capabilities.
 * <p>
 * 
 * The class that implements this interface must be set via testng.xml or
 * command line with key <b>CAPABILITIES_CLASS</b>.
 * 
 * @author ryan
 *
 */
public interface DriverCapabilities {

	/**
	 * Allows custom capabilities to be set.
	 * 
	 * @param browser
	 *            The browser to set capabilities specific to browser being
	 *            used.
	 * @param caps
	 *            The capabilities object to add additional capabilities to.
	 * @return The original caps object with newly set capabilties.
	 */
	DesiredCapabilities getCapabilties(Browser browser, DesiredCapabilities caps);
}
