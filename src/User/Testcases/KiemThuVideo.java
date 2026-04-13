package User.Testcases;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import User.Pages.UserDashboardPage;
import java.util.Set;

public class KiemThuVideo {
    WebDriver driver;
    UserDashboardPage dashboardPage;

    public KiemThuVideo(WebDriver driver) {
        this.driver = driver;
        this.dashboardPage = new UserDashboardPage(driver);
    }

    public void Tc_Kiem_Thu_Video(String expectedUrl) throws InterruptedException {
        dashboardPage.clickVideoTab();
        dashboardPage.joinGoogleMeet();

        String originalWindow = driver.getWindowHandle();
        Set<String> allWindows = driver.getWindowHandles();
        for (String windowHandle : allWindows) {
            if (!windowHandle.equals(originalWindow)) {
                driver.switchTo().window(windowHandle);
                break;
            }
        }

        Thread.sleep(5000);
        String currentUrl = driver.getCurrentUrl();
        Assert.assertTrue(currentUrl.contains(expectedUrl));
        driver.close();
        driver.switchTo().window(originalWindow);
    }
}