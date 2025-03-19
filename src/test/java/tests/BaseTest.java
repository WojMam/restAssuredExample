package tests;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.AfterAll;
import tests.utils.ConfigManager;

public class BaseTest {
    protected static RequestSpecification requestSpec;
    protected static ResponseSpecification responseSpec;

    @BeforeAll
    public static void setup() {
        // Setup request specification
        requestSpec = new RequestSpecBuilder()
            .setBaseUri(ConfigManager.getBaseUrl("dev"))
            .setRelaxedHTTPSValidation()
            .addFilter(new RequestLoggingFilter())
            .addFilter(new ResponseLoggingFilter())
            .build();

        // Setup response specification
        responseSpec = new ResponseSpecBuilder()
            .expectStatusCode(200)
            .build();

        // Configure RestAssured
        RestAssured.requestSpecification = requestSpec;
        RestAssured.responseSpecification = responseSpec;
    }

    @AfterAll
    public static void cleanup() {
        // Reset RestAssured specifications
        RestAssured.reset();
    }
} 