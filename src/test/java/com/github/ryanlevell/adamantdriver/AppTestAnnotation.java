package com.github.ryanlevell.adamantdriver;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class AppTestAnnotation {
	
	@BeforeClass
	public void before() {
		System.out.println("Before class...");
	}
	
	@BeforeTest
	public void beforeTest() {
		System.out.println("Before test...");
	}
	
	@BeforeMethod
	public void beforeMethod() {
		System.out.println("Before method...");
	}
	
	@AfterClass
	public void after() {
		System.out.println("After class...");
	}
	
	@AfterTest
	public void aftertTest() {
		System.out.println("After test...");
	}
	
	@AfterMethod
	public void afterMethod() {
		System.out.println("After method...");
	}
	
	@Test
	public void testWithBeforeAndAfterAnnotations(WebDriver driver) {
		driver.get("http://google.com");
		Assert.assertEquals(driver.getTitle(), "Google");
	}
}
