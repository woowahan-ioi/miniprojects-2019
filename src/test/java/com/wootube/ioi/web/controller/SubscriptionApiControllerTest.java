package com.wootube.ioi.web.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;

class SubscriptionApiControllerTest extends CommonControllerTest {
    @Test
    @DisplayName("구독 중이 아닐 때 구독상태 확인")
    void checkSubscriptionWhenNotSubscribe() {
        String sessionValue = login(USER_A_LOGIN_REQUEST_DTO);

        given().
                cookie("JSESSIONID", sessionValue).
                when().
                get(basicPath() + "/api/subscriptions/" + USER_C_ID + "/checks").
                then().
                statusCode(200).
                body("subscribe", is(false));
    }

    @Test
    @DisplayName("구독 중일 때 구독상태 확인")
    void checkSubscriptionWhenSubscribe() {
        String sessionValue = login(USER_A_LOGIN_REQUEST_DTO);

        given().
                cookie("JSESSIONID", sessionValue).
                when().
                get(basicPath() + "/api/subscriptions/" + USER_B_ID + "/checks").
                then().
                statusCode(200).
                body("subscribe", is(true));
    }

    @Test
    @DisplayName("구독자 수 확인")
    void countSubscription() {
        given().
                when().
                get(basicPath() + "/api/subscriptions/" + USER_B_ID).
                then().
                statusCode(200).
                body("count", is(1));
    }

    @Test
    @DisplayName("구독")
    void subscribe() {
        String sessionValue = login(USER_A_LOGIN_REQUEST_DTO);

        given().
                cookie("JSESSIONID", sessionValue).
                when().
                post(basicPath() + "/api/subscriptions/" + USER_D_ID).
                then().
                statusCode(200);
    }

    @Test
    @DisplayName("구독취소")
    void unsubscribe() {
        String sessionValue = login(USER_D_LOGIN_REQUEST_DTO);

        given().
                cookie("JSESSIONID", sessionValue).
                when().
                delete(basicPath() + "/api/subscriptions/" + USER_A_ID).
                then().
                statusCode(204);
    }
}