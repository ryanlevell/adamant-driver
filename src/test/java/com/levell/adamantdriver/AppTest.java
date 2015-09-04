package com.levell.adamantdriver;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.levell.adamantdriver.AdamantDriver;

//TODO: add tests for multiple browsers + params
//TODO: test with other WD classes (options, navigate, etc)
public class AppTest extends AppTestParent {
	@Test
	public void testNoParams() {
		// if it passes then good!
	}

	@Parameters("test!!!")
	@Test
	public void testAnnotatedNonDriverParam(@Optional("test!!!") String str) {
		Assert.assertEquals(str, "test!!!");
	}

	// Will fail everything - Don't know how to support @Parameters yet when
	// injecting a driver.
	// @Parameters("test!!")
	// @Test
	// public void testDriverParamAndAnnotatedParam(AdamantDriver driver,
	// @Optional("test!!") String str) {
	// driver.get("http://google.com");
	// Assert.assertEquals(driver.getTitle(), "Google");
	// Assert.assertEquals(str, "test!!");
	// }

	@Test(dataProvider = "test")
	public void testNonDriverDataProviderParam(String str) {
		Assert.assertEquals(str, "test!");
	}

	@Test(dataProvider = "test_non_static")
	public void testDriverWithNonStaticDataProvider(AdamantDriver driver, String str) {
		driver.get("http://google.com");
		Assert.assertEquals(str, "test_non_static");
		Assert.assertEquals(driver.getTitle(), "Google");
	}

	@Test
	public void testDriverParam(AdamantDriver driver) {
		driver.get("http://google.com");
		Assert.assertEquals(driver.getTitle(), "Google");
	}

	// sys prop must be set manually to firefox before test to run this test
	// @Test
	// public void testDriverSystemProperty(AdamantDriver driver) {
	// Assert.assertTrue(driver.raw() instanceof FirefoxDriver);
	// }

	// must delete/rename props file to run this test
	// @Test
	// public void testNoPropertiesFile(AdamantDriver driver) {
	// Assert.assertTrue(driver.raw() instanceof FirefoxDriver);
	// }

	// TODO: test multiple browsers in sys prop/prop file

	@Test(dataProvider = "test")
	public void testDriverAndDataProviderParam(AdamantDriver driver, String str) {
		driver.get("http://google.com");
		Assert.assertEquals(driver.getTitle(), "Google");
		Assert.assertEquals(str, "test!");
	}

	@Test(dataProvider = "test")
	public void testDriverAndDataProviderParamAgain(AdamantDriver driver, String str) {
		driver.get("http://google.com");
		Assert.assertEquals(driver.getTitle(), "Google");
		Assert.assertEquals(str, "test!");
	}
	
	@Test(dataProvider = "parent_test")
	public void testDriverAndParentClassDataProviderParam(AdamantDriver driver, String str) {
		driver.get("http://google.com");
		Assert.assertEquals(driver.getTitle(), "Google");
		Assert.assertEquals(str, "test_parent_class!");
	}

	@Test(dataProvider = "test", dataProviderClass = AppTestDataProviders.class)
	public void testDriverAndDifferentClassDataProviderParam(AdamantDriver driver, String str) {
		driver.get("http://google.com");
		Assert.assertEquals(driver.getTitle(), "Google");
		Assert.assertEquals(str, "test_different_class!");
	}

	@Test(dataProvider = "inject_method")
	public void testDriverAndDataProviderWithMethod(AdamantDriver driver, Method m) {
		Assert.assertNotNull(m);
		driver.get("http://google.com");
		Assert.assertEquals(driver.getTitle(), "Google");
	}

	@Test(dataProvider = "inject_context")
	public void testDriverAndDataProviderWithContext(AdamantDriver driver, ITestContext c) {
		Assert.assertNotNull(c);
		driver.get("http://google.com");
		Assert.assertEquals(driver.getTitle(), "Google");
	}

	@Test(dataProvider = "inject_method_context")
	public void testDriverAndDataProviderWithMethodAndContext(AdamantDriver driver, Method m, ITestContext c) {
		Assert.assertNotNull(m);
		Assert.assertNotNull(c);
		driver.get("http://google.com");
		Assert.assertEquals(driver.getTitle(), "Google");
	}

	@Test(dataProvider = "inject_context_method")
	public void testDriverAndDataProviderWithContextAndMethod(AdamantDriver driver, ITestContext c, Method m) {
		Assert.assertNotNull(c);
		Assert.assertNotNull(m);
		driver.get("http://google.com");
		Assert.assertEquals(driver.getTitle(), "Google");
	}

	private static int pos = 0;

	@Test(dataProvider = "test_2_params")
	public void testDriverAndTwoDataProviderParams(AdamantDriver driver, String str, String str2) {
		Assert.assertEquals(str, "with_wd2_" + pos + pos++);
		Assert.assertEquals(str2, "with_wd2_" + pos + pos++);
		driver.get("http://google.com");
		Assert.assertEquals(driver.getTitle(), "Google");
	}

	private static List<String> values = new ArrayList<String>(Arrays.asList("parallel_0", "parallel_1", "parallel_2",
			"parallel_3", "parallel_4", "parallel_5", "parallel_6", "parallel_7", "parallel_8", "parallel_9"));

	@Test(dataProvider = "parallel")
	public void testParallelDataProviderTests(AdamantDriver driver, String str) {
		driver.get("http://google.com");
		Assert.assertEquals(driver.getTitle(), "Google");
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
