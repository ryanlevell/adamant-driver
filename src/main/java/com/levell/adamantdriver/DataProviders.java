package com.levell.adamantdriver;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.testng.annotations.DataProvider;
import org.testng.annotations.ITestAnnotation;

public class DataProviders {

	// TODO works for multiple suites?
	public static final Map<String, Object[][]> OLD_DP_PARAMS = new HashMap<String, Object[][]>();

	public static Object[][] callDataProvider(Class<?> dpClass, Method dpMethod) {
		Object[][] params = null;
		try {
			// TODO need to handle injected params method types as well
			Method method = dpClass.getMethod(dpMethod.getName());
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

	public static Class<?> getDPClass(ITestAnnotation testAnnotation, Method testMethod) {
		// get dp name and dp class
		Class<?> dpClass = testAnnotation.getDataProviderClass();
		if (dpClass == null) {
			// class is declaring class if no dp class attribute
			dpClass = testMethod.getDeclaringClass();
		}
		return dpClass;
	}

	public static boolean isParallel(Method m) {
		// get annotations for each method in dp class
		for (Annotation a : m.getAnnotations()) {
			// use if annotation is dp
			if (a.annotationType().isAssignableFrom(DataProvider.class)) {
				return ((DataProvider) a).parallel();
			}
		}
		return false;
	}

	public static Method getDPMethod(ITestAnnotation testAnnotation, Class<?> dpClass) {

		String dpName = testAnnotation.getDataProvider();

		// get all method in the dp class
		Method[] classMethods = dpClass.getMethods();
		for (Method m : classMethods) {
			// get annotations for each method in dp class
			for (Annotation a : m.getAnnotations()) {
				// use if annotation is dp
				if (a.annotationType().isAssignableFrom(DataProvider.class)) {
					String name = ((DataProvider) a).name();
					if (name.equals(dpName)) {
						return m;
					}
				}
			}
		}
		return null;
	}

	@DataProvider(name = "INJECT_WEBDRIVER", parallel = false)
	public static Object[][] injectWebDriver() {
		return new Object[][] { { new AdamantDriver() } };
	}

	@DataProvider(name = "INJECT_WEBDRIVER_WITH_PARAMS", parallel = false)
	public static Object[][] injectWebDriverWithParams(Method m) {
		return addWdInParams(m);
	}

	@DataProvider(name = "INJECT_WEBDRIVER_WITH_PARAMS_PARALLEL", parallel = true)
	public static Object[][] injectWebDriverWithParamsParallel(Method m) {
		return addWdInParams(m);
	}

	private static Object[][] addWdInParams(Method m) {
		Object[][] params = null;
		params = OLD_DP_PARAMS.remove(m.getName());

		Object[][] paramsWithWd = new Object[params.length][params[0].length + 1];

		// add driver to beginning of params list
		for (int i = 0; i < params.length; i++) {
			Object[] row = new Object[params[0].length + 1];
			row[0] = new AdamantDriver();
			for (int j = 1; j < paramsWithWd[0].length; j++) {
				row[j] = params[i][j - 1];
			}
			paramsWithWd[i] = row;
		}

		return paramsWithWd;
	}
}
