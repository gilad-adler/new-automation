package pages;


import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import ru.yandex.qatools.allure.annotations.Step;

public class HomePage extends BasePage {

    /**Constructor*/
    public HomePage(WebDriver driver) {
        super(driver);
    }

    /**Variables*/
    String baseURL = "http://10.77.224.156/login/";
    private final String SETTING_MENU_XPATH = "//div[@id='highlight-menu-settings']";

    @Step("Open Aternity Webapp")
    public HomePage open() {
        driver.get(baseURL);
        return this;
    }

    @Step("Open tab {0}")
    public void navigateToSetting(String tabName) throws Exception{
        String linkXpath = "//a[contains(@ng-click,'navigate')][text()='" + tabName +"']";
        mouseOver(waitForElementVisible(By.xpath(SETTING_MENU_XPATH), 10));
        Thread.sleep(1000);
        long startTime = System.currentTimeMillis();
        while(driver.findElements(By.xpath(linkXpath)).size()==0 && (System.currentTimeMillis()-startTime)< 20000){
            mouseOver(waitForElementVisible(By.xpath(SETTING_MENU_XPATH), 10));
            Thread.sleep(1000);
        }

        mouseOver(waitForElementVisible(By.xpath(linkXpath), 10));
        Thread.sleep(500);
        String url = driver.getCurrentUrl();
        driver.findElement(By.xpath(linkXpath)).click();
        if (driver.getCurrentUrl().equalsIgnoreCase(url)){
            try {
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", driver.findElement(By.xpath(linkXpath)));
            } catch (Exception e){}

        }
    }

}