package tests;

import io.qameta.allure.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import pages.HomePage;
import pages.LoginPage;
import utilities.CMUtil;
import utils.ScreenshotUtils;

@Feature("Login Tests")
public class LoginTests extends BaseTest {

    @After
    public void tierdown(){
        if (driver != null)
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
        Thread.sleep(1 * 1000);
        ScreenshotUtils.screenshot(driver);
        login.logout();
    }


}