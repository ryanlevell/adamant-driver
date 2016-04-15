package com.github.ryanlevell.adamantdriver.capabilities;

import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;

import com.github.ryanlevell.adamantdriver.config.Browser;
import com.github.ryanlevell.adamantdriver.user_interfaces.DriverCapabilities;

public class ChromeIncognito implements DriverCapabilities {

	public DesiredCapabilities getCapabilties(Browser browser, DesiredCapabilities caps) {
		ChromeOptions opts = new ChromeOptions();
		opts.addArguments("-incognito");
		caps.setCapability(ChromeOptions.CAPABILITY, opts);

		// BrowserMobProxy server = new BrowserMobProxyServer();
		// server.start();
		// caps.setCapability(CapabilityType.PROXY,
		// ClientUtil.createSeleniumProxy(server));

		return caps;
	}
}
