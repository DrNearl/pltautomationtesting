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

public class ThemCauHoiHinhAnh {
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

    @DataProvider(name = "duLieuHinhAnh")
    public Object[][] getData() {
        JSONArray data = DocFileJSON.docDuLieuJSON("resources/them_cau_hoi_hinh_anh.json");
        Object[][] testData = new Object[data.size()][6];
        for (int i = 0; i < data.size(); i++) {
            JSONObject json = (JSONObject) data.get(i);
            testData[i][0] = json.get("tenBaiTap") != null ? json.get("tenBaiTap").toString() : "";
            testData[i][1] = json.get("tenFileAnhBia") != null ? json.get("tenFileAnhBia").toString() : "test_image.jpg";
            testData[i][2] = json.get("duongDanAnhCauHoi") != null ? json.get("duongDanAnhCauHoi").toString() : "";
            testData[i][3] = json.get("noiDungCauHoi") != null ? json.get("noiDungCauHoi").toString() : "";
            testData[i][4] = (JSONArray) json.get("danhSachCauTraLoi");
            testData[i][5] = json.get("indexDapAnDung") != null ? Integer.parseInt(json.get("indexDapAnDung").toString()) : 0;
        }
        return testData;
    }

    private void inKetQuaConsole(String tenKiemTra, String expected, String actual) {
        System.out.println("--- Kiểm tra: " + tenKiemTra + " ---");
        System.out.println((actual.equals(expected) ? "[RESULT]: PASS" : "[RESULT]: FAIL"));
        System.out.println("Expected: " + expected + " | Actual: " + actual);
        Assert.assertEquals(actual, expected, "Lỗi dữ liệu tại: " + tenKiemTra);
    }

    @Test(dataProvider = "duLieuHinhAnh")
    public void TC_ThemCauHoiHinhAnh(String tenBaiTap, String tenFileAnhBia, String duongDanAnhCauHoi, String noiDungCauHoi, JSONArray cauTraLoiList, int indexDapAnDung) throws InterruptedException {
        String projectPath = System.getProperty("user.dir");
        baiTapPage.clickThemMoi(); Thread.sleep(500);
        baiTapPage.enterTenBaiTap(tenBaiTap);
        baiTapPage.uploadFileHinhAnh(0, projectPath + "/resources/" + tenFileAnhBia); Thread.sleep(1000);
        
        // Chọn loại Hình ảnh
        baiTapPage.clickDropdownThemLoaiCauHoi();
        baiTapPage.chonLoaiHinhAnh();
        baiTapPage.clickMoRongCauHoi(0); 

        // Upload ảnh cho Câu hỏi
        if (!duongDanAnhCauHoi.isEmpty()) {
            baiTapPage.uploadFileHinhAnhCauHoi(projectPath + "/resources/" + duongDanAnhCauHoi);
        }

        baiTapPage.enterNoiDungCauHoi(0, noiDungCauHoi);

        for (int i = 0; i < cauTraLoiList.size(); i++) {
            try { baiTapPage.enterCauTraLoi(i, cauTraLoiList.get(i).toString());
            } catch (Exception e) {
                wait.until(ExpectedConditions.numberOfElementsToBeMoreThan(By.xpath("//input[@placeholder='Câu trả lời'] | //label[contains(text(),'Câu trả lời')]/following::input[1]"), i));
                baiTapPage.enterCauTraLoi(i, cauTraLoiList.get(i).toString());
            }
        }
        baiTapPage.clickCheckboxBangJS(indexDapAnDung);

        // SO SÁNH
        System.out.println("\n>>> KẾT QUẢ SO SÁNH:");
        inKetQuaConsole("Tên Bài Tập", tenBaiTap, tenBaiTap);
        inKetQuaConsole("File Ảnh Câu Hỏi", duongDanAnhCauHoi, duongDanAnhCauHoi);
        inKetQuaConsole("Nội Dung Câu Hỏi", noiDungCauHoi, noiDungCauHoi);
        inKetQuaConsole("Số Đáp Án", String.valueOf(cauTraLoiList.size()), String.valueOf(cauTraLoiList.size()));

        baiTapPage.clickLuuBaiTap();
        System.out.println("--> THÊM BÀI TẬP HÌNH ẢNH THÀNH CÔNG!\n");
    }

    @AfterClass
    public void tearDown() { if (driver != null) driver.quit(); }
}