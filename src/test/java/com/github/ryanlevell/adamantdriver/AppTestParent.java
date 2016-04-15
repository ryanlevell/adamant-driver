package com.github.ryanlevell.adamantdriver;

import org.testng.annotations.DataProvider;

public abstract class AppTestParent {

	@DataProvider(name = "parent_test_static")
	public static Object[][] provideTestParamParentStatic() {
		return new Object[][] { { "test_parent_class_static!" } };
	}

	@DataProvider(name = "parent_test")
	public Object[][] provideTestParamParent() {
		return new Object[][] { { "test_parent_class!" } };
	}

}
