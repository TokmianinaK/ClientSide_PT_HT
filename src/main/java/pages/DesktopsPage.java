package pages;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;

import java.util.List;

public class DesktopsPage extends BasePage {
    public DesktopsPage() {
        super();
    }

    @FindBy(xpath = "//h2[@class='title']/a")
    private WebElement desktopsPageButton;

    @FindBy(xpath = "//h2[@class='product-title']/a")
    private List<WebElement> desktopsProductsList;

    @FindBy(xpath = "//strong[@class='current-item']")
    private WebElement openDesktopsTitle;

    @FindBy(xpath = "//div[@class='picture']//img")
    private WebElement productImg;

    public DesktopsPage openDesktopsPage() {
        wait.until(ExpectedConditions.elementToBeClickable(desktopsPageButton));
        desktopsPageButton.click();
        waitUntilPageIsFullyLoaded(wait);
        Assert.assertTrue(productImg.isDisplayed());
        perfNavigationTiming.writeToInflux("DesktopsPage");
        return this;
    }

    public DesktopsPage openFirstDesktop() {
        String firstDesktopInTheListName = desktopsProductsList.get(0).getText();
        desktopsProductsList.get(0).click();
        waitUntilPageIsFullyLoaded(wait);
        Assert.assertTrue(openDesktopsTitle.getText().contains("Build your own computer"));
        perfNavigationTiming.writeToInflux("ProductPage");
        return this;
    }
}
