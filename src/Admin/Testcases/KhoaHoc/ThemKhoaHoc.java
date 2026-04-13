package Admin.Testcases.KhoaHoc;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.openqa.selenium.WebDriver;

import Admin.Pages.AdminDashboardPage;
import Admin.Pages.KhoaHocPage;
import Login.LoginPage;
import Utils.DocFileJSON;

public class ThemKhoaHoc {

    WebDriver driver;
    LoginPage loginPage;
    AdminDashboardPage dashboard;
    KhoaHocPage khoaHocPage;

    // Constructor: Khởi tạo các đối tượng Page Object cần thiết cho Test Case
    public ThemKhoaHoc(WebDriver driver) {
        this.driver = driver;
        loginPage = new LoginPage(driver);
        dashboard = new AdminDashboardPage(driver);
        khoaHocPage = new KhoaHocPage(driver);
    }

    // Hàm test chính (sử dụng throws Exception để xử lý các lỗi có thể xảy ra từ Robot class)
    public void TC_Them_Khoa_Hoc() throws Exception {

        // 1. Mở menu Quản lý khóa học trên Dashboard
        dashboard.openQuanLyKhoaHoc();
        Thread.sleep(3000); // Đợi 3s cho trang tải xong (thực tế nên dùng WebDriverWait thay vì sleep cứng)

        // 2. Đọc toàn bộ dữ liệu (dạng mảng - Array) từ file khoahoc.json
        JSONArray data = DocFileJSON.docDuLieuJSON("resources/khoahoc.json");

        // 3. Vòng lặp: Duyệt qua từng bộ dữ liệu (từng khóa học) trong file JSON
        for (Object obj : data) {

            // Ép kiểu Object thành JSONObject để có thể lấy dữ liệu theo Key (như "tenKhoaHoc", "anhBia")
            JSONObject kh = (JSONObject) obj;

            // Bấm nút "Thêm mới" để mở form thêm khóa học
            khoaHocPage.clickThemMoi();
            Thread.sleep(2000);

            // Bắt đầu điền form từ dữ liệu JSON:
            // Lấy chuỗi đường dẫn ảnh từ key "anhBia" và truyền vào hàm tải ảnh
            khoaHocPage.uploadAnhBia(kh.get("anhBia").toString());
            
            // Lấy chuỗi tên từ key "tenKhoaHoc"
            khoaHocPage.enterTenKhoaHoc(kh.get("tenKhoaHoc").toString());
            
            // Lấy chuỗi mô tả từ key "moTa"
            khoaHocPage.enterMoTa(kh.get("moTa").toString());

            // Bấm nút Lưu form
            khoaHocPage.clickLuuThem();
            Thread.sleep(2000);

            // Bấm xác nhận trên popup thông báo thành công
            khoaHocPage.clickXacNhan();
            Thread.sleep(2000);

            // In log ra Console để theo dõi tiến độ test
            System.out.println("THÊM KHÓA HỌC THÀNH CÔNG - " + kh.get("tenKhoaHoc"));
        }
    }
}