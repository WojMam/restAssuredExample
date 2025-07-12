# Service/Client Pattern Implementation Guide

## 🎯 Overview

The Service/Client pattern has been implemented to solve the problem of testing multiple APIs with different base URIs in a single test. This pattern provides a clean, scalable way to manage multiple API clients.

## 🏗️ Architecture

### Before (Old Pattern)

```java
// BaseTest.java - Single baseUri limitation
@BeforeAll
public static void setup() {
    RestAssured.baseURI = "https://jsonplaceholder.typicode.com"; // Only one API
}

@Test
public void testSingleApi() {
    given().when().get("/posts/1").then().statusCode(200);
}
```

### After (Service/Client Pattern)

```java
// BaseTest.java - Multiple API clients
@BeforeAll
public static void setup() {
    jsonPlaceholderClient = ClientFactory.getJsonPlaceholderClient();
    userApiClient = ClientFactory.getUserApiClient();
    paymentApiClient = ClientFactory.getPaymentApiClient();
    notificationApiClient = ClientFactory.getNotificationApiClient();
}

@Test
public void testMultipleApis() {
    // JSONPlaceholder API
    Response post = jsonPlaceholderClient.getPost(1);

    // User API
    Response login = userApiClient.login("user", "pass");

    // Payment API
    Response payment = paymentApiClient.processPayment(paymentData);

    // Notification API
    Response notification = notificationApiClient.sendNotification(notificationData);
}
```

## 🔧 Components

### 1. BaseApiClient

Abstract base class providing common functionality:

- HTTP methods (GET, POST, PUT, DELETE)
- Request/Response specifications
- Authentication handling
- Logging

### 2. Concrete Clients

Specific implementations for each API:

- **JsonPlaceholderClient**: JSONPlaceholder API operations
- **UserApiClient**: User management operations
- **PaymentApiClient**: Payment processing operations
- **NotificationApiClient**: Notification operations

### 3. ClientFactory

Singleton factory managing client instances:

- Client creation and caching
- Service availability checking
- Configuration validation

### 4. Enhanced ConfigManager

Extended configuration management:

- Service-specific configurations
- Multiple baseUri support
- Service-specific authentication

## 📋 Configuration

### config.properties

```properties
# Service-specific configurations
service.jsonplaceholder.baseurl=https://jsonplaceholder.typicode.com
service.jsonplaceholder.timeout=10000

service.user.baseurl=https://user-api.example.com
service.user.auth.token=user-service-token
service.user.timeout=8000

service.payment.baseurl=https://payment-api.example.com
service.payment.auth.token=payment-service-token
service.payment.timeout=15000

service.notification.baseurl=https://notification-api.example.com
service.notification.auth.token=notification-service-token
service.notification.timeout=5000
```

## 🚀 Usage Examples

### Basic Usage

```java
public class MyTest extends BaseTest {

    @Test
    public void testJsonPlaceholder() {
        // Use JSONPlaceholder client
        Response response = jsonPlaceholderClient.getPost(1);
        response.then().statusCode(200);
    }

    @Test
    public void testUserManagement() {
        // Use User API client
        Response response = userApiClient.login("testuser", "testpass");
        // Handle response...
    }
}
```

### Multi-API Workflow

```java
@Test
public void testCompleteWorkflow() {
    // Step 1: Get user data
    Response user = jsonPlaceholderClient.getUser(1);

    // Step 2: Create content
    PostData postData = new PostData("Title", "Body", 1);
    Response post = jsonPlaceholderClient.createPost(postData);

    // Step 3: Process payment (if available)
    if (paymentApiClient != null) {
        Response payment = paymentApiClient.processPayment(paymentData);
    }

    // Step 4: Send notification (if available)
    if (notificationApiClient != null) {
        Response notification = notificationApiClient.sendNotification(notificationData);
    }
}
```

### Conditional Testing

```java
@Test
public void testWithServiceAvailability() {
    // Always test main API
    Response response = jsonPlaceholderClient.getPost(1);

    // Conditionally test other services
    if (isServiceAvailable("payment")) {
        // Test payment functionality
        Response payment = paymentApiClient.getUserPayments(1);
    }

    if (isServiceAvailable("notification")) {
        // Test notification functionality
        Response notifications = notificationApiClient.getUserNotifications(1);
    }
}
```

## 🎨 Client-Specific Methods

### JsonPlaceholderClient

```java
// Posts
jsonPlaceholderClient.getAllPosts()
jsonPlaceholderClient.getPost(id)
jsonPlaceholderClient.createPost(postData)
jsonPlaceholderClient.updatePost(id, postData)
jsonPlaceholderClient.deletePost(id)

// Users
jsonPlaceholderClient.getAllUsers()
jsonPlaceholderClient.getUser(id)
jsonPlaceholderClient.getUserPosts(userId)

// Comments, Todos, Albums...
```

### UserApiClient

```java
// Authentication
userApiClient.login(username, password)
userApiClient.logout()
userApiClient.refreshToken(token)

// Profile
userApiClient.getProfile()
userApiClient.updateProfile(profileData)

// User Management
userApiClient.getAllUsers()
userApiClient.createUser(userData)
userApiClient.updateUser(id, userData)
userApiClient.deleteUser(id)
```

### PaymentApiClient

```java
// Payments
paymentApiClient.processPayment(paymentData)
paymentApiClient.getPayment(paymentId)
paymentApiClient.getUserPayments(userId)
paymentApiClient.cancelPayment(paymentId)

// Transactions
paymentApiClient.getTransaction(transactionId)
paymentApiClient.getUserTransactions(userId)

// Refunds
paymentApiClient.processRefund(refundData)
paymentApiClient.getRefund(refundId)
```

### NotificationApiClient

```java
// Notifications
notificationApiClient.sendNotification(notificationData)
notificationApiClient.getUserNotifications(userId)
notificationApiClient.markAsRead(notificationId)
notificationApiClient.deleteNotification(notificationId)

// Bulk Operations
notificationApiClient.sendBulkNotifications(bulkData)
notificationApiClient.markAllAsRead(userId)

// Templates
notificationApiClient.getAllTemplates()
notificationApiClient.createTemplate(templateData)
```

## 🔒 Authentication Handling

Each client handles authentication automatically:

```java
// Automatic token injection
Response response = userApiClient.getProfile();
// Internally adds: .header("Authorization", "Bearer " + token)

// Custom authentication for specific requests
Response response = userApiClient.customRequest()
    .header("Authorization", "Bearer " + customToken)
    .get("/special-endpoint");
```

## 🧪 Testing Benefits

### 1. **Separation of Concerns**

- Each client handles its own API specifics
- Clear responsibility boundaries
- Easy to maintain and extend

### 2. **Reusability**

- Client instances can be reused across tests
- Common patterns encapsulated in client methods
- Consistent authentication handling

### 3. **Flexibility**

- Easy to add new APIs
- Conditional testing based on service availability
- Environment-specific configurations

### 4. **Type Safety**

- Strongly typed client methods
- Compile-time validation
- IDE auto-completion support

## 🔄 Migration Guide

### From Old Pattern to Service/Client Pattern

1. **Update test inheritance**:

```java
// Before
public class MyTest extends BaseTest {
    // Uses global RestAssured.baseURI
}

// After
public class MyTest extends BaseTest {
    // Uses specific client instances
}
```

2. **Replace direct RestAssured calls**:

```java
// Before
given().when().get("/posts/1").then().statusCode(200);

// After
jsonPlaceholderClient.getPost(1).then().statusCode(200);
```

3. **Update configuration**:

```properties
# Before
base.url=https://jsonplaceholder.typicode.com

# After
service.jsonplaceholder.baseurl=https://jsonplaceholder.typicode.com
service.user.baseurl=https://user-api.example.com
service.payment.baseurl=https://payment-api.example.com
```

## 💡 Best Practices

1. **Use specific client methods** instead of generic HTTP methods
2. **Check service availability** before using optional clients
3. **Handle exceptions gracefully** for external API calls
4. **Use conditional testing** for optional services
5. **Keep client methods focused** on specific operations
6. **Document client-specific behavior** in method comments

## 🎯 Real-World Usage

The pattern is particularly useful for:

- **E-commerce testing**: User → Product → Cart → Payment → Notification
- **Content management**: Authentication → Content → Publishing → Notification
- **Business workflows**: Data retrieval → Processing → User updates → Auditing
- **Microservices testing**: Testing interactions between multiple services

This implementation provides a robust, scalable solution for testing complex API interactions while maintaining clean, maintainable code.
