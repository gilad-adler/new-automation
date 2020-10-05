package tests;
import org.junit.Before;
import pages.WebdriverBase;
import rest.JsonConnectionManager;
import server.Configuration;
import utilities.CMUtil;


public class BaseTest {
    String configName = "NA";

    static String username = null;
    static String password = null;
    static String baseURL = null;
    protected WebdriverBase webdriverBase;
    
    protected void loginToAcquireJSession(){
        JsonConnectionManager JsonConManager = JsonConnectionManager.getInstance();
        JsonConManager.loginAsUser(username, password, null);
    }
    
    @Before
    public void setupConfiguartion(){
        webdriverBase = new WebdriverBase();
        configName = "auto-saas-sanity-227-95";
        if (System.getProperty("configName") != null){
            configName = System.getProperty("configName");
        }
        if (configName.equalsIgnoreCase("na")){
            System.out.println("Please set configuration using  -DconfigName");
            System.exit(-1);
        }
        System.out.println("Using configuration " + configName);
        Configuration.setConfigName(configName);
        username = CMUtil.getValueFromSut("management.adminUser", configName);
        password = CMUtil.getValueFromSut("management.adminUserPassword", configName);
        baseURL = "http://" + CMUtil.getValueFromSut("management.host", configName) + "/login";
        JsonConnectionManager JsonConManager = JsonConnectionManager.getInstance();
        JsonConManager.setup(configName);
    }


}