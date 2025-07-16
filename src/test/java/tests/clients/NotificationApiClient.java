package tests.clients;

import io.restassured.response.Response;
import tests.utils.ConfigManager;

/**
 * Client for Notification API
 * 
 * This client provides methods for interacting with a notification service API.
 * Includes operations for sending notifications, managing notification preferences,
 * and retrieving notification history.
 * 
 * Available endpoints:
 * - Notifications: /notifications
 * - Templates: /templates
 * - Preferences: /preferences
 * - History: /history
 */
public class NotificationApiClient extends BaseApiClient {
    
    private static final String SERVICE_NAME = "notification";
    
    /**
     * Creates a new NotificationApiClient instance
     */
    public NotificationApiClient() {
        super(SERVICE_NAME, ConfigManager.getServiceBaseUrl(SERVICE_NAME));
    }

    // === Notification endpoints ===
    
    /**
     * Sends a notification
     * 
     * @param notificationData The notification data
     * @return Response containing the notification result
     */
    public Response sendNotification(Object notificationData) {
        return customRequest()
            .header("Authorization", "Bearer " + ConfigManager.getServiceAuthToken(SERVICE_NAME))
            .header("Content-Type", "application/json")
            .body(notificationData)
            .post("/notifications");
    }
    
    /**
     * Gets notification details by ID
     * 
     * @param notificationId The ID of the notification
     * @return Response containing the notification details
     */
    public Response getNotification(String notificationId) {
        return customRequest()
            .header("Authorization", "Bearer " + ConfigManager.getServiceAuthToken(SERVICE_NAME))
            .get("/notifications/" + notificationId);
    }
    
    /**
     * Gets all notifications for a user
     * 
     * @param userId The ID of the user
     * @return Response containing the user's notifications
     */
    public Response getUserNotifications(int userId) {
        return customRequest()
            .header("Authorization", "Bearer " + ConfigManager.getServiceAuthToken(SERVICE_NAME))
            .get("/notifications?userId=" + userId);
    }
    
    /**
     * Marks a notification as read
     * 
     * @param notificationId The ID of the notification to mark as read
     * @return Response from the mark as read operation
     */
    public Response markAsRead(String notificationId) {
        return customRequest()
            .header("Authorization", "Bearer " + ConfigManager.getServiceAuthToken(SERVICE_NAME))
            .post("/notifications/" + notificationId + "/read", "");
    }
    
    /**
     * Deletes a notification
     * 
     * @param notificationId The ID of the notification to delete
     * @return Response from the delete operation
     */
    public Response deleteNotification(String notificationId) {
        return customRequest()
            .header("Authorization", "Bearer " + ConfigManager.getServiceAuthToken(SERVICE_NAME))
            .delete("/notifications/" + notificationId);
    }
    
    // === Bulk operations ===
    
    /**
     * Sends bulk notifications
     * 
     * @param bulkNotificationData The bulk notification data
     * @return Response containing the bulk operation result
     */
    public Response sendBulkNotifications(Object bulkNotificationData) {
        return customRequest()
            .header("Authorization", "Bearer " + ConfigManager.getServiceAuthToken(SERVICE_NAME))
            .header("Content-Type", "application/json")
            .body(bulkNotificationData)
            .post("/notifications/bulk");
    }
    
    /**
     * Marks all notifications as read for a user
     * 
     * @param userId The ID of the user
     * @return Response from the mark all as read operation
     */
    public Response markAllAsRead(int userId) {
        return customRequest()
            .header("Authorization", "Bearer " + ConfigManager.getServiceAuthToken(SERVICE_NAME))
            .post("/notifications/mark-all-read?userId=" + userId, "");
    }
    
    // === Template endpoints ===
    
    /**
     * Gets all notification templates
     * 
     * @return Response containing all notification templates
     */
    public Response getAllTemplates() {
        return customRequest()
            .header("Authorization", "Bearer " + ConfigManager.getServiceAuthToken(SERVICE_NAME))
            .get("/templates");
    }
    
    /**
     * Gets a specific template by ID
     * 
     * @param templateId The ID of the template
     * @return Response containing the template
     */
    public Response getTemplate(String templateId) {
        return customRequest()
            .header("Authorization", "Bearer " + ConfigManager.getServiceAuthToken(SERVICE_NAME))
            .get("/templates/" + templateId);
    }
    
    /**
     * Creates a new notification template
     * 
     * @param templateData The template data
     * @return Response containing the created template
     */
    public Response createTemplate(Object templateData) {
        return customRequest()
            .header("Authorization", "Bearer " + ConfigManager.getServiceAuthToken(SERVICE_NAME))
            .header("Content-Type", "application/json")
            .body(templateData)
            .post("/templates");
    }
    
    /**
     * Updates an existing template
     * 
     * @param templateId The ID of the template to update
     * @param templateData The updated template data
     * @return Response containing the updated template
     */
    public Response updateTemplate(String templateId, Object templateData) {
        return customRequest()
            .header("Authorization", "Bearer " + ConfigManager.getServiceAuthToken(SERVICE_NAME))
            .header("Content-Type", "application/json")
            .body(templateData)
            .put("/templates/" + templateId);
    }
    
    // === Preferences endpoints ===
    
    /**
     * Gets notification preferences for a user
     * 
     * @param userId The ID of the user
     * @return Response containing the user's notification preferences
     */
    public Response getUserPreferences(int userId) {
        return customRequest()
            .header("Authorization", "Bearer " + ConfigManager.getServiceAuthToken(SERVICE_NAME))
            .get("/preferences?userId=" + userId);
    }
    
    /**
     * Updates notification preferences for a user
     * 
     * @param userId The ID of the user
     * @param preferencesData The updated preferences data
     * @return Response containing the updated preferences
     */
    public Response updateUserPreferences(int userId, Object preferencesData) {
        return customRequest()
            .header("Authorization", "Bearer " + ConfigManager.getServiceAuthToken(SERVICE_NAME))
            .header("Content-Type", "application/json")
            .body(preferencesData)
            .put("/preferences?userId=" + userId);
    }
    
    // === History endpoints ===
    
    /**
     * Gets notification history for a user
     * 
     * @param userId The ID of the user
     * @return Response containing the user's notification history
     */
    public Response getUserHistory(int userId) {
        return customRequest()
            .header("Authorization", "Bearer " + ConfigManager.getServiceAuthToken(SERVICE_NAME))
            .get("/history?userId=" + userId);
    }
    
    /**
     * Gets notification history within a date range
     * 
     * @param userId The ID of the user
     * @param startDate The start date (YYYY-MM-DD)
     * @param endDate The end date (YYYY-MM-DD)
     * @return Response containing the notification history
     */
    public Response getUserHistoryByDateRange(int userId, String startDate, String endDate) {
        return customRequest()
            .header("Authorization", "Bearer " + ConfigManager.getServiceAuthToken(SERVICE_NAME))
            .get("/history?userId=" + userId + "&startDate=" + startDate + "&endDate=" + endDate);
    }
} 