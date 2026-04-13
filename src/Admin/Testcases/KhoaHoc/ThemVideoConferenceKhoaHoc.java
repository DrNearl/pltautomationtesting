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

public class ThemVideoConferenceKhoaHoc {

    WebDriver driver;

    LoginPage loginPage;
    AdminDashboardPage adminDashboardPage;
    KhoaHocPage khoaHocPage;

    public ThemVideoConferenceKhoaHoc(WebDriver driver) {
        this.driver = driver;

        loginPage = new LoginPage(driver);
        adminDashboardPage = new AdminDashboardPage(driver);
        khoaHocPage = new KhoaHocPage(driver);
    }
    
    public void TC_Them_Video_Conference_Khoa_Hoc() throws Exception {

        adminDashboardPage.openQuanLyKhoaHoc();
        Thread.sleep(2000);

        String tenKhoaHoc = ThemHocVienVaoKhoaHoc.TEN_KHOA_HOC;

        khoaHocPage.searchKhoaHoc(tenKhoaHoc);
        Thread.sleep(2000);

        khoaHocPage.openKhoaHocTheoTen(tenKhoaHoc);
        Thread.sleep(3000);

        // CLICK TAB VIDEO CONFERENCE
        khoaHocPage.clickVideoConferenceTab();
        Thread.sleep(2000);

        // ===== ĐỌC JSON =====
        JSONArray data = DocFileJSON.docDuLieuJSON("data/videoconference.json");

        for(Object obj : data){

            JSONObject json = (JSONObject) obj;

            String tenMeeting = json.get("tenMeeting").toString();
            String moTa = json.get("moTa").toString();
            String link = json.get("link").toString();

            // ===== ADD =====
            khoaHocPage.clickThemVideoConference();
            Thread.sleep(1000);

            khoaHocPage.nhapTenMeeting(tenMeeting);
            khoaHocPage.nhapMoTaMeeting(moTa);
            khoaHocPage.nhapLinkMeeting(link);

            Thread.sleep(1000);

            khoaHocPage.clickThemMeeting();
            Thread.sleep(1500);
            
            khoaHocPage.clickOKMeeTing();
            Thread.sleep(1500);

            // ===== VERIFY =====
            boolean isExist = khoaHocPage.isMeetingExist(tenMeeting, link);

            if(isExist){
                System.out.println("PASS - Meeting: " + tenMeeting);
            } else {
                System.out.println("FAIL - Meeting: " + tenMeeting);
            }
        }
     // ===== RELOAD =====
        driver.navigate().refresh();
        Thread.sleep(3000);

        adminDashboardPage.openQuanLyKhoaHoc();
        Thread.sleep(2000);

        khoaHocPage.searchKhoaHoc(tenKhoaHoc);
        Thread.sleep(2000);

        khoaHocPage.openKhoaHocTheoTen(tenKhoaHoc);
        Thread.sleep(3000);

        khoaHocPage.clickVideoConferenceTab();
        Thread.sleep(2000);

        // ===== LẤY DATA =====
        List<String> actualList = khoaHocPage.getDanhSachTenMeeting();

        List<String> expectedList = new ArrayList<>();
        for(Object obj2 : data){
            JSONObject json2 = (JSONObject) obj2;
            expectedList.add(json2.get("tenMeeting").toString().trim());
        }

        // ===== SO SÁNH =====
        System.out.println("===== SO SANH VIDEO CONFERENCE =====");

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

        if(isAllPass){
            System.out.println(">>> KET QUA: PASS");
        } else {
            System.out.println(">>> KET QUA: FAIL");
        }
    }

}