package tests;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class BodyFromFileTest {

    @Test
    @Disabled
    public void testPostWithJsonBody() throws IOException {
        String requestBody = readJsonFile("src/test/resources/request-body.json");

        RestAssured.baseURI = "https://example.com/api";

        given()
            .contentType(ContentType.JSON)
            .body(requestBody)
            .log().all()
        .when()
            .post("/login")
        .then()
            .log().body()
            .statusCode(200)
            .body("message", equalTo("Login successful"));
    }

    private String readJsonFile(String path) throws IOException {
        return new String(Files.readAllBytes(Paths.get(path)));
    }
}
