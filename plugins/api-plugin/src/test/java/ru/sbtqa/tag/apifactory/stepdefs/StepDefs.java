package ru.sbtqa.tag.apifactory.stepdefs;

import cucumber.api.java.en.Given;
import static io.restassured.RestAssured.given;

public class StepDefs {

//    @Given("^user activate raw rest$")
//    public void activateRawRest() {
//        ApiFactory.getApiFactory().setRest(RestRawImpl.class);
//    }
//
//    @Given("^user activate entity rest$")
//    public void activateEntityRest() {
//        ApiFactory.getApiFactory().setRest(RestEntityImpl.class);
//    }
//
//    @Given("^user activate form rest$")
//    public void activateFormRest() {
//        ApiFactory.getApiFactory().setRest(RestFormImpl.class);
//    }


    @Given("^test$")
    public void test() {
        System.out.println(

                given()
                        .contentType("Application/json")
                        .body("{\"id\": 11223344, \"email\": \"default_person@google.com\", \"name\": \"Default_person\"}")
                        .post("http://localhost:9998/client/post")


                .then().extract().body().asString()
        );



    }
}
