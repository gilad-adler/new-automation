package pages;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

public class BasePage {
    public WebDriver driver;
    public WebDriverWait wait;

    //Constructor
    public BasePage(WebDriver driver) {
        this.driver = driver;
        wait = new WebDriverWait(driver,20);
    }

    public WebDriver getDriver() {
        return driver;
    }


    public void typeEditBox(String editBoxName, String textToType) {
        String xpath = "//input[@name='" + editBoxName + "' or " +
                "@title='" + editBoxName + "' or " +
                "@ng-model='" + editBoxName + "' or " +
                "@id='"+ editBoxName + "'] | " +
                "//input[contains(@ng-model,'"+ editBoxName + "')] | " +
                "//input[contains(@formcontrolname,'"+ editBoxName + "')] | " +
                "//input[contains(@name,'"+ editBoxName + "')] | " +
                "//input[contains(@id,'"+ editBoxName + "')] | " +
                "//textarea[@ng-model='" + editBoxName + "'] | " +
                "//div/div[contains(text(),'"+editBoxName+"')]/../div/input[contains(@ng-model,'commonEditCtrl.quota')]";
        enterTextToField (xpath, textToType);
    }

    public void enterTextToField(String xpath, String textToType) {
        waitForElementClickable(By.xpath(xpath), 10);
        WebElement editBox = driver.findElement(By.xpath(xpath));
        highlightElement(driver, editBox);
        try {
            Thread.sleep(500);
            editBox.clear();
            Thread.sleep(100);
        } catch (InterruptedException e) {
        }
        editBox.sendKeys(textToType);
    }

    public WebElement mouseOver(WebElement webElement) {
        Actions action = new Actions(driver);
        action.moveToElement(webElement).moveToElement(webElement).pause(300).build().perform();
        return webElement;
    }

    public WebElement waitForElementVisible(By by, int timeOutInSeconds) {
        WebDriverWait wait = new WebDriverWait(driver, timeOutInSeconds);
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(by));
        } catch (TimeoutException e) {
        }
        return null;
    }

    public void clickButton(final String btnName) {
        String btnXpath = "//input[@value='" + btnName + "' or text()='" + btnName + "' or @name='" + btnName + "' or @id='" + btnName + "' or @ng-model='" + btnName + "'] " +
                "| //button[contains(@ng-click,'" + btnName + "')] " +
                "| //button[contains(@ng-bind,'" + btnName + "')] " +
                "| //span[contains(@ng-click,'" + btnName + "')] " +
                "| //button/span[text()='" + btnName + "']  " +
                "| //button[contains(text(),'" + btnName + "')] " +
                "| //div[@title='" + btnName + "'] " +
                "| //div[@ng-click='" + btnName + "'] " +
                "| //a[@ng-click='" + btnName + "'] " +
                "| //label[@ng-click='" + btnName + "'] " +
                "| //i[@ng-click='" + btnName + "'] ";

        clickWithXpath(btnXpath);
    }

    protected void clickWithXpath(final String btnXpath) {
        WebDriverWait wait = new WebDriverWait(driver, 10); // for links that become enable by client-side js
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath(btnXpath)));
        WebElement button = driver.findElement(By.xpath(btnXpath));
        highlightElement(driver, button);
        button.click();
    }
    public static void highlightElement(WebDriver driver, WebElement element) {
        if (driver instanceof JavascriptExecutor) {
            ((JavascriptExecutor)driver).executeScript("arguments[0].style.backgroundColor = '#FFFF00'", element);
        }
    }
    public WebElement waitForElementClickable(By by, int timeOutInSeconds) {
        WebDriverWait wait = new WebDriverWait(driver, timeOutInSeconds);
        try {
            return wait.until(ExpectedConditions.elementToBeClickable(by));
        } catch (TimeoutException e) {
        }
        return null;
    }
}