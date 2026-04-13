package Admin.Testcases.HocVien;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.json.simple.JSONObject;
import Admin.Pages.AdminDashboardPage;
import Admin.Pages.HocVienPage;
import java.time.Duration;

public class XoaHocVien {
    WebDriver driver;
    AdminDashboardPage dashboard;
    HocVienPage hocVienPage;

    public XoaHocVien(WebDriver driver) {
        this.driver = driver;
        this.dashboard = new AdminDashboardPage(driver);
        this.hocVienPage = new HocVienPage(driver);
    }

    public String Tc_Xoa_Hoc_Vien(JSONObject hocVien) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        dashboard.openQuanLyHocVien();
        wait.until(d -> hocVienPage.isHocVienPageDisplayed());

        String maHVCanXoa;
        JSONObject duLieuCapNhat = (JSONObject) hocVien.get("DuLieuCapNhat");
        
        if (duLieuCapNhat != null && duLieuCapNhat.get("MaHocVien") != null) {
            maHVCanXoa = (String) duLieuCapNhat.get("MaHocVien");
        } else {
            maHVCanXoa = (String) hocVien.get("MaHocVien");
        }

        hocVienPage.searchHocVien(maHVCanXoa);    
        wait.until(d -> hocVienPage.isSearchResultDisplayed());
        hocVienPage.clickXoaHocVien();
        hocVienPage.confirmXoa();
        
        String thongBao = hocVienPage.getThongBaoThanhCong();
        hocVienPage.clickOK();
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("swal2-title")));
        hocVienPage.clearSearchBox();
        
        return thongBao;
    }
}