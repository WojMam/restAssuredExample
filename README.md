# 📌 REST API Test Project with RestAssured

## 📖 Description

This project is designed for testing REST APIs using **RestAssured** library and **JUnit 5**. It uses the JSONPlaceholder API as a test endpoint.

## 📂 Project Structure

```
restassured-test/
│── src/
│   ├── main/
│   │   ├── java/
│   │   └── resources/
│   ├── test/
│   │   ├── java/
│   │   │   └── tests/ (API Tests)
│   │   └── resources/
│   │       ├── testdata/ (Test data files)
│   │       └── schemas/ (JSON schemas)
│── pom.xml
│── .gitignore
│── README.md
│── README.pl.md
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

Common test setup and teardown through `BaseTest` class:

```java
public class AdvancedApiTest extends BaseTest {
    @Test
    public void testWithCommonSetup() {
        // Test code here
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

Common request and response configurations:

```java
RequestSpecification requestSpec = new RequestSpecBuilder()
    .setBaseUri("https://jsonplaceholder.typicode.com")
    .setRelaxedHTTPSValidation()
    .addFilter(new RequestLoggingFilter())
    .build();
```

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
    
    Response response = given()
        .header("Content-Type", "application/json")
        .body(postData)
        .when()
        .post("/posts");

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
