package Admin.Testcases.HocVien;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.json.simple.JSONObject;
import Admin.Pages.AdminDashboardPage;
import Admin.Pages.HocVienPage;
import java.time.Duration;

public class TimKiemHocVien {
    WebDriver driver;
    AdminDashboardPage dashboard;
    HocVienPage hocVienPage;

    public TimKiemHocVien(WebDriver driver) {
        this.driver = driver;
        this.dashboard = new AdminDashboardPage(driver);
        this.hocVienPage = new HocVienPage(driver);
    }

    public boolean Tc_Tim_Kiem_Hoc_Vien(JSONObject hocVien) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        
        dashboard.openQuanLyHocVien();
        wait.until(d -> hocVienPage.isHocVienPageDisplayed());

        String maHV = (String) hocVien.get("MaHocVien");
        hocVienPage.searchHocVien(maHV);
        
        wait.until(d -> hocVienPage.isSearchResultDisplayed());

        return hocVienPage.isSearchResultDisplayed(); 
    }
}