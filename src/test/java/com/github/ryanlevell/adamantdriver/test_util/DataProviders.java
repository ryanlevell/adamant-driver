package com.github.ryanlevell.adamantdriver.test_util;

import org.testng.annotations.DataProvider;

public class DataProviders {

	@DataProvider(name = "test")
	public static Object[][] provideTestParam() {
		return new Object[][] { { "test_different_class!" } };
	}
}
