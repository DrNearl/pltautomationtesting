package Test;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import User.Testcases.*;
import Login.LoginPage;
import User.Pages.UserDashboardPage;
import Utils.JsonUtils;
import io.github.bonigarcia.wdm.WebDriverManager;

public class TestUser {
    // Khai báo WebDriver và trang Login
    protected WebDriver driver;
    protected LoginPage loginPage;

    // Cài đặt và mở trình duyệt trước khi test
    @BeforeClass
    public void setupBrowser() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        loginPage = new LoginPage(driver);
    }

    // @DataProvider: Đọc luồng dữ liệu kiểm thử từ file userflow.json truyền vào cho @Test
    @DataProvider(name = "UserFlowData")
    public Object[][] getUserFlowData() {
        return JsonUtils.getTestData("resources/userflow.json");
    }

    // Kịch bản kiểm thử luồng Người dùng, nhận data từ DataProvider
    @Test(dataProvider = "UserFlowData")
    public void TC_Full_Flow_User(JSONObject flowData) {
        // Dùng try-catch để bao bọc toàn bộ luồng, giúp tùy chỉnh cách TestNG báo lỗi
        try {
            // Đọc danh sách tài khoản từ file loginData.json
            JSONArray loginData = JsonUtils.readJsonArray("resources/loginData.json");
            JSONObject user1 = (JSONObject) loginData.get(0); // Lấy account số 1
            JSONObject user2 = (JSONObject) loginData.get(1); // Lấy account số 2
            
            // ---------------- BƯỚC 1: ĐĂNG NHẬP ACCOUNT 1 ----------------
            System.out.println("B1: Đăng nhập hệ thống (Lần 1)");
            loginPage.openLoginPage();
            loginPage.login((String) user1.get("username"), (String) user1.get("password"));
            System.out.println("=> Đăng nhập thành công với tài khoản Test 1: " + user1.get("username"));

            UserDashboardPage dashboardPage = new UserDashboardPage(driver);
            
            // ---------------- BƯỚC 2: KIỂM TRA ĐĂNG XUẤT ----------------
            System.out.println("\nB2: Kiểm tra chức năng Đăng xuất");
            dashboardPage.logout(); // Click avatar -> Đăng xuất -> Xác nhận OK
            System.out.println("=> Đăng xuất thành công tài khoản 1");

            // ---------------- BƯỚC 3: ĐĂNG NHẬP ACCOUNT 2 ----------------
            System.out.println("\nB3: Đăng nhập hệ thống (Lần 2)");
            // Dùng thông tin của tài khoản 2 để đăng nhập lại vào hệ thống
            loginPage.login((String) user2.get("username"), (String) user2.get("password"));
            System.out.println("=> Đăng nhập thành công với tài khoản Test 2: " + user2.get("username"));

            // ---------------- BƯỚC 4: THAO TÁC GIAO DIỆN ----------------
            System.out.println("\nB4: Kiểm tra chức năng đóng/mở mục Khoá học của tôi");
            // Test việc click mở và đóng Accordion khóa học bên tay trái
            dashboardPage.toggleMyCourses();

            // ---------------- BƯỚC 5: KIỂM THỬ NỘI DUNG ----------------
            System.out.println("\nB5: Kiểm thử Nội dung khóa học");
            // Lấy danh sách các Chương (Chapters) từ file JSON truyền vào hàm test
            JSONArray chapters = (JSONArray) flowData.get("Chapters");
            KiemThuNoiDung testNoiDung = new KiemThuNoiDung(driver);
            testNoiDung.Tc_Kiem_Thu_Noi_Dung(chapters); // Hàm này sẽ quét và click kiểm tra từng bài học
            System.out.println("=> Thành công: Nội dung " + chapters.size() + " chương bài học hoàn toàn trùng khớp với JSON.");

            // ---------------- BƯỚC 6: KIỂM THỬ DIỄN ĐÀN ----------------
            System.out.println("\nB6: Kiểm thử Diễn đàn");
            KiemThuDienDan testDienDan = new KiemThuDienDan(driver);
            String forumResponse = (String) flowData.get("ForumResponse");
            testDienDan.Tc_Kiem_Thu_Dien_Dan(forumResponse); // Comment text vào diễn đàn, sau đó Like và Thu hồi
            System.out.println("=> Thành công: Đã gửi và tương tác chính xác với bình luận: " + forumResponse);

            // ---------------- BƯỚC 7: KIỂM THỬ MEET ----------------
            System.out.println("\nB7: Kiểm thử Video Conference");
            KiemThuVideo testVideo = new KiemThuVideo(driver);
            String expectedVideoUrl = (String) flowData.get("ExpectedVideoUrl");
            testVideo.Tc_Kiem_Thu_Video(expectedVideoUrl); // Click tham gia -> Chuyển tab -> So sánh URL -> Đóng tab
            System.out.println("=> Thành công: Đã chuyển hướng đúng URL mong đợi: " + expectedVideoUrl);

        } catch (AssertionError e) {
            // Lỗi AssertionError xảy ra khi dữ liệu so sánh Assert.assertTrue / Assert.assertEquals bị sai lệch
            System.err.println("=> Thất bại (Lỗi so sánh): URL hoặc dữ liệu không trùng khớp với mong đợi!");
            Assert.fail("Test case thất bại do dữ liệu không trùng khớp: " + e.getMessage());
        } catch (Exception e) {
            // Lỗi Exception xảy ra khi có lỗi về tiến trình (VD: Không tìm thấy Element, timeout, code lỗi...)
            System.err.println("=> Thất bại (Lỗi tiến trình): " + e.getMessage());
            Assert.fail("Test case thất bại do lỗi tiến trình: " + e.getMessage());
        }
    }

    // Dọn dẹp và tắt trình duyệt sau khi chạy xong
    @AfterClass
    public void closeBrowser() {
        if (driver != null) {
            driver.quit();
        }
    }
}