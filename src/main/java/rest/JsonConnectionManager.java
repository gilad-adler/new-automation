package rest;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.response.Header;
import com.jayway.restassured.response.Headers;
import com.jayway.restassured.response.Response;
import utilities.CMUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.jayway.restassured.RestAssured.given;

public class JsonConnectionManager {

    private static JsonConnectionManager instance = new JsonConnectionManager(); // A singleton class object
    private static int expectedStatus = 200;
    private static String serverBaseUrl ;
    private static String JSESSIONID;
    private static String configName;
    public static String SESSION_COOKIE_NAME = "JSESSIONID";
    public static String SESSION_COOKIE_NAME_NEW = "x-aternity.com";
    public final static String ADMIN_PASSWORD = "Q!w2e3r4";

    private JsonConnectionManager() {
    }

    public static String getServerBaseUrl() {
        return serverBaseUrl;
    }

    public void setup(String configName) {
        configName = configName;
        serverBaseUrl = "http://" + CMUtil.getValueFromSut("management.host", configName);
    }
    public static JsonConnectionManager getInstance() { return instance; }

    /**
     * this method logins with RST API and extract JSESSIONID
     */
    public String loginAsAdmin(String domain) {
        domain = domain == null || domain.equalsIgnoreCase("local") ? "Local" : domain;
        String cookie = null;
        String username = "";
        String password = "";
        try {
             username = CMUtil.getValueFromSut("management.adminUser", configName);
             password = CMUtil.getValueFromSut("management.adminUserPassword", configName);

            cookie = login(domain,username, password);
        } catch (Exception e){}
        if (null == cookie) {
            if ( !(username.equals("Administrator") && password.equals(ADMIN_PASSWORD))) {
                cookie = login(domain,"Administrator", ADMIN_PASSWORD);
                if (null == cookie) {
                    return null;
                }
            }
        }
        return cookie;
    }

    public String login(final String domain, final String username, final String password) {
        String loginURL = serverBaseUrl + "/j_spring_security_check?j_skiptableauerror=&j_username=" + username + "&j_password=" + password + "&j_domain=" + domain;
        shouldUseRelaxHTTPValidation(loginURL);

        try {
            Response response = given().redirects().follow(false).post(loginURL);
            int statusCode = response.getStatusCode();
            if (statusCode != 200 && statusCode != 302) {
                printHeaders(response);
                return null;
            }
            if (response.getHeaders().get("Location").toString().contains("login_error")) {
                  return null;
            }
            String cookie = response.getCookie(SESSION_COOKIE_NAME);
             if (cookie == null) {
                cookie = testForNewSessionCookieName(response);
            }
            setJSESSIONID(cookie);
            return cookie;
        } catch (Exception e) {
            return null;
        }
    }

    public String testForNewSessionCookieName (Response response) {
         SESSION_COOKIE_NAME = SESSION_COOKIE_NAME_NEW;
        return response.getCookie(SESSION_COOKIE_NAME);
    }

    public void shouldUseRelaxHTTPValidation(String loginURL) {
        if (loginURL.startsWith("https"))
            RestAssured.useRelaxedHTTPSValidation();
    }

    /**
     * this method logins with User credentials and extract JSESSIONID
     */
    public String loginAsUser(String username, String password, String domain) {
        domain = (domain == null || domain.equalsIgnoreCase("local") ? "Local" : domain);
        String loginURL = serverBaseUrl + "/j_spring_security_check?j_username=" + username + "&j_password=" + password + "&j_domain=" + domain;
         shouldUseRelaxHTTPValidation(loginURL);
        Response response = given().post(loginURL);//TODO when changing to POST, pass parameters as form parameters
          int statusCode = response.getStatusCode();
        if (statusCode != expectedStatus) {
            printHeaders(response);
        }

        String cookie = response.getCookie(SESSION_COOKIE_NAME);
        if (cookie == null) {
            cookie = testForNewSessionCookieName(response);
        }
        setJSESSIONID(cookie);
        return cookie;
    }

    private List<String> getHeadersAsList(final Headers headers) {
        List<String> headersList = new ArrayList<>();
        for (Header header : headers) {
            headersList.add(header.toString());
        }
        return headersList;
    }

    private void printHeaders(Response response) {
        final Headers headers = response.getHeaders();
        final List<String> headersAsList = getHeadersAsList(headers);
    }

    public String getJSESSIONID() {
        return JSESSIONID;
    }

    public void setJSESSIONID(String jSESSIONID) {
        JSESSIONID = jSESSIONID;
    }

    public Map<String,String> getSessionAsMap() {
        Map<String, String> jsessionCookieMap = new HashMap<>();
        jsessionCookieMap.put(SESSION_COOKIE_NAME, JSESSIONID);
        return jsessionCookieMap;
    }
}
