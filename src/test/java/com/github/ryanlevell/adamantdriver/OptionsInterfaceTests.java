package com.github.ryanlevell.adamantdriver;

import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

public class OptionsInterfaceTests {

	@Test
	public void testOptions(WebDriver driver) {
		Dimension d = driver.manage().window().getSize();
		Assert.assertEquals(d.width, 453, "Width failed using DriverOptions interface");
		Assert.assertEquals(d.height, 301, "Height failed using DriverOptions interface");
	}
}
