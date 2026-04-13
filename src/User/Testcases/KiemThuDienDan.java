package User.Testcases;

import org.openqa.selenium.WebDriver;
import User.Pages.UserDashboardPage;

public class KiemThuDienDan {
    WebDriver driver;
    UserDashboardPage dashboardPage;

    public KiemThuDienDan(WebDriver driver) {
        this.driver = driver;
        this.dashboardPage = new UserDashboardPage(driver);
    }

    public void Tc_Kiem_Thu_Dien_Dan(String response) throws InterruptedException {
        dashboardPage.clickForumTab();
        Thread.sleep(2000);

        dashboardPage.typeForumAndSend(response);
        Thread.sleep(2000);

        dashboardPage.clickThichLatestPost();
        Thread.sleep(2000);

        dashboardPage.clickThuHoiLatestPost();
        Thread.sleep(2000);

    }
}