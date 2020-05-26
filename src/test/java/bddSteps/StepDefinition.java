package bddSteps;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.session.SessionFilter;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.junit.Assert;
import utilities.APIEnum;
import utilities.TestDataPayloads;
import utilities.Utils;

import java.io.IOException;
import java.util.ArrayList;

import static io.restassured.RestAssured.given;

public class StepDefinition extends Utils {
    ////all given when then definitions will be there


    static RequestSpecification req;
    static ResponseSpecification resspec;
    static Response response;

    TestDataPayloads payload =new TestDataPayloads();
    static String place_id;
    static String csrfTokenFromSetup, userSessionIdFromSetup;
    static SessionFilter sessionFilterFromSetup;


    public  void setReqFromSetup(RequestSpecification req) {
        this.req = req;
    }

    public void setResspecFromSetup(ResponseSpecification resspec) {
        this.resspec = resspec;
    }

    public  void setResponseFromSetup(Response response) {
        this.response = response;
    }

    public  void setCsrfTokenFromSetup(String csrfTokenFromSetup) {
        this.csrfTokenFromSetup = csrfTokenFromSetup;
    }

    public  void setUserSessionIdFromSetup(String userSessionIdFromSetup) {
        this.userSessionIdFromSetup = userSessionIdFromSetup;
    }

    public  void setSessionFilterFromSetup(SessionFilter sessionFilterFromSetup) {
        this.sessionFilterFromSetup = sessionFilterFromSetup;
    }

    @Given("Working payload {string}")
    public void establishBody(String dateCreated) throws IOException {
        /*//TEST CHECKING IF AUTOMATIC NAME-APPLYING to MEALS WORKS AS INTENDED
        0 - BREAKFAST, 1 - LUNCH, 2 - DINNER using pre-created mealCards on test account//*/
        System.out.println("printing from given" + csrfTokenFromSetup);

             req = given().filter(sessionFilterFromSetup).baseUri(getGlobalValue("BASE_URL")).urlEncodingEnabled(true)
                .log().all().contentType(ContentType.JSON).cookie("csrftoken", csrfTokenFromSetup)
                .cookie("sessionid", userSessionIdFromSetup)
                .header("X-CSRFToken", csrfTokenFromSetup)
                .sessionId(userSessionIdFromSetup)
                .body("{" + dateCreated + "}");
    }
    @When("User calls {string} with {string} request")
    public void sendPostWithEstablishedBody(String resourceName, String requestType) throws IOException {

        APIEnum resourceAPI=APIEnum.valueOf(resourceName);
        System.out.println(resourceAPI.getResource());


        resspec =new ResponseSpecBuilder().expectStatusCode(200).build();

        if(requestType.equalsIgnoreCase("POST")) {
            response =req.when().post(resourceAPI.getResource()).then().extract().response();
        }
        else if(requestType.equalsIgnoreCase("GET")) {
            response =req.when().get(resourceAPI.getResource()).then().extract().response();
        }
    }

    @Then("Consecutive Mealcards are named approprietly")
    public void checkMealcardNames() {
        JsonPath jspMealcardsData = new JsonPath(response.asString());
        System.out.println(response.asString());
        ArrayList<String> mealNames = new ArrayList<>();
        mealNames.add("Breakfast");
        mealNames.add("Lunch");
        mealNames.add("Dinner");
        for (int i = 0; i < mealNames.size(); i++) {
            String nameToValidate = jspMealcardsData.getString("[" + i + "].mealTitle");
            System.out.println("Checking " + nameToValidate + " against " + mealNames.get(i));
            Assert.assertEquals(nameToValidate, mealNames.get(i));
        }


    }

    @Given("Valid mealcards payload")
    public void validMealcardsPayload() throws IOException {
        req = given().filter(sessionFilterFromSetup).baseUri(getGlobalValue("BASE_URL")).urlEncodingEnabled(true)
                .log().all().contentType(ContentType.JSON).cookie("csrftoken", csrfTokenFromSetup)
                .cookie("sessionid", userSessionIdFromSetup)
                .header("X-CSRFToken", csrfTokenFromSetup)
                .sessionId(userSessionIdFromSetup)
                .body(TestDataPayloads.payloadMealcardCreation());
    }

    @Then("the API call got status code {int}")
    public void theAPICallGotStatusCodeOK(int expectedStatusCode) {
        Assert.assertEquals(response.getStatusCode(), expectedStatusCode);
    }

    @And("Verification by GetMealcardsEndpoint is successful")
    public void verificationByGetRequestIsSuccessful() throws IOException {
        establishBody("\"dateCreated\":\"2020-03-29\"");
        sendPostWithEstablishedBody("GetMealcardsEndpoint", "Post");

        JsonPath jspMealcardsData1 = new JsonPath(response.asString());
        int mealcardsQuantity = jspMealcardsData1.getList("").size();
        Assert.assertEquals(response.statusCode(),200);
        Assert.assertEquals(mealcardsQuantity, 3);
        Assert.assertEquals(jspMealcardsData1.getInt("[1].id"), 22222222);
    }

}
