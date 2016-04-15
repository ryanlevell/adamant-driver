package com.github.ryanlevell.adamantdriver.dataprovider;

import java.lang.reflect.Method;

import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.annotations.DataProvider;

/**
 * Contains the data providers that will override all original data providers to
 * inject the {@link WebDriver}.
 * 
 * @author ryan
 *
 */
public class DataProviders {

	public static final String INJECT_WEBDRIVER = "INJECT_WEBDRIVER";
	public static final String INJECT_WEBDRIVER_WITH_PARAMS = "INJECT_WEBDRIVER_WITH_PARAMS";
	public static final String INJECT_WEBDRIVER_WITH_PARAMS_PARALLEL = "INJECT_WEBDRIVER_WITH_PARAMS_PARALLEL";
	public static final String INJECT_PROXY = "INJECT_PROXY";
	public static final String INJECT_PROXY_WITH_PARAMS = "INJECT_PROXY_WITH_PARAMS";
	public static final String INJECT_PROXY_WITH_PARAMS_PARALLEL = "INJECT_PROXY_WITH_PARAMS_PARALLEL";

	/**
	 * The data provider that is used when none is specified and AdamantDriver
	 * is the only test parameter.
	 * 
	 * @return The 2D containing only the AdamantDriver object(s).
	 */
	@DataProvider(name = INJECT_WEBDRIVER)
	public static Object[][] injectWebDriver() {
		// use "empty" 2D array so driver initialization is always done in a
		// single place
		return DataProviderUtil.addWdToParams(new Object[1][0]);
	}

	@DataProvider(name = INJECT_PROXY)
	public static Object[][] injectProxy() {
		// use "empty" 2D array so driver initialization is always done in a
		// single place
		return DataProviderUtil.addProxyToParams(new Object[1][0]);
	}

	/**
	 * The data provider that is used when there is already a data provider and
	 * AdamantDriver is the first parameter.
	 * 
	 * @param context
	 *            The injected context.
	 * @param method
	 *            The inject method.
	 * @return The 2D array of the original data with the AdamantDriver object
	 *         inserted at the beginning.
	 */
	@DataProvider(name = INJECT_WEBDRIVER_WITH_PARAMS, parallel = false)
	public static Object[][] injectWebDriverWithParams(ITestContext context, Method method) {
		Object[][] params = DataProviderUtil.callDataProvider(context, method);
		return DataProviderUtil.addWdToParams(params);
	}

	@DataProvider(name = INJECT_PROXY_WITH_PARAMS, parallel = false)
	public static Object[][] injectProxyWithParams(ITestContext context, Method method) {
		Object[][] params = DataProviderUtil.callDataProvider(context, method);
		return DataProviderUtil.addProxyToParams(params);
	}

	/**
	 * Parallel version of
	 * {@link #injectWebDriverWithParams(ITestContext, Method)}.
	 * 
	 * @param context
	 *            The injected context.
	 * @param method
	 *            The injected method.
	 * @return The 2D array of the original data with the AdamantDriver object
	 *         inserted at the beginning.
	 */
	@DataProvider(name = INJECT_WEBDRIVER_WITH_PARAMS_PARALLEL, parallel = true)
	public static Object[][] injectWebDriverWithParamsParallel(ITestContext context, Method method) {
		Object[][] params = DataProviderUtil.callDataProvider(context, method);
		return DataProviderUtil.addWdToParams(params);
	}

	@DataProvider(name = INJECT_PROXY_WITH_PARAMS_PARALLEL, parallel = true)
	public static Object[][] injectProxyWithParamsParallel(ITestContext context, Method method) {
		Object[][] params = DataProviderUtil.callDataProvider(context, method);
		return DataProviderUtil.addProxyToParams(params);
	}
}
