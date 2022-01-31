import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

public class ReqresApiTests {
    String endpointLogin = "/api/login";
    String endpointRegister = "/api/register";
    String endpointUser = "/api/users?page=2";
    String endpointUsers = "/api/users";

    @BeforeAll
    static void setUp() {
        RestAssured.baseURI = "https://reqres.in/";
    }

    @Test
    @DisplayName("Проверка запроса на успешную регистрацию")
    void successfulRegister() {
        String data = "{ \"email\": \"eve.holt@reqres.in\", \"password\": \"olololo\" }";
        given()
                .contentType(JSON)
                .body(data)
                .when()
                .post(endpointRegister)
                .then()
                .statusCode(200)
                .log().body()
                .body("id", notNullValue())
                .body("token", notNullValue());
    }

    @Test
    @DisplayName("Проверка запроса на неуспешную регистрацию")
    void unsuccessfulRegister() {
        String data = "{ \"email\": \"eve.holt@reqres.in\"}";
        given()
                .contentType(JSON)
                .body(data)
                .when()
                .post(endpointRegister)
                .then()
                .statusCode(400)
                .log().body()
                .body("error", is("Missing password"));
    }

    @Test
    @DisplayName("Проверка запроса на успешный логин")
    void successfulLogin() {
        String data = "{ \"email\": \"eve.holt@reqres.in\", \"password\": \"cityslicka\" }";
        given()
                .contentType(JSON)
                .body(data)
                .when()
                .post(endpointLogin)
                .then()
                .statusCode(200)
                .log().body()
                .body("token", notNullValue());
    }

    @Test
    @DisplayName("Проверка запроса на создание пользователя")
    void createUser() {
        String data = "{\"name\": \"neo\",\"job\": \"the elected\"}";
        given()
                .contentType(JSON)
                .body(data)
                .when()
                .post(endpointUsers)
                .then()
                .statusCode(201)
                .log().body()
                .body("name", is("neo"))
                .body("job", is("the elected"))
                .body("id", notNullValue())
                .body("createdAt", notNullValue());
    }

    @Test
    @DisplayName("Проверка запроса на изменение пользователя")
    void updateUser() {
        String data = "{\"name\": \"neo\",\"job\": \"the elected\"}";
        given()
                .contentType(JSON)
                .body(data)
                .when()
                .put(endpointUser)
                .then()
                .statusCode(200)
                .log().body()
                .body("name", is("neo"))
                .body("job", is("the elected"))
                .body("updatedAt", notNullValue());
    }

    @Test
    @DisplayName("Проверка запроса на удаление пользователя")
    void deleteUser() {
        given()
                .when()
                .delete(endpointUser)
                .then()
                .statusCode(204);
    }
}
