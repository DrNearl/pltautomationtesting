package Admin.Testcases.KhoaHoc;

import java.time.Duration;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.*;
import Admin.Pages.AdminDashboardPage;
import Admin.Pages.KhoaHocPage;
import Login.LoginPage;
import Utils.DocFileJSON;
import Utils.JsonUtils;

public class ThemNoiDungKhoaHoc {

    WebDriver driver;
    LoginPage loginPage;
    AdminDashboardPage dashboardPage;
    KhoaHocPage khoaHocPage;

    public ThemNoiDungKhoaHoc(WebDriver driver) {
        this.driver = driver;
        loginPage = new LoginPage(driver);
        dashboardPage = new AdminDashboardPage(driver);
        khoaHocPage = new KhoaHocPage(driver);
    }
    
    public void waitTime(int seconds) {
        try {
            Thread.sleep(seconds * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void TC_Them_Noi_Dung_Khoa_Hoc() throws Exception {

        dashboardPage.openQuanLyKhoaHoc();

        String tenKhoaHoc = ThemHocVienVaoKhoaHoc.TEN_KHOA_HOC;

        khoaHocPage.searchKhoaHoc(tenKhoaHoc);
        waitTime(2);

        khoaHocPage.openKhoaHocTheoTen(tenKhoaHoc);
        waitTime(2);

        khoaHocPage.openNoiDungTab();
        waitTime(2);

        JSONArray data = DocFileJSON.docDuLieuJSON("data/data_globals.json");

        // ================= ADD =================
        for(Object obj : data){

            JSONObject chuong = (JSONObject) obj;

            String title = chuong.get("Title").toString();
            String body = chuong.get("Body").toString();

            khoaHocPage.clickThemChuong();
            waitTime(1);

            khoaHocPage.clickLastChuong();
            waitTime(1);

            khoaHocPage.enterTieuDeLast(title);
            khoaHocPage.enterMoTaLast(body);
            waitTime(1);

            JSONArray lessons = (JSONArray) chuong.get("Lessons");

            for(Object lessonObj : lessons){

                JSONObject lesson = (JSONObject) lessonObj;

                String lessonTitle = lesson.get("TitleLS").toString();
                String lessonBody = lesson.get("BodyLS").toString();

                khoaHocPage.clickThemBaiHocCuoi();
                waitTime(1);

                khoaHocPage.clickLastChuong();
                waitTime(1);

                khoaHocPage.enterTieuDeBaiHocCuoi(lessonTitle);
                khoaHocPage.enterMoTaBaiHocCuoi(lessonBody);
                waitTime(1);
            }

            khoaHocPage.clickLuu();
            waitTime(2);

            khoaHocPage.clickOKPopup();
            waitTime(2);
        }

        System.out.println("THÊM CHƯƠNG + BÀI HỌC MỖI CHƯƠNG THÀNH CÔNG");

        // ================= REFRESH =================
        driver.navigate().refresh();
        waitTime(3);

        dashboardPage.openQuanLyKhoaHoc();
        waitTime(2);

        khoaHocPage.searchKhoaHoc(tenKhoaHoc);
        waitTime(2);

        khoaHocPage.openKhoaHocTheoTen(tenKhoaHoc);
        waitTime(2);

        khoaHocPage.openNoiDungTab();
        waitTime(2);
        
     // ===== QUAY VỀ LIST =====
        dashboardPage.openQuanLyKhoaHoc();
        waitTime(2);
        
        khoaHocPage.clickOKMeeTing();
    }
}
