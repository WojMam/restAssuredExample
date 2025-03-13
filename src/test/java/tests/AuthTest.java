package tests;

import io.restassured.RestAssured;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class AuthTest {

    // 🔹 Basic Authentication (username & password)
    @Disabled
    @Test
    public void testBasicAuth() {
        given()
            .auth().basic("username", "password") // Basic Auth
            .log().all()
        .when()
            .get("/secured-endpoint")
        .then()
            .log().body()
            .statusCode(200);
    }

    // 🔹 Bearer Token Authentication (OAuth 2.0, JWT)
    @Disabled
    @Test
    public void testBearerTokenAuth() {
        String token = "your_access_token_here";

        given()
            .header("Authorization", "Bearer " + token)
            .log().all()
        .when()
            .get("/protected-resource")
        .then()
            .log().body()
            .statusCode(200);
    }

    // 🔹 API Key Authentication (Header)
    @Disabled
    @Test
    public void testApiKeyAuthInHeader() {
        given()
            .header("x-api-key", "your_api_key_here")
            .log().all()
        .when()
            .get("/api-endpoint")
        .then()
            .log().body()
            .statusCode(200);
    }

    // 🔹 API Key Authentication (Query Parameter)
    @Disabled
    @Test
    public void testApiKeyAuthInUrl() {
        given()
            .queryParam("api_key", "your_api_key_here")
            .log().all()
        .when()
            .get("/api-endpoint")
        .then()
            .log().body()
            .statusCode(200);
    }
}
