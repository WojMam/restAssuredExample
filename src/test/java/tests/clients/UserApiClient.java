package tests.clients;

import io.restassured.response.Response;
import tests.utils.ConfigManager;

/**
 * Client for User API
 * 
 * This client provides methods for interacting with a user management API.
 * Includes operations for user authentication, profile management, and user data operations.
 * 
 * Available endpoints:
 * - Authentication: /auth/login, /auth/logout
 * - Profile: /profile
 * - Users: /users
 * - Preferences: /preferences
 */
public class UserApiClient extends BaseApiClient {
    
    private static final String SERVICE_NAME = "user";
    
    /**
     * Creates a new UserApiClient instance
     */
    public UserApiClient() {
        super(SERVICE_NAME, ConfigManager.getServiceBaseUrl(SERVICE_NAME));
    }

    // === Authentication endpoints ===
    
    /**
     * Authenticates a user with credentials
     * 
     * @param username The username
     * @param password The password
     * @return Response containing authentication result
     */
    public Response login(String username, String password) {
        String loginJson = String.format("{\"username\":\"%s\",\"password\":\"%s\"}", username, password);
        return post("/auth/login", loginJson);
    }
    
    /**
     * Logs out the current user
     * 
     * @return Response from the logout operation
     */
    public Response logout() {
        return post("/auth/logout", "");
    }
    
    /**
     * Refreshes the authentication token
     * 
     * @param refreshToken The refresh token
     * @return Response containing the new token
     */
    public Response refreshToken(String refreshToken) {
        String tokenJson = String.format("{\"refreshToken\":\"%s\"}", refreshToken);
        return post("/auth/refresh", tokenJson);
    }
    
    // === Profile endpoints ===
    
    /**
     * Gets the current user's profile
     * 
     * @return Response containing the user profile
     */
    public Response getProfile() {
        return customRequest()
            .header("Authorization", "Bearer " + ConfigManager.getServiceAuthToken(SERVICE_NAME))
            .get("/profile");
    }
    
    /**
     * Updates the current user's profile
     * 
     * @param profileData The updated profile data
     * @return Response containing the updated profile
     */
    public Response updateProfile(Object profileData) {
        return customRequest()
            .header("Authorization", "Bearer " + ConfigManager.getServiceAuthToken(SERVICE_NAME))
            .header("Content-Type", "application/json")
            .body(profileData)
            .put("/profile");
    }
    
    // === Users endpoints ===
    
    /**
     * Gets all users (admin endpoint)
     * 
     * @return Response containing all users
     */
    public Response getAllUsers() {
        return customRequest()
            .header("Authorization", "Bearer " + ConfigManager.getServiceAuthToken(SERVICE_NAME))
            .get("/users");
    }
    
    /**
     * Gets a specific user by ID
     * 
     * @param userId The ID of the user to retrieve
     * @return Response containing the user
     */
    public Response getUser(int userId) {
        return customRequest()
            .header("Authorization", "Bearer " + ConfigManager.getServiceAuthToken(SERVICE_NAME))
            .get("/users/" + userId);
    }
    
    /**
     * Creates a new user (admin endpoint)
     * 
     * @param userData The user data to create
     * @return Response containing the created user
     */
    public Response createUser(Object userData) {
        return customRequest()
            .header("Authorization", "Bearer " + ConfigManager.getServiceAuthToken(SERVICE_NAME))
            .header("Content-Type", "application/json")
            .body(userData)
            .post("/users");
    }
    
    /**
     * Updates an existing user (admin endpoint)
     * 
     * @param userId The ID of the user to update
     * @param userData The updated user data
     * @return Response containing the updated user
     */
    public Response updateUser(int userId, Object userData) {
        return customRequest()
            .header("Authorization", "Bearer " + ConfigManager.getServiceAuthToken(SERVICE_NAME))
            .header("Content-Type", "application/json")
            .body(userData)
            .put("/users/" + userId);
    }
    
    /**
     * Deletes a user (admin endpoint)
     * 
     * @param userId The ID of the user to delete
     * @return Response from the delete operation
     */
    public Response deleteUser(int userId) {
        return customRequest()
            .header("Authorization", "Bearer " + ConfigManager.getServiceAuthToken(SERVICE_NAME))
            .delete("/users/" + userId);
    }
    
    // === Preferences endpoints ===
    
    /**
     * Gets user preferences
     * 
     * @return Response containing user preferences
     */
    public Response getPreferences() {
        return customRequest()
            .header("Authorization", "Bearer " + ConfigManager.getServiceAuthToken(SERVICE_NAME))
            .get("/preferences");
    }
    
    /**
     * Updates user preferences
     * 
     * @param preferences The updated preferences
     * @return Response containing the updated preferences
     */
    public Response updatePreferences(Object preferences) {
        return customRequest()
            .header("Authorization", "Bearer " + ConfigManager.getServiceAuthToken(SERVICE_NAME))
            .header("Content-Type", "application/json")
            .body(preferences)
            .put("/preferences");
    }
} 