# Android Appium Automation Framework
**Enterprise-grade, reusable Android test automation framework**  
Java 17+ · Appium 8 · TestNG · ExtentReports 5 · Log4j2 · Allure

---

## 📁 Project Structure

```
AppiumAndroidFramework/
├── src/
│   ├── main/java/com/company/framework/
│   │   ├── base/
│   │   │   ├── BaseTest.java          ← TestNG lifecycle hooks (driver init/quit)
│   │   │   ├── BasePage.java          ← Parent for all Page Objects
│   │   │   └── DriverManager.java     ← ThreadLocal AndroidDriver management
│   │   ├── config/
│   │   │   ├── FrameworkConfig.java   ← Type-safe config interface (Owner)
│   │   │   └── ConfigManager.java     ← Singleton config accessor
│   │   ├── pages/
│   │   │   ├── LoginPage.java         ← Sample Page Object (replace locators)
│   │   │   └── HomePage.java          ← Sample Page Object (replace locators)
│   │   ├── reports/
│   │   │   └── ExtentReportManager.java ← Thread-safe Extent Reports 5 manager
│   │   ├── utils/
│   │   │   ├── ScreenshotUtil.java    ← Auto screenshot on failure
│   │   │   ├── WaitUtil.java          ← Explicit wait helpers
│   │   │   ├── GestureUtil.java       ← Swipe, scroll, long press (W3C Actions)
│   │   │   └── TestDataUtil.java      ← Fake data via JavaFaker
│   │   ├── constants/
│   │   │   └── AppConstants.java      ← Global constants
│   │   └── listeners/
│   │       ├── TestNGListener.java    ← TestNG event hooks
│   │       └── RetryAnalyzer.java     ← Auto-retry failed tests
│   └── test/
│       ├── java/com/company/tests/
│       │   └── LoginTest.java         ← Sample test class
│       └── resources/
│           ├── config/
│           │   └── config.properties  ← Device & app configuration
│           ├── testng.xml             ← Test suite definition
│           └── log4j2.xml             ← Logging configuration
├── reports/                           ← Generated HTML reports (git-ignored)
├── logs/                              ← Generated log files   (git-ignored)
├── screenshots/                       ← Failure screenshots   (git-ignored)
└── pom.xml
```

---

## ⚙️ Prerequisites

| Tool | Version |
|------|---------|
| Java JDK | 17+ |
| Maven | 3.8+ |
| Node.js | 18+ |
| Appium Server | 2.x |
| UiAutomator2 Driver | latest |
| Android SDK / ADB | installed & on PATH |
| Android Emulator or real device | connected |

### Install Appium
```bash
npm install -g appium
appium driver install uiautomator2
appium                          # start server (default port 4723)
```

---

## 🔧 Configuration — `config.properties`

Edit `src/test/resources/config/config.properties` for **each app/project**:

```properties
appium.server.url=http://127.0.0.1:4723
platform.name=Android
platform.version=13.0
device.name=Pixel_6_API_33
udid=emulator-5554
automation.name=UiAutomator2

# Leave app.path blank if app is already installed
app.path=
app.package=com.example.yourapp
app.activity=com.example.yourapp.MainActivity

implicit.wait=10
explicit.wait=20
screenshot.on.failure=true

environment=QA
tester.name=Automation Team
```

Get device UDID:
```bash
adb devices
```

---

## ▶️ Running Tests

```bash
# Run all tests
mvn clean test

# Run smoke tests only
mvn clean test -Dgroups=smoke

# Run a specific test class
mvn clean test -Dtest=LoginTest

# Generate Allure report
mvn allure:report
```

---

## 📊 Reports

### Extent HTML Report
- Auto-generated at: `reports/<timestamp>/TestReport.html`
- Dark-themed, includes: system info, pass/fail/skip counts, screenshots on failure

### Allure Report
```bash
mvn allure:report
# Opens at: target/site/allure-maven-plugin/index.html
```

### Logs
- Console output: real-time during execution
- File: `logs/framework.log` (rolling, archived by date)
- Test-run log: `logs/test-run.log`

---

## 📝 Adding a New Page Object

1. Create `src/main/java/com/company/framework/pages/YourPage.java`
2. Extend `BasePage`
3. Use `@AndroidFindBy` for locators
4. Add action methods — call `tap()`, `sendKeys()`, `getText()` from `BasePage`

```java
public class YourPage extends BasePage {

    @AndroidFindBy(id = "com.example.app:id/some_button")
    private WebElement someButton;

    public YourPage(AndroidDriver driver) {
        super(driver);
    }

    @Override
    public String getPageName() { return "Your Page"; }

    public void tapSomeButton() {
        tap(someButton);
    }
}
```

---

## 📝 Adding a New Test

1. Create `src/test/java/com/company/tests/YourTest.java`
2. Extend `BaseTest` (driver + reports handled automatically)
3. Add `@Test` methods with Allure annotations

```java
public class YourTest extends BaseTest {

    @Test(description = "Your test description", groups = {"smoke"})
    @Epic("Module") @Feature("Feature") @Story("Story")
    @Severity(SeverityLevel.CRITICAL)
    public void tc001_yourTest() {
        YourPage page = new YourPage(DriverManager.getDriver());
        page.tapSomeButton();
        assertThat(page.isDisplayed()).isTrue();
    }
}
```

4. Register the class in `testng.xml`:
```xml
<class name="com.company.tests.YourTest"/>
```

---

## 🔄 Reusing Across Multiple Android Apps

For each new Android project:
1. Copy this framework (or use as a Git submodule)
2. Update `config.properties` with new `app.package`, `app.activity`, `udid`
3. Replace locators in Page Objects (or add new Page Objects)
4. Add test classes under `src/test/java/com/company/tests/`

---

## 🏗 Framework Stack

| Layer | Technology |
|-------|-----------|
| Driver | Appium 8 + UiAutomator2 |
| Test Runner | TestNG 7 |
| Reporting | ExtentReports 5 (HTML) + Allure |
| Logging | Log4j2 (console + rolling file) |
| Assertions | AssertJ |
| Configuration | Owner (type-safe properties) |
| Test Data | JavaFaker |
| Gestures | W3C Actions API |
| Build | Maven 3 |
| Language | Java 17 |
"# AppiumSamparkApk" 
