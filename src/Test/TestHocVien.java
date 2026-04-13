package Test;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.json.simple.JSONObject;
import org.testng.Assert;

import Admin.Testcases.HocVien.ThemHocVien;
import Admin.Testcases.HocVien.TimKiemHocVien;
import Admin.Testcases.HocVien.CapNhatHocVien;
import Admin.Testcases.HocVien.SoSanhHocVien;
import Admin.Testcases.HocVien.XoaHocVien;
import Login.LoginPage;
import Admin.Pages.AdminDashboardPage;
import Utils.JsonUtils;
import io.github.bonigarcia.wdm.WebDriverManager;

public class TestHocVien {
    // Khai báo các đối tượng WebDriver và các trang (Pages) cần dùng
    protected WebDriver driver;
    protected LoginPage loginPage;
    protected AdminDashboardPage dashboardPage;

    // @BeforeClass: Hàm này sẽ chạy ĐẦU TIÊN và CHỈ 1 LẦN trước khi bắt đầu các test case bên dưới
    @BeforeClass
    public void setupBrowser() throws InterruptedException {
        // Khởi tạo trình duyệt Chrome thông qua thư viện WebDriverManager
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().window().maximize(); // Phóng to toàn màn hình để tránh lỗi che khuất element

        // Khởi tạo các trang (Page Object)
        loginPage = new LoginPage(driver);
        dashboardPage = new AdminDashboardPage(driver);

        // Mở trang đăng nhập và đăng nhập với tài khoản Admin
        loginPage.openLoginPage();
        loginPage.login("test.pltsolutions@gmail.com", "plt@intern_051224");
        
        // Kiểm tra (Assert) xem sau khi đăng nhập, trang Dashboard của Admin có hiện ra đúng không
        Assert.assertTrue(dashboardPage.isDashboardDisplayed());
    }

    // @Test: Đây là kịch bản kiểm thử chính (Main Test Case)
    @Test
    public void TC_Full_Flow_Hoc_Vien() throws Exception {
        // Đọc toàn bộ dữ liệu từ file hocvien.json vào mảng 2 chiều 'data'
        Object[][] data = JsonUtils.getTestData("resources/hocvien.json");

        // Khởi tạo các luồng chức năng (Testcases) tương ứng với từng module
        ThemHocVien testThem = new ThemHocVien(driver);
        TimKiemHocVien testTimKiem = new TimKiemHocVien(driver);
        CapNhatHocVien testCapNhat = new CapNhatHocVien(driver);
        SoSanhHocVien testSoSanh = new SoSanhHocVien(driver);
        XoaHocVien testXoa = new XoaHocVien(driver);
        
        // ---------------- BƯỚC 1: DỌN DẸP DỮ LIỆU CŨ ----------------
        System.out.println("B1: Xóa học viên");
        for (int i = 0; i < data.length; i++) {
            JSONObject hocVien = (JSONObject) data[i][0];
            try {
                // Thử tìm kiếm xem học viên này có tồn tại trong CSDL chưa
                boolean isFound = testTimKiem.Tc_Tim_Kiem_Hoc_Vien(hocVien);
                if (isFound) {
                    // Nếu tìm thấy, gọi hàm Xóa và kiểm tra thông báo xóa thành công
                    String tbXoa = testXoa.Tc_Xoa_Hoc_Vien(hocVien);
                    Assert.assertTrue(tbXoa.toLowerCase().contains("thành công"));
                    System.out.println("=> Đã xóa học viên có mã: " + hocVien.get("MaHocVien"));
                }
            } catch (Exception e) {
                // Bắt lỗi: Nếu database trống (chạy lần đầu) thì bỏ qua, không làm sập script
                System.out.println("=> Bỏ qua xóa: Không tìm thấy học viên (Database trống hoặc đã xóa).");
            }
        }

        // ---------------- BƯỚC 2: THÊM HỌC VIÊN MỚI ----------------
        System.out.println("\nB2: Thêm học viên");
        for (int i = 0; i < data.length; i++) {
            JSONObject hocVien = (JSONObject) data[i][0];
            try {
                // Gọi hàm Thêm học viên, truyền dữ liệu từ JSON vào form
                String tbThem = testThem.Tc_Them_Hoc_Vien(hocVien);
                // Xác nhận (Assert) thông báo trả về phải chứa chữ "thành công"
                Assert.assertTrue(tbThem.toLowerCase().contains("thành công"));
                System.out.println("=> Đã thêm: " + hocVien.get("HoTen"));
            } catch (Exception e) {
                // Nếu thêm lỗi, in ra màn hình và đánh dấu Test Case này là FAILED (Thất bại)
                System.err.println("=> Lỗi khi thêm học viên: " + hocVien.get("HoTen"));
                Assert.fail("Thất bại ở bước Thêm học viên: " + e.getMessage());
            }
        }

        // ---------------- BƯỚC 3: CẬP NHẬT & SO SÁNH ----------------
        System.out.println("\nB3: Tìm kiếm học viên, đồng thời cập nhập thông tin và so sánh");
        for (int i = 0; i < data.length; i++) {
            JSONObject hocVien = (JSONObject) data[i][0];
            try {
                // Tìm kiếm lại học viên vừa thêm xem có lên bảng danh sách chưa
                boolean isFound = testTimKiem.Tc_Tim_Kiem_Hoc_Vien(hocVien);
                Assert.assertTrue(isFound); // Bắt buộc phải tìm thấy mới chạy tiếp

                // Tiến hành sửa thông tin học viên bằng dữ liệu mới trong JSON
                String tbCapNhat = testCapNhat.Tc_Cap_Nhat_Hoc_Vien(hocVien);
                Assert.assertTrue(tbCapNhat.toLowerCase().contains("thành công"));

                // Gọi hàm So sánh để đối chiếu dữ liệu trên web xem có khớp 100% với JSON không
                testSoSanh.Tc_So_Sanh_Hoc_Vien(hocVien);
                System.out.println("=> Hoàn tất cập nhập và so sánh cho học viên có mã: " + hocVien.get("MaHocVien"));
            } catch (Exception e) {
                System.err.println("=> Lỗi khi Cập nhật/So sánh học viên: " + hocVien.get("MaHocVien"));
                Assert.fail("Thất bại ở bước Cập nhật/So sánh: " + e.getMessage());
            }
        }
    }

    // @AfterClass: Hàm này chạy CUỐI CÙNG để dọn dẹp hệ thống
    @AfterClass
    public void closeBrowser() {
        if(driver != null) {
            // Tắt hoàn toàn trình duyệt Chrome, giải phóng RAM
            driver.quit();
        }
    }
}