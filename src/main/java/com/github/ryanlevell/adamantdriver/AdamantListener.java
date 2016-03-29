package com.github.ryanlevell.adamantdriver;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.IAnnotationTransformer;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import org.testng.annotations.ITestAnnotation;
import org.testng.annotations.Parameters;

import com.github.ryanlevell.adamantdriver.dataprovider.DataProviderUtil;
import com.github.ryanlevell.adamantdriver.dataprovider.DataProviders;

public class AdamantListener implements IAnnotationTransformer, ITestListener {

	Logger LOG = LoggerFactory.getLogger(AdamantListener.class);

	@SuppressWarnings("rawtypes")
	public void transform(ITestAnnotation annotation, Class testClass, Constructor testConstructor, Method testMethod) {

		// make sure transform is acting on a method and not class/constructor
		if (testMethod != null) {

			// skip if there is no WebDriver param
			Class<?>[] paramTypes = testMethod.getParameterTypes();
			if (paramTypes != null && 0 < paramTypes.length) {
				if (paramTypes[0].isAssignableFrom(WebDriver.class)) {

					if (testMethod.getAnnotation(Parameters.class) != null) {
						throw new IllegalStateException(
								"@Parameters is not yet supported by AdamantDriver. Use @DataProvider instead.");
					}

					// determine if we need to inject the old data provider
					if (paramTypes.length == 1) {
						annotation.setDataProviderClass(DataProviders.class);
						annotation.setDataProvider(DataProviders.INJECT_WEBDRIVER);
					} else {
						Method dpMethod = DataProviderUtil.getDPMethod(testMethod);
						annotation.setDataProviderClass(DataProviders.class);
						if (DataProviderUtil.isParallel(dpMethod)) {
							annotation.setDataProvider(DataProviders.INJECT_WEBDRIVER_WITH_PARAMS_PARALLEL);
						} else {
							annotation.setDataProvider(DataProviders.INJECT_WEBDRIVER_WITH_PARAMS);
						}
					}
				}
			}
		}
	}

	/**
	 * Closes the AdamantDriver via the test parameters.
	 * 
	 * @param result
	 *            The test result object.
	 */
	private static void closeWebDriver(ITestResult result) {
		if (result.getParameters() == null || result.getParameters().length == 0) {
			return;
		}
		Object param1 = result.getParameters()[0];
		if (param1 instanceof AdamantDriver) {
			AdamantDriver driver = (AdamantDriver) param1;
			if (driver.isOpen()) {
				driver.quit();
			}
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