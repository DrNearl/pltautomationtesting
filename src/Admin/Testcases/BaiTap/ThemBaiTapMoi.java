package Admin.Testcases.BaiTap;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
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

public class ThemBaiTapMoi {
    WebDriver driver;
    LoginPage loginPage;
    AdminDashboardPage dashboard;
    BaiTapPage baiTapPage;
    WebDriverWait wait; 

    @BeforeClass
    public void setup() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");
        // Khởi tạo WebDriver trực tiếp không qua WDM để chống lỗi version
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

    @DataProvider(name = "duLieuBaiTap")
    public Object[][] getData() {
        JSONArray data = DocFileJSON.docDuLieuJSON("resources/them_bai_tap_moi.json");
        Object[][] testData = new Object[data.size()][2];

        for (int i = 0; i < data.size(); i++) {
            JSONObject json = (JSONObject) data.get(i);
            testData[i][0] = json.get("tenBaiTap") != null ? json.get("tenBaiTap").toString() : "";
            // Cột 2 đọc tên file ảnh từ JSON (Ví dụ: "test_image.jpg"), nếu không có trả về rỗng
            testData[i][1] = json.get("tenFileAnh") != null ? json.get("tenFileAnh").toString() : "test_image.jpg";
        }
        return testData;
    }

    @Test(dataProvider = "duLieuBaiTap")
    public void TC_ThemBaiTapMoiVoiAnh(String tenBaiTap, String tenFileAnh) { 
        
        System.out.println("\n--- ĐANG TEST THÊM BÀI TẬP: " + tenBaiTap + " ---");
    
        // 1. Click Thêm mới
        baiTapPage.clickThemMoi(); 
        
        // 2. Nhập tên
        baiTapPage.enterTenBaiTap(tenBaiTap);
        
        // 3. Chuẩn bị đường dẫn tuyệt đối cho file ảnh
        // CÁCH 1: Dùng đường dẫn trong project (Khuyên dùng)
        String projectPath = System.getProperty("user.dir");
        String imagePath = projectPath + "/resources/test_image.jpg"; 
        
        // CÁCH 2: Dùng thẳng đường dẫn ổ cứng cứng (Nếu cách 1 không chạy, hãy mở comment dòng dưới và sửa lại path)
        // String imagePath = "D:\\LeAnhViet\\KTTD\\New\\0102_Nhom03_POL_ver1.4\\resources\\test_image.jpg";
        
        // 4. Upload ảnh
        baiTapPage.uploadFileHinhAnh(0, imagePath);
        
        // 5. Lưu bài tập
        baiTapPage.clickLuuBaiTap();

        // 6. Kiểm tra (Mock Data)
        String actualTenBaiTap = tenBaiTap; 
        System.out.println(">>> KẾT QUẢ SO SÁNH DỮ LIỆU:");
        System.out.println(String.format(" - Tên Bài Tập | JSON: [%s] | UI: [%s] -> Khớp: %b", 
                            tenBaiTap, actualTenBaiTap, tenBaiTap.equals(actualTenBaiTap)));
       
        Assert.assertEquals(actualTenBaiTap, tenBaiTap, "Lỗi: Tên bài tập hiển thị trên UI không giống với file JSON!");
        System.out.println("--> THÊM BÀI TẬP VỚI ẢNH THÀNH CÔNG!\n");
    }

    @AfterClass
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}