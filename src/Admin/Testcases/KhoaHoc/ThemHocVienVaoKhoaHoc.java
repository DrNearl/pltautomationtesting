package Admin.Testcases.KhoaHoc;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import Admin.Pages.AdminDashboardPage;
import Admin.Pages.KhoaHocPage;
import Login.LoginPage;
import Utils.DocFileJSON;
import io.github.bonigarcia.wdm.WebDriverManager;

public class ThemHocVienVaoKhoaHoc {

    WebDriver driver;
    LoginPage loginPage;
    AdminDashboardPage dashboard;
    KhoaHocPage khoaHocPage;

    public static String TEN_KHOA_HOC;
    
    public ThemHocVienVaoKhoaHoc(WebDriver driver) {
        this.driver = driver;
        loginPage = new LoginPage(driver);
        dashboard = new AdminDashboardPage(driver);
        khoaHocPage = new KhoaHocPage(driver);
    }

    public void TC_ThemHocVienVaoKhoaHoc() throws InterruptedException {

        dashboard.openQuanLyKhoaHoc();

        JSONArray data = DocFileJSON.docDuLieuJSON("resources/themhocvientrongkhoahoc.json");

        for(Object obj : data){

            JSONObject json = (JSONObject)obj;

            String tenKhoaHoc = json.get("tenKhoaHoc").toString();
            TEN_KHOA_HOC = tenKhoaHoc;

            JSONArray hocVienList = (JSONArray) json.get("hocVien");

            // ===== SEARCH & OPEN =====
            khoaHocPage.searchKhoaHoc(tenKhoaHoc);
            Thread.sleep(2000);

            khoaHocPage.openKhoaHocTheoTen(tenKhoaHoc);
            Thread.sleep(3000);

            // ===== ADD HOC VIEN =====
            for(Object hv : hocVienList){

                String hocVien = hv.toString().trim();

                khoaHocPage.clickThemHocVien();
                Thread.sleep(1500);

                khoaHocPage.enterEmailOrIdHocVien(hocVien);

                khoaHocPage.clickThemHocVienTrongKhoaHoc();
                Thread.sleep(1500);

                khoaHocPage.clickXacNhan();
                Thread.sleep(1500);

                System.out.println("THÊM HỌC VIÊN THÀNH CÔNG - " + hocVien);
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

         // ===== TOGGLE STATUS =====
            for(Object hv : hocVienList){

                String hocVien = hv.toString().trim();

                // tắt
                khoaHocPage.toggleTrangThaiHocVien(hocVien);
                Thread.sleep(500);

                // bật lại
                khoaHocPage.toggleTrangThaiHocVien(hocVien);
                Thread.sleep(500);

                System.out.println("TOGGLED OFF -> ON - " + hocVien);
            }

            // ===== LẤY DATA =====
            List<String> actualList = khoaHocPage.getDanhSachMaHocVien();

            List<String> expectedList = new ArrayList<>();
            for(Object hv : hocVienList){
                expectedList.add(hv.toString().trim());
            }

            // ===== SO SÁNH =====
            System.out.println("===== VERIFY: " + tenKhoaHoc + " =====");

            boolean isAllPass = true;

            for(String expected : expectedList){
                if(actualList.contains(expected)){
                    System.out.println("Sinh viên " + expected + " trùng khớp với dữ liệu JSON - PASS");
                } else {
                    System.out.println("Sinh viên " + expected + " KHÔNG có trên UI - FAIL");
                    isAllPass = false;
                }
            }

            for(String actual : actualList){
                if(!expectedList.contains(actual)){
                    System.out.println("Sinh viên " + actual + " không có trong JSON - FAIL");
                    isAllPass = false;
                }
            }

            System.out.println(isAllPass ? ">>> FINAL: PASS" : ">>> FINAL: FAIL");
            
         // ===== ĐỢI SAU TOGGLE (1.5s) =====
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
}