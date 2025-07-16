package tests.clients;

import io.restassured.response.Response;
import tests.models.PostData;
import tests.utils.ConfigManager;

/**
 * Client for JSONPlaceholder API
 * 
 * This client provides specific methods for interacting with the JSONPlaceholder API
 * (https://jsonplaceholder.typicode.com) which is commonly used for testing and prototyping.
 * 
 * Available endpoints:
 * - Posts: /posts
 * - Comments: /comments  
 * - Albums: /albums
 * - Photos: /photos
 * - Todos: /todos
 * - Users: /users
 */
public class JsonPlaceholderClient extends BaseApiClient {
    
    private static final String SERVICE_NAME = "jsonplaceholder";
    
    /**
     * Creates a new JsonPlaceholderClient instance
     */
    public JsonPlaceholderClient() {
        super(SERVICE_NAME, ConfigManager.getServiceBaseUrl(SERVICE_NAME));
    }

    // === Posts endpoints ===
    
    /**
     * Gets all posts
     * 
     * @return Response containing all posts
     */
    public Response getAllPosts() {
        return get("/posts");
    }
    
    /**
     * Gets a specific post by ID
     * 
     * @param postId The ID of the post to retrieve
     * @return Response containing the post
     */
    public Response getPost(int postId) {
        return get("/posts/" + postId);
    }
    
    /**
     * Creates a new post
     * 
     * @param postData The post data to create
     * @return Response containing the created post
     */
    public Response createPost(PostData postData) {
        return post("/posts", postData);
    }
    
    /**
     * Updates an existing post
     * 
     * @param postId The ID of the post to update
     * @param postData The updated post data
     * @return Response containing the updated post
     */
    public Response updatePost(int postId, PostData postData) {
        return put("/posts/" + postId, postData);
    }
    
    /**
     * Deletes a post
     * 
     * @param postId The ID of the post to delete
     * @return Response from the delete operation
     */
    public Response deletePost(int postId) {
        return delete("/posts/" + postId);
    }
    
    // === Comments endpoints ===
    
    /**
     * Gets all comments
     * 
     * @return Response containing all comments
     */
    public Response getAllComments() {
        return get("/comments");
    }
    
    /**
     * Gets comments for a specific post
     * 
     * @param postId The ID of the post to get comments for
     * @return Response containing the comments
     */
    public Response getPostComments(int postId) {
        return get("/posts/" + postId + "/comments");
    }
    
    // === Users endpoints ===
    
    /**
     * Gets all users
     * 
     * @return Response containing all users
     */
    public Response getAllUsers() {
        return get("/users");
    }
    
    /**
     * Gets a specific user by ID
     * 
     * @param userId The ID of the user to retrieve
     * @return Response containing the user
     */
    public Response getUser(int userId) {
        return get("/users/" + userId);
    }
    
    /**
     * Gets posts for a specific user
     * 
     * @param userId The ID of the user to get posts for
     * @return Response containing the user's posts
     */
    public Response getUserPosts(int userId) {
        return get("/users/" + userId + "/posts");
    }
    
    // === Todos endpoints ===
    
    /**
     * Gets all todos
     * 
     * @return Response containing all todos
     */
    public Response getAllTodos() {
        return get("/todos");
    }
    
    /**
     * Gets todos for a specific user
     * 
     * @param userId The ID of the user to get todos for
     * @return Response containing the user's todos
     */
    public Response getUserTodos(int userId) {
        return get("/users/" + userId + "/todos");
    }
    
    // === Albums endpoints ===
    
    /**
     * Gets all albums
     * 
     * @return Response containing all albums
     */
    public Response getAllAlbums() {
        return get("/albums");
    }
    
    /**
     * Gets albums for a specific user
     * 
     * @param userId The ID of the user to get albums for
     * @return Response containing the user's albums
     */
    public Response getUserAlbums(int userId) {
        return get("/users/" + userId + "/albums");
    }
} 