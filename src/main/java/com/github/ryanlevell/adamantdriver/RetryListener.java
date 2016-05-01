package com.github.ryanlevell.adamantdriver;

import java.util.HashMap;
import java.util.Map;

import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;
import org.testng.annotations.ITestAnnotation;

import com.github.ryanlevell.adamantdriver.config.AdamantConfig;

/**
 * 
 * Class that will retry a test, if configured to.
 * <p>
 * Determining if a ITestResult has already been retried is difficult to work in
 * all cases. This class forces any test parameter to override toString or it
 * will not work correctly because the retry will have a different value of
 * {@link Object#toString()}.
 * <p>
 * This currently is not implemented due to a bug in TestNG seen at:<br>
 * https://github.com/cbeust/testng/issues/740<br>
 * https://github.com/cbeust/testng/issues/877
 * <p>
 * Will implement retry once these issues are fixed.
 *
 */
public class RetryListener implements IRetryAnalyzer {

	private static final Map<Integer, Integer> manageRetries = new HashMap<>();

	public boolean retry(ITestResult result) {
		int retry = AdamantConfig.getRetryLimit();
		if (retry == 0) {
			return false;
		}

		int hash = getTestHash(result);
		System.out.println(hash);

		Integer currentCount = manageRetries.get(hash);
		if (currentCount == null) {
			currentCount = 0;
		}

		if (currentCount >= retry) {
			return false;
		}

		manageRetries.put(hash, currentCount + 1);
		System.out.println("count: " + manageRetries.size());

		return true;
	}

	/**
	 * Set the {@link IRetryAnalyzer} on the {@link ITestAnnotation} object to
	 * {@link RetryListener}.
	 * 
	 * @param annotation
	 *            The test annotation.
	 */
	public static void setRetryListener(ITestAnnotation annotation) {
		IRetryAnalyzer retry = annotation.getRetryAnalyzer();
		if (retry == null) {
			annotation.setRetryAnalyzer(RetryListener.class);
		}
	}

	private static int getTestHash(ITestResult result) {
		String clazz = result.getTestClass().getName();
		String method = result.getMethod().getMethodName();
		String params = getParametersHash(result.getParameters());
		return (clazz + method).hashCode() + params.hashCode();
	}

	private static String getParametersHash(Object[] params) {
		String paramStr = "";
		try {
			paramStr = getAllParams(params, paramStr);
		} catch (NoSuchMethodException | SecurityException e) {
			throw new IllegalArgumentException(
					"Problem hashing parameters. Submit an issue to AdamanDriver on GitHub with this stack trace attached. As a workaround, set retry_limit to 0.",
					e);
		}
		return paramStr;
	}

	private static String getAllParams(Object obj, String append) throws NoSuchMethodException, SecurityException {
		if (obj == null) {
			return append;
		}
		if (!obj.getClass().isArray()) {
			Class<?> dClass = obj.getClass().getMethod("toString").getDeclaringClass();
			if (dClass == Object.class) {
				throw new IllegalArgumentException(
						"Attempted to configure test retries and found a parameter that does not override Object#toString. All test parameters must override Object#toString or retries will not be handled correctly. Or set retry_limit to 0 to avoid this issue.");
			}
			append += obj.toString();
		} else {
			Object[] array = (Object[]) obj;
			for (Object index : array) {
				append = getAllParams(index, append);
			}
		}

		return append;
	}
}
