package com.github.ryanlevell.adamantdriver.driver;

import org.testng.ITestResult;

public class DriverHelper {

	/**
	 * Closes the AdamantDriver via the test parameters.
	 * 
	 * @param result
	 *            The test result object.
	 */
	public static void closeWebDriver(ITestResult result) {
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
}
