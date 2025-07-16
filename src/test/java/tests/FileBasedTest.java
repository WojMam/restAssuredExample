package tests;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import tests.models.PostData;
import tests.utils.TestDataLoader;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class FileBasedTest {
    private static final String BASE_URL = "https://jsonplaceholder.typicode.com";

    @BeforeAll
    public static void setup() {
        RestAssured.baseURI = BASE_URL;
    }

    @Test
    public void testPostWithJsonData() {
        // Load test data from JSON file
        PostData postData = TestDataLoader.loadJsonData("post_data.json", PostData.class);

        // Perform POST request with loaded data
        Response response = given()
            .header("Content-Type", "application/json")
            .body(postData)
            .when()
            .post("/posts");

        // Verify response
        response.then()
            .statusCode(201)
            .body("title", equalTo(postData.getTitle()))
            .body("body", equalTo(postData.getBody()))
            .body("userId", equalTo(postData.getUserId()));
    }

    @Test
    public void testPostWithRawContent() {
        // Load raw content from text file
        String rawContent = TestDataLoader.loadFileContent("raw_content.txt");

        // Perform POST request with raw content
        Response response = given()
            .header("Content-Type", "text/plain")
            .body(rawContent)
            .when()
            .post("/posts");

        // Verify response
        response.then()
            .statusCode(201);
    }

    @Test
    public void testGetAndCompareWithFileData() {
        // Load expected data from JSON file
        PostData expectedData = TestDataLoader.loadJsonData("post_data.json", PostData.class);

        // Perform GET request
        Response response = given()
            .when()
            .get("/posts/1");

        // Verify response matches file data
        response.then()
            .statusCode(200)
            .body("userId", equalTo(expectedData.getUserId()));
    }
} 