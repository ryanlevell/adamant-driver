package com.github.ryanlevell.adamantdriver.test_util;

import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

/**
 *
 * Pass tests that truly should fail.
 *
 */
public class TestFailureListener implements ITestListener {

	public void onTestStart(ITestResult result) {
	}

	public void onTestSuccess(ITestResult result) {
		result.setStatus(ITestResult.FAILURE);
	}

	public void onTestFailure(ITestResult result) {
		result.setStatus(ITestResult.SUCCESS);
	}

	public void onTestSkipped(ITestResult result) {
		result.setStatus(ITestResult.FAILURE);
	}

	public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
		result.setStatus(ITestResult.FAILURE);
	}

	public void onStart(ITestContext context) {
	}

	public void onFinish(ITestContext context) {
	}
}