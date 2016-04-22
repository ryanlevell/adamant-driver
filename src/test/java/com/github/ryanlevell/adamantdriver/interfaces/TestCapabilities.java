package com.github.ryanlevell.adamantdriver.interfaces;

import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;

import com.github.ryanlevell.adamantdriver.config.Browser;
import com.github.ryanlevell.adamantdriver.user_interfaces.DriverCapabilities;

public class TestCapabilities implements DriverCapabilities {

	public void getCapabilties(Browser browser, DesiredCapabilities caps) {
		ChromeOptions opts = new ChromeOptions();
		opts.addArguments("window-position=44,33");
		caps.setCapability(ChromeOptions.CAPABILITY, opts);
	}
}
