package server.users;

import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import pages.BasePage;
import java.util.List;


public class UsersWebDriverHandler extends BasePage implements IUsers {


    public UsersWebDriverHandler(WebDriver driver) {
        super(driver);
    }

    public void createUser(UserTD userData, String errorToCatch) throws Exception {
        if (driver.findElements(By.xpath("//button[text()='Add User']")).size() > 0) {
            clickButton("Add User");
        } else {
            driver.findElement(By.xpath("//ng-transclude[(text()='Add User/Group')]")).click();
            driver.findElement(By.xpath(("//div/label[text()='Local User']"))).click();
        }
        Thread.sleep(1000);
        fillInUserForm(userData, errorToCatch);
    }

    @Override
    public void createSamlUser(UserTD userData, String errorToCatch) throws Exception {
        driver.findElement(By.xpath("//div[2]/drop-button-cmp/button")).click();
        Thread.sleep(1500);
        driver.findElement(By.xpath("//drop-button-cmp/div/label[text()='SSO User']")).click();
        Thread.sleep(1500);
        fillInUserForm(userData, errorToCatch);
    }
    

    public void createDirectoryGroup(String groupMame, String domain, String userRoles) throws Exception {
        clickOnAddUserOrGroup("Directory Group");
        if (domain != null && domain.length() > 0) {
            selectCmpFromDropDown("$ctrl.ldapProvider", domain);
        }
        if (groupMame != null && groupMame.length() > 0) {
            typeEditBox("$ctrl.data.user.username", groupMame);
        }
        setRoles(userRoles);
        takeScreenshot("Before create directory group");
        clickButton("Create");
        takeScreenshot("After create directory group");
        isFreeFromAngularJSErrors();
    }

    public void createDirectoryUser(String username, String domain, String userRoles) throws Exception {
        clickOnAddUserOrGroup("Directory User");
        if (domain != null && domain.length() > 0) {
            selectCmpFromDropDown("$ctrl.ldapProvider", domain);
        }
        if (username != null && username.length() > 0) {
            typeEditBox("$ctrl.data.user.username", username);
        }
        setRoles(userRoles);
        takeScreenshot("Before create directory user");
        clickButton("Create");
        takeScreenshot("After create directory user");
        isFreeFromAngularJSErrors();
    }

    private void clickOnAddUserOrGroup(String userOrGroup) {
        if (driver.findElements(By.xpath("//drop-button-cmp[text()='Add User/Group']")).size() >  0) {
            driver.findElement(By.xpath("//drop-button-cmp[text()='Add User/Group']")).click();
            driver.findElement(By.xpath("//label[contains(@ng-repeat,'(caption,action) in $ctrl.actions') and contains(text(),'" + userOrGroup + "')]")).click();
        }
    }

    //endregion

    //region --------------------- update ---------------------

    public void modifyUser(String username, UserTD userData, String errorToCatch) throws Exception {
        filterByUserName(username,true);
        performAction("Edit");
        fillInUserForm(userData, errorToCatch);
    }

    public void resetUserPassword(String username, String accountName) throws Exception {
        filterByUserName(username,true);
        performAction("Reset Password");
        Thread.sleep(1000);
        clickButton("$ctrl.confirm($event)");
        Thread.sleep(1000);
        takeScreenshot("reset password");
        filterByUserName("",false);
    }

   

    private void performAction(String action) throws Exception {
        driver.findElement(By.xpath("//button[@ng-click='$ctrl.open($mdMenu, $event)']")).click();
        driver.findElement(By.xpath("//div[@class='ng-binding ng-scope md-autofocus' and text()='" + action + "']")).click();
        takeScreenshot(action);
    }

    public boolean actionOnUsers(String username, String action, String message) throws Exception {
        filterByUserName(username,true);

        performAction(action);

        if (message != null && message.length() > 1) {
            takeScreenshot(action + " " + username);
            if (!driver.findElement(By.xpath("//body")).getText().contains(message.trim())) {
                clickButton("$ctrl.close($event)");
                Thread.sleep(1000);
                filterByUserName("",false);
                return false;
            } else {
                clickButton("$ctrl.confirm($event)");
                Thread.sleep(1000);
                filterByUserName("",false);
                return true;
            }
        }
        takeScreenshot("Close popup");
        filterByUserName("",true);
        return false;
    }

    private void fillInUserForm(UserTD userData, String errorToCatch) throws Exception {
        takeScreenshot("Before Filling the parameters");
        setUserName(userData.getUsername());
        setFirstName(userData.getFirstName());
        setLastName(userData.getLastName());
        setDepartment(userData.getDepartment());
        setDescription(userData.getDescription());
        setRoles(userData.getUserRoles());
        setDataRestriction(userData.getRbacValues());
        setAccounts(userData.getAccount());
        takeScreenshot("After Filling the parameters");
        save();
        handleError(errorToCatch);
        isFreeFromAngularJSErrors();
    }

 

    private void setUserName(String userName) {
        if (userName != null && 0 < userName.length()) {
            typeEditBox("$ctrl.data.user.username", userName);
        }
    }

    private void setFirstName(String firstName) {
        if (firstName != null && 0 < firstName.length()) {
            typeEditBox("$ctrl.data.user.firstname", firstName);
        }
    }

    private void setLastName(String lastName) {
        if (lastName != null && 0 < lastName.length()) {
            typeEditBox("$ctrl.data.user.lastname", lastName);
        }
    }

    private void setDepartment(String department) {
        if (department != null && 0 < department.length()) {
            typeEditBox("$ctrl.data.user.department", department);
        }
    }

    private void setDescription(String description) {
        if (description != null && 0 < description.length()) {
            typeEditBox("$ctrl.data.user.description", description);
        }
    }

    private void setGroupAttribute(String groupAttribute) {
        if (groupAttribute != null && 0 < groupAttribute.length()) {
            typeEditBox("$ctrl.data.user.samlGroupAttributeName", groupAttribute);
        }
    }

    private void setGroupValue(String groupValue) {
        if (groupValue != null &&  0 < groupValue.length()) {
            typeEditBox("$ctrl.data.user.samlGroupAttributeValue", groupValue);
        }
    }

    private void setAccounts(String accounts) throws Exception {
        if (accounts != null && accounts.length() > 0 && driver.findElements(By.xpath("//tr/td[text()='Account']")).size() == 1) {
            if (accounts.equalsIgnoreCase("All Accounts")){
                driver.findElement(By.xpath("//select-cmp[@provider='$ctrl.accountProvider']/div[@ng-click='$ctrl.open($event)']")).click();
                driver.findElement(By.xpath("//label[contains(@ng-repeat,'item in $ctrl') and contains(text(),'All Accounts')]")).click();
            }
            if (accounts.contains(",")) {
                driver.findElement(By.xpath("//select-cmp[@provider='$ctrl.accountProvider']/div[@ng-click='$ctrl.open($event)']")).click();
                driver.findElement(By.xpath("//label[contains(@ng-repeat,'item in $ctrl') and contains(text(),'Selected Account(s)')]")).click();
                clickButton("$ctrl.removeAllAccounts()");
                for (String account: accounts.split(",")) {
                    typeEditBox("$ctrl.searchTerm", account);
                    driver.findElement(By.xpath("//div/pre[text()='" + account + "']")).click();
                    clickButton("$ctrl.addAccounts()");
                }
                clickButton("$ctrl.doneSelected()");
            }
        }
        takeScreenshot("After fill the parameters");
    }

    private void setRoles(String userRoles) throws Exception {
        if (userRoles != null && 0 < userRoles.length() && 0 < driver.findElements(By.xpath("//md-select[contains(@ng-model,'selectedRoles')]")).size()) {
            driver.findElement(By.xpath("//md-select[contains(@ng-model,'selectedRoles')]")).click();
            for (String role : userRoles.split(",")) {
                Thread.sleep(2000);
                driver.findElement(By.xpath("//md-content//div[contains(text(),'" + role + "')]")).click();
            }
            clickOnDialogTitle();
        }
        takeScreenshot("Set Roles");
    }

    private void setDataRestriction(String rbacValues) throws InterruptedException {
        driver.findElement(By.xpath("//md-select[contains(@ng-model,'selectedRbacRoles')]")).click();
        for (String value : rbacValues.split(",")) {
            Thread.sleep(2000);
            driver.findElement(By.xpath("//md-content//div[contains(text(),'" + value +"')]")).click();
        }
        clickOnDialogTitle();
    }

    private void clickOnDialogTitle() {
        new Actions(driver).keyUp(driver.findElement(By.xpath("//header[@class='ng-binding']")), Keys.CONTROL).moveByOffset(10,25 ).keyUp(Keys.CONTROL).build().perform();
    }

    private void save() throws InterruptedException {
        clickByJS(driver.findElements(By.xpath("//button[@ng-click='$ctrl.save()']")).get(0));
        int retries = 12;
        while (driver.findElements(By.xpath("//button[@ng-click='$ctrl.save()']")).size() != 0 &&  0 < retries--) {
            Thread.sleep(5 * 1000);
        }
    }

    private void handleError(String errorToCatch) throws Exception {
        if (errorToCatch != null && errorToCatch.length() > 1) {
            Thread.sleep(1000);
            if (!driver.findElement(By.xpath("//body")).getText().contains(errorToCatch)) {
                takeScreenshot("Error To Catch");
                clickByJS(driver.findElements(By.xpath("//button[@ng-click='$ctrl.close()']")).get(0));
                Assert.fail("Failed to find expected error: " + errorToCatch);
            } else {
                clickByJS(driver.findElements(By.xpath("//button[@ng-click='$ctrl.close()']")).get(0));
            }
            takeScreenshot("Close popup");
        }
    }
    //endregion

    //region --------------------- verify ---------------------

    public void verifyUser(String username, String accountName) {
        try {
            String userXpath = "//td[contains(@class,'username')]/span[text()='" + username + "']";
            Assert.assertTrue("Verify - User " + username + " Not exists anymore", driver.findElement(By.xpath(userXpath)).isDisplayed());
        } catch (Exception ignored) { }
    }

    public boolean verifyUserDetails(UserTD userData) {
        try {
            filterByUserName(userData.getUsername(),false);
            takeScreenshot("User: " + userData.getUsername());
            return verifyUserName(userData.getUsername()) &&
                    verifyFullName(userData.getFirstName(), userData.getLastName()) &&
                    verifyDepartment(userData.getDepartment()) &&
                    verifyUserRoles(userData.getUserRoles()) &&
                    verifyUserAccount(userData.getAccount());
        } catch (Exception e) {
            return false;
        }
    }

    private boolean verifyUserName(String userName) {
        if (userName != null && 0 < userName.length()) {
            if (driver.findElements(By.xpath("//span[@class='ng-binding' and text()='" + userName + "']")).size() == 0) {
                return false;
            }
        }
        return true;
    }

    private boolean verifyFullName(String firstName, String lastName) {
        String fullName = firstName + " " + lastName;
        if (firstName != null && 0 < firstName.length()) {
            if (driver.findElements(By.xpath("//div[@title='" + fullName + "']")).size() == 0) {
                return false;
            }
        }
        return true;
    }

    private boolean verifyDepartment(String department) {
        if (department != null && 0 < department.length()) {
            if (driver.findElements(By.xpath("//div[@title='" + department + "']")).size() == 0) {
                return false;
            }
        }
        return true;
    }

    private boolean verifyUserRoles(String userRoles) {
        if (userRoles != null && 0 < userRoles.length()) {
            if (driver.findElements(By.xpath("//div[@title='" + userRoles + "']")).size() == 0) {
                return false;
            }
        }
        return true;
    }

    private boolean verifyUserAccount(String account) {
        if (account != null && 0 < account.length()) {
            if (driver.findElements(By.xpath("//div[@title='" + account + "']")).size() == 0) {
                return false;
            }
        }
        return true;
    }

    //endregion

    public void deleteUser(String username, String accountName, String errorToCatch) throws Exception {
        String btnXpath = "//td[contains(@class,'username')]/a[text()='" + username + "']/../../td[contains(@class,'contextMenu ng-scope')]//div[@ng-click='$ctrl.open()']";
        clickWithXpath(btnXpath);
        btnXpath = "//label[text()='Delete']";
        clickWithXpath(btnXpath);
        clickButton("Delete");

        if (errorToCatch != null && driver.findElement(By.xpath("//body")).getText().contains(errorToCatch)) {
            clickButton("notification");
            Thread.sleep(4000);
            return;
        }
        verifyUser(username, accountName);;
    }

    @Override
    public void enableUser(String username, String accountName, boolean enabled) throws Exception {
        
    }

    private void filterByUserName(String user, boolean toClick) {
        typeEditBox("$ctrl.filter.searchTerm", user);
        if (toClick) {
            pause(1000);
            List<WebElement> usersTable = driver.findElements(By.xpath("/html/body/aternity-app-cmp/router-outlet-cmp/div/div/manage-users-page-cmp/div/section/table/tbody/tr/td[9]/context-menu-cmp/div/div"));
            for (WebElement link : usersTable) {
                if (link.isEnabled() && link.isDisplayed()) {
                    link.click();
                    break;
                }
            }
            pause(1000);
        }
    }

}
