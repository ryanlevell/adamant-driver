package com.github.ryanlevell.adamantdriver.interfaces;

import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;

import com.github.ryanlevell.adamantdriver.config.Browser;
import com.github.ryanlevell.adamantdriver.user_interfaces.DriverCapabilities;

public class ChromeIncognito implements DriverCapabilities {

	public void getCapabilties(Browser browser, DesiredCapabilities caps) {
		ChromeOptions opts = new ChromeOptions();
		opts.addArguments("-incognito");
		//opts.addArguments("start-maximized");
		caps.setCapability(ChromeOptions.CAPABILITY, opts);

		// BrowserMobProxy server = new BrowserMobProxyServer();
		// server.start();
		// caps.setCapability(CapabilityType.PROXY,
		// ClientUtil.createSeleniumProxy(server));

		// return caps;
	}
}
