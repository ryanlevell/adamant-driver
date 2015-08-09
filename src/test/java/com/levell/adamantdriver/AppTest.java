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

//TODO: test with specified dp class
//TODO: test with other listeners
public class AppTest {

	@Test
	public void testInjectDriverParam(AdamantDriver driver) {
		driver.raw().get("http://google.com");
		Assert.assertEquals("Google", driver.raw().getTitle());
	}

	@Test
	public void testNoParams() {
		// if it passes then good!
	}

	@Parameters("test3")
	@Test
	public void testNonDriverParam(@Optional("test3") String str) {
		Assert.assertEquals("test3", str);
	}

	@Test(dataProvider = "without_wd")
	public void testNonDriverDataProviderParam(String str) {
		Assert.assertEquals("without_wd", str);
	}

	@Test(dataProvider = "with_wd")
	public void testDriverAndDataProviderParam(AdamantDriver driver, String str) {
		driver.raw().get("http://google.com");
		Assert.assertEquals("Google", driver.raw().getTitle());
		Assert.assertEquals("with_wd", str);
	}

	@Test(dataProvider = "with_wd")
	public void testDriverAndDataProviderParamAgain(AdamantDriver driver, String str) {
		driver.raw().get("http://google.com");
		Assert.assertEquals("Google", driver.raw().getTitle());
		Assert.assertEquals("with_wd", str);
	}

	@Test(dataProvider = "with_wd_inject_method")
	public void testDriverAndDataProviderWithMethod(AdamantDriver driver, Method m) {
		Assert.assertNotNull(m);
		driver.raw().get("http://google.com");
		Assert.assertEquals("Google", driver.raw().getTitle());
	}

	@Test(dataProvider = "with_wd_inject_context")
	public void testDriverAndDataProviderWithContext(AdamantDriver driver, ITestContext c) {
		Assert.assertNotNull(c);
		driver.raw().get("http://google.com");
		Assert.assertEquals("Google", driver.raw().getTitle());
	}

	@Test(dataProvider = "with_wd_inject_method_context")
	public void testDriverAndDataProviderWithMethodAndContext(AdamantDriver driver, Method m, ITestContext c) {
		Assert.assertNotNull(m);
		Assert.assertNotNull(c);
		driver.raw().get("http://google.com");
		Assert.assertEquals("Google", driver.raw().getTitle());
	}

	@Test(dataProvider = "with_wd_inject_context_method")
	public void testDriverAndDataProviderWithContextAndMethod(AdamantDriver driver, ITestContext c, Method m) {
		Assert.assertNotNull(c);
		Assert.assertNotNull(m);
		driver.raw().get("http://google.com");
		Assert.assertEquals("Google", driver.raw().getTitle());
	}

	private static int pos = 0;

	@Test(dataProvider = "with_wd2")
	public void testDriverAndTwoDataProviderParams(AdamantDriver driver, String str, String str2) {
		Assert.assertEquals("with_wd2_" + pos + pos++, str);
		Assert.assertEquals("with_wd2_" + pos + pos++, str);
		driver.raw().get("http://google.com");
		Assert.assertEquals("Google", driver.raw().getTitle());
	}

	private static List<String> values = new ArrayList<String>(Arrays.asList("parallel_0", "parallel_1", "parallel_2",
			"parallel_3", "parallel_4", "parallel_5", "parallel_6", "parallel_7", "parallel_8", "parallel_9"));

	@Test(dataProvider = "with_wd_parallel")
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

	@DataProvider(name = "without_wd")
	public static Object[][] withoutWD() {
		return new Object[][] { { "without_wd" } };
	}

	@DataProvider(name = "with_wd")
	public static Object[][] withWD() {
		return new Object[][] { { "with_wd" } };
	}

	@DataProvider(name = "with_wd2")
	public static Object[][] withWD2() {
		return new Object[][] { { "with_wd2_00", "with_wd2_11" }, { "with_wd2_22", "with_wd2_33" } };
	}

	@DataProvider(name = "with_wd_parallel", parallel = true)
	public static Object[][] withWD3() {
		Object[][] params = new Object[10][1];
		for (int i = 0; i < params.length; i++) {
			params[i][0] = "parallel_" + i;
		}
		return params;
	}

	@DataProvider(name = "with_wd_inject_method")
	public static Object[][] withWDInjectMethod(Method m) {
		return new Object[][] { { m } };
	}

	@DataProvider(name = "with_wd_inject_context")
	public static Object[][] withWDInjectContext(ITestContext c) {
		return new Object[][] { { c } };
	}

	@DataProvider(name = "with_wd_inject_method_context")
	public static Object[][] withWDInjectMethodAndContext(Method m, ITestContext c) {
		return new Object[][] { { m, c } };
	}

	@DataProvider(name = "with_wd_inject_context_method")
	public static Object[][] withWDInjectContextAndMethod(ITestContext c, Method m) {
		return new Object[][] { { c, m } };
	}
}
