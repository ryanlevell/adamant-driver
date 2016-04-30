package com.github.ryanlevell.adamantdriver.config;

import org.testng.ITestResult;

/**
 * 
 * When to take a screenshot.
 *
 */
enum ScreenshotOn {
	ALL(Integer.MAX_VALUE), PASS(ITestResult.SUCCESS), FAIL(ITestResult.FAILURE), FAIL_WITHIN_SUCCESS_PERCENTAGE(
			ITestResult.SUCCESS_PERCENTAGE_FAILURE), SKIP(ITestResult.SKIP), NONE(Integer.MIN_VALUE);

	private int status;

	private ScreenshotOn(int status) {
		this.status = status;
	}

	public int getStatus() {
		return status;
	}
}
