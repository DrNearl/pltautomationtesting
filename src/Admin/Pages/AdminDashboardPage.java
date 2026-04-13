package Admin.Pages;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class AdminDashboardPage {

    private WebDriver driver;
    private WebDriverWait wait; // Khai báo Wait

    // Menu Trang chủ admin – luôn có sau khi login thành công
    private By MENU_TRANG_CHU = By.xpath("//a[@href='/quan-tri-vien/trang-chu']");

    // Menu Quản lý học viên
    private By MENU_HOC_VIEN = By.xpath("//a[@href='/quan-tri-vien/hoc-vien']");

    // Menu Quản lý khoá học
    private By MENU_KHOA_HOC = By.xpath("//a[@href='/quan-tri-vien/khoa-hoc']");
    
    // Menu Quản lý bài tập
    private By MENU_BAI_TAP = By.xpath("//a[@href='/quan-tri-vien/bai-tap']");

    public AdminDashboardPage(WebDriver driver) {
        this.driver = driver;
        // Khởi tạo WebDriverWait mặc định chờ 10 giây (Cú pháp chuẩn Selenium 4)
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    // Đã thêm Wait: Chờ đến khi Menu Trang chủ hiển thị rõ ràng mới trả về true
    public boolean isDashboardDisplayed() {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(MENU_TRANG_CHU)).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public void openQuanLyHocVien() {
        wait.until(ExpectedConditions.elementToBeClickable(MENU_HOC_VIEN)).click();
    }

    public void openQuanLyKhoaHoc() {
        wait.until(ExpectedConditions.elementToBeClickable(MENU_KHOA_HOC)).click();
    }
    
    public void openQuanLyBaiTap() {
    	wait.until(ExpectedConditions.elementToBeClickable(MENU_BAI_TAP)).click();
    }
}