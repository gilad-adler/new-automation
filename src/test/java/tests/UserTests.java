package tests;

import io.qameta.allure.Description;
import org.junit.Assert;
import org.junit.Test;
import pages.HomePage;
import pages.LoginPage;
import server.users.IUsers;
import server.users.UserTD;
import server.users.UsersFactory;
import test.TestMethod;

import java.util.UUID;

public class UserTests extends  BaseTest{

    @Test
    @Description("Test Description: User actions - API & GUI")
     public void userActions() throws Exception {
        loginToAcquireJSession();
        String accountName = "Temp";
        IUsers users = UsersFactory.getUsers(TestMethod.Json);
        String uuid= UUID.randomUUID().toString().substring(0,6);
        String newUser= UUID.randomUUID().toString().substring(0,6) +"@automation.com";
        UserTD userData = new UserTD(uuid,uuid,uuid,"QA", "",accountName, "", null);
        users.createUser(userData, "Please select at least one role");

        userData = new UserTD(newUser,newUser,newUser,"QA", "",accountName, "View Dashboards", null);
        users.createUser(userData, null);
        webdriverBase.driver = webdriverBase.startChrome();
        LoginPage login = new LoginPage(webdriverBase.getDriver());
        HomePage homePage = new HomePage(webdriverBase.getDriver());
        homePage.open(baseURL);
        login.loginAsAdmin();
        homePage.navigateToSetting("Users");
        Thread.sleep(1 * 1000);
        homePage.takeScreenshot("new user");
        Assert.assertTrue(homePage.textExistsOnScreen(newUser)); 
        login.logout();
        webdriverBase.teardown();
        users.deleteUser(newUser,accountName,null);
    }
    
}
