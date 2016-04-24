# **adamant-driver**

AdamantDriver is a library combining Selenium WebDriver + TestNG. It enables a user to begin writing tests very quickly with very little boiler-plate code.

The first 3 links will get you started. Links 4-9 are advanced usage.

1. **[Add the adamant-driver jar to your project](#add-jar)**
2. **[Inject a WebDriver object as a test parameter](#inject-wd)**
3. **[Add AdamantListener to testng.xml](#add-list)**
4. [Using a DataProvider](#dp)
5. [AdamantDriver Parameters](#test-params)
6. [The DriverCapabilities Interface](#caps-int)
7. [The DriverOptions Interface](#options-int)
8. [The DriverProxy Interface](#proxy-int)
9. [Injecting a BrowserMobProxy object as a test parameter](#inject-bmp)
10. [Manually building the project](#manual-build)
11. [Limitations](#limitations)
12. [TODO Features](#todo)
13. [Help](#help)

## 1. Add the adamant-driver jar to your project<a name="add-jar"></a>
---
This library is now hosted at [Maven Central](http://mvnrepository.com/artifact/com.github.ryanlevell/adamant-driver). SNAPSHOT versions are hosted at [https://oss.sonatype.org](https://oss.sonatype.org).

The latest version is ```1.1.0-SNAPSHOT```:
```XML
<dependency>
  <groupId>com.github.ryanlevell</groupId>
  <artifactId>adamant-driver</artifactId>
  <version>1.1.0-SNAPSHOT</version>
</dependency>
```

## 2. Inject a WebDriver object as a test parameter<a name="inject-wd"></a>
---
```JAVA
@Test
public void test(WebDriver driver) {
    // the driver is initialized and ready to use
    driver.get("http://google.com");
    Assert.assertEquals(driver.getTitle(), "Google");
    // the driver is closed automatically at the end of a test
}
```

## 3. Add AdamantListener to [testng.xml](http://testng.org/doc/documentation-main.html#testng-xml)<a name="add-list"></a>
---
```XML
<suite name="SomeSuite">
	<listeners>
		<listener class-name="com.github.ryanlevell.adamantdriver.AdamantListener" />
	</listeners>
	...
</suite>
```

Execute tests:
```BASH
mvn test
```

## 4. Using a DataProvider<a name="dp"></a>
---
A data provider is optional for Selenium tests. They can be used normally and the driver will still be injected. The driver **must be the first parameter** followed by the data provider parameters:

```JAVA
@Test(dataProvider="someDataProvider")
public void test(WebDriver driver, String url, String title) {
    driver.get(url);
    Assert.assertEquals(driver.getTitle(), title);
}

@DataProvider(name="someDataProvider")
public static Object[][] dataProvider() {
    return new Object[][]{ {"http://google.com", "Google"} };
}
```

## 5. AdamantDriver Parameters<a name="test-params"></a>
---
No AdamantDriver parameters are required, but they can be used for additional functionality.  
All AdamantDriver specific parameters can be specified in 2 ways:

#### 1. testng.xml parameter:
```XML
...
  <suite name="suite-name">
    <parameter name="parameter_name" value="parameter_value" />
    ...
  </suite>
...
```

#### 2. command line parameter:
```BASH
mvn test -Dparameter_name=parameter_value
```

#### Parameters:
|parameter         |values                              |default|description                                               |
|------------------|------------------------------------|-------|----------------------------------------------------------|
|browser           |firefox, chrome                     |firefox|The driver to use for testing.                            |
|chrome_path       |*&lt;full path to chrome driver&gt;*|none   |The path to the chrome driver.                            |
|capabilities_class|*&lt;fully qualified class&gt;*     |none   |A class that implements the DriverCapabilities interface.|
|options_class     |*&lt;fully qualified class&gt;*     |none   |A class that implements the DriverOptions interface.    |
|proxy_class       |*&lt;fully qualified class&gt;*     |none   |A class that implements the DriverProxy interface.      |
|use_grid          |true, false                         |false  |Whether to run tests locally or on the grid.              |
|grid_url          |*&lt;your grid URL&gt;*             |none   |The URL to the Selenium grid hub.                         |

## 6. The DriverCapabilities Interface<a name="caps-int"></a>
---
The DriverCapabilities interface provides a way to supply custom [DesiredCapabilities](https://github.com/SeleniumHQ/selenium/wiki/DesiredCapabilities) to the WebDriver object before the test if the capabilities_class parameter is set.
```JAVA
public class MyCapabilities implements DriverCapabilities {
	public void getCapabilities(Browser browser, DesiredCapabilities caps) {
		caps.setCapability(CapabilityType.UNEXPECTED_ALERT_BEHAVIOUR, "accept");
	}
}
```

## 7. The DriverOptions Interface<a name="options-int"></a>
---
The DriverOptions interface provides a way to supply custom [Options](https://selenium.googlecode.com/git/docs/api/java/org/openqa/selenium/WebDriver.Options.html) to the WebDriver object before the test if the options_class parameter is set.
```JAVA
public class MyOptions implements DriverOptions {
	public void getOptions(Browser browser, Options options) {
		options.window().maximize();
	}
}
```

## 8. The DriverProxy Interface<a name="proxy-int"></a>
---
The DriverProxy interface provides a way to supply custom [BrowserMobProxy](https://github.com/lightbody/browsermob-proxy#http-request-manipulation) settings before a test if the proxy_class parameter is set and [Injecting a BrowserMobProxy object as a test parameter](#inject-bmp) is injected. Note, if you manually set a proxy in DesiredCapabilities this will NOT be used and if the injected BrowserMobProxy is present, it will override the initial proxy.
```JAVA
public class MyProxy implements DriverProxy {
	public void getProxy(BrowserMobProxy proxy) {
		proxy.addResponseFilter(new ResponseFilter() {
			public void filterResponse(HttpResponse response, HttpMessageContents contents, HttpMessageInfo messageInfo) {
				contents.setTextContents("Edit the response before testing");
			}
		});
	}
}
```

## 9. Injecting a BrowserMobProxy object as a test parameter<a name="inject-bmp"></a>
---
In addition to injecting a WebDriver object, a [BrowserMobProxy](https://github.com/lightbody/browsermob-proxy#http-request-manipulation) object can also be injected. The proxy object **must be the second parameter** and the **first parameter must be a WebDriver object**. As usual, a DataProvider can still be used with the same rule as when injecting a WebDriver object: the DataProvider parameters must follow the WebDriver and BrowserMobProxy parameters. The proxy can be customized before a test by implementing [The DriverProxy Interface](#proxy-int).
```JAVA
@Test
public void test(WebDriver driver, BrowserMobProxy proxy) {
	proxy.newHar();
	driver.get("https://google.com");
	
	List<HarEntry> harEntries = proxy.getHar().getLog().getEntries();
	Assert.assertTrue(!harEntries.isEmpty(), "Har was empty");
}
```

## 10. Manually building the project<a name="manual-build"></a>
---
```bash
git clone https://github.com/ryanlevell/adamant-driver.git
cd adamant-driver
mvn package -Dmaven.test.skip=true
```
The jar can be found in ```<project root>/target/adamant-driver...jar-with-dependencies.jar```.

## 11. Limitations<a name="limitations"></a>
---
1. The TestNG ```@Parameter``` annotation cannot be used with a WebDriver test. This is because AdamantDriver injects a data provider to all WebDriver tests.
2. Only ```FirefoxDriver``` and ```ChromeDriver``` can be used. More browsers will be added.
3. Anything not supported described in [TODO Features](#todo).


## 12. TODO Features<a name="todo"></a>
---
1. Screenshots + path/folder
2. Retry analyzer + boolean whether to remove retries from results

## 13. Help<a name="help"></a>
---

#### How do I use SNAPSHOT versions?
SNAPSHOT versions can be used by adding the following to ```settings.xml``` or ```pom.xml```:
```XML
<repository>
  <id>snapshots-repo</id>
  <url>https://oss.sonatype.org/content/repositories/snapshots</url>
  <releases><enabled>false</enabled></releases>
  <snapshots><enabled>true</enabled></snapshots>
</repository>
```

#### Can ```@Listener(AdamantListener)``` be used instead of the ```<listener>``` tag?
No, AdamantListener implements ```IAnnotationTransformer```. The [TestNG documentation](http://testng.org/doc/documentation-main.html#listeners-testng-xml) states:
>The @Listeners annotation can contain any class that extends org.testng.ITestNGListener
**except IAnnotationTransformer and IAnnotationTransformer2**. The reason is that these
listeners need to be known very early in the process so that TestNG can use them to
rewrite your annotations, therefore you need to specify these listeners in your
testng.xml file.

#### Tip To Eclipse Users:
If you are running tests via the Eclipse TestNG plugin, you may need to point Eclipse to your ```testng.xml```. The TestNG plugin uses its own ```testng.xml``` by default. Follow the steps below to use your own XML as the template:

1. ```Project``` > ```Properties```
2. Click ```TestNG``` in the left panel
3. Find ```Template XML file```
4. Enter the path to your XML, or browse for it
5. Click ```Apply```
