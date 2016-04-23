# **adamant-driver**

AdamantDriver is a library combining Selenium WebDriver + TestNG. It enables a user to begin writing tests very quickly with very little boiler-plate code.

The first 3 steps will get you started:

1. **[Add the adamant-driver jar to your project](#add-jar)**
2. **[Inject a WebDriver object as a test parameter](#inject-wd)**
3. **[Add AdamantListener to testng.xml](#add-list)**
4. [Using a DataProvider](#dp)
5. [AdamantDriver Parameters](#test-params)
6. [Manually building the project](#manual-build)
7. [Limitations](#limitations)
8. [TODO Features](#todo)
9. [Help](#help)

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

## 3. Add ```AdamantListener``` to [```testng.xml```](http://testng.org/doc/documentation-main.html#testng-xml)<a name="add-list"></a>
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
1. The testng.xml ```<suite>``` tag. (Note: Parameters in a ```<test>``` tag will not be used)
2. From the command line.

#### 1. testng.xml usage:
```XML
...
  <suite name="suite-name">
    <parameter name="parameter-name" value="parameter-value" />
    ...
  </suite>
...
```

#### 2. command line usage:
```BASH
mvn test -Dparametername=parametervalue
```

#### Parameters:
| parameter          | values                             | default | description                    |
|--------------------|------------------------------------|---------|--------------------------------|
| browser            | firefox, chrome                    | firefox | The driver to use for testing. |
| chrome_path        | *&lt;full path to chrome driver&gt;* | none    | The path to the chrome driver. |
| capabilities_class | *&lt;fully qualified class&gt;*      | none    | The class that implements the DriverCapabilties interface. |
| options_class      | *&lt;fully qualified class&gt;*      | none    | The class that implements the DriverOptions interface. |
| proxy_class        | *&lt;fully qualified class&gt;*      | none    | The class that implements the DriverProxy interface. |
| use_grid           | true, false                        | false   | Whether to run tests locally or on the grid. |
| grid_url           | *&lt;your grid URL&gt;*              | none    | The URL to the Selenium grid hub. |


## 6. Manually building the project<a name="manual-build"></a>
---
```bash
git clone https://github.com/ryanlevell/adamant-driver.git
cd adamant-driver
mvn package -Dmaven.test.skip=true
```
The jar can be found in ```<project root>/target/adamant-driver...jar-with-dependencies.jar```.

## 7. Limitations<a name="limitations"></a>
---
1. The TestNG ```@Parameter``` annotation cannot be used with a WebDriver test. This is because AdamantDriver injects a data provider to all WebDriver tests.
2. Only ```FirefoxDriver``` and ```ChromeDriver``` can be used. More browsers will be added.
3. Anything not supported described in [TODO Features](#todo).


## 8. TODO Features<a name="todo"></a>
---
1. Screenshots + path/folder
2. Retry analyzer + boolean whether to remove retries from results

## 9. Help<a name="help"></a>
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

#### Can ```@Listener(AdamantListener)``` instead of the ```<listener>``` tag?
No, AdamantListener implements ```IAnnotationTransformer```.  
The [documentation](http://testng.org/doc/documentation-main.html#listeners-testng-xml) states:
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
