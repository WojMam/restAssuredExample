package tests;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

/**
 * Body From File Test Suite
 * 
 * This class demonstrates how to send API requests with request bodies loaded from files.
 * It's particularly useful when dealing with complex JSON payloads or when you want to
 * maintain test data separately from the test code.
 * 
 * The test uses a JSON file containing post data to create a new post through the API.
 * This approach is beneficial for:
 * - Maintaining test data separately from test code
 * - Reusing test data across multiple tests
 * - Testing with complex JSON structures
 * - Version controlling test data independently
 */
public class BodyFromFileTest extends BaseTest {

    /**
     * Tests POST request with JSON body loaded from file
     * 
     * This test demonstrates how to:
     * 1. Read JSON data from a file
     * 2. Send it as the request body in a POST request
     * 3. Validate the response
     * 
     * Expected behavior:
     * - Request should be successful (201 Created)
     * - Response should contain all required fields
     * - Field values should not be null or empty
     * 
     * @throws IOException if there's an error reading the JSON file
     */
    @Test
    public void testPostWithJsonBody() throws IOException {
        // Load JSON data from file
        String requestBody = readJsonFile("src/test/resources/testdata/post_data.json");

        given()
            .contentType(ContentType.JSON)     // Set content type to JSON
            .body(requestBody)                 // Set request body from file
            .log().all()                       // Log request details for debugging
        .when()
            .post("/posts")                    // Send POST request to create new post
        .then()
            .log().body()                      // Log response body for verification
            .statusCode(201)                   // Verify resource creation (201 Created)
            .body("title", not(emptyOrNullString()))    // Verify title is present
            .body("body", not(emptyOrNullString()))     // Verify body is present
            .body("userId", notNullValue())             // Verify userId is present
            .body("id", notNullValue());                // Verify id is present
    }

    /**
     * Helper method to read JSON file content
     * 
     * @param path The path to the JSON file
     * @return The content of the file as a String
     * @throws IOException if there's an error reading the file
     */
    private String readJsonFile(String path) throws IOException {
        return new String(Files.readAllBytes(Paths.get(path)));
    }
}
