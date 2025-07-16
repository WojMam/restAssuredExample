package tests.clients;

import io.restassured.response.Response;
import tests.utils.ConfigManager;

/**
 * Client for Payment API
 * 
 * This client provides methods for interacting with a payment processing API.
 * Includes operations for payment processing, transaction management, and payment history.
 * 
 * Available endpoints:
 * - Payments: /payments
 * - Transactions: /transactions
 * - Refunds: /refunds
 * - Payment Methods: /payment-methods
 */
public class PaymentApiClient extends BaseApiClient {
    
    private static final String SERVICE_NAME = "payment";
    
    /**
     * Creates a new PaymentApiClient instance
     */
    public PaymentApiClient() {
        super(SERVICE_NAME, ConfigManager.getServiceBaseUrl(SERVICE_NAME));
    }

    // === Payment endpoints ===
    
    /**
     * Processes a payment
     * 
     * @param paymentData The payment data
     * @return Response containing the payment result
     */
    public Response processPayment(Object paymentData) {
        return customRequest()
            .header("Authorization", "Bearer " + ConfigManager.getServiceAuthToken(SERVICE_NAME))
            .header("Content-Type", "application/json")
            .body(paymentData)
            .post("/payments");
    }
    
    /**
     * Gets payment details by ID
     * 
     * @param paymentId The ID of the payment
     * @return Response containing the payment details
     */
    public Response getPayment(String paymentId) {
        return customRequest()
            .header("Authorization", "Bearer " + ConfigManager.getServiceAuthToken(SERVICE_NAME))
            .get("/payments/" + paymentId);
    }
    
    /**
     * Gets all payments for a user
     * 
     * @param userId The ID of the user
     * @return Response containing the user's payments
     */
    public Response getUserPayments(int userId) {
        return customRequest()
            .header("Authorization", "Bearer " + ConfigManager.getServiceAuthToken(SERVICE_NAME))
            .get("/payments?userId=" + userId);
    }
    
    /**
     * Cancels a payment
     * 
     * @param paymentId The ID of the payment to cancel
     * @return Response from the cancel operation
     */
    public Response cancelPayment(String paymentId) {
        return customRequest()
            .header("Authorization", "Bearer " + ConfigManager.getServiceAuthToken(SERVICE_NAME))
            .post("/payments/" + paymentId + "/cancel", "");
    }
    
    // === Transaction endpoints ===
    
    /**
     * Gets transaction details by ID
     * 
     * @param transactionId The ID of the transaction
     * @return Response containing the transaction details
     */
    public Response getTransaction(String transactionId) {
        return customRequest()
            .header("Authorization", "Bearer " + ConfigManager.getServiceAuthToken(SERVICE_NAME))
            .get("/transactions/" + transactionId);
    }
    
    /**
     * Gets all transactions for a user
     * 
     * @param userId The ID of the user
     * @return Response containing the user's transactions
     */
    public Response getUserTransactions(int userId) {
        return customRequest()
            .header("Authorization", "Bearer " + ConfigManager.getServiceAuthToken(SERVICE_NAME))
            .get("/transactions?userId=" + userId);
    }
    
    /**
     * Gets transactions within a date range
     * 
     * @param startDate The start date (YYYY-MM-DD)
     * @param endDate The end date (YYYY-MM-DD)
     * @return Response containing the transactions
     */
    public Response getTransactionsByDateRange(String startDate, String endDate) {
        return customRequest()
            .header("Authorization", "Bearer " + ConfigManager.getServiceAuthToken(SERVICE_NAME))
            .get("/transactions?startDate=" + startDate + "&endDate=" + endDate);
    }
    
    // === Refund endpoints ===
    
    /**
     * Processes a refund
     * 
     * @param refundData The refund data
     * @return Response containing the refund result
     */
    public Response processRefund(Object refundData) {
        return customRequest()
            .header("Authorization", "Bearer " + ConfigManager.getServiceAuthToken(SERVICE_NAME))
            .header("Content-Type", "application/json")
            .body(refundData)
            .post("/refunds");
    }
    
    /**
     * Gets refund details by ID
     * 
     * @param refundId The ID of the refund
     * @return Response containing the refund details
     */
    public Response getRefund(String refundId) {
        return customRequest()
            .header("Authorization", "Bearer " + ConfigManager.getServiceAuthToken(SERVICE_NAME))
            .get("/refunds/" + refundId);
    }
    
    /**
     * Gets all refunds for a payment
     * 
     * @param paymentId The ID of the payment
     * @return Response containing the payment's refunds
     */
    public Response getPaymentRefunds(String paymentId) {
        return customRequest()
            .header("Authorization", "Bearer " + ConfigManager.getServiceAuthToken(SERVICE_NAME))
            .get("/refunds?paymentId=" + paymentId);
    }
    
    // === Payment Methods endpoints ===
    
    /**
     * Gets all payment methods for a user
     * 
     * @param userId The ID of the user
     * @return Response containing the user's payment methods
     */
    public Response getUserPaymentMethods(int userId) {
        return customRequest()
            .header("Authorization", "Bearer " + ConfigManager.getServiceAuthToken(SERVICE_NAME))
            .get("/payment-methods?userId=" + userId);
    }
    
    /**
     * Adds a new payment method
     * 
     * @param paymentMethodData The payment method data
     * @return Response containing the added payment method
     */
    public Response addPaymentMethod(Object paymentMethodData) {
        return customRequest()
            .header("Authorization", "Bearer " + ConfigManager.getServiceAuthToken(SERVICE_NAME))
            .header("Content-Type", "application/json")
            .body(paymentMethodData)
            .post("/payment-methods");
    }
    
    /**
     * Deletes a payment method
     * 
     * @param paymentMethodId The ID of the payment method to delete
     * @return Response from the delete operation
     */
    public Response deletePaymentMethod(String paymentMethodId) {
        return customRequest()
            .header("Authorization", "Bearer " + ConfigManager.getServiceAuthToken(SERVICE_NAME))
            .delete("/payment-methods/" + paymentMethodId);
    }
} 