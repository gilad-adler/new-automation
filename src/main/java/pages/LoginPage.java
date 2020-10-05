package pages;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;


public class LoginPage extends BasePage {

    private final String SETTING_USER_MENU_XPATH = "//div[@class='anchor menu-anchor-user']";
    private final String adminUsername = "Administrator ";
    private final String adminPassword = "Q!w2e3r4 ";
    
    public LoginPage(WebDriver driver) {
        super(driver);
    }

    /**Page Methods*/
    @Step("Login with username: {0}, password: {1}, for method: {method} step...")
    public void login(String username, String password) {
        typeEditBox("j_username", username);
        typeEditBox("j_password", password);
        clickButton("Sign In");
    }

    @Step("Login As Admin with username,password taken fron configuration")
    public void loginAsAdmin() {
        typeEditBox("j_username", adminUsername);
        typeEditBox("j_password", adminPassword);
        clickButton("Sign In");
    }
    
    @Step("Logout from Application")
    public void logout() throws Exception {
        String linkXpath = "//a[@id='logOut']";
        mouseOver(waitForElementVisible(By.xpath(SETTING_USER_MENU_XPATH), 10));
        WebElement elementToClick = driver.findElement(By.xpath(linkXpath));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", elementToClick);
    }
}