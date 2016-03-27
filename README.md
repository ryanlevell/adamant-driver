# adamant-driver

AdamantDriver is a library combining Selenium WebDriver + TestNG. It enables a tester to begin writing tests very quickly with very little boiler-plate code.

It requires three steps to begin using in a project:
1. [Add the adamant-driver jar to your project](#add-jar)
2. [Inject a WebDriver object as a test parameter](#inject-wd)
3. [Add the ```AdamantListener``` to the ```testng.xml```](#add-list)

### Add the adamant-driver jar to your project<a name="add-jar"></a>
This library is not yet hosted anywhere. It must be manually added into a project. This simplest way is to download the jar included in this project.

1. Download [here](https://github.com/ryanlevell/adamant-driver/tree/master/release/adamant-driver-0.0.1.jar)
2. Copy into project
3. Add to build path

### Inject a WebDriver object as a test parameter<a name="inject-wd"></a>
```JAVA
@Test
public void test(WebDriver driver) {
    // the driver is initialized and ready to use
    driver.get("http://google.com");
    Assert.assertEquals(driver.getTitle(), "Google");
    // the driver is closed automatically at the end of a test
}
```

#### Add ```AdamantListener``` to ```testng.xml```<a name="add-list"></a>
```XML
<suite name="SomeSuite">
	<listeners>
		<listener class-name="com.levell.adamantdriver.AdamantListener" />
	</listeners>
	...
</suite>
```

### Data Providers
Although a ```DataProvider``` is not needed, they can be used normally and the driver will still be injected. The driver **must** be the **first** parameter followed by the ```DataProvider``` parameters:

```JAVA
@Test(dataProvider="someDataProvider")
public void test(WebDriver driver, String dataProviderParam1, String dataProviderParam2) {
    driver.get(dataProviderParam1);
    Assert.assertEquals(driver.getTitle(), dataProviderParam2);
}

@DataProvider(name="someDataProvider")
public Object[][] dataProvider() {
    return new Object[][]{ {"str1", "str2"} };
}
```

### AdamantDriver Object
The AdamantDriver object is a full wrapper of WebDriver. All the normal WebDriver methods can be used. The only difference is the browser is not opened on initialization. This is to prevent a large amount of browser openings when the driver is instantiated in a data provider with many iterations.

### Manually building the project
```bash
git clone https://github.com/ryanlevell/adamant-driver.git
cd adamant-driver
mvn package -Dmaven.test.skip=true
```
The jar can be found in ```<project root>/target/adamant-driver...jar-with-dependencies.jar```.

### Limitations
1. The TestNG ```@Parameter``` annotation is not currently supported. A workaround is to replace it with a ```DataProvider```.
2. Only ```FirefoxDriver``` and ```ChromeDriver``` can be used. More drivers will be supported.


### ```TODO``` Features:
1. Additional config options
2. Browser Capabilities
3. Grid Support
4. Proxy Support
5. Screenshots
