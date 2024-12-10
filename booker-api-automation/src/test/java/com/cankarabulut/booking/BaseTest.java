package com.cankarabulut.booking;

import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;

public class BaseTest {
    protected String token;
    protected static final String BASE_URI = "https://restful-booker.herokuapp.com/";

    @BeforeEach
    public void generateToken() {
        String username = "admin";
        String password = "password123";

        Map<String, String > userInfo = new HashMap<>();
        userInfo.put("username", username);
        userInfo.put("password", password);

        Response response = given()
                .header("Content-Type", "application/json")
                .body(userInfo)
                .post(BASE_URI + "auth");
        token = response.getBody().jsonPath().getString("token");
    }
}
