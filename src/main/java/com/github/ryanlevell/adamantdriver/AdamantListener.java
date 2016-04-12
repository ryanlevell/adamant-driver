package com.github.ryanlevell.adamantdriver;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.IAnnotationTransformer;
import org.testng.ISuite;
import org.testng.ISuiteListener;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import org.testng.annotations.ITestAnnotation;

import com.github.ryanlevell.adamantdriver.config.AdamantProperties;
import com.github.ryanlevell.adamantdriver.dataprovider.DataProviderUtil;
import com.github.ryanlevell.adamantdriver.driver.DriverHelper;

public class AdamantListener implements IAnnotationTransformer, ITestListener, ISuiteListener {

	Logger LOG = LoggerFactory.getLogger(AdamantListener.class);

	@SuppressWarnings("rawtypes")
	public void transform(ITestAnnotation annotation, Class testClass, Constructor testConstructor, Method testMethod) {

		// make sure transform is acting on a method and not class/constructor
		if (testMethod != null) {
			// skip if there is no WebDriver param
			if(DataProviderUtil.isWebDriverTest(testMethod)) {
				// inject custom data provider that adds the WebDriver
				DataProviderUtil.injectDataProvider(annotation, testMethod);
			}
		}
	}

	public void onTestStart(ITestResult result) {
		// not used
	}

	public void onTestSuccess(ITestResult result) {
		DriverHelper.closeWebDriver(result);
	}

	public void onTestFailure(ITestResult result) {
		DriverHelper.closeWebDriver(result);
	}

	public void onTestSkipped(ITestResult result) {
		DriverHelper.closeWebDriver(result);
	}

	public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
		DriverHelper.closeWebDriver(result);
	}

	public void onStart(ITestContext context) {
		if (!context.getCurrentXmlTest().getLocalParameters().isEmpty()) {
			LOG.warn(
					"Parameters in <test> tags are not yet be used in AdamantDriver config. Place AdamantDriver config parameters in the <suite> tag.");
		}
	}

	public void onFinish(ITestContext context) {
		// not used
	}

	/**
	 * Get suite level testng.xml parameters. Class level parameters are not
	 * currently used.
	 * 
	 * @param suite
	 */
	public void onStart(ISuite suite) {
		LOG.debug("suite params: " + suite.getXmlSuite().getParameters());
		AdamantProperties.setProperties(suite.getXmlSuite().getParameters());
	}

	public void onFinish(ISuite suite) {
		// not used
	}
}