package utilities;

import com.jayway.restassured.path.json.JsonPath;
import com.jayway.restassured.response.Response;


import static com.jayway.restassured.RestAssured.given;

public class CMUtil {

    public static String CM = "http://configuration-manager.lab.aternity.com";

    public static String getConfigurationStr(final String configName) {
        String url = CM + "/configurations/" + configName;
        Response response = given().get(url).andReturn();
        int statusCode = response.getStatusCode();
        String respText = response.asString();
        if (statusCode != 200 || respText.contains("Could not find configuration")) {
            if (respText.contains("Could not find configuration"))
                return null;
            return null;
        }
        return respText;
    }

    public static String getValueFromSut(String path, String configName) {
        final String configurationStr = getConfigurationStr(configName);
        return JsonPath.from(configurationStr).getString(path);
    }

}
