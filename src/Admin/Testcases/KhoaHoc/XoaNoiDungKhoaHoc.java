package Admin.Testcases.KhoaHoc;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.*;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import Admin.Pages.AdminDashboardPage;
import Admin.Pages.KhoaHocPage;
import Login.LoginPage;
import Utils.DocFileJSON;
import io.github.bonigarcia.wdm.WebDriverManager;

public class XoaNoiDungKhoaHoc {

    WebDriver driver;
    LoginPage loginPage;
    AdminDashboardPage dashboardPage;
    KhoaHocPage khoaHocPage;

    // ===== WAIT CUSTOM =====
    public void waitTime(int seconds) {
        try {
            Thread.sleep(seconds * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    // ===== SETUP =====
    @BeforeClass
    public void setup() {

        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();

        driver.manage().window().maximize();

        loginPage = new LoginPage(driver);
        dashboardPage = new AdminDashboardPage(driver);
        khoaHocPage = new KhoaHocPage(driver);

        loginPage.openLoginPage();
        loginPage.login("test.pltsolutions@gmail.com", "plt@intern_051224");

        waitTime(3);
    }

    // ===== TEST =====
    @Test
    public void TC_Xoa_Noi_Dung_Khoa_Hoc() throws Exception {

        dashboardPage.openQuanLyKhoaHoc();
        waitTime(2);

        // ===== LẤY TÊN KHÓA HỌC TỪ JSON =====
        JSONArray data = DocFileJSON.docDuLieuJSON("resources/themhocvientrongkhoahoc.json");
        JSONObject json = (JSONObject) data.get(0);

        String tenKhoaHoc = json.get("tenKhoaHoc").toString();

        // ===== SEARCH & OPEN =====
        khoaHocPage.searchKhoaHoc(tenKhoaHoc);
        waitTime(2);

        khoaHocPage.openKhoaHocTheoTen(tenKhoaHoc);
        waitTime(2);

        khoaHocPage.openNoiDungTab();
        waitTime(2);

        // ===== DELETE =====
        System.out.println("===== DELETE CHUONG =====");

        while (true) {

            // nếu không còn chương thì thoát
            if (!khoaHocPage.isConChuong()) {
                break;
            }

            try {
                khoaHocPage.moChuongDauTien();
                waitTime(1);

                khoaHocPage.clickXoaChuong();
                waitTime(1);

                khoaHocPage.confirmXoa();
                waitTime(2);

                khoaHocPage.clickLuu();
                waitTime(2);

                System.out.println("ĐÃ XÓA 1 CHƯƠNG");

                // reload lại để cập nhật UI
                driver.navigate().refresh();
                waitTime(2);

                khoaHocPage.openNoiDungTab();
                waitTime(1);

            } catch (Exception e) {
                System.out.println("KHÔNG CÒN CHƯƠNG ĐỂ XÓA");
                break;
            }
        }

        // ===== SAVE CUỐI =====
        khoaHocPage.clickLuu();
        waitTime(2);

        khoaHocPage.clickOKPopup();
        waitTime(2);

        // ===== VERIFY =====
        if (!khoaHocPage.isConChuong()) {
            System.out.println("VERIFY PASS - KHÔNG CÒN CHƯƠNG");
        } else {
            System.out.println("VERIFY FAIL - VẪN CÒN CHƯƠNG");
        }

        // ===== QUAY VỀ =====
        dashboardPage.openQuanLyKhoaHoc();
        waitTime(2);
    }

    // ===== TEARDOWN =====
    @AfterClass
    public void tearDown() {
        driver.quit();
    }
}