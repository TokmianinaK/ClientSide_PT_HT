package pages;

import navigationtiming.PerfNavigationTiming;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;

import static util.WebDriverHolder.getDriver;

public class BasePage {
    protected WebDriverWait wait;

    public BasePage() {
        wait = new WebDriverWait(getDriver(), 50);
        PageFactory.initElements(getDriver(), this);
    }

    protected PerfNavigationTiming perfNavigationTiming = new PerfNavigationTiming();

    @FindBy(xpath = "//a[@href='/computers']")
    protected WebElement computersButton;

    protected void waitUntilPageIsFullyLoaded(WebDriverWait webDriverWait) {
        webDriverWait.until(
                webDriver -> ((JavascriptExecutor) webDriver).executeScript("return document.readyState").equals("complete"));
    }

}
