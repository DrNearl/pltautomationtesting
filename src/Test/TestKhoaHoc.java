package Test;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.*;

import Admin.Pages.AdminDashboardPage;
import Admin.Pages.KhoaHocPage;
import Admin.Testcases.KhoaHoc.ThemDienDanKhoaHoc;
import Admin.Testcases.KhoaHoc.ThemHocVienVaoKhoaHoc;
import Admin.Testcases.KhoaHoc.ThemKhoaHoc;
import Admin.Testcases.KhoaHoc.ThemNoiDungKhoaHoc;
import Admin.Testcases.KhoaHoc.ThemVideoConferenceKhoaHoc;
import Admin.Testcases.KhoaHoc.CapNhatThongTinKhoaHoc;
import Login.LoginPage;
import io.github.bonigarcia.wdm.WebDriverManager;

public class TestKhoaHoc {

    protected WebDriver driver;

    protected LoginPage loginPage;
    protected AdminDashboardPage dashboardPage;
    protected KhoaHocPage khoaHocPage;

    @BeforeClass
    public void setupBrowser() {

        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().window().maximize();

        loginPage = new LoginPage(driver);
        dashboardPage = new AdminDashboardPage(driver);
        khoaHocPage = new KhoaHocPage(driver);
        
        loginPage.openLoginPage();
        loginPage.login("test.pltsolutions@gmail.com","plt@intern_051224");
    }

    @Test
    public void Test_FullFlow_KhoaHoc() throws Exception {

        // ===== INIT TEST =====
    	ThemKhoaHoc testKhoaHoc = new ThemKhoaHoc(driver);
        ThemHocVienVaoKhoaHoc testHocVien = new ThemHocVienVaoKhoaHoc(driver);
        ThemNoiDungKhoaHoc testNoiDung = new ThemNoiDungKhoaHoc(driver);
        ThemDienDanKhoaHoc testDienDan = new ThemDienDanKhoaHoc(driver);
        ThemVideoConferenceKhoaHoc testVideo = new ThemVideoConferenceKhoaHoc(driver);
        CapNhatThongTinKhoaHoc testUpdate = new CapNhatThongTinKhoaHoc(driver);

        // ===== STEP 1: THÊM KHÓA HỌC =====
        testKhoaHoc.TC_Them_Khoa_Hoc();

        // ===== STEP 2: THÊM HỌC VIÊN =====
        testHocVien.TC_ThemHocVienVaoKhoaHoc();

        // ===== STEP 3: NỘI DUNG =====
         testNoiDung.TC_Them_Noi_Dung_Khoa_Hoc();

        // ===== STEP 4: DIỄN ĐÀN =====
         testDienDan.TC_Them_Dien_Dan_Khoa_Hoc();

        // ===== STEP 5: VIDEO =====
         testVideo.TC_Them_Video_Conference_Khoa_Hoc();
         
         dashboardPage.openQuanLyKhoaHoc();
         new WebDriverWait(driver, Duration.ofSeconds(5))
         .until(ExpectedConditions.visibilityOfElementLocated(
             By.xpath("//label[text()='Search']/following-sibling::input")
     ));

        // ===== STEP 6: UPDATE =====
         testUpdate.TC_Cap_Nhat_Thong_Tin_Khoa_Hoc();
    }

    @AfterClass
    public void closeBrowser() {
    	driver.quit();
    }
}