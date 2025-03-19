package tests.utils;

import io.restassured.response.Response;
import org.hamcrest.Matcher;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class CustomAssertions {
    
    public static void assertResponseTime(Response response, long maxTimeInMillis) {
        assertThat(response.getTime(), lessThan(maxTimeInMillis));
    }

    public static void assertContentType(Response response, String expectedContentType) {
        assertThat(response.getContentType(), containsString(expectedContentType));
    }

    public static void assertHeaderExists(Response response, String headerName) {
        assertThat(response.getHeader(headerName), notNullValue());
    }

    public static void assertJsonSchema(Response response, String schemaPath) {
        response.then().body(matchesJsonSchemaInClasspath(schemaPath));
    }

    public static void assertArraySize(Response response, String jsonPath, int expectedSize) {
        response.then().body(jsonPath, hasSize(expectedSize));
    }

    public static void assertFieldValue(Response response, String jsonPath, Matcher<?> matcher) {
        response.then().body(jsonPath, matcher);
    }

    public static void assertFieldValue(Response response, String jsonPath, Object expectedValue) {
        response.then().body(jsonPath, equalTo(expectedValue));
    }
} 