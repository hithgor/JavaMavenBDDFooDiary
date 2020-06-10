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
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import utilities.APIEnum;
import utilities.TestDataPayloads;
import utilities.Utils;
import utilities.driver.DriverFactory;
import utilities.driver.SharedDriver;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static io.restassured.RestAssured.given;

public class StepDefinition extends Utils {
    ////all given when then definitions will be there

    public StepDefinition(SharedDriver driver) {

    }

    static RequestSpecification req;
    static ResponseSpecification resspec;
    static Response response;

    TestDataPayloads payload = new TestDataPayloads();
    static String place_id;
    static String csrfTokenFromSetup, userSessionIdFromSetup;
    static SessionFilter sessionFilterFromSetup;



    public void setReqFromSetup(RequestSpecification req) {
        this.req = req;
    }

    public void setResspecFromSetup(ResponseSpecification resspec) {
        this.resspec = resspec;
    }

    public void setResponseFromSetup(Response response) {
        this.response = response;
    }

    public void setCsrfTokenFromSetup(String csrfTokenFromSetup) {
        this.csrfTokenFromSetup = csrfTokenFromSetup;
    }

    public void setUserSessionIdFromSetup(String userSessionIdFromSetup) {
        this.userSessionIdFromSetup = userSessionIdFromSetup;
    }

    public void setSessionFilterFromSetup(SessionFilter sessionFilterFromSetup) {
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

        APIEnum resourceAPI = APIEnum.valueOf(resourceName);
        System.out.println(resourceAPI.getResource());


        resspec = new ResponseSpecBuilder().expectStatusCode(200).build();

        if (requestType.equalsIgnoreCase("POST")) {
            response = req.when().post(resourceAPI.getResource()).then().extract().response();
        } else if (requestType.equalsIgnoreCase("GET")) {
            response = req.when().get(resourceAPI.getResource()).then().extract().response();
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

    @And("Verification by GetMealcardsEndpoint with expectedQuantity={int}")
    public void verificationByGetRequestIsSuccessful(int expectedQuantity) throws IOException {
        establishBody("\"dateCreated\":\"2020-03-29\"");
        sendPostWithEstablishedBody("GetMealcardsEndpoint", "Post");

        JsonPath jspMealcardsData1 = new JsonPath(response.asString());
        int mealcardsQuantity = jspMealcardsData1.getList("").size();
        Assert.assertEquals(response.statusCode(), 200);
        Assert.assertEquals(mealcardsQuantity, expectedQuantity);
        Assert.assertEquals(jspMealcardsData1.getInt("[1].id"), 22222222);
    }


    @Given("User is on {string}")
    public void userIsOn(String expectedPage) throws IOException {


        APIEnum resourceAPI = APIEnum.valueOf(expectedPage);
        String pathBuilder = getGlobalValue("BASE_URL") + resourceAPI.getResource();
        DriverFactory.getDriver().get(pathBuilder);

    }

    @When("User clicks on element XPATH {string}")
    public void userClicksOnElement(String xpathToBeClicked) {

        DriverFactory.getDriver().findElement(By.xpath(xpathToBeClicked)).click();
    }

    @Then("Login form is displayed")
    public void loginFormIsShown() {

        Boolean loginForm = DriverFactory.getDriver().findElement(By.xpath("/html[1]/body[1]/div[1]/form[2]")).isDisplayed();
        Assert.assertTrue(loginForm);
    }

    @And("User logs in with valid credentials")
    public void userSendsLogsInWithValidCredentials() throws IOException {
        WebElement inputField = DriverFactory.getDriver().findElement(By.xpath("//form[2]//label[1]//input[1]"));
        inputField.sendKeys(getGlobalValue("VALID_EMAIL"));
        inputField = DriverFactory.getDriver().findElement(By.xpath("//form[2]//label[2]//input[1]"));
        inputField.sendKeys(getGlobalValue("VALID_PASSWORD"));
        DriverFactory.getDriver().findElement(By.xpath("//button[@class='btn-secondary'][contains(text(),'Login')]")).click();
    }

    @Then("Element with xpath {string} is displayed")
    public void elementWithXPATHIsDisplayed(String xpathOfSearchedElement) {

        WebElement searchedWebElement = DriverFactory.getDriver().findElement(By.xpath(xpathOfSearchedElement));
        Assert.assertTrue(searchedWebElement.isDisplayed());
    }

    @Given("Element with xpath {string} does not exist")
    public void elementWithXpathIsNotDisplayed(String xpathOfSearchedElement) {

        boolean searchedWebElement = DriverFactory.getDriver().findElements(By.xpath(xpathOfSearchedElement)).isEmpty();
        Assert.assertTrue(searchedWebElement);
    }

    @Given("Full successful login procedure")
    public void fullSuccessfulLoginProcedure() throws IOException {

        DriverFactory.getDriver().findElement(By.xpath("/html[1]/body[1]/button[1]")).click();
        WebElement inputField = DriverFactory.getDriver().findElement(By.xpath("//form[2]//label[1]//input[1]"));
        inputField.sendKeys(getGlobalValue("VALID_EMAIL"));
        inputField = DriverFactory.getDriver().findElement(By.xpath("//form[2]//label[2]//input[1]"));
        inputField.sendKeys(getGlobalValue("VALID_PASSWORD"));
        DriverFactory.getDriver().findElement(By.xpath("//button[@class='btn-secondary'][contains(text(),'Login')]")).click();
    }

    @Then("User waits up to {int} seconds for element XPATH {string}")
    public void userWaitsUpToSeconds(int maxWaitingTime, String xpathElement) {
        WebDriverWait wait = new WebDriverWait(DriverFactory.getDriver(), maxWaitingTime);
        WebElement linkToBeClicked = wait.until(ExpectedConditions
                .visibilityOfElementLocated(By
                        .xpath(xpathElement)));
        Assert.assertTrue(linkToBeClicked.isDisplayed());
    }

    @When("User clicks on element XPATH {string} by submit")
    public void userClicksOnElementXPATHBySubmit(String xpathToBeClicked) {
        WebDriverWait wait = new WebDriverWait(DriverFactory.getDriver(), 2);
        WebElement linkToBeClicked = wait.until(ExpectedConditions
                .visibilityOfElementLocated(By
                        .xpath(xpathToBeClicked)));
        linkToBeClicked.submit();
    }

    @When("User clicks on element XPATH {string} by Keys.RETURN")
    public void userClicksOnElementXPATHByKeysReturn(String xpathToBeClicked) {
        WebDriverWait wait = new WebDriverWait(DriverFactory.getDriver(), 2);
        WebElement linkToBeClicked = wait.until(ExpectedConditions
                .visibilityOfElementLocated(By
                        .xpath(xpathToBeClicked)));
        linkToBeClicked.sendKeys(Keys.RETURN);
    }

    @When("User chooses date day {int} month {string} year {int}")
    public void userChoosesDateDayMonthYear(int targetDay, String targetMonth, int targetYear) throws InterruptedException {
        WebDriverWait wait = new WebDriverWait(DriverFactory.getDriver(), 7);
        Thread.sleep(1000);
        WebElement datepicker = DriverFactory.getDriver().findElement(By.id("datepicker"));
        datepicker.click();
        WebElement linkToBeClicked = wait.until(ExpectedConditions
                .visibilityOfElementLocated(By
                        .xpath("//div[@id='datepicker-frame']/ul/li[2]")));
        linkToBeClicked.click();

        WebElement displayedYearElement = DriverFactory.getDriver().findElement(By.xpath("//div[@id='datepicker-frame']/ul/li[2]"));
        ///displayedYearElement.click();
        displayedYearElement = DriverFactory.getDriver().findElement(By.xpath("//div[@id='datepicker-frame']/ul/li[2]"));
        String displayedYear = displayedYearElement.getText();
        int parsedDisplayedYear = Integer.parseInt(displayedYear);
        System.out.println(displayedYear);


        while (parsedDisplayedYear > targetYear) {
            DriverFactory.getDriver().findElement(By.xpath("//div[@id='datepicker-frame']/ul/li[1]")).click();
            displayedYearElement = DriverFactory.getDriver().findElement(By.xpath("//div[@id='datepicker-frame']/ul/li[2]"));
            displayedYear = displayedYearElement.getText();
            parsedDisplayedYear = Integer.parseInt(displayedYear);
        }
        while (parsedDisplayedYear < targetYear) {
            DriverFactory.getDriver().findElement(By.xpath("//div[@id='datepicker-frame']/ul/li[3]")).click();
            displayedYearElement = DriverFactory.getDriver().findElement(By.xpath("//div[@id='datepicker-frame']/ul/li[2]"));
            displayedYear = displayedYearElement.getText();
            parsedDisplayedYear = Integer.parseInt(displayedYear);
        }
        targetMonth = targetMonth.substring(0, 3);
        WebElement monthElement = DriverFactory.getDriver().findElement(By.xpath("//td[contains(text(),'" + targetMonth + "')]"));
        monthElement.click();

        List<WebElement> dayElementsDisplayed = DriverFactory.getDriver()
                .findElements(By.xpath("//div[@id='datepicker-frame']/table/tr/td[contains(@class, 'pointer')]"));
        dayElementsDisplayed.get(targetDay - 1).click();

    }

    @Then("{int} Mealcards are displayed")
    public void numberofmealcardsMealcardsAreDisplayed(int predictedNumberOfMealcards) throws InterruptedException {
        //^[0-9]{5,10}$  --> just leaving that regex here to use for validation of IDs later
        Thread.sleep(1500);
        List<WebElement> foundMealcards = DriverFactory.getDriver()
                .findElements(By.xpath("//div[@id='cardContainer']/div"));
        System.out.println(foundMealcards.size());
        Assert.assertEquals(foundMealcards.size(), predictedNumberOfMealcards);
        ArrayList<Integer> acceptableIdLength = new ArrayList<>();
        acceptableIdLength.add(7);
        acceptableIdLength.add(8);
        if (foundMealcards.size() != 0) {
            for (int i = 1; i <= foundMealcards.size(); i++) {
                WebElement child = DriverFactory.getDriver()
                        .findElement(By.xpath("//body/div[@class='cardContainer']/div[" + i + "]/div[1]"));
                Assert.assertTrue(acceptableIdLength.contains(child.getAttribute("id").length()));
            }
        }
    }

    @And("Check for mealcard naming convention")
    public void checkForMealcardNamingConvention() {
        WebElement thirdMealcard = DriverFactory.getDriver().findElement(By.xpath("//body/div[@class='cardContainer']/div[3]/div[1]"));
        String thirdMealCardId = thirdMealcard.getAttribute("id");

        DriverFactory.getDriver().findElement(By.xpath("//div[2]//div[1]//div[1]//button[1]")).click();
        DriverFactory.getDriver().findElement(By.xpath("//button[@id='addMealButton']")).click();

        WebElement secondMealcard = DriverFactory.getDriver().findElement(By.xpath("//body/div[@class='cardContainer']/div[2]/div[1]"));
        String secondMealcardId = secondMealcard.getAttribute("id");
        String expectedLunch = DriverFactory.getDriver().findElement(By.xpath("//div[2]//div[1]//div[1]//div[1]//h5[1]")).getText();

        Assert.assertEquals(thirdMealCardId, secondMealcardId);
        Assert.assertEquals(expectedLunch, "Lunch");

    }

    @When("User adds ingredient {string} to mealcard number {int}")
    public void userAddsIngredientStringToIntMealcard(String ingredientFullName, int mealcardNumber) throws InterruptedException {
        /*
        Since our site gives unique IDs dynamically to mealcard elements - there's a better way of writing this
        You could extract id from mealcardNumber and use that to create all needed xpaths on the way.
        Please refactor some day to make it less disgusting.
         */
        mealcardNumber = mealcardNumber + 1;
        String mealcardXpath = "//body/div[@class='cardContainer']/div[" + mealcardNumber + "]/div[1]";
        userClicksOnElement(mealcardXpath);
        userClicksOnElement(" //span[@class='btn-sm spanFoodInvisible spanFoodVisible']");

        WebElement foodNameInput = DriverFactory.getDriver().findElement(By.xpath("//input[@id='searchFoodNameInput1']"));
        foodNameInput.sendKeys(ingredientFullName);

        userClicksOnElement("//div[@class='formIndex mb-3 formIndexVisible']//button[@class='btn-secondary btnForm']");
        Thread.sleep(2000);
        WebElement dropdown = DriverFactory.getDriver().findElement(By
                .xpath("//div[@class='formIndex mb-3 formIndexVisible']//select[@name='locality']"));
        Select dropdownSelect = new Select(dropdown);
        dropdownSelect.selectByVisibleText(ingredientFullName);

        dropdown = DriverFactory.getDriver().findElement(By
                .xpath("//div[@class='formIndex mb-3 formIndexVisible']//select[@placeholder='Pick a portion']"));
        Thread.sleep(1500);
        dropdownSelect = new Select(dropdown);
        dropdownSelect.selectByIndex(0);
        userClicksOnElement("//div[@class='formIndex mb-3 formIndexVisible']//button[@class='btn-secondary btn-search-food'][contains(text(),'Add position')]");


    }

    @Then("Ingredient {string} is present in mealcard number {int}")
    public void ingredientIsPresentInMealcardNumber(String ingredientFullName, int mealcardNumber) {
        mealcardNumber = mealcardNumber + 1;
        WebElement searchedIngredient = DriverFactory.getDriver().findElement(By
                .xpath("//div[" + mealcardNumber + "]//div[1]//div[1]//div[2]//span[contains(text(), '" + ingredientFullName + "')]"));

        Assert.assertTrue(searchedIngredient.isDisplayed());
    }


    @Then("Ingredient {string} is not present in mealcard number {int}")
    public void ingredientIsNotPresentInMealcardNumber(String ingredientFullName, int mealcardNumber) {

        mealcardNumber = mealcardNumber + 1;
        List<WebElement> searchedIngredients = DriverFactory.getDriver().findElements(By
                .xpath("//div[" + mealcardNumber + "]//div[1]//div[1]//div[2]//span[@class='ingredientName']"));

        for (WebElement i : searchedIngredients) {
            Assert.assertNotEquals(i.getText(), ingredientFullName);
        }

    }


    @And("Sum of calories at each mealcard is properly counted")
    public void sumOfCaloriesAtEachMealcardIsProperlyCounted() {

        List<WebElement> foundMealcards = DriverFactory.getDriver()
                .findElements(By.xpath("//div[@id='cardContainer']/div"));
        int sumOfCalories;
        for (int i = 1; i <= foundMealcards.size(); i++) {
            sumOfCalories = 0;
            ///List grabs every odd span of a mealcard
            List<WebElement> spansOfMealcards = DriverFactory.getDriver().findElements(By
                    .xpath("//div[" + i + "]//div[1]//div[1]//div[2]//span[position() mod 2 = 0]"));
            for (WebElement j : spansOfMealcards) {
                sumOfCalories += Integer.parseInt(j.getText());
            }
            int displayedSumOfCalories = Integer.parseInt(DriverFactory.getDriver().findElement(By
                    .xpath("//div[" + i + "]//div[1]//div[1]//div[2]//span[last()]")).getText());
            System.out.println("Displayed" + displayedSumOfCalories + " counted" + sumOfCalories);
            Assert.assertEquals(sumOfCalories, displayedSumOfCalories);
        }

    }

}

