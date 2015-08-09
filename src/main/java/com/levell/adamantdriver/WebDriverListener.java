package com.levell.adamantdriver;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import org.testng.IAnnotationTransformer;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import org.testng.annotations.ITestAnnotation;
import org.testng.annotations.Test;

public class WebDriverListener implements IAnnotationTransformer, ITestListener {

	@SuppressWarnings("rawtypes")
	public void transform(ITestAnnotation annotation, Class testClass, Constructor testConstructor, Method testMethod) {

		if (testMethod != null) {
			Class<?>[] paramTypes = testMethod.getParameterTypes();
			if (paramTypes != null && 0 < paramTypes.length) {
				if (paramTypes[0].isAssignableFrom(AdamantDriver.class)) {
					if (paramTypes.length == 1) {
						annotation.setDataProviderClass(DataProviders.class);
						annotation.setDataProvider("INJECT_WEBDRIVER");
					} else {
						Class<?> dpClass = DataProviders.getDPClass(testMethod);
						Method dpMethod = DataProviders.getDPMethod(testMethod.getAnnotation(Test.class), dpClass);
						annotation.setDataProviderClass(DataProviders.class);
						if (DataProviders.isParallel(dpMethod)) {
							annotation.setDataProvider("INJECT_WEBDRIVER_WITH_PARAMS_PARALLEL");
						} else {
							annotation.setDataProvider("INJECT_WEBDRIVER_WITH_PARAMS");
						}
					}
				}
			}
		}
	}

	private static void closeWebDriver(ITestResult result) {
		if (result.getParameters() == null || result.getParameters().length == 0) {
			return;
		}
		Object param1 = result.getParameters()[0];
		if (param1 instanceof AdamantDriver) {
			((AdamantDriver) param1).raw().quit();
		}
	}

	public void onTestStart(ITestResult result) {
	}

	public void onTestSuccess(ITestResult result) {
		closeWebDriver(result);
	}

	public void onTestFailure(ITestResult result) {
		closeWebDriver(result);
	}

	public void onTestSkipped(ITestResult result) {
		closeWebDriver(result);
	}

	public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
		closeWebDriver(result);
	}

	public void onStart(ITestContext context) {
	}

	public void onFinish(ITestContext context) {
	}
}