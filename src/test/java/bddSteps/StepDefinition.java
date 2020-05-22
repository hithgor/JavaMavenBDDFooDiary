package bddSteps;

import io.restassured.filter.session.SessionFilter;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import utilities.TestDataPayloads;
import utilities.Utils;

public class StepDefinition extends Utils {
    ////all given when then definitions will be there

    RequestSpecification res;
    ResponseSpecification resspec;
    Response response;
    TestDataPayloads payload =new TestDataPayloads();
    static String place_id;

    public void setCsrfTokenFromSetup(String csrfTokenFromSetup) {
        this.csrfTokenFromSetup = csrfTokenFromSetup;
    }

    public void setUserSessionIdFromSetup(String userSessionIdFromSetup) {
        this.userSessionIdFromSetup = userSessionIdFromSetup;
    }

    public void setSessionFilterFromSetup(SessionFilter sessionFilterFromSetup) {
        this.sessionFilterFromSetup = sessionFilterFromSetup;
    }

    private String csrfTokenFromSetup, userSessionIdFromSetup;
    private SessionFilter sessionFilterFromSetup;


}
