# adamant-driver

AdamantDriver is a library for Selenium WebDriver + TestNG. It handles initializing and closing WebDriver using a TestNG listener. The user just needs to include an AdamantDriver parameter in their TestNG test:

```JAVA
@Test
public void test(AdamantDriver driver) {
    // the driver is initialized and ready to use
    driver.get("http://google.com");
    Assert.assertEquals(driver.getTitle(), "Google");
    // the driver is closed automatically at the end of a test
}
```

There is no need for a DataProvider or any other code to start using the driver variable. Outside of code, there is one additional step in testng.xml. The WebDriverListener listener must be added under the &lt;suite&gt; tag:

```XML
<suite name="SomeSuite">
	<listeners>
		<listener class-name="com.levell.adamantdriver.WebDriverListener" />
	</listeners>
	...
</suite>
```

For now, the jar must be created and added to the project. The jar can be created with ```mvn package``` and found in ```target/adamant-driver...jar-with-dependencies.jar```.

Although a DataProvider is not needed, DataProviders can be used normally and the driver will still be injected. The driver must be the first parameter followed by the DataProvider parameters:

```JAVA
@Test(dataProvider="someDataProvider")
public void test(AdamantDriver driver, String dataProviderParam1, String dataProviderParam2) {
    driver.get(dataProviderParam1);
    Assert.assertEquals(driver.getTitle(), dataProviderParam2);
}
```

The ```AdamantDriver``` object is a full wrapper of WebDriver. All the normal WebDriver methods can be used.

#Limitations

The TestNG Parameter annotation is not currently supported. A workaround is to replace it with a DataProvider.

Only FirefoxDriver can be used. All drivers will be supported soon.


TODO Features:
Config file
Screenshots
Any browser - maven depenencies in mvn
...
