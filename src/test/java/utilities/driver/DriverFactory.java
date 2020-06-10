package utilities.driver;

import org.openqa.selenium.WebDriver;

import java.util.ArrayList;
import java.util.List;

public final class DriverFactory {

    private static ThreadLocal<WebDriver> drivers = new ThreadLocal<>();

    // To quit the drivers and browsers at the end only.
    // Right now code below's only killing chromedrivers. Need to learn
    // about threads and parallelism of Java to fix that.
    // Logic's okay, It's just running at the wrong time
    private static List<WebDriver> storedDrivers = new ArrayList<>();

    static {
        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                storedDrivers.forEach(WebDriver::quit);
            }
        });

    }

    private DriverFactory() {}

    public static WebDriver getDriver() {
        return drivers.get();
    }

    public static void addDriver(WebDriver driver) {
        storedDrivers.add(driver);
        drivers.set(driver);
    }

    public static void removeDriver() {
        storedDrivers.remove(drivers.get());
        drivers.remove();
    }

    public static void printDrivers() {
        System.out.println("PRINTING THE LIST OF WEBDRIVERS========");
        for (WebDriver storedDriver : storedDrivers) {
            System.out.println(storedDriver);
        }
    }



}