package com.github.ryanlevell.adamantdriver;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Listeners;

import com.github.ryanlevell.adamantdriver.test_util.TestFailureListener;

@Listeners(TestFailureListener.class)
public class ListenerTests {

	// works but shows as failure on report
	// @Test
	public void testTestListener(WebDriver driver) {
		driver.get("http://google.com");
		Assert.assertEquals(driver.getTitle(), "FAIL (But pass via listener)");
	}
}
