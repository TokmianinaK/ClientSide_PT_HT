package util;

import org.openqa.selenium.WebDriver;

import java.util.Optional;

import static util.Constants.BROWSER_FACTORY;
import static util.Constants.BROWSER_NAME;

public class WebDriverHolder {
    private static WebDriver driver;

    public static WebDriver getDriver() {
        if (!Optional.ofNullable(driver).isPresent()) {
            BROWSER_FACTORY.startBrowser(BROWSER_NAME, driver);
            return driver;
        } else {
            return driver;
        }
    }

    public static void setDriver(WebDriver driver) {
        WebDriverHolder.driver = driver;
    }
}
