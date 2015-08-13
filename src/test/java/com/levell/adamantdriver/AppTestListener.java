package com.levell.adamantdriver;

import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

@Listeners(TestListener.class)
public class AppTestListener {

	@Test
	public void testTestListener(AdamantDriver driver) {
		driver.get("http://google.com");
		Assert.assertEquals(driver.getTitle(), "FAIL (But pass via listener)");
	}
}
