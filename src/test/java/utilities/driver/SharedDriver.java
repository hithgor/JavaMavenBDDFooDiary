package utilities.driver;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

import java.util.concurrent.TimeUnit;


public class SharedDriver {

    public SharedDriver() {
            WebDriver driver;
            String browser = System.getProperty("browser");
            switch (browser) {
                case "chrome":
                    System.setProperty("webdriver.chrome.driver", "D:\\chromedriver.exe");
                    System.setProperty("webdriver.chrome.whitelistedIps", "");
                    ChromeOptions chromeHeadlessConfig = new ChromeOptions();
                    chromeHeadlessConfig.addArguments("headless");
                    driver = new ChromeDriver(chromeHeadlessConfig);
                    break;
                    
                case "firefox":
                    System.setProperty("webdriver.gecko.driver", "D:\\geckodriver.exe");
                    FirefoxOptions firefoxHeadlessConfig = new FirefoxOptions();
                    firefoxHeadlessConfig.setHeadless(true);
                    driver = new FirefoxDriver(firefoxHeadlessConfig);
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + browser);
            }
            driver.manage().timeouts()
                    .implicitlyWait(2, TimeUnit.SECONDS);
            driver.manage().window().maximize();
            DriverFactory.addDriver(driver);

        }
}