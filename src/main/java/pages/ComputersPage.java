package pages;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;

public class ComputersPage extends BasePage {
    public ComputersPage() {
        super();
    }

    @FindBy(xpath = "//div[@class='page category-page']")
    private WebElement categoriesBanners;

    public ComputersPage openComputersPage() {
        computersButton.click();
        wait.until(ExpectedConditions.elementToBeClickable(categoriesBanners));
        waitUntilPageIsFullyLoaded(wait);
        return this;
    }
}
