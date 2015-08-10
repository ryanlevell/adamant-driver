package com.levell.adamantdriver;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.testng.ITestContext;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class DataProviders {

	public static Object[][] callDataProvider(ITestContext testContext, Method testMethod) {

		// get dp class and method
		Test ta = testMethod.getAnnotation(Test.class);
		Class<?> dpClass = getDPClass(testMethod);
		Method dpMethod = getDPMethod(ta, dpClass);

		Object[][] params = null;
		try {
			Class<?>[] types = dpMethod.getParameterTypes();
			if (types.length == 2) {
				if (types[0].isAssignableFrom(ITestContext.class)) {
					params = (Object[][]) dpMethod.invoke(null, testContext, testMethod);
				} else {
					params = (Object[][]) dpMethod.invoke(null, testMethod, testContext);
				}
			} else if (types.length == 1) {
				if (types[0].isAssignableFrom(ITestContext.class)) {
					params = (Object[][]) dpMethod.invoke(null, testContext);
				} else if (types[0].isAssignableFrom(Method.class)) {
					params = (Object[][]) dpMethod.invoke(null, testMethod);
				}
			} else {
				params = (Object[][]) dpMethod.invoke(null);
			}
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		return params;
	}

	public static Class<?> getDPClass(Method testMethod) {
		// get dp name and dp class
		Class<?> dpClass = testMethod.getAnnotation(Test.class).dataProviderClass();

		// #dataProviderClass() returns Object if not found so check for it explicitly
		if (dpClass == null || dpClass == Object.class) {
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

	public static Method getDPMethod(Test testAnnotation, Class<?> dpClass) {

		String dpName = testAnnotation.dataProvider();

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
		throw new IllegalStateException("Data Provider not found with name [" + dpName + "] in class [" + dpClass
				+ "]. Check that the DataProvider name and DataProvider class are correct.");
	}

	@DataProvider(name = "INJECT_WEBDRIVER")
	public static Object[][] injectWebDriver() {
		return new Object[][] { { new AdamantDriver() } };
	}

	@DataProvider(name = "INJECT_WEBDRIVER_WITH_PARAMS", parallel = false)
	public static Object[][] injectWebDriverWithParams(ITestContext context, Method method) {
		Object[][] params = callDataProvider(context, method);
		return addWdInParams(params);
	}

	@DataProvider(name = "INJECT_WEBDRIVER_WITH_PARAMS_PARALLEL", parallel = true)
	public static Object[][] injectWebDriverWithParamsParallel(ITestContext context, Method method) {
		Object[][] params = callDataProvider(context, method);
		return addWdInParams(params);
	}

	private static Object[][] addWdInParams(Object[][] oldParams) {
		Object[][] params = oldParams;

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
