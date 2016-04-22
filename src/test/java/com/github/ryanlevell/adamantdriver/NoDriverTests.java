package com.github.ryanlevell.adamantdriver;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.github.ryanlevell.adamantdriver.test_util.DataProviderParent;

public class NoDriverTests extends DataProviderParent {
	@Test
	public void testNoParams() {
		// if it passes then good!
	}

	@Parameters("test!!!")
	@Test
	public void testAnnotatedNonDriverParam(@Optional("test!!!") String str) {
		Assert.assertEquals(str, "test!!!");
	}

	@Test(dataProvider = "test")
	public void testNonDriverDataProviderParam(String str) {
		Assert.assertEquals(str, "test!");
	}

	@DataProvider(name = "test")
	public static Object[][] provideTestParam() {
		return new Object[][] { { "test!" } };
	}
}
