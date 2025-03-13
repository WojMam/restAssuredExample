import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class RestApiTest {

    @BeforeAll
    public static void setup() {
        RestAssured.baseURI = "https://jsonplaceholder.typicode.com"; // Przykładowy endpoint
    }

    @Test
    public void testGetRequest() {
        given()
            .when()
            .get("/posts/1")
            .then()
            .statusCode(200)
            .body("id", equalTo(1))
            .body("userId", notNullValue());
    }

    @Test
    public void testPostRequest() {
        Response response = given()
            .header("Content-Type", "application/json")
            .body("{\"title\":\"foo\", \"body\":\"bar\", \"userId\":1}")
            .when()
            .post("/posts");

        response.then()
            .statusCode(201)
            .body("title", equalTo("foo"))
            .body("body", equalTo("bar"))
            .body("userId", equalTo(1));
    }

    @Test
    public void testGetRequestWithLogging() {
        given()
            .log().all() // Loguje całe żądanie (nagłówki, body, itp.)
        .when()
            .get("/posts/1")
        .then()
            .log().body() // Loguje tylko body odpowiedzi
            .statusCode(200);
    }

}
