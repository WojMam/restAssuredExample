package tests;

import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import tests.models.PostData;
import tests.utils.TestDataLoader;
import tests.utils.CustomAssertions;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class AdvancedApiTest extends BaseTest {

    @Test
    public void testPostWithSchemaValidation() {
        // Load test data
        PostData postData = TestDataLoader.loadJsonData("post_data.json", PostData.class);

        // Perform POST request
        Response response = given()
            .body(postData)
            .when()
            .post("/posts");

        // Verify response
        response.then()
            .statusCode(201);

        // Custom assertions
        CustomAssertions.assertResponseTime(response, 2000);
        CustomAssertions.assertContentType(response, "application/json");
        CustomAssertions.assertHeaderExists(response, "Content-Type");
        CustomAssertions.assertJsonSchema(response, "schemas/post_schema.json");
    }

    @Test
    public void testGetWithAdvancedAssertions() {
        // Perform GET request
        Response response = given()
            .when()
            .get("/posts");

        // Verify response
        response.then()
            .statusCode(200);

        // Custom assertions
        CustomAssertions.assertArraySize(response, "$", greaterThan(0));
        CustomAssertions.assertFieldValue(response, "$[0].userId", greaterThan(0));
        CustomAssertions.assertFieldValue(response, "$[0].title", not(emptyString()));
    }

    @Test
    public void testAuthentication() {
        // Perform authenticated request
        Response response = given()
            .auth()
            .basic(ConfigManager.getAuthUsername(), ConfigManager.getAuthPassword())
            .when()
            .get("/secured/posts");

        // Verify response
        response.then()
            .statusCode(200);

        // Custom assertions
        CustomAssertions.assertResponseTime(response, 1000);
        CustomAssertions.assertContentType(response, "application/json");
    }
} 