package tests;

import io.qameta.allure.*;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import pages.HomePage;
import pages.LoginPage;
import utils.Listeners.TestListener;
import utils.ScreenshotUtils;

@Feature("Login Tests")
public class LoginTests extends BaseTest {

    //Test Data
    String username = "Administrator";
    String password = "Q!w2e3r4";

    @After
    public void tierdown(){
        driver.quit();
    }

    @Before
    public void setup(){
        driver = startChrome();
    }

    @Test
    @Severity(SeverityLevel.BLOCKER)
    @Category(tests.LoginTests.class)
    @Description("Test Description: Login As Admin and click Users Tab ")
    public void loginAsAdmin () throws Exception{
        LoginPage login = new LoginPage(getDriver());
        HomePage homePage = new HomePage(getDriver());
        homePage.open();
        login.login(username,password);
        homePage.navigateToSetting("Users");
        Thread.sleep(5 * 1000);
        ScreenshotUtils.screenshot(driver);
        login.logout();
    }



}