# 📌 REST API Test Project with RestAssured

## 📖 Opis

Ten projekt służy do testowania API REST za pomocą biblioteki **RestAssured** oraz **JUnit 5**. Wykorzystuje API JSONPlaceholder jako endpoint testowy.

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
│   │   └── resources/
│   │       ├── testdata/ (Pliki z danymi testowymi)
│   │       └── schemas/ (Schematy JSON)
│── pom.xml
│── .gitignore
│── README.md
│── README.pl.md
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

Projekt demonstruje różne metody autoryzacji wykorzystując API JSONPlaceholder. Należy zaznaczyć, że JSONPlaceholder nie waliduje faktycznie autoryzacji, więc używamy fikcyjnych danych uwierzytelniających w celach demonstracyjnych.

### 🔹 Basic Authentication (username & password)

```java
given()
    .auth().basic("user", "pass")  // Fikcyjne dane uwierzytelniające
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
String token = "dummy_token";  // Fikcyjny token
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
    .header("x-api-key", "dummy_key")  // Fikcyjny klucz API
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
    .queryParam("api_key", "dummy_key")  // Fikcyjny klucz API
    .log().all()
.when()
    .get("/posts/4")
.then()
    .log().body()
    .statusCode(200)
    .body("id", equalTo(4));
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

## ⚙️ Zaawansowane funkcje

### 🔹 Bazowa klasa testowa

Wspólna konfiguracja testów poprzez klasę `BaseTest`:

```java
public class AdvancedApiTest extends BaseTest {
    @Test
    public void testWithCommonSetup() {
        // Kod testu tutaj
    }
}
```

### 🔹 Własne asercje

Wielokrotnie używane asercje do typowych walidacji:

```java
CustomAssertions.assertResponseTime(response, 5000);  // 5 sekund timeout dla zewnętrznego API
CustomAssertions.assertContentType(response, "application/json");
CustomAssertions.assertArraySize(response, "$", greaterThan(0));
```

### 🔹 Walidacja schematu JSON

Walidacja struktury odpowiedzi względem schematu JSON:

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

### 🔹 Specyfikacje żądań i odpowiedzi

Wspólne konfiguracje żądań i odpowiedzi:

```java
RequestSpecification requestSpec = new RequestSpecBuilder()
    .setBaseUri("https://jsonplaceholder.typicode.com")
    .setRelaxedHTTPSValidation()
    .addFilter(new RequestLoggingFilter())
    .build();
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
        .body("title", equalTo(postData.getTitle()))
        .body("body", equalTo(postData.getBody()))
        .body("userId", equalTo(postData.getUserId()))
        .body("id", notNullValue());
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

## 📝 Licencja

Ten projekt jest dostępny na licencji MIT - szczegóły znajdują się w pliku [LICENSE](LICENSE).

## 🌿 Strategia rozgałęzień

Ten projekt stosuje strategię rozgałęzień, która pozwala na stabilny i eksperymentalny rozwój:

- **master** - Główna gałąź deweloperska, gdzie implementowane są nowe funkcje i ulepszenia
- **Basic** - Stabilna gałąź zawierająca podstawową implementację frameworka testowania API REST

### 🔹 Praca z gałęziami

Aby przełączyć się między wersjami:
```bash
# Przełącz na stabilną wersję Basic
git checkout Basic

# Wróć do wersji deweloperskiej
git checkout master
```

Gałąź `Basic` służy jako punkt odniesienia dla podstawowej implementacji, podczas gdy gałąź `master` stale się rozwija z nowymi funkcjami i ulepszeniami.
