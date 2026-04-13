package Admin.Pages;

import java.util.List;
import org.openqa.selenium.By;
//import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import org.openqa.selenium.JavascriptExecutor;

//import org.openqa.selenium.support.ui.FluentWait;

public class BaiTapPage {

    private WebDriver driver;

    public BaiTapPage(WebDriver driver) {
        this.driver = driver;
    }

    // ==========================================
    // LOCATORS - MÀN HÌNH DANH SÁCH BÀI TẬP
    // ==========================================
    private By TITLE_PAGE =
            By.xpath("//*[contains(text(),'Bài tập') and not(contains(text(),'Bài tập '))]");    //xong

    private By BTN_THEM_MOI_LIST = By.xpath("//a[@href='/quan-tri-vien/bai-tap/them-moi']");   //xong

    private By BTN_TAI_LAI = By.xpath("//button[normalize-space()='Tải lại dữ liệu']");//xong

    private By BTN_EDIT = By.xpath("//a[contains(@href, '/quan-tri-vien/bai-tap/chinh-sua')]");//xong

    private By BTN_XOA = By.xpath("//button[.//i[contains(@class, 'mdi-close')]]");//xong
    
    
    
    // ==========================================
    // LOCATORS - MÀN HÌNH THÊM/SỬA BÀI TẬP (Dựa trên 2 ảnh mới)
    // ==========================================
    
    // Tên bài tập
    private By INPUT_TEN_BAI_TAP = By.xpath("//label[contains(text(),'Tên bài tập')]/following::input[1]");//xong

    // Nút Thêm Câu Hỏi (có dropdown)
    private By INPUT_FILE_HINH_ANH = By.xpath("//input[@type='file']");//xong
    
 // Cách này "miễn nhiễm" với mọi loại dấu cách, xuống dòng thừa trong HTML
    private By BTN_THEM_CAU_HOI = By.xpath("//button[contains(@class, 'primary') and contains(., 'câu hỏi')]");//xong

    //private By ICON_MO_RONG_CAU_HOI = By.xpath("//i[contains(@class, 'mdi-chevron-down')]");//xong
    // Nội dung câu hỏi (áp dụng cho cả form ngắn và form dài trong card)
    // Lấy list các input nội dung câu hỏi (phòng trường hợp add nhiều câu hỏi)
    

    private By INPUT_NOI_DUNG_CAU_HOI =   //xong
    		By.xpath("//div[contains(@class, 'v-expansion-panel')]//label[contains(text(),'Nội dung câu hỏi')]/following::input[1] | //div[contains(@class, 'v-expansion-panel')]//textarea[contains(@placeholder,'Nội dung câu hỏi')]");
    // Nút Thêm Câu Trả Lời (trong mỗi card câu hỏi)
    
    
   

    // Các ô input nhập Câu trả lời
 // Bắt ô nhập Câu trả lời (Vì có nhiều ô nên XPath này sẽ đại diện cho một List các ô)
    private By INPUT_CAU_TRA_LOI = By.xpath("//label[contains(text(),'Câu trả lời')]/following-sibling::input");//xong

    // Các checkbox đánh dấu đáp án đúng
    private By INPUT_CHECKBOX_AN = By.xpath("//input[@type='checkbox']");//xong

 // Nút Lưu bài tập (Dùng chung cho cả màn hình Thêm mới và Cập nhật, khóa chặt bằng type='submit')
    private By BTN_LUU_BAI_TAP = By.xpath("//button[@type='submit' and (contains(., 'Thêm') or contains(., 'CẬP NHẬT') or contains(., 'Cập nhật'))]");
    // ==========================================
    // LOCATORS BỔ SUNG CHO CÁC DẠNG CÂU HỎI MỚI
    // ==========================================
    // Nút mũi tên dropdown bên cạnh nút THÊM CÂU HỎI
    private By BTN_CHON_LOAI_CAU_HOI = By.xpath("//button[.//i[contains(@class, 'mdi-menu-down')]]");

    // Lấy list các input tùy theo dạng câu hỏi
    private By OPTION_AM_THANH = By.xpath("//div[contains(@class, 'v-list-item__title') and text()='Âm thanh']");
    
    private By INPUT_FILE_AM_THANH = By.xpath("//input[@type='file' and (contains(@accept, 'audio') or contains(@accept, '.mp3'))]");
///
/// 
/// 
///             
 // --- Các tuỳ chọn trong Dropdown Loại Câu Hỏi ---
    private By OPTION_HINH_ANH = By.xpath("//div[contains(@class, 'v-list-item__title') and text()='Hình ảnh']");
    private By OPTION_VIDEO = By.xpath("//div[contains(@class, 'v-list-item__title') and text()='Video']");
    private By OPTION_TU_LUAN = By.xpath("//div[contains(@class, 'v-list-item__title') and text()='Tự luận']");

    // --- Các ô nhập liệu theo từng loại ---
    // (Lấy input ngay sau cái label chứa text tương ứng để đảm bảo chính xác 100%)
    private By INPUT_FILE_HINH_ANH_CAU_HOI = By.xpath("//label[contains(text(),'File hình ảnh của câu hỏi')]/following::input[@type='file'][1]");
    private By INPUT_ID_VIDEO = By.xpath("//label[contains(text(),'ID video trên youtube')]/following::input[1]");
    private By INPUT_SO_LUONG_KY_TU = By.xpath("//label[contains(text(),'Số lượng ký tự cho phép trả lời')]/following::input[1]");

    // ==========================================
    // ACTIONS BỔ SUNG
    // ==========================================
    public void clickDropdownThemLoaiCauHoi() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        
        try {
            // Gọi biến BTN_CHON_LOAI_CAU_HOI đã khai báo ở trên
            WebElement btn = wait.until(ExpectedConditions.presenceOfElementLocated(BTN_CHON_LOAI_CAU_HOI));
            
            // Ép click bằng JS để xuyên qua mọi animation
            ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("arguments[0].click();", btn);
            System.out.println(">>> Đã click mũi tên xổ xuống để TẠO loại câu hỏi mới.");
            
            // Chờ 0.5s cho menu xổ ra hoàn toàn
            Thread.sleep(500); 
            
        } catch (Exception e) {
            System.err.println("LỖI: Không tìm thấy nút mũi tên ngoài cùng.");
            e.printStackTrace();
        }
    }

    // Hàm chọn chữ "Âm thanh" trong menu vừa xổ ra
    public void chonLoaiAmThanh() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        
        try {
            // Gọi biến OPTION_AM_THANH đã khai báo ở trên
            WebElement option = wait.until(ExpectedConditions.presenceOfElementLocated(OPTION_AM_THANH));
            
            // Ép click bằng JS
            ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("arguments[0].click();", option);
            System.out.println(">>> Đã chọn 'Âm thanh'. Đang chờ web sinh ra khung Câu hỏi 1...");
            
            // CỰC KỲ QUAN TRỌNG: Chờ 1 giây để web kịp "đẻ" ra mã HTML của câu hỏi
            Thread.sleep(1000); 
            
        } catch (Exception e) {
            System.err.println("LỖI: Không tìm thấy tùy chọn Âm thanh.");
            e.printStackTrace();
        }
    }

    public void uploadFileAmThanh(String filePath) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        
        // Lấy tất cả các ô upload âm thanh đang có trên trang
        java.util.List<org.openqa.selenium.WebElement> inputs = wait.until(
            ExpectedConditions.presenceOfAllElementsLocatedBy(INPUT_FILE_AM_THANH)
        );
        
        // Gửi file vào ô upload CUỐI CÙNG (tương ứng với câu hỏi vừa thêm)
        org.openqa.selenium.WebElement input = inputs.get(inputs.size() - 1);
        input.sendKeys(filePath);
    }
///
/// 
/// 
    public void chonLoaiHinhAnh() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement option = wait.until(ExpectedConditions.presenceOfElementLocated(OPTION_HINH_ANH));
        ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("arguments[0].click();", option);
        try { Thread.sleep(1000); } catch (Exception e) {} // Đợi form sinh ra
    }

    public void chonLoaiVideo() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        
        // 1. Đợi tuỳ chọn Video có mặt trong dropdown và ép click
        WebElement option = wait.until(ExpectedConditions.presenceOfElementLocated(OPTION_VIDEO));
        ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("arguments[0].click();", option);
        
        // 2. Tạm nghỉ 0.5s để web sinh ra HTML của panel (Lưu ý: Không dùng Wait visibility ở đây vì panel đang bị ĐÓNG)
        try { 
            Thread.sleep(500); 
            System.out.println(">>> Đã click chọn Loại Video. Đang chờ mở rộng Panel...");
        } catch (Exception e) {}
    }
    
    
    public void chonLoaiTuLuan() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement option = wait.until(ExpectedConditions.presenceOfElementLocated(OPTION_TU_LUAN));
        ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("arguments[0].click();", option);
        try { Thread.sleep(1000); } catch (Exception e) {}
    }
///
/// 
/// 
/// 
/// 
    
    public void uploadFileHinhAnhCauHoi(String filePath) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        // Input type="file" thường bị ẩn nên dùng presenceOfElementLocated
        WebElement input = wait.until(ExpectedConditions.presenceOfElementLocated(INPUT_FILE_HINH_ANH_CAU_HOI));
        input.sendKeys(filePath);
    }
    
    public void enterIdVideo(String idVideo) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        JavascriptExecutor js = (JavascriptExecutor) driver;

        // 1. Đợi các ô input ID Video xuất hiện trong HTML (lúc này hàm clickMoRongCauHoi đã mở panel ra rồi)
        java.util.List<WebElement> listInputs = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(INPUT_ID_VIDEO));
        
        // 2. Lấy ô input CUỐI CÙNG (thuộc về câu hỏi Video bạn vừa mới Thêm)
        WebElement input = listInputs.get(listInputs.size() - 1);
        
        // 3. Cuộn màn hình đến ô đó và dùng WebDriverWait đợi nó hiển thị rõ ràng
        js.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", input);
        wait.until(ExpectedConditions.visibilityOf(input));
        wait.until(ExpectedConditions.elementToBeClickable(input));

        // 4. Click vào ô nhập liệu
        input.click();

        // 5. TUYỆT CHIÊU XÓA: Dùng Ctrl+A -> Backspace để xóa sạch ID mặc định (XEzRZ35urlkS)
        input.sendKeys(org.openqa.selenium.Keys.chord(org.openqa.selenium.Keys.CONTROL, "a"), org.openqa.selenium.Keys.BACK_SPACE);
        
        // 6. Khóa chốt xóa bằng JS (Đề phòng framework Vue.js tự động điền lại)
        js.executeScript("arguments[0].value = '';", input);
        
        // 7. Nhập ID video mới từ JSON vào
        input.sendKeys(idVideo);
        
        // 8. Báo cho hệ thống web biết là dữ liệu đã được thay đổi
        js.executeScript("arguments[0].dispatchEvent(new Event('input', { bubbles: true }));", input);
        js.executeScript("arguments[0].dispatchEvent(new Event('change', { bubbles: true }));", input);
        
        System.out.println(">>> Đã xóa ID video mặc định và nhập ID mới thành công: " + idVideo);
    }

    public void enterSoLuongKyTu(String soLuong) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement input = wait.until(ExpectedConditions.presenceOfElementLocated(INPUT_SO_LUONG_KY_TU));
        org.openqa.selenium.JavascriptExecutor js = (org.openqa.selenium.JavascriptExecutor) driver;
        js.executeScript("arguments[0].value = '';", input);
        js.executeScript("arguments[0].value = arguments[1];", input, soLuong);
        js.executeScript("arguments[0].dispatchEvent(new Event('input', { bubbles: true }));", input);
    }
    // ==========================================
    // VERIFY
    // ==========================================
    public boolean isBaiTapPageDisplayed() {
        return driver.findElement(TITLE_PAGE).isDisplayed();
    }

    // ==========================================
    // ACTIONS - MÀN HÌNH DANH SÁCH
    // ==========================================
    public void clickThemMoi() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.elementToBeClickable(BTN_THEM_MOI_LIST)).click(); //xong
    }

    public void clickTaiLaiDuLieu() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        // Chờ nút Tải lại dữ liệu có thể click được rồi mới click
        wait.until(ExpectedConditions.elementToBeClickable(BTN_TAI_LAI)).click();  //xong
        System.out.println("Đã click nút Tải lại dữ liệu.");
    }

  

    public void clickSuaBaiTapDauTien() {
        driver.findElements(BTN_EDIT).get(0).click();//xong
    }

    public void clickXoaBaiTapDauTien() {
        driver.findElements(BTN_XOA).get(0).click();//xong
    }

    // ==========================================
    // ACTIONS - MÀN HÌNH THÊM/SỬA BÀI TẬP
    // ==========================================
    
    public void enterTenBaiTap(String tenBaiTap) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        
        // ĐỢI cho đến khi ô nhập xuất hiện hoàn toàn rồi mới điền
        WebElement input = wait.until(ExpectedConditions.visibilityOfElementLocated(INPUT_TEN_BAI_TAP));//xong
        
        input.clear(); 
        input.sendKeys(tenBaiTap);
    }

    public void uploadFileHinhAnh(int index,String absolutePath) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        
        // 1. Tìm cái thẻ input ẩn (không cần quan tâm đến nút máy ảnh)
        WebElement inputFile = wait.until(ExpectedConditions.presenceOfElementLocated(INPUT_FILE_HINH_ANH));  //xong
        
        // 2. Bắn thẳng đường dẫn file ảnh vào thẻ input đó
        inputFile.sendKeys(absolutePath); 
        
        System.out.println("Đã upload file: " + absolutePath);
    }
 // TOÀN BỘ CODE PHẢI NẰM BÊN TRONG CẶP NGOẶC NHỌN CỦA HÀM NÀY
    public void clickThemCauHoi() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        
        // 1. Tìm nút
        WebElement btn = wait.until(ExpectedConditions.presenceOfElementLocated(BTN_THEM_CAU_HOI));
        
        // 2. Cuộn chuột đến nút đó (tránh lỗi out of view)
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", btn);
        
        try {
            // 3. Đợi nút Clickable rồi click
            wait.until(ExpectedConditions.elementToBeClickable(btn)).click();
        } catch (Exception e) {
            // 4. Nếu bị che, dùng JS ép click đâm xuyên thẳng vào nút!
            js.executeScript("arguments[0].click();", btn);
        }
        
        System.out.println("Đã click nút Thêm câu hỏi.");
    }
 
 
    public void clickMoRongCauHoi(int index) {
        try {
            // 1. Phải có một nhịp nghỉ nhỏ để HTML của câu hỏi mới kịp sinh ra trong DOM
            //Thread.sleep(1000);

            JavascriptExecutor js = (JavascriptExecutor) driver;

            // 2. Viết một đoạn mã Javascript thuần túy để trình duyệt tự xử lý
            // Script này làm 3 việc: Tìm đúng câu hỏi -> Cuộn đến nó -> Kiểm tra xem mở chưa -> Nếu chưa thì ÉP CLICK!
            String script = 
                "var headers = document.querySelectorAll('.v-expansion-panel-header');" +
                "if (headers.length > arguments[0]) {" +
                "   var targetHeader = headers[arguments[0]];" +
                "   targetHeader.scrollIntoView({behavior: 'smooth', block: 'center'});" +
                "   if (targetHeader.getAttribute('aria-expanded') !== 'true') {" +
                "       targetHeader.click();" +
                "       return 'CLICKED';" +
                "   }" +
                "   return 'ALREADY_OPEN';" +
                "}" +
                "return 'NOT_FOUND';";

            // 3. Bắn đoạn Javascript vào trình duyệt và truyền index vào
            String result = (String) js.executeScript(script, index);

            // 4. Đọc kết quả mà Javascript báo về
            if ("CLICKED".equals(result)) {
                System.out.println("--> [Pure JS] Đã ép click mở rộng Câu hỏi số " + (index + 1));
                Thread.sleep(1000); // Đợi 1s cho form từ từ xổ xuống
            } else if ("ALREADY_OPEN".equals(result)) {
                System.out.println("--> [Pure JS] Câu hỏi số " + (index + 1) + " đã mở sẵn rồi.");
            } else {
                System.err.println("--> [Pure JS] LỖI: Không tìm thấy HTML của Câu hỏi số " + (index + 1));
            }

        } catch (Exception e) {
            System.err.println("Lỗi JS kịch độc: " + e.getMessage());
        }
    }
    
    public void enterNoiDungCauHoi(int questionIndex, String noiDung) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        // Đợi list các ô nhập câu hỏi xuất hiện
        wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(INPUT_NOI_DUNG_CAU_HOI));
        
        List<WebElement> inputs = driver.findElements(INPUT_NOI_DUNG_CAU_HOI);
        inputs.get(questionIndex).clear();
        inputs.get(questionIndex).sendKeys(noiDung);
        System.out.println("Đã gõ nội dung câu hỏi: " + noiDung);
    }
  
    public void enterCauTraLoi(int index, String noiDung) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        
        // Đợi cho các ô nhập liệu có mặt trên giao diện
        wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(INPUT_CAU_TRA_LOI));
        
        // Lấy ra toàn bộ danh sách các ô nhập Câu trả lời đang có trên màn hình
        List<WebElement> inputs = driver.findElements(INPUT_CAU_TRA_LOI);
        
        // Trỏ vào đúng ô thứ 'index' (0, 1, 2...) để điền
        inputs.get(index).clear();
        inputs.get(index).sendKeys(noiDung);
        
        System.out.println("Đã nhập đáp án " + (index + 1) + ": " + noiDung);
    }
    
    public void clickCheckboxBangJS(int index) {
        List<WebElement> listInputs = driver.findElements(INPUT_CHECKBOX_AN);
        
        if (index < listInputs.size()) {
            WebElement inputCanClick = listInputs.get(index);
            
            // Dùng Javascript để click ép buộc vào thẻ bị ẩn
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("arguments[0].click();", inputCanClick);
            
            System.out.println("Đã dùng Javascript click chọn đáp án số: " + (index + 1));
        }
    }
 // Hàm click nút THÊM (Lưu bài tập)
    public void clickLuuBaiTap() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        
        // Đợi cho đến khi nút THÊM sẵn sàng (không bị mờ, không bị che khuất) rồi mới click
        wait.until(ExpectedConditions.elementToBeClickable(BTN_LUU_BAI_TAP)).click();
        
        System.out.println("Đã click nút THÊM (Lưu bài tập).");
    }
    
    
 // ================= LOCATOR CHO TÍNH NĂNG CẬP NHẬT & XÓA =================
    // Cập nhật lại XPath theo thư viện SweetAlert2
    private By BTN_OK_POPUP = By.xpath("//button[contains(@class, 'swal2-confirm') and text()='OK']");
    
    // Nút xác nhận xóa (Bao quát cả 2 trường hợp viết hoa/thường hoặc dùng thư viện Swal)
    private By BTN_XAC_NHAN_XOA = By.xpath("//div[contains(@class, 'v-dialog--active') or contains(@class, 'swal2-shown')]//button[contains(normalize-space(), 'XOÁ') or contains(normalize-space(), 'Xóa')] | //button[contains(normalize-space(), 'XOÁ') or contains(normalize-space(), 'Xóa')]");

    // ================= ACTIONS =================
    
    // 1. Hàm click OK trên Popup thông báo (Đã sửa theo SweetAlert2)
    public void clickOkPopup() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        try {
            WebElement btnOk = wait.until(ExpectedConditions.elementToBeClickable(BTN_OK_POPUP));
            ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("arguments[0].click();", btnOk);
            
            // Đợi cái nền mờ của SweetAlert2 biến mất hoàn toàn
            wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//div[contains(@class, 'swal2-container')]")));
            System.out.println(">>> Đã click OK trên popup thông báo.");
        } catch (Exception e) {
            System.err.println("LỖI: Không tìm thấy nút OK trên popup.");
        }
    }

    // 2. Hàm click nút CẬP NHẬT (Cây bút) theo tên bài tập
    public void clickSuaBaiTap(String tenBaiTap) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        try {
            // RẤT QUAN TRỌNG: Đợi 1.5s để API của web kịp tải lại bảng danh sách sau khi tắt Popup
            Thread.sleep(1500); 

            // Tìm dòng chứa bài tập (Dùng contains(., 'text') bao quát toàn bộ thẻ, tránh lỗi khoảng trắng)
            String xpathRow = "//tr[contains(., '" + tenBaiTap + "')]";
            wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(xpathRow)));

            // Tìm nút Cây bút: Thử tìm class pencil/edit, NẾU KHÔNG CÓ thì bốc luôn phần tử ĐẦU TIÊN ở cột cuối cùng
            String xpathBtnSua = xpathRow + "//*[contains(@class, 'pencil') or contains(@class, 'edit')] | " + xpathRow + "//td[last()]/*[1]";
            WebElement btnSua = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(xpathBtnSua)));
            
            ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", btnSua);
            Thread.sleep(500); // Chờ hiệu ứng cuộn mượt
            
            // Ép click bằng JS
            ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("arguments[0].click();", btnSua);
            System.out.println(">>> Đã click nút Cập nhật (Cây bút) cho bài tập: " + tenBaiTap);
            
        } catch (Exception e) {
            System.err.println("LỖI: Không tìm thấy nút Cập nhật của bài tập: " + tenBaiTap);
            e.printStackTrace();
            // Ném lỗi văng ra ngoài để DỪNG TEST NGAY LẬP TỨC, tránh chạy tiếp sinh ra lỗi 'Tên đề' ảo
            throw new RuntimeException("Dừng Test: Không thể vào trang Cập nhật bài tập!"); 
        }
    }

    // 3. Hàm click nút XÓA (Dấu X đỏ) theo tên bài tập
    public void clickXoaBaiTap(String tenBaiTap) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        JavascriptExecutor js = (JavascriptExecutor) driver;
        try {
            // 1. [WAIT] Đợi dòng chứa bài tập hiển thị rõ ràng
            String xpathRow = "//tr[contains(., '" + tenBaiTap + "')]";
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpathRow)));

            // 2. [WAIT] Tìm nút Dấu X đỏ và đợi đến khi có thể click
            String xpathBtnXoa = xpathRow + "//*[contains(@class, 'close') or contains(@class, 'delete')] | " + xpathRow + "//td[last()]/*[last()]";
            WebElement btnXoa = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(xpathBtnXoa)));
            
            js.executeScript("arguments[0].scrollIntoView({behavior: 'auto', block: 'center'});", btnXoa);
            wait.until(ExpectedConditions.elementToBeClickable(btnXoa));
            
            // Bấm mở popup Xóa
            js.executeScript("arguments[0].click();", btnXoa);
            System.out.println(">>> Đã click nút Xóa (Dấu X đỏ) cho bài tập: " + tenBaiTap);

            // ==========================================================
            // 3. [TUYỆT CHIÊU WEBDRIVERWAIT]: Custom ExpectedCondition
            // ==========================================================
            // Dạy cho WebDriverWait cách tìm đúng cái nút đang hiển thị trong một mớ nút bị ẩn
            WebElement btnConfirm = wait.until(webDriver -> {
                java.util.List<WebElement> btns = webDriver.findElements(By.xpath("//button[contains(normalize-space(), 'XOÁ') or contains(normalize-space(), 'Xóa') or contains(normalize-space(), 'Xoá')]"));
                
                // Duyệt ngược từ dưới lên (ưu tiên popup mới nhất vừa sinh ra ở cuối HTML)
                for (int i = btns.size() - 1; i >= 0; i--) {
                    WebElement btn = btns.get(i);
                    // Nếu phát hiện nút ĐANG HIỂN THỊ thì lập tức chộp lấy và trả về
                    if (btn.isDisplayed()) {
                        return btn;
                    }
                }
                // Nếu chưa có nút nào hiển thị (do animation chưa xong), trả về null để Wait tiếp tục lặp lại
                return null; 
            });
            
            // [WAIT] Đợi cái nút hiển thị vừa tìm được sẵn sàng nhận lệnh Click
            wait.until(ExpectedConditions.elementToBeClickable(btnConfirm));
            js.executeScript("arguments[0].click();", btnConfirm);
            // ==========================================================

            // 4. Bấm OK trên popup thông báo thành công (nếu có)
            clickOkPopup();
            System.out.println(">>> Đã xác nhận Xóa thành công trên hệ thống.");
            
        } catch (Exception e) {
            System.err.println("LỖI: Không thể thực hiện Xóa bài tập: " + tenBaiTap);
            e.printStackTrace();
        }
    }
}
    
    
 
   