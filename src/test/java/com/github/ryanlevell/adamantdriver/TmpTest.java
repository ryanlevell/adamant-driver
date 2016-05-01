package com.github.ryanlevell.adamantdriver;

import java.util.ArrayList;
import java.util.List;

import org.testng.Assert;
import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class TmpTest implements IRetryAnalyzer{

	@Test(dataProvider = "dp", retryAnalyzer=TmpTest.class)
	public static void test(String i) {
		Assert.fail();
	}

	@DataProvider(name = "dp")
	public Object[][] dp() {
//		return new Object[][] { { 1, new Object[][] {{ "asd", "abd" },{"def"}} }, { 1, new Object[][]{ { "abd", "abc" }} } };
//		return new Object[][] { { 1, new Object[][] {{ "asd", "abd" },{"def"}} } };
//		return new Object[][] { { 1, null} };
		return new Object[][] { {"a"}, {"b"}, {"c"} };
	}

	List<String> retried = new ArrayList<>();
	
	@Override
	public boolean retry(ITestResult arg0) {
		if(retried.contains((String)arg0.getParameters()[0])) {
			return false;
		} else {
			retried.add((String)arg0.getParameters()[0]);
			return true;
		}
	}
}
