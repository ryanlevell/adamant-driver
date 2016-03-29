package com.github.ryanlevell.adamantdriver;

import org.testng.annotations.DataProvider;

public class AppTestDataProviders {

	@DataProvider(name = "test")
	public static Object[][] provideTestParam() {
		return new Object[][] { { "test_different_class!" } };
	}
}
