package ru.yandex.scooter.api.client;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import ru.yandex.scooter.api.constants.Endpoints;


public class ScooterApiClient {

    protected RequestSpecification getBaseReqSpec() {
        return new RequestSpecBuilder()
                .setContentType(ContentType.JSON)
                .setBaseUri(Endpoints.BASE_URL)
                .build();
    }

}
