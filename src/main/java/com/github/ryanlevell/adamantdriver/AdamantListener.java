package com.github.ryanlevell.adamantdriver;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.commons.lang3.tuple.Pair;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.IAnnotationTransformer;
import org.testng.IHookCallBack;
import org.testng.IHookable;
import org.testng.ISuite;
import org.testng.ISuiteListener;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import org.testng.annotations.ITestAnnotation;

import com.beust.jcommander.Parameter;
import com.github.ryanlevell.adamantdriver.config.AdamantConfig;
import com.github.ryanlevell.adamantdriver.config.AdamantProperties;
import com.github.ryanlevell.adamantdriver.config.Browser;
import com.github.ryanlevell.adamantdriver.dataprovider.DataProviderUtil;
import com.github.ryanlevell.adamantdriver.driver.DriverHelper;

import net.lightbody.bmp.BrowserMobProxy;

/**
 * Implements all interfaces used by AdamantDriver. This makes it simple for the
 * user to get up and running using this single class.
 * 
 * @author ryan
 *
 */
public class AdamantListener implements IAnnotationTransformer, ITestListener, ISuiteListener, IHookable {

	public static final String ATTR_TEST_NUMBER = "adamant_test_number";
	public static final String ATTR_PROXY = "adamant_proxy";

	private static Logger LOG = LoggerFactory.getLogger(AdamantListener.class);
	private static AtomicLong testNum = new AtomicLong();

	/**
	 * Inject custom data providers if needed.
	 */
	@SuppressWarnings("rawtypes")
	public void transform(ITestAnnotation annotation, Class testClass, Constructor testConstructor, Method testMethod) {

		// make sure transform is acting on a method and not class/constructor
		if (testMethod != null) {

			RetryListener.setRetryListener(annotation);

			// skip if there is no WebDriver param
			if (DataProviderUtil.hasWebDriverParam(testMethod)) {

				// inject custom data provider that adds the WebDriver
				if (DataProviderUtil.hasProxyParam(testMethod)) {
					DataProviderUtil.injectProxyProvider(annotation, testMethod);
				} else {
					DataProviderUtil.injectWebDriverProvider(annotation, testMethod);
				}
			}
		}
	}

	public void onTestStart(ITestResult result) {
		// not used
	}

	/**
	 * Stop WebDriver and Proxy.
	 */
	public void onTestSuccess(ITestResult result) {
		teardown(result);
	}

	/**
	 * Stop WebDriver and Proxy.
	 */
	public void onTestFailure(ITestResult result) {
		teardown(result);
	}

	/**
	 * Stop WebDriver and Proxy.
	 */
	public void onTestSkipped(ITestResult result) {
		teardown(result);
	}

	/**
	 * Stop WebDriver and Proxy.
	 */
	public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
		teardown(result);
	}

	/**
	 * Warn that {@link Parameter} annotation is not supported by AdamantDriver.
	 */
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
	 *            The XML test suite.
	 */
	public void onStart(ISuite suite) {
		LOG.debug("suite params: " + suite.getXmlSuite().getParameters());
		AdamantProperties.setProperties(suite.getXmlSuite().getParameters());
	}

	public void onFinish(ISuite suite) {
		// not used
	}

	/**
	 * Initialize everything needed for the test right before the test is ran.
	 * <br>
	 * Instantiates the WebDriver, DesiredCapabilities, and BrowserMobProxy.
	 */
	public void run(IHookCallBack callBack, ITestResult testResult) {

		long num = testNum.incrementAndGet();
		testResult.setAttribute(ATTR_TEST_NUMBER, num);
		LOG.info("Starting test #" + num);

		WebDriver driver = DriverHelper.getDriverFromTestParams(testResult);

		if (driver != null) {

			// instantiate driver - currently stubbed
			Browser browser = AdamantConfig.getBrowser();
			URL gridUrl = AdamantConfig.getGridUrl();
			boolean useGrid = AdamantConfig.getUseGrid();
			DesiredCapabilities caps = AdamantConfig.getCapabilities();

			BrowserMobProxy proxy = DriverHelper.getProxyFromTestParams(testResult);
			if (proxy != null) {

				if (caps.getCapability(CapabilityType.PROXY) != null) {
					LOG.warn(
							"Found proxy capability. Overwriting with built-in proxy. Remove BrowserMobProxy parameter from test parameter to use original proxy");
				}

				Pair<BrowserMobProxy, Proxy> proxyTuple = AdamantConfig.getProxy();
				DriverHelper.setProxyTestParam(testResult, proxyTuple, caps);
			}

			LOG.info("Starting WebDriver");
			WebDriver wd = DriverHelper.createDriver(browser, gridUrl, useGrid, caps);
			AdamantConfig.getOptions(wd);
			DriverHelper.setDriverTestParam(testResult, wd);
		}

		callBack.runTestMethod(testResult);
	}

	/**
	 * Test tear down process: take screenshot, shutdown driver, shutdown proxy.
	 * 
	 * @param result
	 *            The current test result object.
	 */
	private void teardown(ITestResult result) {

		Object testNumObj = result.getAttribute(AdamantListener.ATTR_TEST_NUMBER);
		if (testNumObj == null) {
			LOG.warn("Test result number was null, an exception was likely thrown");
			return;
		}

		long testNum = (Long) testNumObj;
		LOG.info("Stopping test #" + testNum);

		try {
			DriverHelper.takeScreenshot(result, testNum);
		} finally {
			try {
				DriverHelper.closeDriver(result);
			} finally {
				DriverHelper.stopProxy(result);
			}
		}
	}
}