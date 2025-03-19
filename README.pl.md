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

## 📁 Zarządzanie danymi testowymi

Projekt implementuje system zarządzania danymi testowymi poprzez pliki, co pozwala na lepszą organizację i utrzymanie testów.

### 🔹 Struktura katalogów

```
src/test/
├── java/
│   └── tests/
│       ├── models/         # Klasy modelowe dla danych testowych
│       ├── utils/          # Narzędzia do obsługi danych
│       └── FileBasedTest.java
└── resources/
    └── testdata/          # Pliki z danymi testowymi
        ├── post_data.json
        └── raw_content.txt
```

### 🔹 Przykład użycia danych z plików

1. **Ładowanie danych JSON**:
```java
PostData postData = TestDataLoader.loadJsonData("post_data.json", PostData.class);
```

2. **Ładowanie surowych danych**:
```java
String content = TestDataLoader.loadFileContent("raw_content.txt");
```

3. **Przykład testu z danymi z pliku**:
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

### 🔹 Korzyści z tego podejścia

- **Separacja danych od kodu**: Dane testowe są przechowywane osobno od kodu testowego
- **Łatwość utrzymania**: Możliwość szybkiej aktualizacji danych bez modyfikacji kodu
- **Wielokrotne użycie**: Te same dane mogą być wykorzystane w różnych testach
- **Elastyczność**: Obsługa różnych formatów plików i typów danych
- **Typebezpieczeństwo**: Dane JSON są prawidłowo typowane przez klasy modelowe

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
