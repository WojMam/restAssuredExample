# 📌 REST API Test Project with RestAssured

## 📖 Description

This project is designed for testing REST APIs using **RestAssured** library and **JUnit 5**.

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

The project supports various authentication methods:

### 🔹 Basic Authentication (username & password)

```java
given()
    .auth().basic("username", "password")
    .log().all()
.when()
    .get("/secured-endpoint")
.then()
    .log().body()
    .statusCode(200);
```

### 🔹 Bearer Token Authentication (OAuth 2.0, JWT)

```java
String token = "your_access_token_here";
given()
    .header("Authorization", "Bearer " + token)
    .log().all()
.when()
    .get("/protected-resource")
.then()
    .log().body()
    .statusCode(200);
```

### 🔹 API Key Authentication (Header)

```java
given()
    .header("x-api-key", "your_api_key_here")
    .log().all()
.when()
    .get("/api-endpoint")
.then()
    .log().body()
    .statusCode(200);
```

### 🔹 API Key Authentication (Query Parameter)

```java
given()
    .queryParam("api_key", "your_api_key_here")
    .log().all()
.when()
    .get("/api-endpoint")
.then()
    .log().body()
    .statusCode(200);
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

### 🔹 Environment Configuration

The project supports multiple environments through a configuration system:

```properties
# config.properties
base.url.dev=https://dev-api.example.com
base.url.staging=https://staging-api.example.com
base.url.prod=https://api.example.com
```

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
CustomAssertions.assertResponseTime(response, 2000);
CustomAssertions.assertContentType(response, "application/json");
CustomAssertions.assertJsonSchema(response, "schemas/post_schema.json");
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
    .setBaseUri(ConfigManager.getBaseUrl("dev"))
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
        .body("title", equalTo(postData.getTitle()));
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

- **RestAssured** – for API testing
- **JUnit 5** – testing framework
- **SLF4J + Logback** – for logging

Add to `pom.xml`:

```xml
<dependency>
    <groupId>io.rest-assured</groupId>
    <artifactId>rest-assured</artifactId>
    <version>5.3.0</version>
    <scope>test</scope>
</dependency>
```

## 🌐 Language Versions

This README is available in multiple languages:
- [English](README.md)
- [Polski](README.pl.md)

## 📜 License

The project is available under the MIT License. You can freely develop and modify it. 🚀
