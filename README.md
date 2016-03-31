# adamant-driver

AdamantDriver is a library combining Selenium WebDriver + TestNG. It enables a tester to begin writing tests very quickly with very little boiler-plate code.

It requires three steps to begin using in a project:

1. [Add the adamant-driver jar to your project](#add-jar)
2. [Inject a WebDriver object as a test parameter](#inject-wd)
3. [Add the ```AdamantListener``` to the ```testng.xml```](#add-list)

### 1. Add the adamant-driver jar to your project<a name="add-jar"></a>
This library is now hosted at [Maven Central](http://mvnrepository.com/artifact/com.github.ryanlevell/adamant-driver).
```xml
<dependency>
  <groupId>com.github.ryanlevell</groupId>
  <artifactId>adamant-driver</artifactId>
  <version>1.0.0</version>
</dependency>
```

### 2. Inject a WebDriver object as a test parameter<a name="inject-wd"></a>
```JAVA
@Test
public void test(WebDriver driver) {
    // the driver is initialized and ready to use
    driver.get("http://google.com");
    Assert.assertEquals(driver.getTitle(), "Google");
    // the driver is closed automatically at the end of a test
}
```

#### 3. Add ```AdamantListener``` to [```testng.xml```](http://testng.org/doc/documentation-main.html#testng-xml)<a name="add-list"></a>
```XML
<suite name="SomeSuite">
	<listeners>
		<listener class-name="com.github.ryanlevell.adamantdriver.AdamantListener" />
	</listeners>
	...
</suite>
```

Execute tests: ```mvn test```

#### Why can't I just use ```@Listener(AdamantListener)``` instead of adding a ```<listener>``` to ```testng.xml```?
AdamantListener implements ```IAnnotationTransformer```.  
The [documentation](http://testng.org/doc/documentation-main.html#listeners-testng-xml) states:
```
The @Listeners annotation can contain any class that extends org.testng.ITestNGListener
except IAnnotationTransformer and IAnnotationTransformer2. The reason is that these
listeners need to be known very early in the process so that TestNG can use them to
rewrite your annotations, therefore you need to specify these listeners in your
testng.xml file.
```

#### Tip To Eclipse Users:
If you are running tests via the Eclipse TestNG plugin, you may need to point Eclipse to your ```tesng.xml```.  
The TestNG plugin uses its own ```testng.xml``` by default.

1. ```Project > Properties```
2. Click ```TestNG``` in the left panel
3. Find ```Template XML file```
4. Enter the path to your XML, or browse for it
5. Click Apply

### Data Providers
Although a data provider is not needed, they can be used normally and the driver will still be injected. The driver **must be the first parameter** followed by the data provider parameters:

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

### Other Browsers
Firefox will be used by default. Currently Chrome is the only other supported browser.  
It can be used in two ways:

1. Specify command line parameter
```bash
mvn test -Dbrowser=chrome
```
2. Specify in ```adamant.properties```
#### adamant.properties
```
browser chrome
```

Using Chrome also requires defining the path to ```ChromeDriver``` in similar two ways:
1. Specify command line parameter
```bash
mvn test -Dbrowser=chrome -Dchrome_path=<path to chrome driver>
```
2. Specify in ```adamant.properties```
#### adamant.properties
```
browser chrome
chrome_path <path to chrome driver>
```

#### NOTE: ```adamant.properties``` must be in a **root** resource folder. Except the path may also be defined with ```-DadamantProps=<path to properties file>```. It will still only look in resource folders, but into sub folders as well.

### AdamantDriver Object
The AdamantDriver object is a full wrapper of WebDriver. All the normal WebDriver methods can be used. The only difference is the browser is not opened on instantiation. This is to prevent a large amount of browsers from opening in a data provider with many iterations.

### Manually building the project
```bash
git clone https://github.com/ryanlevell/adamant-driver.git
cd adamant-driver
mvn package -Dmaven.test.skip=true
```
The jar can be found in ```<project root>/target/adamant-driver...jar-with-dependencies.jar```.

### Limitations
1. The TestNG ```@Parameter``` annotation is not currently supported. A workaround is to replace it with ```@DataProvider```.
2. Only ```FirefoxDriver``` and ```ChromeDriver``` can be used. More drivers will be supported.
3. Anything not supported described in [```TODO``` Features](#todo).


### ```TODO``` Features<a name="todo"></a>
1. Additional config options
2. Browser Capabilities
3. Grid Support
4. Proxy Support
5. Screenshots
