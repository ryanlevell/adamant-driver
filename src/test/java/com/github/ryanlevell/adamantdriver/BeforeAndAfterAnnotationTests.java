package com.github.ryanlevell.adamantdriver;

import java.util.concurrent.atomic.AtomicInteger;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class BeforeAndAfterAnnotationTests {
	
	private final AtomicInteger count = new AtomicInteger();

	@BeforeClass
	public void before() {
		count.incrementAndGet();
	}

	@BeforeTest
	public void beforeTest() {
		count.incrementAndGet();
	}

	@BeforeMethod
	public void beforeMethod() {
		count.incrementAndGet();
	}

	@Test
	public void testWithBeforeAndAfterAnnotations(WebDriver driver) {
		Assert.assertEquals(count.get(), 3, "Not all before annotations were called");
		
		driver.get("http://google.com");
		Assert.assertEquals(driver.getTitle(), "Google");
	}
}
