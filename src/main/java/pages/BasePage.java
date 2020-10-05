package pages;

import com.google.common.io.Files;
import io.qameta.allure.Attachment;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.io.IOException;

public class BasePage {
    public static WebDriver driver;
    public WebDriverWait wait;

    //Constructor
    public BasePage(WebDriver driver) {
        this.driver = driver;
        wait = new WebDriverWait(driver,20);
    }

    public WebDriver getDriver() {
        return driver;
    }

    
    @Attachment(value = "{0}", type = "image/png")
    public static byte[] takeScreenshot( String linkMessage)/* throws IOException */ {
        try {
            File screen = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            return Files.toByteArray(screen);
        } catch (IOException e) {
            return null;
        }
    }

    public void selectCmpFromDropDown(String provider, String optionToSelect) {
        String selectXpath = "//select-cmp[@provider='" + provider + "']";
        if (driver.findElements(By.xpath(selectXpath)).size() > 0) {
            WebElement selectCmp = driver.findElement(By.xpath(selectXpath));
            selectCmp.click();
            selectCmpField(selectXpath, optionToSelect);
        }
    }
    public void selectCmpField(String selectCmpProviderXpath, String optionToSelect ) {
        String selectItem = selectCmpProviderXpath + "//label[contains(@ng-repeat,'item in $ctrl.provider.getItems()') and contains(text(),'" + optionToSelect + "')]";
        if (driver.findElements(By.xpath(selectItem)).size() > 0) {
            driver.findElement(By.xpath(selectItem)).click();
        } 
    }

    public static void clickByJS(WebElement element) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
    }

    
    protected void pause(long milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException ignored) { }
    }
    /**
     * Use this method to check whether AngularJS error exists over Page by @ng-bind='error'
     */
    protected boolean isFreeFromAngularJSErrors() {
        try {
            Thread.sleep(1000);
            //found an error on screen
            return !driver.findElement(By.xpath("//span[@ng-bind='error']")).isDisplayed();
        } catch (Exception e) {
            return true;
        }
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

    public boolean textExistsOnScreen(String expectedText){
        return driver.getPageSource().contains(expectedText);
    }
}