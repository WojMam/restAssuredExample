# 📌 REST API Test Project with RestAssured

## 📖 Opis

Ten projekt służy do testowania API REST za pomocą biblioteki **RestAssured** oraz **JUnit 5**.

## 📂 Struktura projektu

```
restassured-test/
│── src/
│   ├── main/
│   │   ├── java/
│   │   └── resources/
│   ├── test/
│   │   ├── java/
│   │   │   └── tests/ (Testy API)
│── pom.xml
│── .gitignore
│── README.md
│── LICENSE
```

## 🚀 Uruchamianie testów

### 🔹 1️⃣ Przejdź do katalogu projektu

```bash
cd restassured-test
```

### 🔹 2️⃣ Uruchom wszystkie testy

```bash
mvn test
```

### 🔹 3️⃣ Uruchomienie konkretnego testu

```bash
mvn -Dtest=RestApiTest test
```

Lub konkretnej metody testowej:

```bash
mvn -Dtest=RestApiTest#testGetRequest test
```

## 🔐 Autoryzacja w testach

Projekt obsługuje różne metody autoryzacji:

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

## 🔧 Konfiguracja logowania

Projekt obsługuje logowanie testów poprzez **RestAssured** oraz **SLF4J + Logback**.

- Aby logować całe żądanie i odpowiedź w RestAssured, użyj:

```java
given().log().all().when().get("/posts/1").then().log().body();
```

- Logowanie w SLF4J:

```java
private static final Logger logger = LoggerFactory.getLogger(RestApiTest.class);
logger.info("Rozpoczynam test");
```

## 📦 Zależności (Maven)

Projekt wykorzystuje następujące biblioteki:

- **RestAssured** – do testowania API
- **JUnit 5** – framework testowy
- **SLF4J + Logback** – do logowania

Dodaj do `pom.xml`:

```xml
<dependency>
    <groupId>io.rest-assured</groupId>
    <artifactId>rest-assured</artifactId>
    <version>5.3.0</version>
    <scope>test</scope>
</dependency>
```

## 📜 Licencja

Projekt dostępny na licencji MIT. Możesz dowolnie go rozwijać i modyfikować. 🚀
