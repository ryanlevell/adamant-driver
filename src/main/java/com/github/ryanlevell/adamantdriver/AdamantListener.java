package com.github.ryanlevell.adamantdriver;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Properties;

import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.IAnnotationTransformer;
import org.testng.ISuite;
import org.testng.ISuiteListener;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import org.testng.annotations.ITestAnnotation;
import org.testng.annotations.Parameters;

import com.github.ryanlevell.adamantdriver.config.AdamantProperties.Prop;
import com.github.ryanlevell.adamantdriver.dataprovider.DataProviderUtil;
import com.github.ryanlevell.adamantdriver.driver.DriverHelper;

public class AdamantListener implements IAnnotationTransformer, ITestListener, ISuiteListener {

	Logger LOG = LoggerFactory.getLogger(AdamantListener.class);

	@SuppressWarnings("rawtypes")
	public void transform(ITestAnnotation annotation, Class testClass, Constructor testConstructor, Method testMethod) {

		// make sure transform is acting on a method and not class/constructor
		if (testMethod != null) {

			// skip if there is no WebDriver param
			Class<?>[] paramTypes = testMethod.getParameterTypes();
			if (paramTypes != null && 0 < paramTypes.length) {
				if (paramTypes[0].isAssignableFrom(WebDriver.class)) {

					if (testMethod.getAnnotation(Parameters.class) != null) {
						throw new IllegalStateException(
								"@Parameters is not yet supported by AdamantDriver. Use @DataProvider instead.");
					}

					// inject custom data provider that adds the WebDriver
					DataProviderUtil.injectDataProvider(annotation, testMethod);
				}
			}
		}
	}

	public void onTestStart(ITestResult result) {
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
					"Parameters in <test> tags will not yet be used in AdamantDriver config. Place AdamantDriver config parameters in the <suite> tag.");
		}
	}

	public void onFinish(ITestContext context) {
		// not used
	}

	// TODO: test this method
	// TODO: Add DriverCapabilties interface and test using a implmentation as a
	// xml param
	public void onStart(ISuite suite) {
		LOG.debug("suite params: " + suite.getXmlSuite().getParameters());
		Properties props = new Properties();

		// xml parameters
		for (Prop prop : Prop.values()) {
			for (Map.Entry<String, String> param : suite.getXmlSuite().getParameters().entrySet()) {
				if (prop.name().toLowerCase().equals(param.getKey())) {
					LOG.debug("Adding XML param: [" + prop.name() + "=" + param.getValue() + "]");
					props.setProperty(prop.name(), param.getValue());
					break;
				}
			}
		}

		// cli parameters override xml
		for (Prop prop : Prop.values()) {
			String param = System.getProperty(prop.name().toLowerCase());
			if (param != null) {
				LOG.debug("Overwrite XML param: [" + prop.name() + "=" + param + "]");
				props.setProperty(prop.name(), param);
			}
		}

		// TODO: set AdamantProperties from here - add new method for it
		// AdamantProperties.setProperties(props);
	}

	public void onFinish(ISuite suite) {
		// not used
	}
}