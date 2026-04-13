package Test;

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
import org.testng.annotations.Test;

import Admin.Pages.AdminDashboardPage;
import Admin.Pages.BaiTapPage;
import Login.LoginPage;
import Utils.DocFileJSON;
import io.github.bonigarcia.wdm.WebDriverManager;

import java.time.Duration;

public class TestBaiTap {
    protected WebDriver driver;
    protected LoginPage loginPage;
    protected AdminDashboardPage dashboardPage;
    protected BaiTapPage baiTapPage;
    protected WebDriverWait wait;
    protected JavascriptExecutor js;

    @BeforeClass
    public void setupBrowser() throws InterruptedException {
        // Có thể dùng WebDriverManager theo format của TestHocVien
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");
        
        driver = new ChromeDriver(options);
        driver.manage().window().maximize();
        wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        js = (JavascriptExecutor) driver;

        loginPage = new LoginPage(driver);
        dashboardPage = new AdminDashboardPage(driver);
        baiTapPage = new BaiTapPage(driver);

        loginPage.openLoginPage();
        loginPage.login("test.pltsolutions@gmail.com", "plt@intern_051224");
        wait.until(ExpectedConditions.urlContains("trang-chu"));
        
        Assert.assertTrue(dashboardPage.isDashboardDisplayed());
        
        // Chuyển hướng sang trang Quản lý bài tập
        dashboardPage.openQuanLyBaiTap();
        wait.until(ExpectedConditions.urlContains("bai-tap"));
    }

    @Test
    public void TC_Full_Flow_Bai_Tap() throws Exception {
        // Đọc dữ liệu từ file JSON tổng hợp
        JSONArray data = DocFileJSON.docDuLieuJSON("resources/them_xoa_bai_tap_tong_hop.json");

        System.out.println("=========================================================");
        System.out.println("B1: XÓA BÀI TẬP (Dọn dẹp dữ liệu cũ nếu có)");
        System.out.println("=========================================================");
        for (int i = 0; i < data.size(); i++) {
            JSONObject baiTap = (JSONObject) data.get(i);
            String tenBaiTap = baiTap.get("tenBaiTap").toString();
            try {
                // Thử tìm và xóa bài tập nếu nó đã tồn tại từ lần test trước
                baiTapPage.clickXoaBaiTap(tenBaiTap);
                System.out.println("=> Đã xóa bài tập cũ có tên: " + tenBaiTap);
            } catch (Exception e) {
                System.out.println("=> Bỏ qua bước xóa vì ko tìm thấy bài tập (Database trống hoặc đã xóa): " + tenBaiTap);
            }
        }

        System.out.println("\n=========================================================");
        System.out.println("B2: THÊM BÀI TẬP MỚI (Xử lý Đa dạng câu hỏi)");
        System.out.println("=========================================================");
        for (int i = 0; i < data.size(); i++) {
            JSONObject baiTap = (JSONObject) data.get(i);
            String tenBaiTap = baiTap.get("tenBaiTap").toString();
            try {
                thucHienThemBaiTap(baiTap);
                System.out.println("=> Đã thêm bài tập thành công: " + tenBaiTap);
            } catch (Exception e) {
                System.err.println("=> Lỗi khi thêm bài tập: " + tenBaiTap);
                Assert.fail("Thất bại ở bước Thêm bài tập: " + e.getMessage());
            }
        }

        System.out.println("\n=========================================================");
        System.out.println("B3: CẬP NHẬT BÀI TẬP");
        System.out.println("=========================================================");
        for (int i = 0; i < data.size(); i++) {
            JSONObject baiTap = (JSONObject) data.get(i);
            String tenBaiTap = baiTap.get("tenBaiTap").toString();
            try {
                // Thực hiện cập nhật thêm câu hỏi mới
                thucHienCapNhatBaiTap(baiTap);
                System.out.println("=> Hoàn tất cập nhật cho bài tập: " + tenBaiTap);
                
            } catch (Exception e) {
                System.err.println("=> Lỗi khi Cập nhật bài tập: " + tenBaiTap);
                Assert.fail("Thất bại ở bước Cập nhật: " + e.getMessage());
            }
        }
    }

    @AfterClass
    public void closeBrowser() {
        if(driver != null) {
            driver.quit();
        }
    }

    // =========================================================================
    // CÁC HÀM HELPER HỖ TRỢ XỬ LÝ LOGIC PHỨC TẠP (Giữ cho code @Test luôn sạch)
    // =========================================================================

    private void thucHienThemBaiTap(JSONObject jsonBaiTap) throws InterruptedException {
        String projectPath = System.getProperty("user.dir");
        String tenBaiTap = jsonBaiTap.get("tenBaiTap").toString();
        String tenFileAnhBia = jsonBaiTap.get("tenFileAnhBia").toString();
        JSONArray danhSachCauHoiThemMoi = (JSONArray) jsonBaiTap.get("danhSachCauHoiThemMoi");

        baiTapPage.clickThemMoi();
        wait.until(ExpectedConditions.urlContains("them-moi")); 
        
        baiTapPage.enterTenBaiTap(tenBaiTap);
        baiTapPage.uploadFileHinhAnh(0, projectPath + "/resources/" + tenFileAnhBia); 

        int tongSoCauTraLoiDaNhap = 0;

        for (int q = 0; q < danhSachCauHoiThemMoi.size(); q++) {
            JSONObject cauHoi = (JSONObject) danhSachCauHoiThemMoi.get(q);
            String loaiCauHoi = cauHoi.get("loaiCauHoi").toString();
            String noiDungText = cauHoi.get("noiDungCauHoi").toString();
            String fileDinhKem = cauHoi.get("fileDinhKem") != null ? cauHoi.get("fileDinhKem").toString() : "";
            JSONArray cauTraLoiList = (JSONArray) cauHoi.get("danhSachCauTraLoi");
            int dapAnDung = Integer.parseInt(cauHoi.get("indexDapAnDung").toString());

            // TẠO FORM THEO LOẠI
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

            By titleMoi = By.xpath("//*[contains(@class, 'title-panel-header') and contains(normalize-space(), 'Câu hỏi số " + (q + 1) + "')]");
            WebElement titleElement = wait.until(ExpectedConditions.presenceOfElementLocated(titleMoi));
            js.executeScript("arguments[0].scrollIntoView({behavior: 'auto', block: 'center'});", titleElement);
            wait.until(ExpectedConditions.elementToBeClickable(titleElement)); 
            
            baiTapPage.clickMoRongCauHoi(q);

            // UPLOAD FILE/NHẬP THÔNG TIN PHỤ
            if (!fileDinhKem.isEmpty()) {
                switch (loaiCauHoi) {
                    case "AmThanh": baiTapPage.uploadFileAmThanh(projectPath + "/resources/" + fileDinhKem); break;
                    case "HinhAnh": baiTapPage.uploadFileHinhAnhCauHoi(projectPath + "/resources/" + fileDinhKem); break;
                    case "Video": baiTapPage.enterIdVideo(fileDinhKem); break;
                    case "TuLuan": baiTapPage.enterSoLuongKyTu(fileDinhKem); break;
                }
            }

            // NHẬP NỘI DUNG VÀ ĐÁP ÁN
            baiTapPage.enterNoiDungCauHoi(q, noiDungText);
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

        js.executeScript("window.scrollTo(0, document.body.scrollHeight)");
        baiTapPage.clickLuuBaiTap();
        baiTapPage.clickOkPopup(); 
        wait.until(ExpectedConditions.urlContains("bai-tap")); 
    }

    private void thucHienCapNhatBaiTap(JSONObject jsonBaiTap) throws InterruptedException {
        String projectPath = System.getProperty("user.dir"); // Lấy đường dẫn project cho phần upload file
        String tenBaiTap = jsonBaiTap.get("tenBaiTap").toString();
        JSONArray danhSachCauHoiThemMoi = (JSONArray) jsonBaiTap.get("danhSachCauHoiThemMoi");
        JSONArray danhSachCauHoiCapNhat = (JSONArray) jsonBaiTap.get("danhSachCauHoiCapNhat");

        baiTapPage.clickSuaBaiTap(tenBaiTap);
        
        By waitFormCapNhat = By.xpath("//label[contains(text(),'Tên bài tập') or contains(text(),'Tên đề')] | //button[contains(normalize-space(), 'Lưu') or contains(normalize-space(), 'Cập nhật')]");
        wait.until(ExpectedConditions.visibilityOfElementLocated(waitFormCapNhat));
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[contains(normalize-space(), 'Lưu') or contains(normalize-space(), 'Cập nhật')]")));

        By headerLocator = By.xpath("//button[contains(@class, 'v-expansion-panel-header')]");
        int soLuongCauHoiTuBuoc1 = danhSachCauHoiThemMoi.size();
        if (soLuongCauHoiTuBuoc1 > 0) {
            wait.until(ExpectedConditions.numberOfElementsToBe(headerLocator, soLuongCauHoiTuBuoc1));
        }

        for (int c = 0; c < danhSachCauHoiCapNhat.size(); c++) {
            JSONObject cauHoiUpdate = (JSONObject) danhSachCauHoiCapNhat.get(c);
            String loaiCauHoi = cauHoiUpdate.get("loaiCauHoi") != null ? cauHoiUpdate.get("loaiCauHoi").toString() : "VanBan";
            String ndCapNhat = cauHoiUpdate.get("noiDungCauHoi").toString();
            JSONArray cauTraLoiList = (JSONArray) cauHoiUpdate.get("danhSachCauTraLoi");
            int dapAnDung = Integer.parseInt(cauHoiUpdate.get("indexDapAnDung").toString());

            int soLuongCauHoiCu = driver.findElements(headerLocator).size();
            
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
            
            wait.until(ExpectedConditions.numberOfElementsToBe(headerLocator, soLuongCauHoiCu + 1));
            
            java.util.List<WebElement> headers = driver.findElements(headerLocator);
            WebElement headerCuoiCung = headers.get(headers.size() - 1); 
            js.executeScript("arguments[0].scrollIntoView({behavior: 'auto', block: 'center'});", headerCuoiCung);
            wait.until(ExpectedConditions.elementToBeClickable(headerCuoiCung)); 
            
            String trangThaiMo = headerCuoiCung.getAttribute("aria-expanded");
            if ("false".equals(trangThaiMo) || trangThaiMo == null) {
                js.executeScript("arguments[0].click();", headerCuoiCung);
                wait.until(ExpectedConditions.attributeToBe(headerCuoiCung, "aria-expanded", "true"));
            }
            
            String activePanelXpath = "//div[contains(@class, 'v-expansion-panel--active')]";

            // XỬ LÝ NHẬP LẠI ID VIDEO / UPLOAD ẢNH CHO PHẦN CẬP NHẬT
            String fileDinhKem = cauHoiUpdate.get("fileDinhKem") != null ? cauHoiUpdate.get("fileDinhKem").toString() : "";
            if (!fileDinhKem.isEmpty()) {
                switch (loaiCauHoi) {
                    case "AmThanh": baiTapPage.uploadFileAmThanh(projectPath + "/resources/" + fileDinhKem); break;
                    case "HinhAnh": baiTapPage.uploadFileHinhAnhCauHoi(projectPath + "/resources/" + fileDinhKem); break;
                    case "Video": baiTapPage.enterIdVideo(fileDinhKem); break; 
                    case "TuLuan": baiTapPage.enterSoLuongKyTu(fileDinhKem); break;
                }
            }

            By txtNDLocator = By.xpath(activePanelXpath + "//label[contains(text(),'Nội dung câu hỏi')]/following::input[1] | " + activePanelXpath + "//textarea");
            WebElement txtND = wait.until(ExpectedConditions.visibilityOfElementLocated(txtNDLocator));
            wait.until(ExpectedConditions.elementToBeClickable(txtND));
            
            txtND.click();
            txtND.sendKeys(org.openqa.selenium.Keys.chord(org.openqa.selenium.Keys.CONTROL, "a"), org.openqa.selenium.Keys.BACK_SPACE);
            txtND.sendKeys(ndCapNhat);

            if (!"TuLuan".equalsIgnoreCase(loaiCauHoi)) {
                By listDapAnLocator = By.xpath(activePanelXpath + "//label[contains(text(),'Câu trả lời')]/following-sibling::input");
                wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(listDapAnLocator));
                
                for (int a = 0; a < cauTraLoiList.size(); a++) {
                    String expectedDapAn = cauTraLoiList.get(a).toString();
                    java.util.List<WebElement> listDapAn = driver.findElements(listDapAnLocator);
                    
                    if (a >= listDapAn.size()) {
                        WebElement btnThemDapAn = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(activePanelXpath + "//button[contains(normalize-space(), 'Thêm') or contains(@class, 'mdi-plus')]")));
                        ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("arguments[0].click();", btnThemDapAn);
                        wait.until(ExpectedConditions.numberOfElementsToBeMoreThan(listDapAnLocator, listDapAn.size()));
                        listDapAn = driver.findElements(listDapAnLocator);
                    }
                    
                    WebElement txtDapAn = listDapAn.get(a);
                    wait.until(ExpectedConditions.visibilityOf(txtDapAn));
                    wait.until(ExpectedConditions.elementToBeClickable(txtDapAn));
                    
                    txtDapAn.click();
                    txtDapAn.sendKeys(org.openqa.selenium.Keys.chord(org.openqa.selenium.Keys.CONTROL, "a"), org.openqa.selenium.Keys.BACK_SPACE);
                    txtDapAn.sendKeys(expectedDapAn);
                }
                
                By checkboxLocator = By.xpath(activePanelXpath + "//input[@type='checkbox' or @type='radio']");
                wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(checkboxLocator));
                java.util.List<WebElement> listCheckbox = driver.findElements(checkboxLocator);
                if (!listCheckbox.isEmpty() && dapAnDung < listCheckbox.size()) {
                    ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("arguments[0].click();", listCheckbox.get(dapAnDung));
                }
            }
        }

        WebElement btnLuu = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//button[@type='submit' and (contains(., 'Thêm') or contains(., 'CẬP NHẬT') or contains(., 'Cập nhật'))]")));
        js.executeScript("arguments[0].scrollIntoView({behavior: 'auto', block: 'center'});", btnLuu);
        wait.until(ExpectedConditions.elementToBeClickable(btnLuu));
        
        baiTapPage.clickLuuBaiTap();
        baiTapPage.clickOkPopup();
        wait.until(ExpectedConditions.urlContains("bai-tap"));
    }
}