package cucumber.Options;


import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(features="src/test/java/features/selenium/firefox/userLoginSelenium.feature",
        plugin ="json:target/jsonReports/cucumber-report.json",
        glue={"bddSteps"})
public class TestRunnerFFIT {


}
