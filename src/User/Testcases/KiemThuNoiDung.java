package User.Testcases;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.openqa.selenium.WebDriver;
import User.Pages.UserDashboardPage;

public class KiemThuNoiDung {
    WebDriver driver;
    UserDashboardPage dashboardPage;

    public KiemThuNoiDung(WebDriver driver) {
        this.driver = driver;
        this.dashboardPage = new UserDashboardPage(driver);
    }

    public void Tc_Kiem_Thu_Noi_Dung(JSONArray chapters) {
        dashboardPage.clickCourse();
        
        for (int i = 0; i < chapters.size(); i++) {
            JSONObject chapterData = (JSONObject) chapters.get(i);
            dashboardPage.clickChapterAndAllLessons(chapterData);
        }
    }
}