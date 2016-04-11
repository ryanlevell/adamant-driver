package com.github.ryanlevell.adamantdriver.dataprovider;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.ITestContext;
import org.testng.annotations.DataProvider;
import org.testng.annotations.ITestAnnotation;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import org.testng.internal.annotations.TestAnnotation;

import com.github.ryanlevell.adamantdriver.config.AdamantConfig;
import com.github.ryanlevell.adamantdriver.config.Browser;
import com.github.ryanlevell.adamantdriver.driver.AdamantDriver;

/**
 * Contains the data provider util methods.
 * 
 * @author ryan
 *
 */
public class DataProviderUtil {

	/**
	 * Calls the original data provider method.
	 * 
	 * @param testContext
	 *            One of the possible params injected by TestNG.
	 * @param testMethod
	 *            One of the possible params injected by TestNG.
	 * @return The original 2D array of data.
	 */
	public static Object[][] callDataProvider(ITestContext testContext, Method testMethod) {

		Class<?> dpClass = getDPClass(testMethod);
		Method dpMethod = getDPMethod(testMethod);

		// only DPs in another class must be static - same class DPs can be
		// instance methods.
		boolean dpIsStatic = Modifier.isStatic(dpMethod.getModifiers());
		Object clazz = null;
		if (!dpIsStatic) {
			try {
				// can't use dpMethod.getDeclaringClass(), method could be in
				// abstract parent class
				clazz = dpClass.newInstance();
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}

		Object[][] params = null;
		try {
			Class<?>[] types = dpMethod.getParameterTypes();

			// check all possible combinations of possibly injected params to
			// make the call via reflection
			if (types.length == 2) {
				if (types[0].isAssignableFrom(ITestContext.class)) {
					params = (Object[][]) dpMethod.invoke(clazz, testContext, testMethod);
				} else {
					params = (Object[][]) dpMethod.invoke(clazz, testMethod, testContext);
				}
			} else if (types.length == 1) {
				if (types[0].isAssignableFrom(ITestContext.class)) {
					params = (Object[][]) dpMethod.invoke(clazz, testContext);
				} else if (types[0].isAssignableFrom(Method.class)) {
					params = (Object[][]) dpMethod.invoke(clazz, testMethod);
				}
			} else {
				params = (Object[][]) dpMethod.invoke(clazz);
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

	/**
	 * Gets the {@link DataProvider} class. It first checks the test annotation
	 * dataProviderClass attribute.<br>
	 * If the attribute is missing, the data provider must be declared in the
	 * same class.
	 * 
	 * @param testMethod
	 *            The test method of which we are trying to find the data
	 *            provider.
	 * @return The data provider class.
	 */
	private static Class<?> getDPClass(Method testMethod) {
		// get dp class
		Class<?> dpClass = testMethod.getAnnotation(Test.class).dataProviderClass();

		// #dataProviderClass() returns Object if not found so check for it
		// explicitly
		if (dpClass == null || dpClass == Object.class) {
			// class is declaring class (or a super class) if no dp class
			// attribute
			dpClass = testMethod.getDeclaringClass();
		}
		return dpClass;
	}

	/**
	 * Determine if the test method has the parallel attribute set.
	 * 
	 * @param m
	 *            The test method.
	 * @return Whether the test will be ran in parallel.
	 */
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

	/**
	 * Gets the {@link DataProvider} method.
	 * 
	 * @param testMethod
	 *            The test method.
	 * @return The data provider method.
	 */
	public static Method getDPMethod(Method testMethod) {

		Class<?> dpClass = getDPClass(testMethod);
		Test ta = testMethod.getAnnotation(Test.class);
		String dpName = ta.dataProvider();

		List<Method> classMethods = new ArrayList<Method>();
		classMethods.addAll(Arrays.asList(dpClass.getMethods()));

		// look into all super class's methods as well
		Class<?> superClass = dpClass.getSuperclass();
		while (superClass != null) {
			classMethods.addAll(Arrays.asList(superClass.getMethods()));
			superClass = superClass.getSuperclass();
		}

		// get all method in the dp class
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

	/**
	 * Adds the AdamantDriver object to the beginning of the original data
	 * provider array.
	 * 
	 * @param oldParams
	 *            The original data provider array.
	 * @return The new array with the AdamantDriver object inserted at at the
	 *         beginning.
	 */
	static Object[][] addWdToParams(Object[][] oldParams) {

		Browser browser = AdamantConfig.getBrowser();
		DesiredCapabilities caps = AdamantConfig.getCapabilities();
		URL gridUrl = AdamantConfig.getGridUrl();
		boolean useGrid = AdamantConfig.getUseGrid();

		Object[][] params = oldParams;
		Object[][] paramsWithWd = new Object[params.length][params[0].length + 1];

		// add driver to beginning of params list
		for (int i = 0; i < params.length; i++) {
			Object[] row = new Object[params[i].length + 1];
			row[0] = new AdamantDriver(browser, caps, gridUrl, useGrid);
			for (int j = 1; j < paramsWithWd[i].length; j++) {
				row[j] = params[i][j - 1];
			}
			paramsWithWd[i] = row;
		}
		return paramsWithWd;
	}

	/**
	 * Injects a custom {@link DataProvider} into the {@link TestAnnotation}
	 * that adds the {@link WebDriver} to the params list.
	 * 
	 * @param annotation
	 *            The annotation is customize.
	 * @param testMethod
	 *            The test method with the annotation.
	 */
	public static void injectDataProvider(ITestAnnotation annotation, Method testMethod) {
		Class<?>[] paramTypes = testMethod.getParameterTypes();

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

	public static boolean isWebDriverTest(Method testMethod) {
		Class<?>[] paramTypes = testMethod.getParameterTypes();
		if (paramTypes != null && 0 < paramTypes.length) {
			if (paramTypes[0].isAssignableFrom(WebDriver.class)) {

				if (testMethod.getAnnotation(Parameters.class) != null) {
					throw new IllegalStateException(
							"@Parameters is not yet supported by AdamantDriver. Use @DataProvider instead.");
				}
				return true;
			}
		}
		return false;
	}
}
