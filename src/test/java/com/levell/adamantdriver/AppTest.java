package com.levell.adamantdriver;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.testng.ITestContext;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.levell.adamantdriver.AdamantDriver;

import junit.framework.Assert;

//TODO: test with other listeners
public class AppTest {
	@Test
	public void testNoParams() {
		// if it passes then good!
	}

	@Parameters("test!")
	@Test
	public void testNonDriverParam(@Optional("test!") String str) {
		Assert.assertEquals("test!", str);
	}

	@Test(dataProvider = "test")
	public void testNonDriverDataProviderParam(String str) {
		Assert.assertEquals("test!", str);
	}

	@Test
	public void testDriverParam(AdamantDriver driver) {
		driver.raw().get("http://google.com");
		Assert.assertEquals("Google", driver.raw().getTitle());
	}

	@Test(dataProvider = "test")
	public void testDriverAndDataProviderParam(AdamantDriver driver, String str) {
		driver.raw().get("http://google.com");
		Assert.assertEquals("Google", driver.raw().getTitle());
		Assert.assertEquals("test!", str);
	}

	@Test(dataProvider = "test")
	public void testDriverAndDataProviderParamAgain(AdamantDriver driver, String str) {
		driver.raw().get("http://google.com");
		Assert.assertEquals("Google", driver.raw().getTitle());
		Assert.assertEquals("test!", str);
	}

	@Test(dataProvider = "test", dataProviderClass = AppTestDataProviders.class)
	public void testDriverAndDifferentClassDataProviderParam(AdamantDriver driver, String str) {
		driver.raw().get("http://google.com");
		Assert.assertEquals("Google", driver.raw().getTitle());
		Assert.assertEquals("test_different_class!", str);
	}

	@Test(dataProvider = "inject_method")
	public void testDriverAndDataProviderWithMethod(AdamantDriver driver, Method m) {
		Assert.assertNotNull(m);
		driver.raw().get("http://google.com");
		Assert.assertEquals("Google", driver.raw().getTitle());
	}

	@Test(dataProvider = "inject_context")
	public void testDriverAndDataProviderWithContext(AdamantDriver driver, ITestContext c) {
		Assert.assertNotNull(c);
		driver.raw().get("http://google.com");
		Assert.assertEquals("Google", driver.raw().getTitle());
	}

	@Test(dataProvider = "inject_method_context")
	public void testDriverAndDataProviderWithMethodAndContext(AdamantDriver driver, Method m, ITestContext c) {
		Assert.assertNotNull(m);
		Assert.assertNotNull(c);
		driver.raw().get("http://google.com");
		Assert.assertEquals("Google", driver.raw().getTitle());
	}

	@Test(dataProvider = "inject_context_method")
	public void testDriverAndDataProviderWithContextAndMethod(AdamantDriver driver, ITestContext c, Method m) {
		Assert.assertNotNull(c);
		Assert.assertNotNull(m);
		driver.raw().get("http://google.com");
		Assert.assertEquals("Google", driver.raw().getTitle());
	}

	private static int pos = 0;

	@Test(dataProvider = "test_2_params")
	public void testDriverAndTwoDataProviderParams(AdamantDriver driver, String str, String str2) {
		Assert.assertEquals("with_wd2_" + pos + pos++, str);
		Assert.assertEquals("with_wd2_" + pos + pos++, str2);
		driver.raw().get("http://google.com");
		Assert.assertEquals("Google", driver.raw().getTitle());
	}

	private static List<String> values = new ArrayList<String>(Arrays.asList("parallel_0", "parallel_1", "parallel_2",
			"parallel_3", "parallel_4", "parallel_5", "parallel_6", "parallel_7", "parallel_8", "parallel_9"));

	@Test(dataProvider = "parallel")
	public void testParallelDataProviderTests(AdamantDriver driver, String str) {
		driver.raw().get("http://google.com");
		Assert.assertEquals("Google", driver.raw().getTitle());
		try {
			Thread.sleep(8000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		Assert.assertTrue(values.remove(str));
	}

	@DataProvider(name = "test")
	public static Object[][] provideTestParam() {
		return new Object[][] { { "test!" } };
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
