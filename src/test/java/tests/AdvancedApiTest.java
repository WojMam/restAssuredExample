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
            .header("Content-Type", "application/json")
            .body(postData)
            .when()
            .post("/posts");

        // Custom assertions
        CustomAssertions.assertResponseTime(response, 5000); // Increased timeout for external API
        CustomAssertions.assertContentType(response, "application/json");
        CustomAssertions.assertHeaderExists(response, "Content-Type");
        CustomAssertions.assertFieldValue(response, "title", equalTo(postData.getTitle()));
        CustomAssertions.assertFieldValue(response, "body", equalTo(postData.getBody()));
        CustomAssertions.assertFieldValue(response, "userId", equalTo(postData.getUserId()));
        CustomAssertions.assertFieldValue(response, "id", notNullValue());
    }

    @Test
    public void testGetWithAdvancedAssertions() {
        // Perform GET request
        Response response = given()
            .when()
            .get("/posts");

        // Custom assertions
        CustomAssertions.assertResponseTime(response, 5000); // Increased timeout for external API
        CustomAssertions.assertContentType(response, "application/json");
        CustomAssertions.assertArraySize(response, "$", greaterThan(0));
        CustomAssertions.assertFieldValue(response, "[0].userId", notNullValue());
        CustomAssertions.assertFieldValue(response, "[0].id", notNullValue());
        CustomAssertions.assertFieldValue(response, "[0].title", not(emptyOrNullString()));
        CustomAssertions.assertFieldValue(response, "[0].body", not(emptyOrNullString()));
    }
} 