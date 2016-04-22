package com.github.ryanlevell.adamantdriver;

import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

public class CapabilitiesInterfaceTest {

	@Test
	public void testCapability(WebDriver driver) {
		if (driver instanceof ChromeDriver) {
			Point p = driver.manage().window().getPosition();
			Assert.assertEquals(p.x, 44, "Window position was not set by DriverCapability interface");
			Assert.assertEquals(p.y, 33, "Window position was not set by DriverCapability interface");
		}
	}
}
