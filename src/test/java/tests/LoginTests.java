package tests;

import io.qameta.allure.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import pages.HomePage;
import pages.LoginPage;


@Feature("Login Tests")
public class LoginTests extends BaseTest {
    

    @Test
    @Severity(SeverityLevel.BLOCKER)
    @Category(tests.LoginTests.class)
    @Description("Test Description: Login As Admin and click Users Tab ")
    public void loginAsAdmin () throws Exception{
        webdriverBase.driver = webdriverBase.startChrome();
        LoginPage login = new LoginPage(webdriverBase.getDriver());
        HomePage homePage = new HomePage(webdriverBase.getDriver());
        homePage.open(baseURL);
        login.loginAsAdmin();
        homePage.navigateToSetting("Users");
        Thread.sleep(1 * 1000);
        homePage.takeScreenshot("see screen");
        login.logout();
        webdriverBase.teardown();
    }


}