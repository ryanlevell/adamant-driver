package com.github.ryanlevell.adamantdriver;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.github.ryanlevell.adamantdriver.test_util.DataProviderParent;
import com.github.ryanlevell.adamantdriver.test_util.DataProviders;

//TODO: add tests for multiple browsers + params
//TODO: test with other WD classes (options, navigate, etc)
public class DriverTests extends DataProviderParent {

	@Test(dataProvider = "test_non_static")
	public void testDriverWithNonStaticDataProvider(WebDriver driver, String str) {
		driver.get("http://google.com");
		Assert.assertEquals(str, "test_non_static");
		Assert.assertEquals(driver.getTitle(), "Google");
	}

	@Test
	public void testDriverParam(WebDriver driver) {// , BrowserMobProxy proxy) {
		driver.get("http://google.com");
		Assert.assertEquals(driver.getTitle(), "Google");
	}

	// TODO: test multiple browsers in sys prop/prop file

	@Test(dataProvider = "test")
	public void testDriverAndDataProviderParam(WebDriver driver, String str) {
		driver.get("http://google.com");
		Assert.assertEquals(driver.getTitle(), "Google");
		Assert.assertEquals(str, "test!");
	}

	@Test(dataProvider = "test")
	public void testDriverAndDataProviderParamAgain(WebDriver driver, String str) {
		driver.get("http://google.com");
		Assert.assertEquals(driver.getTitle(), "Google");
		Assert.assertEquals(str, "test!");
	}

	@Test(dataProvider = "parent_test")
	public void testDriverAndParentClassDataProviderParam(WebDriver driver, String str) {
		driver.get("http://google.com");
		Assert.assertEquals(driver.getTitle(), "Google");
		Assert.assertEquals(str, "test_parent_class!");
	}

	@Test(dataProvider = "parent_test_static")
	public void testDriverAndStaticParentClassDataProviderParam(WebDriver driver, String str) {
		driver.get("http://google.com");
		Assert.assertEquals(driver.getTitle(), "Google");
		Assert.assertEquals(str, "test_parent_class_static!");
	}

	@Test(dataProvider = "test", dataProviderClass = DataProviders.class)
	public void testDriverAndDifferentClassDataProviderParam(WebDriver driver, String str) {
		driver.get("http://google.com");
		Assert.assertEquals(driver.getTitle(), "Google");
		Assert.assertEquals(str, "test_different_class!");
	}

	@Test(dataProvider = "inject_method")
	public void testDriverAndDataProviderWithMethod(WebDriver driver, Method m) {
		Assert.assertNotNull(m);
		driver.get("http://google.com");
		Assert.assertEquals(driver.getTitle(), "Google");
	}

	@Test(dataProvider = "inject_context")
	public void testDriverAndDataProviderWithContext(WebDriver driver, ITestContext c) {
		Assert.assertNotNull(c);
		driver.get("http://google.com");
		Assert.assertEquals(driver.getTitle(), "Google");
	}

	@Test(dataProvider = "inject_method_context")
	public void testDriverAndDataProviderWithMethodAndContext(WebDriver driver, Method m, ITestContext c) {
		Assert.assertNotNull(m);
		Assert.assertNotNull(c);
		driver.get("http://google.com");
		Assert.assertEquals(driver.getTitle(), "Google");
	}

	@Test(dataProvider = "inject_context_method")
	public void testDriverAndDataProviderWithContextAndMethod(WebDriver driver, ITestContext c, Method m) {
		Assert.assertNotNull(c);
		Assert.assertNotNull(m);
		driver.get("http://google.com");
		Assert.assertEquals(driver.getTitle(), "Google");
	}

	private int pos = 0;

	@Test(dataProvider = "test_2_params")
	public void testDriverAndTwoDataProviderParams(WebDriver driver, String str, String str2) {
		Assert.assertEquals(str, "with_wd2_" + pos + pos++);
		Assert.assertEquals(str2, "with_wd2_" + pos + pos++);
		driver.get("http://google.com");
		Assert.assertEquals(driver.getTitle(), "Google");
	}

	private List<String> values = Collections
			.synchronizedList(new ArrayList<String>(Arrays.asList("parallel_0", "parallel_1", "parallel_2",
					"parallel_3", "parallel_4", "parallel_5", "parallel_6", "parallel_7", "parallel_8", "parallel_9")));

	@Test(dataProvider = "parallel")
	public void testParallelDataProviderTests(WebDriver driver, String str) {
		driver.get("http://google.com");
		Assert.assertEquals(driver.getTitle(), "Google");
		try {
			Thread.sleep(4000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		Assert.assertTrue(values.remove(str), "[" + str + "] was not found: " + values);
	}
	
	@DataProvider(name = "test")
	public static Object[][] provideTestParam() {
		return new Object[][] { { "test!" } };
	}

	@DataProvider(name = "test_non_static")
	public Object[][] provideTestNonStaticMethodParam() {
		return new Object[][] { { "test_non_static" } };
	}

	@DataProvider(name = "test_2_params")
	public static Object[][] provideTwoTestParams() {
		return new Object[][] { { "with_wd2_00", "with_wd2_11" }, { "with_wd2_22", "with_wd2_33" } };
	}

	@DataProvider(name = "parallel", parallel = true)
	public static Object[][] provideParallel() {
		Object[][] params = new Object[10][1];
		for (int i = 0; i < params.length; i++) {
			params[i][0] = "parallel_" + i;
		}
		return params;
	}

	@DataProvider(name = "inject_method")
	public static Object[][] provideMethod(Method m) {
		return new Object[][] { { m } };
	}

	@DataProvider(name = "inject_context")
	public static Object[][] provideContext(ITestContext c) {
		return new Object[][] { { c } };
	}

	@DataProvider(name = "inject_method_context")
	public static Object[][] provideMethodAndContext(Method m, ITestContext c) {
		return new Object[][] { { m, c } };
	}

	@DataProvider(name = "inject_context_method")
	public static Object[][] provideContextAndMethod(ITestContext c, Method m) {
		return new Object[][] { { c, m } };
	}
}
