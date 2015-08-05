package com.levell.adamantdriver;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.levell.adamantdriver.AdamantDriver;

public class AppTest {

	// test with only WdWrapper param
	@Test
	public void testApp(AdamantDriver driver) {
		System.out.println("test..........");
		driver.raw().get("http://google.com");
	}

	// test with no params
	@Test
	public void testApp2() {
		System.out.println("test2..........");
	}

	// test with only non-WdWrapper param
	@Parameters("test3")
	@Test
	public void testApp3(@Optional("test3") String str) {
		System.out.println("\"" + str + "\"..........");
	}

	// test with only one DP param
	@Test(dataProvider = "without_wd")
	public void testApp4(String str) {
		System.out.println("\"" + str + "\"..........");
	}

	// test with WdWrapper param and DP param
	@Test(dataProvider = "with_wd")
	public void testApp5(AdamantDriver driver, String str) {
		System.out.println("\"" + str + "\"..........");
		driver.raw().get("http://google.com");
	}

	// test with same DP as test5
	@Test(dataProvider = "with_wd")
	public void testApp5_5(AdamantDriver driver, String str) {
		System.out.println("\"" + str + "_5\"..........");
		driver.raw().get("http://google.com");
	}

	// test with different DP
	@Test(dataProvider = "with_wd2")
	public void testApp6(AdamantDriver driver, String str, String str2) {
		System.out.println("\"" + str + "\"..........\"" + str2 + "\"");
		driver.raw().get("http://google.com");
	}

	//
	@Test(dataProvider = "with_wd3")
	public void testApp7(AdamantDriver driver, String str) {
		System.out.println("\"" + str + "\"..........");
		driver.raw().get("http://google.com");
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@DataProvider(name = "without_wd")
	public static Object[][] withoutWD() {
		return new Object[][] { { "test4" } };
	}

	@DataProvider(name = "with_wd")
	public static Object[][] withWD() {
		return new Object[][] { { "test5" } };
	}

	@DataProvider(name = "with_wd2")
	public static Object[][] withWD2() {
		return new Object[][] { { "test6", "test6!" }, { "test6_6", "test6_6!" } };
	}

	@DataProvider(name = "with_wd3", parallel = true)
	public static Object[][] withWD3() {
		Object[][] params = new Object[20][1];
		for (int i = 0; i < params.length; i++) {
			params[i][0] = "test7-" + i;
		}
		return params;
	}
}
