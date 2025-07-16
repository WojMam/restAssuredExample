# 📌 REST API Test Project with RestAssured

## 📖 Description

This project is designed for testing REST APIs using **RestAssured** library and **JUnit 5**. It uses the JSONPlaceholder API as a test endpoint and implements a **Service/Client pattern** for multi-API testing.

### 🆕 Latest Updates

- **Service/Client Pattern**: Support for testing multiple APIs in a single test
- **Multi-Service Configuration**: Configure different services with separate base URIs
- **API Clients**: Pre-configured clients for JSONPlaceholder, User, Payment, and Notification APIs
- **Conditional Testing**: Graceful handling of optional services

## 📂 Project Structure

```
restassured-test/
│── src/
│   ├── main/
│   │   ├── java/
│   │   └── resources/
│   │       └── logback.xml
│   ├── test/
│   │   ├── java/
│   │   │   └── tests/
│   │   │       ├── clients/               # NEW: API Client Classes
│   │   │       │   ├── BaseApiClient.java
│   │   │       │   ├── JsonPlaceholderClient.java
│   │   │       │   ├── UserApiClient.java
│   │   │       │   ├── PaymentApiClient.java
│   │   │       │   ├── NotificationApiClient.java
│   │   │       │   └── ClientFactory.java
│   │   │       ├── models/               # Data Models
│   │   │       │   └── PostData.java
│   │   │       ├── utils/                # Utility Classes
│   │   │       │   ├── ConfigManager.java
│   │   │       │   ├── CustomAssertions.java
│   │   │       │   └── TestDataLoader.java
│   │   │       ├── BaseTest.java         # Base Test Class
│   │   │       ├── MultiApiTest.java     # NEW: Multi-API Testing
│   │   │       ├── RestApiTest.java      # Basic API Tests
│   │   │       ├── AuthTest.java         # Authentication Tests
│   │   │       ├── AdvancedApiTest.java  # Advanced Features
│   │   │       ├── FileBasedTest.java    # File-based Testing
│   │   │       └── BodyFromFileTest.java # Body from File Tests
│   │   └── resources/
│   │       ├── config.properties         # Multi-Service Configuration
│   │       ├── testdata/                 # Test Data Files
│   │       │   ├── post_data.json
│   │       │   └── raw_content.txt
│   │       └── schemas/                  # JSON Schemas
│   │           └── post_schema.json
│── pom.xml
│── .gitignore
│── README.md
│── README.pl.md
│── SERVICE_CLIENT_PATTERN.md             # NEW: Implementation Guide
│── LICENSE
```

## 🚀 Running Tests

### 🔹 1️⃣ Navigate to project directory

```bash
cd restassured-test
```

### 🔹 2️⃣ Run all tests

```bash
mvn test
```

### 🔹 3️⃣ Run specific test

```bash
mvn -Dtest=RestApiTest test
```

Or specific test method:

```bash
mvn -Dtest=RestApiTest#testGetRequest test
```

### 🔹 4️⃣ Run multi-API tests

```bash
mvn -Dtest=MultiApiTest test
```

Or specific multi-API test method:

```bash
mvn -Dtest=MultiApiTest#testMultiApiWorkflow test
```

## 🔐 Authentication in Tests

The project demonstrates various authentication methods using the JSONPlaceholder API. Note that JSONPlaceholder doesn't actually validate authentication, so we use dummy credentials for demonstration purposes.

### 🔹 Basic Authentication (username & password)

```java
given()
    .auth().basic("user", "pass")  // Dummy credentials
    .log().all()
.when()
    .get("/posts/1")
.then()
    .log().body()
    .statusCode(200)
    .body("id", equalTo(1));
```

### 🔹 Bearer Token Authentication (OAuth 2.0, JWT)

```java
String token = "dummy_token";  // Dummy token
given()
    .header("Authorization", "Bearer " + token)
    .log().all()
.when()
    .get("/posts/2")
.then()
    .log().body()
    .statusCode(200)
    .body("id", equalTo(2));
```

### 🔹 API Key Authentication (Header)

```java
given()
    .header("x-api-key", "dummy_key")  // Dummy API key
    .log().all()
.when()
    .get("/posts/3")
.then()
    .log().body()
    .statusCode(200)
    .body("id", equalTo(3));
```

### 🔹 API Key Authentication (Query Parameter)

```java
given()
    .queryParam("api_key", "dummy_key")  // Dummy API key
    .log().all()
.when()
    .get("/posts/4")
.then()
    .log().body()
    .statusCode(200)
    .body("id", equalTo(4));
```

## 🔧 Logging Configuration

The project handles test logging through **RestAssured** and **SLF4J + Logback**.

- To log entire request and response in RestAssured:

```java
given().log().all().when().get("/posts/1").then().log().body();
```

- Logging with SLF4J:

```java
private static final Logger logger = LoggerFactory.getLogger(RestApiTest.class);
logger.info("Starting test");
```

## ⚙️ Advanced Features

### 🔹 Base Test Class

Common test setup and teardown through `BaseTest` class with API clients:

```java
public class AdvancedApiTest extends BaseTest {
    @Test
    public void testWithCommonSetup() {
        // Use pre-configured API clients
        Response response = jsonPlaceholderClient.getPost(1);
        response.then().statusCode(200);

        // Use conditional clients
        if (userApiClient != null) {
            Response login = userApiClient.login("user", "pass");
        }
    }
}
```

### 🔹 Custom Assertions

Reusable assertions for common validations:

```java
CustomAssertions.assertResponseTime(response, 5000);  // 5 seconds timeout for external API
CustomAssertions.assertContentType(response, "application/json");
CustomAssertions.assertArraySize(response, "$", greaterThan(0));
```

### 🔹 JSON Schema Validation

Validate response structure against JSON schemas:

```json
{
	"$schema": "http://json-schema.org/draft-07/schema#",
	"type": "object",
	"required": ["id", "title", "body", "userId"],
	"properties": {
		"id": { "type": "integer" },
		"title": { "type": "string" },
		"body": { "type": "string" },
		"userId": { "type": "integer" }
	}
}
```

### 🔹 Request/Response Specifications

Common request and response configurations are now handled per client:

```java
// Each client has its own request/response specifications
// Configured automatically based on service configuration
JsonPlaceholderClient client = ClientFactory.getJsonPlaceholderClient();

// Custom request specifications when needed
Response response = client.customRequest()
    .header("Custom-Header", "value")
    .queryParam("param", "value")
    .get("/custom-endpoint");
```

## 🏗️ Service/Client Pattern (NEW)

The project now implements a **Service/Client pattern** for testing multiple APIs with different base URIs in a single test. This solves the limitation of having only one baseUri per test class.

### 🎯 Problem Solved

**Before**: Limited to one API per test class

```java
@BeforeAll
public static void setup() {
    RestAssured.baseURI = "https://jsonplaceholder.typicode.com"; // Only one API
}
```

**After**: Multiple APIs in a single test

```java
@Test
public void testMultiApiWorkflow() {
    // JSONPlaceholder API
    Response user = jsonPlaceholderClient.getUser(1);

    // User API
    Response login = userApiClient.login("testuser", "testpass");

    // Payment API
    Response payment = paymentApiClient.processPayment(paymentData);

    // Notification API
    Response notification = notificationApiClient.sendNotification(notificationData);
}
```

### 🔧 Available API Clients

The project provides pre-configured clients for different services:

- **JsonPlaceholderClient**: JSONPlaceholder API operations (posts, users, comments, todos)
- **UserApiClient**: User management and authentication
- **PaymentApiClient**: Payment processing and transaction management
- **NotificationApiClient**: Notification management and templates

### 📋 Multi-Service Configuration

Configure multiple services in `config.properties`:

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

### 🚀 Usage Examples

#### Basic Usage

```java
public class MyTest extends BaseTest {

    @Test
    public void testJsonPlaceholder() {
        Response response = jsonPlaceholderClient.getPost(1);
        response.then().statusCode(200);
    }

    @Test
    public void testUserManagement() {
        Response response = userApiClient.login("testuser", "testpass");
        // Handle response...
    }
}
```

#### Multi-API Workflow

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

#### Conditional Testing

```java
@Test
public void testWithServiceAvailability() {
    // Always test main API
    Response response = jsonPlaceholderClient.getPost(1);

    // Conditionally test other services
    if (isServiceAvailable("payment")) {
        Response payment = paymentApiClient.getUserPayments(1);
    }

    if (isServiceAvailable("notification")) {
        Response notifications = notificationApiClient.getUserNotifications(1);
    }
}
```

### 🎨 Client-Specific Methods

Each client provides domain-specific methods:

```java
// JsonPlaceholderClient
jsonPlaceholderClient.getAllPosts()
jsonPlaceholderClient.getPost(id)
jsonPlaceholderClient.createPost(postData)
jsonPlaceholderClient.getUserPosts(userId)

// UserApiClient
userApiClient.login(username, password)
userApiClient.getProfile()
userApiClient.updateProfile(profileData)

// PaymentApiClient
paymentApiClient.processPayment(paymentData)
paymentApiClient.getPayment(paymentId)
paymentApiClient.getUserPayments(userId)

// NotificationApiClient
notificationApiClient.sendNotification(notificationData)
notificationApiClient.getUserNotifications(userId)
notificationApiClient.markAsRead(notificationId)
```

### 💡 Benefits

- **Multi-API Testing**: Test interactions between different services
- **Separation of Concerns**: Each client handles its own API specifics
- **Reusability**: Client instances can be reused across tests
- **Type Safety**: Strongly typed client methods with IDE support
- **Flexible Configuration**: Environment-specific service configurations
- **Automatic Authentication**: Token injection per service

### 📖 Documentation

For complete implementation details, see [SERVICE_CLIENT_PATTERN.md](SERVICE_CLIENT_PATTERN.md)

## 📁 Test Data Management

The project implements a file-based test data management system, allowing for better organization and maintenance of tests.

### 🔹 Directory Structure

```
src/test/
├── java/
│   └── tests/
│       ├── models/         # Model classes for test data
│       ├── utils/          # Data handling utilities
│       └── FileBasedTest.java
└── resources/
    └── testdata/          # Test data files
        ├── post_data.json
        └── raw_content.txt
```

### 🔹 Example Usage

1. **Loading JSON Data**:

```java
PostData postData = TestDataLoader.loadJsonData("post_data.json", PostData.class);
```

2. **Loading Raw Data**:

```java
String content = TestDataLoader.loadFileContent("raw_content.txt");
```

3. **Example Test with File Data**:

```java
@Test
public void testPostWithJsonData() {
    PostData postData = TestDataLoader.loadJsonData("post_data.json", PostData.class);

    // Using the new client pattern
    Response response = jsonPlaceholderClient.createPost(postData);

    response.then()
        .statusCode(201)
        .body("title", equalTo(postData.getTitle()))
        .body("body", equalTo(postData.getBody()))
        .body("userId", equalTo(postData.getUserId()))
        .body("id", notNullValue());
}
```

### 🔹 Benefits of This Approach

- **Data-Code Separation**: Test data is stored separately from test code
- **Easy Maintenance**: Quick data updates without code modifications
- **Reusability**: Same data can be used across multiple tests
- **Flexibility**: Support for different file formats and data types
- **Type Safety**: JSON data is properly typed through model classes

## 📦 Dependencies (Maven)

The project uses the following libraries:

```xml
<dependencies>
    <!-- RestAssured -->
    <dependency>
        <groupId>io.rest-assured</groupId>
        <artifactId>rest-assured</artifactId>
        <version>5.3.0</version>
        <scope>test</scope>
    </dependency>

    <!-- JUnit 5 -->
    <dependency>
        <groupId>org.junit.jupiter</groupId>
        <artifactId>junit-jupiter</artifactId>
        <version>5.9.2</version>
        <scope>test</scope>
    </dependency>

    <!-- JSON Schema Validator -->
    <dependency>
        <groupId>io.rest-assured</groupId>
        <artifactId>json-schema-validator</artifactId>
        <version>5.3.0</version>
        <scope>test</scope>
    </dependency>

    <!-- Jackson for JSON -->
    <dependency>
        <groupId>com.fasterxml.jackson.core</groupId>
        <artifactId>jackson-databind</artifactId>
        <version>2.15.2</version>
    </dependency>

    <!-- SLF4J + Logback -->
    <dependency>
        <groupId>ch.qos.logback</groupId>
        <artifactId>logback-classic</artifactId>
        <version>1.4.7</version>
    </dependency>
</dependencies>
```

## 📝 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 🌐 Language Versions

This README is available in multiple languages:

- [English](README.md)
- [Polski](README.pl.md)

## 🌿 Branching Strategy

This project follows a branching strategy that allows for both stable and experimental development:

- **master** - The main development branch where new features and improvements are implemented
- **Basic** - A stable branch containing the basic implementation of the REST API testing framework

### 🔹 Working with Branches

To switch between versions:

```bash
# Switch to the stable Basic version
git checkout Basic

# Switch back to the development version
git checkout master
```

The `Basic` branch serves as a reference point for the basic implementation, while the `master` branch continues to evolve with new features and improvements.
