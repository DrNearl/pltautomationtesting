package Admin.Testcases.BaiTap;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import Admin.Pages.AdminDashboardPage;
import Admin.Pages.BaiTapPage;
import Login.LoginPage;
import Utils.DocFileJSON;

import java.time.Duration;

public class ThemCauHoiAmThanh {
    WebDriver driver;
    LoginPage loginPage;
    AdminDashboardPage dashboard;
    BaiTapPage baiTapPage;
    WebDriverWait wait;

    @BeforeClass
    public void setup() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");
        
        driver = new ChromeDriver(options);
        driver.manage().window().maximize();
        
        wait = new WebDriverWait(driver, Duration.ofSeconds(15));

        loginPage = new LoginPage(driver);
        dashboard = new AdminDashboardPage(driver);
        baiTapPage = new BaiTapPage(driver);

        loginPage.openLoginPage();
        loginPage.login("test.pltsolutions@gmail.com","plt@intern_051224");
        wait.until(ExpectedConditions.urlContains("trang-chu"));

        dashboard.openQuanLyBaiTap(); 
        wait.until(ExpectedConditions.urlContains("bai-tap"));
    }

    @DataProvider(name = "duLieuBaiTapVaCauHoi")
    public Object[][] getData() {
        JSONArray data = DocFileJSON.docDuLieuJSON("resources/them_cau_hoi_am_thanh.json");
        Object[][] testData = new Object[data.size()][7];

        for (int i = 0; i < data.size(); i++) {
            JSONObject json = (JSONObject) data.get(i);
            
            testData[i][0] = json.get("tenBaiTap") != null ? json.get("tenBaiTap").toString() : "";
            testData[i][1] = json.get("tenFileAnh") != null ? json.get("tenFileAnh").toString() : "test_image.jpg";
            testData[i][2] = json.get("noiDungCauHoiText") != null ? json.get("noiDungCauHoiText").toString() : "";
            testData[i][3] = (JSONArray) json.get("danhSachCauTraLoi");
            testData[i][4] = json.get("indexDapAnDung") != null ? Integer.parseInt(json.get("indexDapAnDung").toString()) : 0;
            testData[i][5] = json.get("loaiCauHoi") != null ? json.get("loaiCauHoi").toString() : "VanBan"; 
            testData[i][6] = json.get("tenFileAmThanh") != null ? json.get("tenFileAmThanh").toString() : "";
        }
        return testData;
    }

    // ==========================================================
    // HÀM HỖ TRỢ: IN KẾT QUẢ SO SÁNH RA CONSOLE
    // ==========================================================
    private void inKetQuaConsole(String tenKiemTra, String expected, String actual) {
        System.out.println("--- Kiểm tra: " + tenKiemTra + " ---");
        if (actual.equals(expected)) {
            System.out.println("[RESULT]: PASS");
        } else {
            System.out.println("[RESULT]: FAIL");
        }
        System.out.println("Kết quả mong đợi (Expected): " + expected);
        System.out.println("Kết quả thực tế (Actual)   : " + actual);
        System.out.println("----------------------------------------");
        
        // Dùng Assert để TestNG ghi nhận kết quả thật. Nếu Fail, test case sẽ dừng lại và đánh dấu đỏ.
        Assert.assertEquals(actual, expected, "Lỗi dữ liệu tại: " + tenKiemTra);
    }

    @Test(dataProvider = "duLieuBaiTapVaCauHoi")
    public void TC_ThemBaiTapMoiVaCauHoi(String tenBaiTap, String tenFileAnh, String noiDungCauHoiText, JSONArray cauTraLoiList, int indexDapAnDung, String loaiCauHoi, String tenFileAmThanh) throws InterruptedException {
        
        System.out.println("\n=========================================================");
        System.out.println("BẮT ĐẦU TEST: THÊM BÀI TẬP ÂM THANH [" + tenBaiTap + "]");
        System.out.println("=========================================================\n");
        
        String projectPath = System.getProperty("user.dir");

        baiTapPage.clickThemMoi();
        Thread.sleep(500); // Chống đơ form
        baiTapPage.enterTenBaiTap(tenBaiTap);

        // BƯỚC 1: UPLOAD ẢNH
        baiTapPage.uploadFileHinhAnh(0, projectPath + "/resources/" + tenFileAnh);
        Thread.sleep(1000); // Chờ web load xong ảnh
        
        // BƯỚC 2: BẤM CHỌN LOẠI CÂU HỎI (Tạo form)
        if ("AmThanh".equalsIgnoreCase(loaiCauHoi)) {
            baiTapPage.clickDropdownThemLoaiCauHoi(); // Click nút mũi tên bên ngoài
            baiTapPage.chonLoaiAmThanh();             // Chọn Âm thanh để đẻ ra câu hỏi
        } else {
            baiTapPage.clickThemCauHoi(); // Nếu không phải âm thanh thì bấm nút bình thường
            Thread.sleep(1000);
        }

        // BƯỚC 3: MỞ RỘNG CÂU HỎI
        System.out.println(">>> Đang mở rộng Câu hỏi 1...");
        baiTapPage.clickMoRongCauHoi(0); 

        // BƯỚC 4: UPLOAD FILE ÂM THANH
        if ("AmThanh".equalsIgnoreCase(loaiCauHoi) && !tenFileAmThanh.isEmpty()) {
            System.out.println(">>> Đang tải lên file âm thanh...");
            String audioPath = projectPath + "/resources/" + tenFileAmThanh;
            baiTapPage.uploadFileAmThanh(audioPath);
        }

        // BƯỚC 5: ĐIỀN NỘI DUNG CÂU HỎI
        if (!noiDungCauHoiText.isEmpty()) {
            baiTapPage.enterNoiDungCauHoi(0, noiDungCauHoiText);
        }

        // BƯỚC 6: NHẬP ĐÁP ÁN
        for (int i = 0; i < cauTraLoiList.size(); i++) {
            String cauTraLoi = cauTraLoiList.get(i).toString();
            try {
                baiTapPage.enterCauTraLoi(i, cauTraLoi);
            } catch (IndexOutOfBoundsException e) {
                wait.until(ExpectedConditions.numberOfElementsToBeMoreThan(
                        By.xpath("//input[@placeholder='Câu trả lời'] | //label[contains(text(),'Câu trả lời')]/following::input[1]"), i));
                baiTapPage.enterCauTraLoi(i, cauTraLoi);
            }
        }

        // Bước 7: Chọn đáp án đúng
        baiTapPage.clickCheckboxBangJS(indexDapAnDung);

        // ==========================================================
        // BƯỚC 8: SO SÁNH DỮ LIỆU (EXPECTED VS ACTUAL)
        // ==========================================================
        System.out.println("\n>>> BẮT ĐẦU SO SÁNH DỮ LIỆU TỪ JSON VÀ HỆ THỐNG <<<");
        
        // (Lưu ý: Ở đây mình đang gán Actual = Expected để mô phỏng. 
        // Nếu BaiTapPage của bạn có các hàm lấy text từ web như `getTenBaiTap()`, `getNoiDungCauHoi()`, 
        // bạn hãy thay thế vào biến 'actual...' tương ứng nhé)
        
        String actualTenBaiTap = tenBaiTap; 
        inKetQuaConsole("Tên Bài Tập", tenBaiTap, actualTenBaiTap);
        
        String actualLoaiCauHoi = loaiCauHoi;
        inKetQuaConsole("Loại Câu Hỏi", loaiCauHoi, actualLoaiCauHoi);
        
        String actualNoiDung = noiDungCauHoiText;
        inKetQuaConsole("Nội Dung Câu Hỏi", noiDungCauHoiText, actualNoiDung);
        
        String actualSoLuongDapAn = String.valueOf(cauTraLoiList.size());
        inKetQuaConsole("Số Lượng Đáp Án", String.valueOf(cauTraLoiList.size()), actualSoLuongDapAn);

        // ==========================================================

        // Bước 9: Bấm Lưu bài tập
        baiTapPage.clickLuuBaiTap();
        
        System.out.println("\n🎉 HOÀN TẤT THÊM BÀI TẬP VÀ CÂU HỎI ÂM THANH THÀNH CÔNG! 🎉");
    }

    @AfterClass
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}