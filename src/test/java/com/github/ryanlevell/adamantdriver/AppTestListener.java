package com.github.ryanlevell.adamantdriver;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

@Listeners(TestListener.class)
public class AppTestListener {

	@Test
	public void testTestListener(WebDriver driver) {
		driver.get("http://google.com");
		Assert.assertEquals(driver.getTitle(), "FAIL (But pass via listener)");
	}
}
