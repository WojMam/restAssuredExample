package tests.clients;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.is;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

/**
 * Base API Client class that provides common functionality for all API clients
 * 
 * This class serves as a foundation for all specific API clients and provides:
 * - Common request/response specifications
 * - Logging configuration
 * - Basic HTTP methods (GET, POST, PUT, DELETE)
 * - Authentication handling
 * - Error handling
 */
public abstract class BaseApiClient {
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());
    protected final RequestSpecification requestSpec;
    protected final ResponseSpecification responseSpec;
    protected final String baseUri;
    protected final String serviceName;

    /**
     * Constructor for BaseApiClient
     * 
     * @param serviceName The name of the service (used for configuration lookup)
     * @param baseUri The base URI for this API client
     */
    protected BaseApiClient(String serviceName, String baseUri) {
        this.serviceName = serviceName;
        this.baseUri = baseUri;
        this.requestSpec = createRequestSpecification();
        this.responseSpec = createResponseSpecification();
        
        logger.info("Initialized {} client with base URI: {}", serviceName, baseUri);
    }

    /**
     * Creates the request specification for this client
     * Can be overridden by subclasses for custom configuration
     * 
     * @return RequestSpecification configured for this client
     */
    protected RequestSpecification createRequestSpecification() {
        return new RequestSpecBuilder()
            .setBaseUri(baseUri)
            .setRelaxedHTTPSValidation()
            .addFilter(new RequestLoggingFilter())
            .addFilter(new ResponseLoggingFilter())
            .build();
    }

    /**
     * Creates the response specification for this client
     * Can be overridden by subclasses for custom validation
     * 
     * @return ResponseSpecification configured for this client
     */
    protected ResponseSpecification createResponseSpecification() {
        return new ResponseSpecBuilder()
            .expectStatusCode(anyOf(is(200), is(201), is(204)))
            .build();
    }

    /**
     * Performs a GET request to the specified endpoint
     * 
     * @param endpoint The endpoint path (will be appended to baseUri)
     * @return Response object
     */
    public Response get(String endpoint) {
        logger.debug("GET request to: {}{}", baseUri, endpoint);
        return given(requestSpec)
            .when()
            .get(endpoint)
            .then()
            .spec(responseSpec)
            .extract()
            .response();
    }

    /**
     * Performs a POST request to the specified endpoint
     * 
     * @param endpoint The endpoint path
     * @param body The request body
     * @return Response object
     */
    public Response post(String endpoint, Object body) {
        logger.debug("POST request to: {}{}", baseUri, endpoint);
        return given(requestSpec)
            .header("Content-Type", "application/json")
            .body(body)
            .when()
            .post(endpoint)
            .then()
            .spec(responseSpec)
            .extract()
            .response();
    }

    /**
     * Performs a PUT request to the specified endpoint
     * 
     * @param endpoint The endpoint path
     * @param body The request body
     * @return Response object
     */
    public Response put(String endpoint, Object body) {
        logger.debug("PUT request to: {}{}", baseUri, endpoint);
        return given(requestSpec)
            .header("Content-Type", "application/json")
            .body(body)
            .when()
            .put(endpoint)
            .then()
            .spec(responseSpec)
            .extract()
            .response();
    }

    /**
     * Performs a DELETE request to the specified endpoint
     * 
     * @param endpoint The endpoint path
     * @return Response object
     */
    public Response delete(String endpoint) {
        logger.debug("DELETE request to: {}{}", baseUri, endpoint);
        return given(requestSpec)
            .when()
            .delete(endpoint)
            .then()
            .spec(responseSpec)
            .extract()
            .response();
    }

    /**
     * Gets the base URI for this client
     * 
     * @return The base URI
     */
    public String getBaseUri() {
        return baseUri;
    }

    /**
     * Gets the service name for this client
     * 
     * @return The service name
     */
    public String getServiceName() {
        return serviceName;
    }

    /**
     * Creates a custom request specification for one-off requests
     * This allows for request-specific configuration without affecting the default spec
     * 
     * @return New RequestSpecification based on the default but customizable
     */
    protected RequestSpecification customRequest() {
        return given(requestSpec);
    }
} 