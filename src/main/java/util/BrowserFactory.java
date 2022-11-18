package util;

import lombok.SneakyThrows;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import java.util.concurrent.TimeUnit;

public class BrowserFactory {

    @SneakyThrows
    public WebDriver startBrowser(String browserName, WebDriver driver) {

        if ("Firefox".equalsIgnoreCase(browserName)) {
            driver = new FirefoxDriver();
        } else if ("ChromeLocal".equalsIgnoreCase(browserName)) {
            driver = new ChromeDriver();
        }

        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(50, TimeUnit.SECONDS);
        return driver;
    }
}
