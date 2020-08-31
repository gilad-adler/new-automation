package tests;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.TestException;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import pages.HomePage;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

public class BaseTest {
    public WebDriver driver;


    public WebDriver getDriver() {
        return driver;
    }


    protected WebDriver startChrome() {
        String chromedriverExeName = "chromedriver.exe";
        try {
            String webdriverPath =  new java.io.File( "." ).getCanonicalPath() + "\\src\\test\\java\\resources\\webdriver\\" + chromedriverExeName;
            System.setProperty("webdriver.chrome.driver", webdriverPath);
        } catch (Exception e){
            System.out.println("Failed to get webdriver.chrome.driver fodler");
        }


        DesiredCapabilities capabilities = DesiredCapabilities.chrome();

        ChromeOptions options = new ChromeOptions();
        options.addArguments("test-type");
        options.addArguments("start-maximized"); // see http://stackoverflow.com/questions/25890027/cannot-get-automation-extension-from-timeout-timed-out-receiving-message-from-r
        //options.addArguments("chrome.switches", "--disable-extensions");


        // see work-around specific over https://bugs.chromium.org/p/chromedriver/issues/detail?id=2758
        options.addArguments("--disable-features=site-per-process");
        //////////////////////
        options.setExperimentalOption("excludeSwitches", Arrays.asList("enable-automation"));

        //// Special attributes to allow using flash pages WITHOUT asking the user first
        Map<String, Object> prefs = new HashMap<String, Object>();
        prefs.put("profile.default_content_setting_values.plugins", 1);
        prefs.put("profile.content_settings.plugin_whitelist.adobe-flash-player", 1);
        prefs.put("profile.content_settings.exceptions.plugins.*,*.per_resource.adobe-flash-player", 1);

        prefs.put("safebrowsing.enabled", "false");


        options.setExperimentalOption("prefs", prefs);


        LoggingPreferences logPrefs = new LoggingPreferences();
        logPrefs.enable(LogType.BROWSER, Level.ALL);
        logPrefs.enable(LogType.PERFORMANCE, Level.ALL);
        capabilities.setCapability(CapabilityType.LOGGING_PREFS, logPrefs);
        //options.setBinary("C:\\Program Files (x86)\\Google\\Chrome\\Application\\chrome.exe");
        // set workaround for cases where chrome binary not found
        capabilities.setCapability(ChromeOptions.CAPABILITY, options);
        WebDriver driver = new ChromeDriver(capabilities);
        return driver;
    }
    @AfterClass
    public void teardown() {
        driver.quit();
    }
}