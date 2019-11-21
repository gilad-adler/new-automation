package tests;

import io.qameta.allure.*;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.HomePage;
import pages.LoginPage;
import utils.Listeners.TestListener;

//In order to eliminate attachment problem for Allure, you should add @Listener line.
//link: https://github.com/allure-framework/allure1/issues/730
@Listeners({ TestListener.class })
@Epic("Regression Tests")
@Feature("Login Tests")
public class LoginTests extends BaseTest {

    //Test Data
    String username = "Administrator";
    String password = "Q!w2e3r4";



    @BeforeClass
    public void setup(){
        driver = startChrome();
    }

    @Test (priority = 0, description="Correct Login")
    @Severity(SeverityLevel.BLOCKER)
    @Description("Test Description: Login As Admin.")
    public void loginAsAdmin () throws Exception{
        LoginPage login = new LoginPage(getDriver());
        HomePage homePage = new HomePage(getDriver());
        homePage.open();
        login.login(username,password);
        homePage.navigateToSetting("Users");
        login.logout();
    }

    @Test (priority = 0, description="Fail setting navigation")
    @Severity(SeverityLevel.BLOCKER)
    @Description("Test Description: try to get invalid setting tab.")
    public void failNavigate () throws Exception{
        LoginPage login = new LoginPage(getDriver());
        HomePage homePage = new HomePage(getDriver());
        homePage.open();
        login.login(username,password);
        homePage.navigateToSetting("xxx");
    }


}