package tests;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIf;

import io.restassured.response.Response;
import tests.clients.ClientFactory;
import tests.models.PostData;
import tests.utils.TestDataLoader;

/**
 * Multi-API Test Suite
 * 
 * This class demonstrates how to use multiple API clients in a single test scenario.
 * It shows the power of the Service/Client pattern where one test can interact
 * with multiple different APIs seamlessly.
 * 
 * Example scenarios:
 * - E-commerce workflow: User registration → Product browsing → Payment processing → Notification
 * - Content management: Post creation → User management → Notification dispatch
 * - Business process: Data retrieval → Processing → User updates → Notifications
 */
public class MultiApiTest extends BaseTest {

    /**
     * Test that demonstrates interaction with multiple APIs in a single workflow
     * 
     * This test simulates a complete user workflow:
     * 1. Get user data from JSONPlaceholder API
     * 2. Create a post on behalf of the user
     * 3. Simulate user login (if User API is available)
     * 4. Process a payment (if Payment API is available)
     * 5. Send notifications (if Notification API is available)
     */
    @Test
    public void testMultiApiWorkflow() {
        // Step 1: Get user data from JSONPlaceholder API
        logger.info("Step 1: Getting user data from JSONPlaceholder API");
        Response userResponse = jsonPlaceholderClient.getUser(1);
        
        userResponse.then()
            .statusCode(200)
            .body("id", equalTo(1))
            .body("name", notNullValue())
            .body("email", notNullValue());
        
        String userName = userResponse.jsonPath().getString("name");
        String userEmail = userResponse.jsonPath().getString("email");
        logger.info("Retrieved user: {} ({})", userName, userEmail);
        
        // Step 2: Create a post on behalf of the user
        logger.info("Step 2: Creating a post for the user");
        PostData postData = TestDataLoader.loadJsonData("post_data.json", PostData.class);
        
        Response postResponse = jsonPlaceholderClient.createPost(postData);
        postResponse.then()
            .statusCode(201)
            .body("title", equalTo(postData.getTitle()))
            .body("userId", equalTo(postData.getUserId()));
        
        int postId = postResponse.jsonPath().getInt("id");
        logger.info("Created post with ID: {}", postId);
        
        // Step 3: Simulate user authentication (if User API is available)
        if (userApiClient != null) {
            logger.info("Step 3: Simulating user authentication");
            try {
                Response loginResponse = userApiClient.login("testuser", "testpass");
                // Note: This will likely fail since it's a mock API, but demonstrates the pattern
                logger.info("User login attempted - Status: {}", loginResponse.getStatusCode());
            } catch (Exception e) {
                logger.info("User login failed as expected (mock API): {}", e.getMessage());
            }
        } else {
            logger.info("Step 3: Skipped - User API not configured");
        }
        
        // Step 4: Process a payment (if Payment API is available)
        if (paymentApiClient != null) {
            logger.info("Step 4: Processing a payment");
            try {
                String paymentData = "{\"amount\": 99.99, \"currency\": \"USD\", \"userId\": 1}";
                Response paymentResponse = paymentApiClient.processPayment(paymentData);
                logger.info("Payment processed - Status: {}", paymentResponse.getStatusCode());
            } catch (Exception e) {
                logger.info("Payment processing failed as expected (mock API): {}", e.getMessage());
            }
        } else {
            logger.info("Step 4: Skipped - Payment API not configured");
        }
        
        // Step 5: Send notifications (if Notification API is available)
        if (notificationApiClient != null) {
            logger.info("Step 5: Sending notifications");
            try {
                String notificationData = String.format(
                    "{\"userId\": 1, \"message\": \"Your post '%s' has been created successfully!\", \"type\": \"info\"}",
                    postData.getTitle()
                );
                Response notificationResponse = notificationApiClient.sendNotification(notificationData);
                logger.info("Notification sent - Status: {}", notificationResponse.getStatusCode());
            } catch (Exception e) {
                logger.info("Notification sending failed as expected (mock API): {}", e.getMessage());
            }
        } else {
            logger.info("Step 5: Skipped - Notification API not configured");
        }
        
        logger.info("Multi-API workflow completed successfully");
    }
    
    /**
     * Test that demonstrates parallel API calls to different services
     * 
     * This test shows how to make concurrent calls to different APIs
     * and aggregate the results.
     */
    @Test
    public void testParallelApiCalls() {
        logger.info("Testing parallel API calls to different services");
        
        // Make multiple calls to JSONPlaceholder API
        Response postsResponse = jsonPlaceholderClient.getAllPosts();
        Response usersResponse = jsonPlaceholderClient.getAllUsers();
        Response todosResponse = jsonPlaceholderClient.getAllTodos();
        
        // Verify all responses
        postsResponse.then()
            .statusCode(200)
            .body("size()", greaterThan(0));
        
        usersResponse.then()
            .statusCode(200)
            .body("size()", greaterThan(0));
        
        todosResponse.then()
            .statusCode(200)
            .body("size()", greaterThan(0));
        
        // Log response sizes
        int postsCount = postsResponse.jsonPath().getList("$").size();
        int usersCount = usersResponse.jsonPath().getList("$").size();
        int todosCount = todosResponse.jsonPath().getList("$").size();
        
        logger.info("Retrieved {} posts, {} users, {} todos", postsCount, usersCount, todosCount);
        
        // Verify we got some data
        assertTrue(postsCount > 0, "Should have retrieved posts");
        assertTrue(usersCount > 0, "Should have retrieved users");
        assertTrue(todosCount > 0, "Should have retrieved todos");
    }
    
    /**
     * Test that demonstrates conditional API usage based on service availability
     * 
     * This test shows how to write tests that gracefully handle missing services
     * and only run certain operations when services are available.
     */
    @Test
    public void testConditionalApiUsage() {
        logger.info("Testing conditional API usage based on service availability");
        
        // Always test JSONPlaceholder (it's our main API)
        Response response = jsonPlaceholderClient.getPost(1);
        response.then()
            .statusCode(200)
            .body("id", equalTo(1));
        
        // Conditionally test other APIs
        if (isServiceAvailable("user")) {
            logger.info("User API is available - testing user operations");
            assertNotNull(userApiClient, "User API client should be initialized");
            // Add user-specific tests here
        } else {
            logger.info("User API is not available - skipping user operations");
        }
        
        if (isServiceAvailable("payment")) {
            logger.info("Payment API is available - testing payment operations");
            assertNotNull(paymentApiClient, "Payment API client should be initialized");
            // Add payment-specific tests here
        } else {
            logger.info("Payment API is not available - skipping payment operations");
        }
        
        if (isServiceAvailable("notification")) {
            logger.info("Notification API is available - testing notification operations");
            assertNotNull(notificationApiClient, "Notification API client should be initialized");
            // Add notification-specific tests here
        } else {
            logger.info("Notification API is not available - skipping notification operations");
        }
        
        logger.info("Conditional API usage test completed");
    }
    
    /**
     * Test that runs only when all services are configured
     * 
     * This test demonstrates using JUnit's conditional execution features
     * to run tests only when all required services are available.
     */
    @Test
    @EnabledIf("areAllServicesAvailable")
    public void testCompleteWorkflowWithAllServices() {
        logger.info("Testing complete workflow with all services available");
        
        // This test will only run if all services are configured
        assertNotNull(jsonPlaceholderClient, "JSONPlaceholder client should be available");
        assertNotNull(userApiClient, "User API client should be available");
        assertNotNull(paymentApiClient, "Payment API client should be available");
        assertNotNull(notificationApiClient, "Notification API client should be available");
        
        // Perform comprehensive workflow testing here
        logger.info("All services are available - running complete workflow test");
        
        // Example: Get user, create post, process payment, send notification
        // This would be the place for end-to-end testing
    }
    
    /**
     * Utility method for conditional test execution
     * 
     * @return true if all services are available
     */
    static boolean areAllServicesAvailable() {
        return ClientFactory.isServiceAvailable("jsonplaceholder") &&
               ClientFactory.isServiceAvailable("user") &&
               ClientFactory.isServiceAvailable("payment") &&
               ClientFactory.isServiceAvailable("notification");
    }
} 