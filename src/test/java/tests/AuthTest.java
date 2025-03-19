package tests;

import io.restassured.RestAssured;
import org.junit.jupiter.api.Test;
import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

/**
 * Authentication Test Suite
 * 
 * This class demonstrates different types of authentication methods that can be used in REST API testing.
 * Since we're using JSONPlaceholder API which doesn't actually validate authentication,
 * we're using dummy credentials to demonstrate the syntax and structure of different auth methods.
 * 
 * The tests cover:
 * - Basic Authentication (username/password)
 * - Bearer Token Authentication (OAuth 2.0/JWT)
 * - API Key Authentication (Header)
 * - API Key Authentication (Query Parameter)
 */
public class AuthTest extends BaseTest {

    /**
     * Tests Basic Authentication
     * 
     * Demonstrates how to use username/password authentication in API requests.
     * The test uses dummy credentials since JSONPlaceholder doesn't validate them.
     * 
     * Expected behavior:
     * - Request should be successful (200 OK)
     * - Response should contain a post with ID 1
     */
    @Test
    public void testBasicAuth() {
        given()
            .auth().basic("user", "pass") // Basic Auth credentials (dummy values)
            .log().all()                  // Log request details for debugging
        .when()
            .get("/posts/1")             // GET request to fetch a specific post
        .then()
            .log().body()                // Log response body for verification
            .statusCode(200)             // Verify successful response
            .body("id", equalTo(1));     // Verify correct post ID
    }

    /**
     * Tests Bearer Token Authentication
     * 
     * Demonstrates how to use Bearer token authentication (commonly used with OAuth 2.0 and JWT).
     * The test uses a dummy token since JSONPlaceholder doesn't validate tokens.
     * 
     * Expected behavior:
     * - Request should be successful (200 OK)
     * - Response should contain a post with ID 2
     */
    @Test
    public void testBearerTokenAuth() {
        String token = "dummy_token";     // Dummy token for demonstration

        given()
            .header("Authorization", "Bearer " + token)  // Add Bearer token to request header
            .log().all()                                 // Log request details
        .when()
            .get("/posts/2")                            // GET request to fetch a specific post
        .then()
            .log().body()                               // Log response body
            .statusCode(200)                           // Verify successful response
            .body("id", equalTo(2));                   // Verify correct post ID
    }

    /**
     * Tests API Key Authentication via Header
     * 
     * Demonstrates how to use API key authentication by sending the key in the request header.
     * The test uses a dummy key since JSONPlaceholder doesn't validate API keys.
     * 
     * Expected behavior:
     * - Request should be successful (200 OK)
     * - Response should contain a post with ID 3
     */
    @Test
    public void testApiKeyAuthInHeader() {
        given()
            .header("x-api-key", "dummy_key")  // Add API key to custom header
            .log().all()                       // Log request details
        .when()
            .get("/posts/3")                  // GET request to fetch a specific post
        .then()
            .log().body()                     // Log response body
            .statusCode(200)                  // Verify successful response
            .body("id", equalTo(3));          // Verify correct post ID
    }

    /**
     * Tests API Key Authentication via Query Parameter
     * 
     * Demonstrates how to use API key authentication by sending the key as a query parameter.
     * The test uses a dummy key since JSONPlaceholder doesn't validate API keys.
     * 
     * Expected behavior:
     * - Request should be successful (200 OK)
     * - Response should contain a post with ID 4
     */
    @Test
    public void testApiKeyAuthInUrl() {
        given()
            .queryParam("api_key", "dummy_key")  // Add API key as query parameter
            .log().all()                         // Log request details
        .when()
            .get("/posts/4")                    // GET request to fetch a specific post
        .then()
            .log().body()                       // Log response body
            .statusCode(200)                    // Verify successful response
            .body("id", equalTo(4));            // Verify correct post ID
    }
}
