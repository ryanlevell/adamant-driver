package com.levell.adamantdriver;

import org.testng.annotations.DataProvider;

public class AppTestParent {
	
	@DataProvider(name = "parent_test")
	public static Object[][] provideTestParam() {
		return new Object[][] { { "test_parent_class!" } };
	}

}
