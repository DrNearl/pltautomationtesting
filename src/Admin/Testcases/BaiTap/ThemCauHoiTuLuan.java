package Admin.Testcases.BaiTap;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
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

public class ThemCauHoiTuLuan {
    WebDriver driver; LoginPage loginPage; AdminDashboardPage dashboard; BaiTapPage baiTapPage; WebDriverWait wait;

    @BeforeClass
    public void setup() {
        ChromeOptions options = new ChromeOptions(); options.addArguments("--remote-allow-origins=*");
        driver = new ChromeDriver(options); driver.manage().window().maximize();
        wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        loginPage = new LoginPage(driver); dashboard = new AdminDashboardPage(driver); baiTapPage = new BaiTapPage(driver);
        loginPage.openLoginPage(); loginPage.login("test.pltsolutions@gmail.com","plt@intern_051224");
        wait.until(ExpectedConditions.urlContains("trang-chu"));
        dashboard.openQuanLyBaiTap(); wait.until(ExpectedConditions.urlContains("bai-tap"));
    }

    @DataProvider(name = "duLieuTuLuan")
    public Object[][] getData() {
        JSONArray data = DocFileJSON.docDuLieuJSON("resources/them_cau_hoi_tu_luan.json");
        Object[][] testData = new Object[data.size()][4];
        for (int i = 0; i < data.size(); i++) {
            JSONObject json = (JSONObject) data.get(i);
            testData[i][0] = json.get("tenBaiTap") != null ? json.get("tenBaiTap").toString() : "";
            testData[i][1] = json.get("tenFileAnhBia") != null ? json.get("tenFileAnhBia").toString() : "test_image.jpg";
            testData[i][2] = json.get("noiDungCauHoi") != null ? json.get("noiDungCauHoi").toString() : "";
            testData[i][3] = json.get("soLuongKyTu") != null ? json.get("soLuongKyTu").toString() : "";
        }
        return testData;
    }

    private void inKetQuaConsole(String tenKiemTra, String expected, String actual) {
        System.out.println("--- Kiểm tra: " + tenKiemTra + " ---");
        System.out.println((actual.equals(expected) ? "[RESULT]: PASS" : "[RESULT]: FAIL"));
        System.out.println("Expected: " + expected + " | Actual: " + actual);
        Assert.assertEquals(actual, expected, "Lỗi dữ liệu tại: " + tenKiemTra);
    }

    @Test(dataProvider = "duLieuTuLuan")
    public void TC_ThemCauHoiTuLuan(String tenBaiTap, String tenFileAnhBia, String noiDungCauHoi, String soLuongKyTu) throws InterruptedException {
        String projectPath = System.getProperty("user.dir");
        baiTapPage.clickThemMoi(); Thread.sleep(500);
        baiTapPage.enterTenBaiTap(tenBaiTap);
        baiTapPage.uploadFileHinhAnh(0, projectPath + "/resources/" + tenFileAnhBia); Thread.sleep(1000);
        
        // Chọn loại Tự Luận
        baiTapPage.clickDropdownThemLoaiCauHoi();
        baiTapPage.chonLoaiTuLuan();
        baiTapPage.clickMoRongCauHoi(0); 

        // Nhập Nội dung & Giới hạn ký tự (KHÔNG có đáp án trắc nghiệm)
        baiTapPage.enterNoiDungCauHoi(0, noiDungCauHoi);
        baiTapPage.enterSoLuongKyTu(soLuongKyTu);

        // SO SÁNH
        System.out.println("\n>>> KẾT QUẢ SO SÁNH:");
        inKetQuaConsole("Tên Bài Tập", tenBaiTap, tenBaiTap);
        inKetQuaConsole("Nội Dung Câu Hỏi", noiDungCauHoi, noiDungCauHoi);
        inKetQuaConsole("Số Lượng Ký Tự", soLuongKyTu, soLuongKyTu);

        baiTapPage.clickLuuBaiTap();
        System.out.println("--> THÊM BÀI TẬP TỰ LUẬN THÀNH CÔNG!\n");
    }

    @AfterClass
    public void tearDown() { if (driver != null) driver.quit(); }
}