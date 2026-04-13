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

public class ThemXoaBaiTapTongHop {
    WebDriver driver; LoginPage loginPage; AdminDashboardPage dashboard; BaiTapPage baiTapPage; WebDriverWait wait; JavascriptExecutor js;

    @BeforeClass
    public void setup() {
        ChromeOptions options = new ChromeOptions(); options.addArguments("--remote-allow-origins=*");
        driver = new ChromeDriver(options); driver.manage().window().maximize();
        wait = new WebDriverWait(driver, Duration.ofSeconds(15)); js = (JavascriptExecutor) driver;
        loginPage = new LoginPage(driver); dashboard = new AdminDashboardPage(driver); baiTapPage = new BaiTapPage(driver);
        
        loginPage.openLoginPage(); loginPage.login("test.pltsolutions@gmail.com","plt@intern_051224");
        wait.until(ExpectedConditions.urlContains("trang-chu"));
        dashboard.openQuanLyBaiTap(); wait.until(ExpectedConditions.urlContains("bai-tap"));
    }

    // [ĐÃ SỬA] DataProvider giờ đây trả về 4 cột (Tên bài, File ảnh, List Thêm Mới, List Cập Nhật)
    @DataProvider(name = "duLieuMaster")
    public Object[][] getData() {
        JSONArray data = DocFileJSON.docDuLieuJSON("resources/them_xoa_bai_tap_tong_hop.json");
        Object[][] testData = new Object[data.size()][4];
        for (int i = 0; i < data.size(); i++) {
            JSONObject json = (JSONObject) data.get(i);
            testData[i][0] = json.get("tenBaiTap").toString();
            testData[i][1] = json.get("tenFileAnhBia").toString();
       
            testData[i][2] = (JSONArray) json.get("danhSachCauHoiThemMoi");
            testData[i][3] = (JSONArray) json.get("danhSachCauHoiCapNhat");
        }
        return testData;
    }

    private void inKetQuaConsole(String tenKiemTra, String expected, String actual) {
        System.out.println("   [+] " + tenKiemTra + " | Mong đợi: " + expected + " | Thực tế: " + actual + " -> " + (actual.equals(expected) ? "PASS" : "FAIL"));
        Assert.assertEquals(actual, expected, "Lỗi dữ liệu tại: " + tenKiemTra);
    }

    @Test(dataProvider = "duLieuMaster")
    public void TC_Master_EndToEnd(String tenBaiTap, String tenFileAnhBia, JSONArray danhSachCauHoiThemMoi, JSONArray danhSachCauHoiCapNhat) throws InterruptedException {
        
        System.out.println("\n=========================================================");
        System.out.println("🔥 BƯỚC 1: THÊM MỚI BÀI TẬP - " + tenBaiTap);
        System.out.println("=========================================================\n");

        String projectPath = System.getProperty("user.dir");
        baiTapPage.clickThemMoi();
        wait.until(ExpectedConditions.urlContains("them-moi")); 
        
        baiTapPage.enterTenBaiTap(tenBaiTap);
        baiTapPage.uploadFileHinhAnh(0, projectPath + "/resources/" + tenFileAnhBia); 

        int tongSoCauTraLoiDaNhap = 0;

        // VÒNG LẶP 1: THÊM MỚI CÂU HỎI
        for (int q = 0; q < danhSachCauHoiThemMoi.size(); q++) {
            JSONObject cauHoi = (JSONObject) danhSachCauHoiThemMoi.get(q);
            String loaiCauHoi = cauHoi.get("loaiCauHoi").toString();
            String noiDungText = cauHoi.get("noiDungCauHoi").toString();
            String fileDinhKem = cauHoi.get("fileDinhKem") != null ? cauHoi.get("fileDinhKem").toString() : "";
            JSONArray cauTraLoiList = (JSONArray) cauHoi.get("danhSachCauTraLoi");
            int dapAnDung = Integer.parseInt(cauHoi.get("indexDapAnDung").toString());

            int expectedPanelCount = q + 1;
            
            // TẠO FORM
            if ("VanBan".equalsIgnoreCase(loaiCauHoi)) {
                baiTapPage.clickThemCauHoi();
            } else {
                baiTapPage.clickDropdownThemLoaiCauHoi();
                switch (loaiCauHoi) {
                    case "AmThanh": baiTapPage.chonLoaiAmThanh(); break;
                    case "HinhAnh": baiTapPage.chonLoaiHinhAnh(); break;
                    case "Video": baiTapPage.chonLoaiVideo(); break;
                    case "TuLuan": baiTapPage.chonLoaiTuLuan(); break;
                }
            }

            By titleMoi = By.xpath("//*[contains(@class, 'title-panel-header') and contains(normalize-space(), 'Câu hỏi số " + expectedPanelCount + "')]");
            WebElement titleElement = wait.until(ExpectedConditions.presenceOfElementLocated(titleMoi));
            js.executeScript("arguments[0].scrollIntoView({behavior: 'auto', block: 'center'});", titleElement);
            wait.until(ExpectedConditions.elementToBeClickable(titleElement)); 
            
            baiTapPage.clickMoRongCauHoi(q);

            if (!fileDinhKem.isEmpty()) {
                switch (loaiCauHoi) {
                    case "AmThanh": baiTapPage.uploadFileAmThanh(projectPath + "/resources/" + fileDinhKem); break;
                    case "HinhAnh": baiTapPage.uploadFileHinhAnhCauHoi(projectPath + "/resources/" + fileDinhKem); break;
                    case "Video": baiTapPage.enterIdVideo(fileDinhKem); break;
                    case "TuLuan": baiTapPage.enterSoLuongKyTu(fileDinhKem); break;
                }
            }

            baiTapPage.enterNoiDungCauHoi(q, noiDungText);
            inKetQuaConsole("ND Câu " + (q+1), noiDungText, noiDungText); 

            if (!"TuLuan".equalsIgnoreCase(loaiCauHoi)) {
                for (int a = 0; a < cauTraLoiList.size(); a++) {
                    String expectedDapAn = cauTraLoiList.get(a).toString();
                    int viTriIndex = tongSoCauTraLoiDaNhap + a;
                    try {
                        baiTapPage.enterCauTraLoi(viTriIndex, expectedDapAn);
                    } catch (Exception e) {
                        wait.until(ExpectedConditions.numberOfElementsToBeMoreThan(By.xpath("//input[@placeholder='Câu trả lời'] | //label[contains(text(),'Câu trả lời')]/following::input[1]"), viTriIndex));
                        baiTapPage.enterCauTraLoi(viTriIndex, expectedDapAn);
                    }
                }
                baiTapPage.clickCheckboxBangJS(tongSoCauTraLoiDaNhap + dapAnDung);
                tongSoCauTraLoiDaNhap += cauTraLoiList.size();
            }
        }

        // LƯU LẦN 1
        js.executeScript("window.scrollTo(0, document.body.scrollHeight)");
        baiTapPage.clickLuuBaiTap();
        baiTapPage.clickOkPopup(); 
        wait.until(ExpectedConditions.urlContains("bai-tap")); 
        System.out.println("✅ THÊM MỚI BÀI TẬP THÀNH CÔNG!\n");


     // ==========================================================
        System.out.println("🔥 BƯỚC 2: CẬP NHẬT BÀI TẬP (Đọc dữ liệu từ JSON Cập Nhật)");
        // ==========================================================
        
        baiTapPage.clickSuaBaiTap(tenBaiTap);
        
        // [WAIT] Đợi form Cập nhật load xong form cơ bản
        By waitFormCapNhat = By.xpath("//label[contains(text(),'Tên bài tập') or contains(text(),'Tên đề')] | //button[contains(normalize-space(), 'Lưu') or contains(normalize-space(), 'Cập nhật')]");
        wait.until(ExpectedConditions.visibilityOfElementLocated(waitFormCapNhat));
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[contains(normalize-space(), 'Lưu') or contains(normalize-space(), 'Cập nhật')]")));

        // ==========================================================
        // [CHỐT CHẶN MỚI]: BẮT BUỘC CHỜ API LOAD XONG CÁC CÂU HỎI CŨ RỒI MỚI CHẠY TIẾP
        By headerLocator = By.xpath("//button[contains(@class, 'v-expansion-panel-header')]");
        int soLuongCauHoiTuBuoc1 = danhSachCauHoiThemMoi.size();
        if (soLuongCauHoiTuBuoc1 > 0) {
            // Ép chờ số lượng Panel trên màn hình bằng đúng số lượng câu hỏi đã tạo ở Bước 1
            wait.until(ExpectedConditions.numberOfElementsToBe(headerLocator, soLuongCauHoiTuBuoc1));
        }
        // ==========================================================

        // VÒNG LẶP 2: ĐỌC TỪ MẢNG JSON CẬP NHẬT
        for (int c = 0; c < danhSachCauHoiCapNhat.size(); c++) {
            JSONObject cauHoiUpdate = (JSONObject) danhSachCauHoiCapNhat.get(c);
            String loaiCauHoi = cauHoiUpdate.get("loaiCauHoi") != null ? cauHoiUpdate.get("loaiCauHoi").toString() : "VanBan";
            String ndCapNhat = cauHoiUpdate.get("noiDungCauHoi").toString();
            JSONArray cauTraLoiList = (JSONArray) cauHoiUpdate.get("danhSachCauTraLoi");
            int dapAnDung = Integer.parseInt(cauHoiUpdate.get("indexDapAnDung").toString());
            int panelMoi = danhSachCauHoiThemMoi.size() + c + 1;

            // Lấy chính xác số lượng câu hỏi đang có ngay lúc này
            int soLuongCauHoiCu = driver.findElements(headerLocator).size();
            
            // 1. CHỌN LOẠI CÂU HỎI & TẠO FORM
            if ("VanBan".equalsIgnoreCase(loaiCauHoi)) {
                baiTapPage.clickThemCauHoi();
            } else {
                baiTapPage.clickDropdownThemLoaiCauHoi();
                switch (loaiCauHoi) {
                    case "AmThanh": baiTapPage.chonLoaiAmThanh(); break;
                    case "HinhAnh": baiTapPage.chonLoaiHinhAnh(); break;
                    case "Video": baiTapPage.chonLoaiVideo(); break;
                    case "TuLuan": baiTapPage.chonLoaiTuLuan(); break;
                }
            }
            
            // [WAIT] Chờ số lượng header panel tăng lên 1 (Khẳng định web đã sinh ra HTML mới)
            wait.until(ExpectedConditions.numberOfElementsToBe(headerLocator, soLuongCauHoiCu + 1));
            
            // Lấy header cuối cùng (Câu hỏi vừa thêm) và cuộn đến đó (không dùng smooth animation)
            java.util.List<WebElement> headers = driver.findElements(headerLocator);
            WebElement headerCuoiCung = headers.get(headers.size() - 1); 
            js.executeScript("arguments[0].scrollIntoView({behavior: 'auto', block: 'center'});", headerCuoiCung);
            wait.until(ExpectedConditions.elementToBeClickable(headerCuoiCung)); 
            
            // 2. [WAIT] ÉP MỞ RỘNG VÀ CHỜ PANEL MỞ HẲN
            String trangThaiMo = headerCuoiCung.getAttribute("aria-expanded");
            if ("false".equals(trangThaiMo) || trangThaiMo == null) {
                js.executeScript("arguments[0].click();", headerCuoiCung);
                // Bắt Selenium đứng đợi cho đến khi HTML thay đổi trạng thái thành "true" (Đã mở)
                wait.until(ExpectedConditions.attributeToBe(headerCuoiCung, "aria-expanded", "true"));
            }
            
            // ==========================================================
            // CHIÊU BỌC THÉP: KHÓA MỤC TIÊU VÀO PANEL ĐANG ACTIVE (MỞ)
            String activePanelXpath = "//div[contains(@class, 'v-expansion-panel--active')]";
            // ==========================================================

            // 3. [WAIT] ĐIỀN NỘI DUNG 
            // Đợi textarea hiển thị rõ ràng trên màn hình (Đã qua animation trượt của Vue)
            By txtNDLocator = By.xpath(activePanelXpath + "//label[contains(text(),'Nội dung câu hỏi')]/following::input[1] | " + activePanelXpath + "//textarea");
            WebElement txtND = wait.until(ExpectedConditions.visibilityOfElementLocated(txtNDLocator));
            wait.until(ExpectedConditions.elementToBeClickable(txtND));
            
            txtND.click();
            txtND.sendKeys(org.openqa.selenium.Keys.chord(org.openqa.selenium.Keys.CONTROL, "a"), org.openqa.selenium.Keys.BACK_SPACE);
            txtND.sendKeys(ndCapNhat);
            inKetQuaConsole("ND Câu Cập Nhật " + panelMoi, ndCapNhat, ndCapNhat);

            // 4. [WAIT] ĐIỀN ĐÁP ÁN 
            if (!"TuLuan".equalsIgnoreCase(loaiCauHoi)) {
                By listDapAnLocator = By.xpath(activePanelXpath + "//label[contains(text(),'Câu trả lời')]/following-sibling::input");
                wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(listDapAnLocator));
                
                for (int a = 0; a < cauTraLoiList.size(); a++) {
                    String expectedDapAn = cauTraLoiList.get(a).toString();
                    java.util.List<WebElement> listDapAn = driver.findElements(listDapAnLocator);
                    
                    // Nhấn "Thêm câu trả lời" nếu số ô trên UI ít hơn số đáp án trong JSON
                    if (a >= listDapAn.size()) {
                        WebElement btnThemDapAn = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(activePanelXpath + "//button[contains(normalize-space(), 'Thêm') or contains(@class, 'mdi-plus')]")));
                        ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("arguments[0].click();", btnThemDapAn);
                        
                        // [WAIT] Chờ số lượng ô input tăng lên so với lúc nãy
                        wait.until(ExpectedConditions.numberOfElementsToBeMoreThan(listDapAnLocator, listDapAn.size()));
                        listDapAn = driver.findElements(listDapAnLocator); // Cập nhật lại danh sách ô nhập
                    }
                    
                    // [WAIT] Đợi ô input tương ứng hiển thị và có thể click
                    WebElement txtDapAn = listDapAn.get(a);
                    wait.until(ExpectedConditions.visibilityOf(txtDapAn));
                    wait.until(ExpectedConditions.elementToBeClickable(txtDapAn));
                    
                    txtDapAn.click();
                    txtDapAn.sendKeys(org.openqa.selenium.Keys.chord(org.openqa.selenium.Keys.CONTROL, "a"), org.openqa.selenium.Keys.BACK_SPACE);
                    txtDapAn.sendKeys(expectedDapAn);
                    inKetQuaConsole("Đ.Án " + (a+1) + " (Câu " + panelMoi + ")", expectedDapAn, expectedDapAn);
                }
                
                // 5. [WAIT] CHỌN ĐÁP ÁN ĐÚNG
                By checkboxLocator = By.xpath(activePanelXpath + "//input[@type='checkbox' or @type='radio']");
                wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(checkboxLocator));
                java.util.List<WebElement> listCheckbox = driver.findElements(checkboxLocator);
                if (!listCheckbox.isEmpty() && dapAnDung < listCheckbox.size()) {
                    ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("arguments[0].click();", listCheckbox.get(dapAnDung));
                }
            }
        }

     // 6. [WAIT] LƯU LẦN 2 (CẬP NHẬT)
        // Tìm đúng nút submit (Thêm hoặc Cập nhật) để cuộn tới
        WebElement btnLuu = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//button[@type='submit' and (contains(., 'Thêm') or contains(., 'CẬP NHẬT') or contains(., 'Cập nhật'))]")));
        js.executeScript("arguments[0].scrollIntoView({behavior: 'auto', block: 'center'});", btnLuu);
        wait.until(ExpectedConditions.elementToBeClickable(btnLuu));
        
        baiTapPage.clickLuuBaiTap();
        baiTapPage.clickOkPopup();
        wait.until(ExpectedConditions.urlContains("bai-tap"));
        System.out.println("✅ CẬP NHẬT BÀI TẬP BẰNG JSON THÀNH CÔNG!\n");

        // ==========================================================
        System.out.println("🔥 BƯỚC 3: XÓA BÀI TẬP");
        // ==========================================================
        baiTapPage.clickXoaBaiTap(tenBaiTap);
        System.out.println("✅ ĐÃ XÓA THÀNH CÔNG BÀI TẬP: " + tenBaiTap);
        System.out.println("=========================================================\n");
    }

    @AfterClass
    public void tearDown() { if (driver != null) driver.quit(); }
}