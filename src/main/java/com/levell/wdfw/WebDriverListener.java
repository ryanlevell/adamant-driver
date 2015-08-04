package com.levell.wdfw;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.testng.IAnnotationTransformer;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import org.testng.annotations.DataProvider;
import org.testng.annotations.ITestAnnotation;

public class WebDriverListener implements IAnnotationTransformer, ITestListener {

	// private static final Object LOCK = new Object();
	// TODO works for multiple suites?
	private static final Map<String, Object[][]> OLD_DP_PARAMS = new HashMap<String, Object[][]>();

	@SuppressWarnings("rawtypes")
	public void transform(ITestAnnotation annotation, Class testClass, Constructor testConstructor, Method testMethod) {

		if (testMethod != null) {
			Class<?>[] paramTypes = testMethod.getParameterTypes();

			if (paramTypes != null && 0 < paramTypes.length) {
				if (paramTypes[0].isAssignableFrom(WdWrapper.class)) {
					if (paramTypes.length == 1) {
						annotation.setDataProviderClass(WebDriverListener.class);
						annotation.setDataProvider("INJECT_WEBDRIVER");
					} else {

						// get dp name and dp class
						Class<?> dpClass = annotation.getDataProviderClass();
						String dpName = annotation.getDataProvider();
						if (dpClass == null) {
							// class is declaring class if no dp class attribute
							dpClass = testMethod.getDeclaringClass();
						}

						// get dp method name
						String dpMethod = null;

						// get all method in the dp class
						Method[] classMethods = dpClass.getMethods();
						for (Method m : classMethods) {

							// get annotations for each method in dp class
							for (Annotation a : m.getAnnotations()) {

								// use if annotation is dp
								if (a.annotationType().isAssignableFrom(DataProvider.class)) {

									// loop through annotation attributes
									for (Method aMethod : a.annotationType().getDeclaredMethods()) {
										// find "name" attribute
										if (aMethod.getName().equalsIgnoreCase("name")) {
											String name = ((DataProvider) a).name();
											if (name.equals(dpName)) {
												dpMethod = m.getName();

												// determine if parallel
												// TODO keep original DP
												// attirbutes!
												// boolean parallel =
												// ((DataProvider)a).parallel();
												// a.annotationType().

												break;
											}
										}
									}
								}
							}
						}

						Object[][] params = callDataProvider(dpClass, dpMethod);

						// make key the test method name (unique)
						// synchronized (LOCK) {
						OLD_DP_PARAMS.put(testMethod.getName(), params);
						// }

						annotation.setDataProviderClass(WebDriverListener.class);
						annotation.setDataProvider("INJECT_WEBDRIVER_WITH_PARAMS");
					}
				}
			}
		}
	}

	private static Object[][] callDataProvider(Class<?> dpClass, String dpMethod) {
		Object[][] params = null;
		try {
			// TODO need to handle injected params method types as well
			Method method = dpClass.getMethod(dpMethod);
			params = (Object[][]) method.invoke(null);
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		return params;
	}

	@DataProvider(name = "INJECT_WEBDRIVER")
	public static Object[][] injectWebDriver() {
		return new Object[][] { { new WdWrapper() } };
	}

	@DataProvider(name = "INJECT_WEBDRIVER_WITH_PARAMS")
	public static Object[][] injectWebDriverWithParams(Method m) {

		Object[][] params = null;
		// synchronized (LOCK) {
		params = OLD_DP_PARAMS.remove(m.getName());
		// }

		Object[][] paramsWithWd = new Object[params.length][params[0].length + 1];

		// add driver to beginning of params list
		for (int i = 0; i < params.length; i++) {
			Object[] row = new Object[params[0].length + 1];
			row[0] = new WdWrapper();
			for (int j = 1; j < paramsWithWd[0].length; j++) {
				row[j] = params[i][j - 1];
			}
			paramsWithWd[i] = row;
		}

		return paramsWithWd;
	}

	private static void closeWebDriver(ITestResult result) {
		if (result.getParameters() == null || result.getParameters().length == 0) {
			return;
		}
		Object param1 = result.getParameters()[0];
		if (param1 instanceof WdWrapper) {
			((WdWrapper) param1).raw().quit();
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