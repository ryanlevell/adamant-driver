package com.github.ryanlevell.adamantdriver;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import net.lightbody.bmp.BrowserMobProxy;

public class ProxyInterfaceTest {

	@Test
	public void testProxyInterceptor(WebDriver driver, BrowserMobProxy proxy) {
		driver.get("https://google.com");
		String body = driver.getPageSource();
		Assert.assertTrue(body.contains("Body replaced by proxy!"),
				"ProxyDriver interface should have injected [Body replaced by proxy!] into the page");
	}

	@Test
	public void testProxyHar(WebDriver driver, BrowserMobProxy proxy) {
		proxy.newHar();
		driver.get("https://google.com");
		Assert.assertTrue(!proxy.getHar().getLog().getEntries().isEmpty(), "Har was empty");
	}
}
