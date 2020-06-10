package cucumber.Options;


import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.AfterClass;
import org.junit.runner.RunWith;

import java.io.IOException;

@RunWith(Cucumber.class)
@CucumberOptions(features="src/test/java/features/selenium/firefox/",
        plugin ="json:target/jsonReports/cucumber-report.json",
        glue={"bddSteps"})
public class TestRunnerFFIT {
    ///This is an ugly workaround until I find a way to kill geckodrivers from
    ///DriverFactory.java. It takes a hard 8 seconds, which is annoying for small
    /// [<1 minute] tests, but is okay for anything past 2 minutes, i guess.
    @AfterClass
    public static void cleanUp() throws IOException {
            Runtime.getRuntime().exec("taskkill /F /IM geckodriver.exe");
        }
    }


