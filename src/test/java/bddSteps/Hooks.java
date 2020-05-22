package bddSteps;

import io.cucumber.java.Before;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.session.SessionFilter;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import utilities.APIEnum;
import utilities.Utils;

import java.io.IOException;

import static io.restassured.RestAssured.given;

public class Hooks {
    ///Cucumber hooks as @before will land here


    @Before("@loginRequired")
    public void beforeScenario() throws IOException {
        //execute this code only when place id is null
        //write a code that will give you place id
        StepDefinition loginInData =new StepDefinition();
        APIEnum endpointResource = APIEnum.valueOf("UserLoginEndpoint");

        RequestSpecification req = new RequestSpecBuilder().setUrlEncodingEnabled(true)
                .setBaseUri(Utils.getGlobalValue("BASE_URL"))
                .addFormParam("email", "hithgor1@gmail.com")
                .addFormParam("password", "1234")
                .setContentType(ContentType.URLENC).build();
        SessionFilter sessionFilter = new SessionFilter();
        RequestSpecification res = given().spec(req).log().all().filter(sessionFilter);
        Response response = res.when().post(endpointResource.getResource())
                .then().extract().response();
        String userSessionId = response.getCookie("sessionid");

        ///cutting off CSRF TOKEN for usage in testing post methods later on.
        ///String csrftoken = response.body().asString().split("csrftoken = '")[1].substring(0,64);
        String csrftoken = response.getCookie("csrftoken");
        System.out.println("===============Running test setUp===============");
        System.out.println("Status code of login request: " + response.statusCode());
        System.out.println("CSRFTOKEN saved: " + csrftoken);
        System.out.println("Session ID saved: " + userSessionId);

        loginInData.setCsrfTokenFromSetup(csrftoken);
        loginInData.setUserSessionIdFromSetup(userSessionId);
        loginInData.setSessionFilterFromSetup(sessionFilter);




    }
}
