package com.github.ryanlevell.adamantdriver.capabilities;

import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;

import com.github.ryanlevell.adamantdriver.capabilties.DriverCapabilities;

public class ChromeIncognito implements DriverCapabilities {

	public DesiredCapabilities getCapabilties() {
		DesiredCapabilities caps = DesiredCapabilities.chrome();
		
		ChromeOptions opts = new ChromeOptions();
		opts.addArguments("-incognito");
		caps.setCapability(ChromeOptions.CAPABILITY, opts);
		return caps;
	}

}
