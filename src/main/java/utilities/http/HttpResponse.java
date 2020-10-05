package utilities.http;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.restassured.response.Response;
import org.junit.Assert;

import java.io.IOException;
import java.util.logging.Logger;

import static com.fasterxml.jackson.databind.SerializationFeature.FAIL_ON_EMPTY_BEANS;

public class HttpResponse {
    public static int statusCode;
    public static String message;

    private static final ObjectMapper objectMapper = new ObjectMapper().disable(FAIL_ON_EMPTY_BEANS);

    public HttpResponse(Response response) {
        statusCode = response.getStatusCode();
        message = getMessage(response);
    }

    static public String getMessage(Response response) {
        String message = response.asString();
        return message;
    }

    public static String toJson(Response response) throws IOException {
        if (response.asString().isEmpty()) {
            return response.asString();
        }

        JsonNode json = objectMapper.readTree(response.asString());
        return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(json);
    }

    public static void validate(Response response) throws IOException {
        Assert.assertEquals("Response: " + response.getStatusCode() + " - " + getMessage(response),200, response.getStatusCode());
    }

    public static void validate(Response response, int expectedStatusCode) throws IOException {
        Assert.assertEquals("Response: " + response.getStatusCode() + " - " + getMessage(response) ,expectedStatusCode, response.getStatusCode());
     }

    public static void validate(Response response, int expectedStatusCode, String errorToCatch) throws IOException {
        String message = getMessage(response);

        if (errorToCatch != null && response.getStatusCode() != expectedStatusCode) {
            Assert.assertTrue(message.contains(errorToCatch));
              Assert.assertTrue("Failed to find EXPECTED error: " + errorToCatch + " instead got " + response.asString(),errorToCatch.startsWith("HTTP") && response.getStatusCode() != expectedStatusCode);
        }
        Assert.assertEquals("Response: " + response.getStatusCode() + " - " + message, expectedStatusCode, response.getStatusCode());
    }

    public static void validate(Response response, String expectedError) throws IOException {
        String message = getMessage(response);
        if (expectedError != null && response.getStatusCode() != 200) {
            Assert.assertTrue(message.contains(expectedError));
           } else {
            Assert.assertEquals("Response: " + response.getStatusCode() + " - " + message,200, response.getStatusCode());
         }
    }

    public static boolean validated(Response response, int expectedStatusCode, String errorToCatch) throws IOException {
        String message = getMessage(response);

        if (errorToCatch != null) {

            if (message.contains(errorToCatch)) {
                  return true;
            }
            if (errorToCatch.startsWith("HTTP") && response.getStatusCode() != expectedStatusCode) {
                return false;
            }
        }

        if (response.getStatusCode() == expectedStatusCode) {
            return true;
        } else {
            return false;
        }
    }


}
