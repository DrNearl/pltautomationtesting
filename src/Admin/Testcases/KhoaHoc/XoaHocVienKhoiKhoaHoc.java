package Admin.Testcases.KhoaHoc;

import java.time.Duration;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import Admin.Pages.AdminDashboardPage;
import Admin.Pages.KhoaHocPage;
import Login.LoginPage;
import Utils.DocFileJSON;
import io.github.bonigarcia.wdm.WebDriverManager;

public class XoaHocVienKhoiKhoaHoc {
	WebDriver driver;
    LoginPage loginPage;
    AdminDashboardPage dashboard;
    KhoaHocPage khoaHocPage;
    
    @BeforeClass
    public void setup(){

        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();

        driver.manage().window().maximize();

        loginPage = new LoginPage(driver);
        dashboard = new AdminDashboardPage(driver);
        khoaHocPage = new KhoaHocPage(driver);

        loginPage.openLoginPage();
        loginPage.login("test.pltsolutions@gmail.com","plt@intern_051224");
    }
    
    @Test
    public void TC_XoaHocVienKhoiKhoaHoc() throws InterruptedException {

        dashboard.openQuanLyKhoaHoc();

        JSONArray data = DocFileJSON.docDuLieuJSON("resources/themhocvientrongkhoahoc.json");

        for(Object obj : data){

            JSONObject json = (JSONObject)obj;

            String tenKhoaHoc = json.get("tenKhoaHoc").toString();
            JSONArray hocVienList = (JSONArray) json.get("hocVien");

            // ===== SEARCH & OPEN =====
            khoaHocPage.searchKhoaHoc(tenKhoaHoc);
            Thread.sleep(2000);

            khoaHocPage.openKhoaHocTheoTen(tenKhoaHoc);
            Thread.sleep(3000);

            // ===== DELETE HOC VIEN =====
            System.out.println("===== DELETE HOC VIEN =====");

            for(Object hv : hocVienList){

                String hocVien = hv.toString().trim();

                try {
                    khoaHocPage.xoaHocVien(hocVien);
                    Thread.sleep(1500);

                    System.out.println("XÓA HỌC VIÊN THÀNH CÔNG - " + hocVien);
                } catch (Exception e) {
                    System.out.println("KHÔNG TÌM THẤY HỌC VIÊN - " + hocVien);
                }
            }

            // ===== RELOAD =====
            driver.navigate().refresh();
            Thread.sleep(5000);

            dashboard.openQuanLyKhoaHoc();
            Thread.sleep(2000);

            khoaHocPage.searchKhoaHoc(tenKhoaHoc);
            Thread.sleep(2000);

            khoaHocPage.openKhoaHocTheoTen(tenKhoaHoc);
            Thread.sleep(3000);

            // ===== VERIFY SAU KHI XÓA =====
            List<String> actualList = khoaHocPage.getDanhSachMaHocVien();

            System.out.println("===== VERIFY DELETE: " + tenKhoaHoc + " =====");

            boolean isAllPass = true;

            for(Object hv : hocVienList){

                String hocVien = hv.toString().trim();

                if(!actualList.contains(hocVien)){
                    System.out.println("Sinh viên " + hocVien + " đã được xóa - PASS");
                } else {
                    System.out.println("Sinh viên " + hocVien + " vẫn còn trên UI - FAIL");
                    isAllPass = false;
                }
            }

            System.out.println(isAllPass ? ">>> DELETE FINAL: PASS" : ">>> DELETE FINAL: FAIL");

            // ===== WAIT =====
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(3));

            wait.until(driver -> {
                try {
                    Thread.sleep(1500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return true;
            });
        }
    }
    
    @AfterClass
    public void tearDown(){
        driver.quit();
    }
}
