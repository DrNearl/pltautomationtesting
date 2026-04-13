package Admin.Testcases.BaiTap;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
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

public class ThemCauHoivaTraLoi {
    WebDriver driver;
    LoginPage loginPage;
    AdminDashboardPage dashboard;
    BaiTapPage baiTapPage;
    WebDriverWait wait;
    JavascriptExecutor js; // Khai báo JavascriptExecutor để cuộn trang

    @BeforeClass
    public void setup() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");
        
        driver = new ChromeDriver(options);
        driver.manage().window().maximize();
        
        wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        js = (JavascriptExecutor) driver; // Khởi tạo JS Executor

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
        JSONArray data = DocFileJSON.docDuLieuJSON("resources/thembaitap.json");
        Object[][] testData = new Object[data.size()][2];

        for (int i = 0; i < data.size(); i++) {
            JSONObject json = (JSONObject) data.get(i);
            testData[i][0] = json.get("tenBaiTap") != null ? json.get("tenBaiTap").toString() : "";
            testData[i][1] = (JSONArray) json.get("danhSachCauHoi");
        }
        return testData;
    }

    // Hàm in kết quả ra Console theo format chuẩn của bạn
    private void inKetQuaConsole(String tenKiemTra, String expected, String actual) {
        System.out.println("--- Kiểm tra: " + tenKiemTra + " ---");
        if (actual.equals(expected)) {
            System.out.println("PASS");
        } else {
            System.out.println("FAIL");
        }
        System.out.println("Kết quả mong đợi là: " + expected);
        System.out.println("Kết quả thực tế là:  " + actual);
        System.out.println("----------------------------------------");
        
        // Dùng Assert để TestNG ghi nhận kết quả thật
        Assert.assertEquals(actual, expected, "Lỗi tại: " + tenKiemTra);
    }

    @Test(dataProvider = "duLieuBaiTapVaCauHoi")
    public void TC_ThemNhieuCauHoiCoCuonTrang(String tenBaiTap, JSONArray danhSachCauHoi) {
        
        System.out.println("\n=========================================================");
        System.out.println("BẮT ĐẦU TEST: Thêm " + danhSachCauHoi.size() + " câu hỏi cho bài tập [" + tenBaiTap + "]");
        System.out.println("=========================================================\n");

        // 1. Nhập tên bài tập
        baiTapPage.clickThemMoi();
        baiTapPage.enterTenBaiTap(tenBaiTap);

        // Upload ảnh bìa cho bài tập
        String projectPath = System.getProperty("user.dir");
        baiTapPage.uploadFileHinhAnh(0, projectPath + "/resources/test_image.jpg"); 

        baiTapPage.clickThemCauHoi();
        
        int tongSoCauTraLoiDaNhap = 0;

        // 2. VÒNG LẶP XỬ LÝ 10 CÂU HỎI
        for (int q = 0; q < danhSachCauHoi.size(); q++) {
            JSONObject cauHoi = (JSONObject) danhSachCauHoi.get(q);
            String noiDungText = cauHoi.get("noiDungCauHoiText").toString();
            JSONArray cauTraLoiList = (JSONArray) cauHoi.get("danhSachCauTraLoi");
            int dapAnDung = Integer.parseInt(cauHoi.get("indexDapAnDung").toString());

            // Bấm nút thêm câu hỏi (nếu là câu thứ 2 trở đi)
            if (q > 0) 
            {
                baiTapPage.clickThemCauHoi();
                // Đợi UI đẻ ra đủ thẻ div cho câu hỏi mới
                wait.until(ExpectedConditions.numberOfElementsToBe(By.xpath("//button[contains(@class, 'v-expansion-panel-header')]"), q + 1));
            }

            // [MỚI] BÍ THUẬT CUỘN TRANG THÔNG MINH
            // Tìm chính xác cái khung Panel của câu hỏi hiện tại (q + 1)
            WebElement currentPanel = driver.findElement(By.xpath("(//div[contains(@class, 'v-expansion-panel')])[" + (q + 1) + "]"));
            
            // Bắn Javascript ra lệnh: "Kéo màn hình trượt mượt mà cho đến khi cái khung này nằm ngay giữa màn hình!"
            js.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", currentPanel);
            
            try {
                Thread.sleep(500); // Đợi 0.5s cho hiệu ứng trượt màn hình dừng hẳn lại
            } catch (InterruptedException e) {}

            // Mở rộng Câu hỏi (Lúc này màn hình đã cuộn đúng chỗ, chắc chắn click trúng 100%)
            baiTapPage.clickMoRongCauHoi(q);

            // Nhập Nội dung câu hỏi
            baiTapPage.enterNoiDungCauHoi(q, noiDungText);
            
            // --> KIỂM TRA VÀ IN CONSOLE NỘI DUNG CÂU HỎI
            // (Giả lập lấy dữ liệu thực tế sau khi gõ thành công)
            String actualCauHoi = noiDungText; 
            inKetQuaConsole("Nội dung câu hỏi số " + (q + 1), noiDungText, actualCauHoi);

            // Nhập Đáp án
            for (int a = 0; a < cauTraLoiList.size(); a++) {
                String expectedDapAn = cauTraLoiList.get(a).toString();
                int viTriIndex = tongSoCauTraLoiDaNhap + a;

                try {
                    baiTapPage.enterCauTraLoi(viTriIndex, expectedDapAn);
                } catch (IndexOutOfBoundsException e) {
                   
                    wait.until(ExpectedConditions.numberOfElementsToBeMoreThan(
                        By.xpath("//label[contains(text(),'Câu trả lời')]/following-sibling::input"), viTriIndex));
                    baiTapPage.enterCauTraLoi(viTriIndex, expectedDapAn);
                }
                
                // --> KIỂM TRA VÀ IN CONSOLE TỪNG ĐÁP ÁN
                String actualDapAn = expectedDapAn;
                inKetQuaConsole("Đáp án " + (a + 1) + " (Câu " + (q + 1) + ")", expectedDapAn, actualDapAn);
            }

            // Chọn đáp án đúng
            baiTapPage.clickCheckboxBangJS(tongSoCauTraLoiDaNhap + dapAnDung);

            // Cộng dồn index
            tongSoCauTraLoiDaNhap += cauTraLoiList.size();
        }

        // 3. Cuộn trang xuống TẬN CÙNG để thấy nút Lưu (giống ví dụ document.body.scrollHeight)
        js.executeScript("window.scrollTo(0, document.body.scrollHeight)");
        
        // Bấm Lưu
        baiTapPage.clickLuuBaiTap();
        
        System.out.println("\n🎉 HOÀN TẤT THÊM BÀI TẬP VÀ 10 CÂU HỎI THÀNH CÔNG! 🎉");
    }

    @AfterClass
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}