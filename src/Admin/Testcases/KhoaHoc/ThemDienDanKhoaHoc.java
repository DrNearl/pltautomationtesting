package Admin.Testcases.KhoaHoc;

import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.openqa.selenium.WebDriver;

import Admin.Pages.AdminDashboardPage;
import Admin.Pages.KhoaHocPage;
import Login.LoginPage;
import Utils.DocFileJSON;

public class ThemDienDanKhoaHoc {

    WebDriver driver;

    LoginPage loginPage;
    AdminDashboardPage adminDashboardPage;
    KhoaHocPage khoaHocPage;

    public ThemDienDanKhoaHoc(WebDriver driver) {
        this.driver = driver;

        loginPage = new LoginPage(driver);
        adminDashboardPage = new AdminDashboardPage(driver);
        khoaHocPage = new KhoaHocPage(driver);
    }

    public void waitTime(int ms) {
        try {
            Thread.sleep(ms);
        } catch (Exception e) {}
    }

    public void TC_Them_Dien_Dan_Khoa_Hoc() throws Exception {

        // ===== OPEN PAGE =====
        adminDashboardPage.openQuanLyKhoaHoc();
        waitTime(500);

        // ===== HANDLE POPUP =====
        try {
            khoaHocPage.clickConfirmPopupChuyenTrang();
        } catch (Exception e) {
            System.out.println("Khong co popup chuyen trang");
        }
        waitTime(1000);

        // ===== LẤY KHÓA HỌC (KHÔNG DÙNG STATIC) =====
        JSONArray dataKH = DocFileJSON.docDuLieuJSON("resources/themhocvientrongkhoahoc.json");
        JSONObject jsonKH = (JSONObject) dataKH.get(0);

        String tenKhoaHoc = jsonKH.get("tenKhoaHoc").toString();

        // ===== SEARCH =====
        khoaHocPage.searchKhoaHoc(tenKhoaHoc);
        waitTime(1000);

        khoaHocPage.openKhoaHocTheoTen(tenKhoaHoc);
        waitTime(1500);

        khoaHocPage.clickDienDanThaoLuanTab();
        waitTime(1000);

        JSONArray data = DocFileJSON.docDuLieuJSON("data/diendan.json");

        for(Object obj : data){

            JSONObject json = (JSONObject) obj;

            String tieuDe = json.get("tieuDe").toString();
            String moTa = json.get("moTa").toString();
            String image = json.get("image").toString();

            // ===== ADD =====
            khoaHocPage.clickThemDienDan();
            waitTime(500);

            khoaHocPage.enterTieuDeDienDan(tieuDe);   // đã fix TAB trong page
            khoaHocPage.enterMoTaDienDan(moTa);
            khoaHocPage.uploadAnhDienDan(image);

            waitTime(500);

            khoaHocPage.clickThemDienDanConfirm();

            waitTime(800);

            // ===== POPUP OK =====
            try {
                khoaHocPage.clickOKPopup();
            } catch (Exception e) {
                System.out.println("Khong co popup OK");
            }

            waitTime(800);

            // ===== VERIFY =====
            boolean isExist = khoaHocPage.isDienDanExist(tieuDe);

            if(isExist){
                System.out.println("PASS - Dien dan: " + tieuDe);
            } else {
                System.out.println("FAIL - Dien dan: " + tieuDe);
            }
        }

        // ===== RELOAD =====
        driver.navigate().refresh();
        waitTime(2000);

        adminDashboardPage.openQuanLyKhoaHoc();
        waitTime(1000);

        khoaHocPage.searchKhoaHoc(tenKhoaHoc);
        waitTime(1000);

        khoaHocPage.openKhoaHocTheoTen(tenKhoaHoc);
        waitTime(1500);

        khoaHocPage.clickDienDanThaoLuanTab();
        waitTime(1000);

        // ===== VERIFY LIST =====
        List<String> actualList = khoaHocPage.getDanhSachTenDienDan();

        List<String> expectedList = new ArrayList<>();
        for(Object obj2 : data){
            JSONObject json2 = (JSONObject) obj2;
            expectedList.add(json2.get("tieuDe").toString().trim());
        }

        System.out.println("===== SO SANH DIEN DAN =====");

        boolean isAllPass = true;

        for(String expected : expectedList){
            if(actualList.contains(expected)){
                System.out.println("PASS - " + expected);
            } else {
                System.out.println("FAIL - Thieu: " + expected);
                isAllPass = false;
            }
        }

        for(String actual : actualList){
            if(!expectedList.contains(actual)){
                System.out.println("FAIL - Sai tren UI: " + actual);
                isAllPass = false;
            }
        }

        System.out.println(isAllPass ? ">>> KET QUA: PASS" : ">>> KET QUA: FAIL");
    }
}