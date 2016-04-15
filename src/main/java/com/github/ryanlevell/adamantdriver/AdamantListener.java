package com.github.ryanlevell.adamantdriver;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.concurrent.atomic.AtomicInteger;

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

import com.github.ryanlevell.adamantdriver.config.AdamantConfig;
import com.github.ryanlevell.adamantdriver.config.AdamantProperties;
import com.github.ryanlevell.adamantdriver.config.Browser;
import com.github.ryanlevell.adamantdriver.dataprovider.DataProviderUtil;
import com.github.ryanlevell.adamantdriver.driver.DriverHelper;

import net.lightbody.bmp.BrowserMobProxy;

public class AdamantListener implements IAnnotationTransformer, ITestListener, ISuiteListener, IHookable {

	public static final String ATTR_TEST_NUMBER = "adamant_test_number";
	public static final String ATTR_PROXY = "adamant_proxy";

	private static Logger LOG = LoggerFactory.getLogger(AdamantListener.class);
	private static AtomicInteger testNum = new AtomicInteger();

	@SuppressWarnings("rawtypes")
	public void transform(ITestAnnotation annotation, Class testClass, Constructor testConstructor, Method testMethod) {

		// make sure transform is acting on a method and not class/constructor
		if (testMethod != null) {
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

	public void onTestSuccess(ITestResult result) {
		try {
			DriverHelper.closeDriver(result);
		} finally {
			DriverHelper.stopProxy(result);
		}
		Object testNum = result.getAttribute(AdamantListener.ATTR_TEST_NUMBER);
		LOG.info("Stopping test #" + testNum);
	}

	public void onTestFailure(ITestResult result) {
		try {
			DriverHelper.closeDriver(result);
		} finally {
			DriverHelper.stopProxy(result);
		}
		Object testNum = result.getAttribute(AdamantListener.ATTR_TEST_NUMBER);
		LOG.info("Stopping test #" + testNum);
	}

	public void onTestSkipped(ITestResult result) {
		try {
			DriverHelper.closeDriver(result);
		} finally {
			DriverHelper.stopProxy(result);
		}
		Object testNum = result.getAttribute(AdamantListener.ATTR_TEST_NUMBER);
		LOG.info("Stopping test #" + testNum);
	}

	public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
		try {
			DriverHelper.closeDriver(result);
		} finally {
			DriverHelper.stopProxy(result);
		}
		Object testNum = result.getAttribute(AdamantListener.ATTR_TEST_NUMBER);
		LOG.info("Stopping test #" + testNum);
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

	public void run(IHookCallBack callBack, ITestResult testResult) {

		long num = testNum.incrementAndGet();
		testResult.setAttribute(ATTR_TEST_NUMBER, num);
		LOG.info("Starting test #" + num);

		WebDriver driver = DriverHelper.getDriver(testResult);
		if (driver != null) {

			// moved from DataProviderUtil.addWdToParams so we can access caps
			// desired caps are lost when webdriver is instantiated
			// need caps to get proxy data

			// instantiate driver - currently stubbed
			Browser browser = AdamantConfig.getBrowser();
			URL gridUrl = AdamantConfig.getGridUrl();
			boolean useGrid = AdamantConfig.getUseGrid();
			DesiredCapabilities caps = AdamantConfig.getCapabilities();

			if (testResult.getParameters() != null && testResult.getParameters().length > 1) {
				Object param2 = testResult.getParameters()[1];
				if (param2 instanceof BrowserMobProxy) {

					if (caps.getCapability(CapabilityType.PROXY) != null) {
						LOG.warn(
								"Found proxy capability. Overwriting with built-in proxy. Remove BrowserMobProxy parameter from test parameter to use initial proxy");
					}

					Pair<BrowserMobProxy, Proxy> proxyTuple = AdamantConfig.getProxy();
					testResult.getParameters()[1] = proxyTuple.getLeft();
					caps.setCapability(CapabilityType.PROXY, proxyTuple.getRight());
					testResult.setAttribute(ATTR_PROXY, proxyTuple.getLeft());
				}
			}

			// AdamantDriver aDriver = new AdamantDriver(browser, gridUrl,
			// useGrid, caps);

			LOG.info("Starting WebDriver");
			WebDriver wd = DriverHelper.createDriver(browser, gridUrl, useGrid, caps);
			DriverHelper.setDriver(testResult, wd);
		}

		callBack.runTestMethod(testResult);
	}
}