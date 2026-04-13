package Admin.Testcases.KhoaHoc;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import Admin.Pages.AdminDashboardPage;
import Admin.Pages.KhoaHocPage;
import Login.LoginPage;
import Utils.DocFileJSON;

import java.time.Duration;

public class CapNhatThongTinKhoaHoc {

    WebDriver driver;

    LoginPage loginPage;
    AdminDashboardPage adminDashboardPage;
    KhoaHocPage khoaHocPage;

    WebDriverWait wait;

    public CapNhatThongTinKhoaHoc(WebDriver driver) {

        this.driver = driver;

        loginPage = new LoginPage(driver);
        adminDashboardPage = new AdminDashboardPage(driver);
        khoaHocPage = new KhoaHocPage(driver);

        wait = new WebDriverWait(driver, Duration.ofSeconds(5));
    }

    public void TC_Cap_Nhat_Thong_Tin_Khoa_Hoc() throws Exception {

        // ===== OPEN PAGE =====
        adminDashboardPage.openQuanLyKhoaHoc();

        // ===== HANDLE POPUP =====
        try {
            khoaHocPage.clickConfirmPopupChuyenTrang();
        } catch (Exception e) {
            System.out.println("Khong co popup chuyen trang");
        }

        // ===== LẤY TÊN KHÓA HỌC (KHÔNG DÙNG STATIC) =====
        JSONArray dataKH = DocFileJSON.docDuLieuJSON("resources/themhocvientrongkhoahoc.json");
        JSONObject jsonKH = (JSONObject) dataKH.get(0);

        String tenKhoaHocCu = jsonKH.get("tenKhoaHoc").toString();

        // ===== DATA UPDATE =====
        JSONArray data = DocFileJSON.docDuLieuJSON("data/capnhatkhoahoc.json");

        for(Object obj : data){

            JSONObject json = (JSONObject) obj;

            String tenKhoaHocMoi = json.get("tenKhoaHoc").toString();
            String moTaMoi = json.get("moTa").toString();
            String anh = json.get("anh").toString();

            // ===== SEARCH =====
            khoaHocPage.searchKhoaHoc(tenKhoaHocCu);

            khoaHocPage.openKhoaHocTheoTen(tenKhoaHocCu);
            wait.until(ExpectedConditions.visibilityOfElementLocated(
            	    By.xpath("//div[contains(text(),'Thông tin môn học')]")
            	));

            // ===== VÀO TAB THÔNG TIN =====
            khoaHocPage.clickThongTinMonHocTab();
            wait.until(ExpectedConditions.visibilityOfElementLocated(
            	    By.name("name")
            	));

            // ===== UPDATE =====
            khoaHocPage.enterTenKhoaHocCapNhat(tenKhoaHocMoi);
            khoaHocPage.enterMoTaKhoaHoc(moTaMoi);
            khoaHocPage.uploadAnhBiaKhoaHoc(anh);

            khoaHocPage.clickCapNhatKhoaHoc();

            // ===== POPUP =====
            try {
                khoaHocPage.clickOKPopup();
            } catch (Exception e) {
                System.out.println("Khong co popup OK");
            }

            // ===== RELOAD =====
            driver.navigate().refresh();

            // ===== LOAD LẠI =====
            adminDashboardPage.openQuanLyKhoaHoc();

            khoaHocPage.searchKhoaHoc(tenKhoaHocMoi);

            khoaHocPage.openKhoaHocTheoTen(tenKhoaHocMoi);

            khoaHocPage.clickThongTinMonHocTab();

            // ĐỢI LẠI INPUT
            wait.until(ExpectedConditions.visibilityOfElementLocated(
            	    By.name("name")
            	));

            // ===== VERIFY =====
            String actualTen = khoaHocPage.getTenKhoaHoc();
            String actualMoTa = khoaHocPage.getMoTaKhoaHoc();

            boolean isPass = true;

            if(!actualTen.trim().equals(tenKhoaHocMoi.trim())){
                System.out.println("FAIL - Ten khoa hoc sai");
                isPass = false;
            }

            if(!actualMoTa.trim().equals(moTaMoi.trim())){
                System.out.println("FAIL - Mo ta sai");
                isPass = false;
            }

            System.out.println(isPass ? "PASS - Cap nhat khoa hoc" : "FAIL - Cap nhat khoa hoc");
        }
    }
}