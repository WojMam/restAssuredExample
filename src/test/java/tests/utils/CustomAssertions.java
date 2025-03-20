package tests.utils;

import io.restassured.response.Response;
import org.hamcrest.Matcher;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

public class CustomAssertions {
    
    /**
     * Asserts that the response time is less than the specified maximum time
     * @param response The response to check
     * @param maxTimeInMillis The maximum allowed response time in milliseconds
     */
    public static void assertResponseTime(Response response, long maxTimeInMillis) {
        assertThat(response.getTime(), lessThan(maxTimeInMillis));
    }

    /**
     * Asserts that the response content type contains the expected content type string
     * @param response The response to check
     * @param expectedContentType The expected content type (e.g. "application/json")
     */
    public static void assertContentType(Response response, String expectedContentType) {
        assertThat(response.getContentType(), containsString(expectedContentType));
    }

    /**
     * Asserts that a specific header exists in the response
     * @param response The response to check
     * @param headerName The name of the header that should exist
     */
    public static void assertHeaderExists(Response response, String headerName) {
        assertThat(response.getHeader(headerName), notNullValue());
    }

    /**
     * Validates that the response body matches a JSON schema
     * @param response The response to validate
     * @param schemaPath The classpath location of the JSON schema file
     */
    public static void assertJsonSchema(Response response, String schemaPath) {
        response.then().body(matchesJsonSchemaInClasspath(schemaPath));
    }

    /**
     * Asserts that an array in the response matches a size condition
     * @param response The response containing the array
     * @param jsonPath The JSON path to the array
     * @param sizeMatcher The matcher to verify the array size
     */
    public static void assertArraySize(Response response, String jsonPath, Matcher<?> sizeMatcher) {
        response.then().body(jsonPath + ".size()", sizeMatcher);
    }

    /**
     * Asserts that an array in the response has the expected size
     * @param response The response containing the array
     * @param jsonPath The JSON path to the array
     * @param expectedSize The expected size of the array
     */
    public static void assertArraySize(Response response, String jsonPath, int expectedSize) {
        response.then().body(jsonPath, hasSize(expectedSize));
    }

    /**
     * Asserts that a field in the response matches a condition
     * @param response The response to check
     * @param jsonPath The JSON path to the field
     * @param matcher The matcher to verify the field value
     */
    public static void assertFieldValue(Response response, String jsonPath, Matcher<?> matcher) {
        response.then().body(jsonPath, matcher);
    }

    /**
     * Asserts that a field in the response equals an expected value
     * @param response The response to check
     * @param jsonPath The JSON path to the field
     * @param expectedValue The expected value of the field
     */
    public static void assertFieldValue(Response response, String jsonPath, Object expectedValue) {
        response.then().body(jsonPath, equalTo(expectedValue));
    }
} 