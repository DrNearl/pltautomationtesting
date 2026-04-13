package Admin.Testcases.HocVien;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.json.simple.JSONObject;
import Admin.Pages.AdminDashboardPage;
import Admin.Pages.HocVienPage;
import java.time.Duration;

public class CapNhatHocVien {
    WebDriver driver;
    AdminDashboardPage dashboard;
    HocVienPage hocVienPage;

    public CapNhatHocVien(WebDriver driver) {
        this.driver = driver;
        this.dashboard = new AdminDashboardPage(driver);
        this.hocVienPage = new HocVienPage(driver);
    }

    @SuppressWarnings("unchecked")
    public String Tc_Cap_Nhat_Hoc_Vien(JSONObject hocVien) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        
        dashboard.openQuanLyHocVien();
        wait.until(d -> hocVienPage.isHocVienPageDisplayed());

        String maHV = (String) hocVien.get("MaHocVien");
        hocVienPage.searchHocVien(maHV);
        wait.until(d -> hocVienPage.isSearchResultDisplayed());

        hocVienPage.clickSuaHocVien();
        
        wait.until(ExpectedConditions.elementToBeClickable(By.name("student_code")));
        
        try { Thread.sleep(500); } catch (InterruptedException e) {}

        JSONObject duLieuCapNhat = (JSONObject) hocVien.get("DuLieuCapNhat");
        
        String maHVMoi = (String) duLieuCapNhat.get("MaHocVien");
        String sdtMoi = (String) duLieuCapNhat.get("SoDienThoai");
        String diaChiMoi = (String) duLieuCapNhat.get("DiaChi");

        hocVienPage.enterMaHocVien(maHVMoi);
        hocVienPage.enterSoDienThoai(sdtMoi);
        hocVienPage.enterDiaChi(diaChiMoi);

        hocVien.put("MaHocVien", maHVMoi);
        hocVien.put("SoDienThoai", sdtMoi);
        hocVien.put("DiaChi", diaChiMoi);

        hocVienPage.clickSuaChinh();
        
        String thongBao = hocVienPage.getThongBaoThanhCong();
        hocVienPage.clickOK();
        
        try { Thread.sleep(500); } catch (InterruptedException e) {}
        
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("swal2-title")));
        
        return thongBao;
    }
}