package Admin.Pages;

import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class KhoaHocPage {

	WebDriver driver;
    WebDriverWait wait;

    // ===== TAB =====
    private By TAB_NOI_DUNG = By.xpath("//*[@id=\"v-main-app\"]/div/div/div/div[1]/div/div[2]/div/div[3]");

    // ===== TITLE =====
    private By TITLE_PAGE =
            By.xpath("//div[contains(@class,'v-toolbar__title') and contains(text(),'Quản lý khoá học')]");

    // ===== SEARCH =====
    private By TXT_SEARCH =
            By.xpath("//label[text()='Search']/following-sibling::input");

    // ===== BUTTON =====
    private By BTN_THEM_MOI =
            By.xpath("//a[contains(@href,'khoa-hoc/them-moi')]");

    private By BTN_TAI_LAI =
            By.xpath("//button[contains(.,'Tải lại dữ liệu')]");

    private By BTN_THEM_CHUONG =
            By.xpath("//*[@id=\"v-main-app\"]/div/div/div/div[2]/div/div[2]/div/button[2]/span");

    private By BTN_LUU =
            By.xpath("//*[@id=\"v-main-app\"]/div/div/div/div[2]/div/div[2]/div/button[3]/span");

    private By BTN_OK_POPUP =
            By.xpath("//button[contains(@class,'swal2-confirm')]");
    
    private By okBtn = 
    		By.xpath("//button[contains(@class,'swal2-confirm') and text()='OK']");

    // ===== TABLE =====
    private By ROW_KHOA_HOC =
            By.xpath("//table/tbody/tr");

    private By LINK_TIEU_DE =
            By.xpath("//table/tbody/tr/td[2]//a");

    private By BTN_XOA =
            By.xpath("//table/tbody/tr/td[last()]//button");
    
    private static final By INPUT_CAP_NHAT_KHOA_HOC =
    	    By.name("name");

    // ===== FORM THEM KHOA HOC =====
    private By INPUT_TEN_KHOA_HOC =
            By.name("name");

    private By INPUT_MO_TA =
            By.name("summary");

    // ĐÃ SỬA: Lấy locator của icon camera
    private By INPUT_ANH_BIA =
            By.xpath("//button[contains(@class,'mdi-camera')]");

    private By BTN_LUU_THEM =
            By.xpath("//button[@type='submit']");

    private By BTN_OK =
            By.xpath("//button[contains(.,'OK')]");

    // ===== THEM HOC VIEN =====
    private By BTN_THEM_HOC_VIEN =
            By.xpath("//button[contains(.,'Thêm học viên')]");

    private By INPUT_EMAIL_OR_ID =
            By.name("email_or_id_student");

    private By BTN_THEM_HOC_VIEN_CONFIRM =
            By.xpath("//button[@type='submit' and contains(.,'Thêm')]");

    // ===== CHUONG HEADER LAST =====
    private By CHUONG_HEADER_LAST =
            By.xpath("(//div[contains(@class,'v-expansion-panel-header')])[last()]");

    // ===== INPUT LAST =====
    private By TXT_TIEU_DE_LAST =
            By.xpath("(//input[@name='title_course_item'])[last()]");

    private By TXT_MO_TA_LAST =
            By.xpath("(//textarea[@name='description_course_item'])[last()]");
    
    // ==== VIDEO CONFERENCE TAB ====
    private By BTN_VIDEO_CONFERENCE =
            By.xpath("//*[@id=\"v-main-app\"]/div/div/div/div[1]/div/div[2]/div/div[5]");

    // ==== BUTTON ====
    private By BTN_THEM_VIDEO_CONFERENCE =
    		By.xpath("//button[.//span[contains(text(),'Thêm video conference mới')]]");

    private By BTN_HUY_VIDEO_CONFERENCE =
            By.xpath("//*[@id=\"app\"]/div[3]/div/div/form/div[2]/button[1]/span");

    private By BTN_THEM_VIDEO_MEETING =
            By.xpath("//*[@id=\"app\"]/div[3]/div/div/form/div[2]/button[2]/span");

    // ==== INPUT FIELD ====
    private By INPUT_TEN_MEETING =
            By.name("name_video_conference");

    private By INPUT_LINK_MEETING =
            By.name("google_meeting_url");

    // ==== TEXTAREA ====
    private By TEXTAREA_MO_TA_MEETING =
            By.name("des_video_conference");
    
    // ===== TAB =====
    private By TAB_THONG_TIN_MON_HOC =
            By.xpath("//div[contains(text(),'Thông tin môn học')]");

    // ===== TEXTAREA =====
    private By TEXTAREA_MO_TA_KHOA_HOC =
            By.name("summary");

    // ===== BUTTON =====
    private By BTN_CAP_NHAT_KHOA_HOC =
            By.xpath("//button[@type='submit']//span[text()='Cập nhật']");
   
    // ===== POPUP OK =====
    private By OK_POPUP =
            By.xpath("//button[contains(@class,'swal2-confirm')]");

    public KhoaHocPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(20));
    }

    // ================= VERIFY =================

    public boolean isKhoaHocPageDisplayed() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(TITLE_PAGE)).isDisplayed();
    }

    // ================= SEARCH =================

    public void searchKhoaHoc(String keyword) {
        WebElement search = wait.until(ExpectedConditions.visibilityOfElementLocated(TXT_SEARCH));
        search.clear();
        search.sendKeys(keyword);
    }

    // ================= LIST PAGE =================

    public void clickThemMoi() {
        wait.until(ExpectedConditions.elementToBeClickable(BTN_THEM_MOI)).click();
    }

    public void clickTaiLaiDuLieu() {
        wait.until(ExpectedConditions.elementToBeClickable(BTN_TAI_LAI)).click();
    }

    public int getSoLuongKhoaHoc() {
        List<WebElement> rows = driver.findElements(ROW_KHOA_HOC);
        return rows.size();
    }

    public void openKhoaHocDauTien() {
        wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(LINK_TIEU_DE)).get(0).click();
    }

    public void openKhoaHocTheoTen(String ten) {
        By khoaHoc = By.xpath("//table//a[contains(text(),'" + ten + "')]");
        wait.until(ExpectedConditions.elementToBeClickable(khoaHoc)).click();
    }

    public void xoaKhoaHocDauTien() {
        wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(BTN_XOA)).get(0).click();
    }

    // ================= THEM KHOA HOC =================

    public void enterTenKhoaHoc(String ten) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(INPUT_TEN_KHOA_HOC)).sendKeys(ten);
    }

    public void enterMoTa(String moTa) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(INPUT_MO_TA)).sendKeys(moTa);
    }

    // ĐÃ SỬA: Dùng Robot class để thao tác với File Explorer
    public void uploadAnhBia(String path) throws Exception {
        WebElement btnCamera = wait.until(ExpectedConditions.elementToBeClickable(INPUT_ANH_BIA));
        btnCamera.click();

        Robot robot = new Robot();
        robot.delay(2000); 

        StringSelection selection = new StringSelection(path);
        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(selection, null);

        robot.keyPress(KeyEvent.VK_CONTROL);
        robot.keyPress(KeyEvent.VK_V);
        robot.keyRelease(KeyEvent.VK_V);
        robot.keyRelease(KeyEvent.VK_CONTROL);

        robot.delay(1000);

        robot.keyPress(KeyEvent.VK_ENTER);
        robot.keyRelease(KeyEvent.VK_ENTER);
    }

    public void clickLuuThem() {
        wait.until(ExpectedConditions.elementToBeClickable(BTN_LUU_THEM)).click();
    }

    public void clickXacNhan() {
        wait.until(ExpectedConditions.elementToBeClickable(BTN_OK)).click();
    }

    // ================= THEM HOC VIEN =================

    public void clickThemHocVien() {
        wait.until(ExpectedConditions.elementToBeClickable(BTN_THEM_HOC_VIEN)).click();
    }

    public void enterEmailOrIdHocVien(String value) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(INPUT_EMAIL_OR_ID)).sendKeys(value);
    }

    public void clickThemHocVienTrongKhoaHoc() {
        wait.until(ExpectedConditions.elementToBeClickable(BTN_THEM_HOC_VIEN_CONFIRM)).click();
    }
    
    public void toggleTrangThaiHocVien(String maHocVien) throws InterruptedException {

        By checkbox = By.xpath(
            "//tr[td[1][text()='" + maHocVien + "']]//input[@type='checkbox']"
        );

        WebElement cb = driver.findElement(checkbox);

        ((JavascriptExecutor)driver).executeScript("arguments[0].scrollIntoView(true);", cb);

        Thread.sleep(500);

        boolean before = cb.isSelected();

        ((JavascriptExecutor)driver).executeScript("arguments[0].click();", cb);

        Thread.sleep(1000);

        boolean after = cb.isSelected();

        if(before != after){
            System.out.println("TOGGLE SUCCESS - " + maHocVien);
        } else {
            System.out.println("TOGGLE FAIL - " + maHocVien);
        }

        Thread.sleep(2000);
    }
    
    public void xoaHocVien(String maHocVien) {

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        // ===== CLICK ICON XOÁ =====
        By deleteIcon = By.xpath(
            "//tr[td[1][text()='" + maHocVien + "']]//button[.//i[contains(@class,'mdi-close')]]"
        );

        WebElement btn = wait.until(ExpectedConditions.presenceOfElementLocated(deleteIcon));

        ((JavascriptExecutor)driver).executeScript("arguments[0].scrollIntoView(true);", btn);

        wait.until(ExpectedConditions.elementToBeClickable(btn));

        ((JavascriptExecutor)driver).executeScript("arguments[0].click();", btn);

        // ===== CLICK "XOÁ" TRONG POPUP =====
        By confirmDelete = By.xpath("//button[.//span[normalize-space()='Xoá']]");

        WebElement confirmBtn = wait.until(
            ExpectedConditions.elementToBeClickable(confirmDelete)
        );

        ((JavascriptExecutor)driver).executeScript("arguments[0].click();", confirmBtn);

        // ===== CLICK "OK" SWEET ALERT =====
        By okBtn = By.xpath("//button[contains(@class,'swal2-confirm') and text()='OK']");

        WebElement ok = wait.until(
            ExpectedConditions.elementToBeClickable(okBtn)
        );

        ok.click();

        // ===== VERIFY XÓA XONG =====
        wait.until(ExpectedConditions.invisibilityOfElementLocated(deleteIcon));
    }

    // ================= NOI DUNG KHOA HOC =================

    public void openNoiDungTab() {
        wait.until(ExpectedConditions.elementToBeClickable(TAB_NOI_DUNG)).click();
    }

    public void clickThemChuong() {
        WebElement btn = wait.until(ExpectedConditions.presenceOfElementLocated(BTN_THEM_CHUONG));
        scrollToElement(btn);
        jsClick(btn);
    }

    public void clickLastChuong() {
        WebElement chuong = wait.until(ExpectedConditions.presenceOfElementLocated(CHUONG_HEADER_LAST));
        scrollToElement(chuong);
        jsClick(chuong);
    }

    public void enterTieuDeLast(String text) {
        WebElement el = wait.until(ExpectedConditions.visibilityOfElementLocated(TXT_TIEU_DE_LAST));
        el.clear();
        el.sendKeys(text);
    }

    public void enterMoTaLast(String text) {
        WebElement el = wait.until(ExpectedConditions.visibilityOfElementLocated(TXT_MO_TA_LAST));
        el.clear();
        el.sendKeys(text);
    }

    public void clickLuu() {
        WebElement btn = wait.until(ExpectedConditions.elementToBeClickable(BTN_LUU));
        jsClick(btn);
    }

    public void clickOKPopup() {
        WebElement btn = wait.until(ExpectedConditions.elementToBeClickable(BTN_OK_POPUP));
        jsClick(btn);
    }

    public void clickKhoaHocByName(String name) {
        By khoaHoc = By.xpath("//a[contains(text(),'" + name + "')]");
        WebElement el = wait.until(ExpectedConditions.elementToBeClickable(khoaHoc));
        jsClick(el);
    }
    
    public void clickThemBaiHocCuoi() {
        By btn = By.xpath("(//button[.//span[contains(text(),'Thêm bài học')]])[last()]");
        driver.findElement(btn).click();
    }
    
    public void enterTieuDeBaiHocCuoi(String title) {
        By input = By.xpath("(//input[@name='title_course_item'])[last()]");
        driver.findElement(input).sendKeys(title);
    }

    public void enterMoTaBaiHocCuoi(String body) {
        By textarea = By.xpath("(//textarea[@name='description_course_item'])[last()]");
        driver.findElement(textarea).sendKeys(body);
    }
    
 // ===== CHƯƠNG =====
    private By BTN_OPEN_CHUONG_LAST =
        By.xpath("(//div[contains(@class,'v-expansion-panel-header')])[last()]");

    // ===== INPUT CHƯƠNG =====
    private By INPUT_CHUONG_TITLE =
        By.xpath("(//input[@name='title_course_item'])[last()]");

    private By INPUT_CHUONG_BODY =
        By.xpath("(//textarea[@name='description_course_item'])[last()]");

    // ===== BÀI HỌC =====
    private By BTN_THEM_BAI_HOC =
        By.xpath("(//button[.//span[contains(text(),'Thêm bài học')]])[last()]");

    private By BTN_OPEN_BAI_HOC_LAST =
        By.xpath("(//div[contains(@class,'v-expansion-panel-header')])[last()]");

    // ===== INPUT BÀI HỌC =====
    private By INPUT_LESSON_TITLE =
        By.xpath("(//input[@name='title_course_item'])[last()]");

    private By INPUT_LESSON_BODY =
        By.xpath("(//textarea[@name='description_course_item'])[last()]");
    
    public By getChuongByTitle(String title){
        return By.xpath("//div[contains(@class,'v-expansion-panel')]//span[contains(text(),'" + title + "')]");
    }

    // ===== DIEN DAN THAO LUAN =====

    private By BTN_THEM_DIEN_DAN =
    		By.xpath("//button[.//span[contains(text(),'Thêm diễn đàn mới')]]");

    private By INPUT_TEN_DIEN_DAN =
            By.name("name_conversation");

    private By INPUT_MO_TA_DIEN_DAN =
            By.name("des_conversation");

    private By BTN_CHON_ANH =
            By.xpath("//*[@id=\"app\"]/div[3]/div/div/form/div[1]/div/div[2]/div[2]/div[2]");

    private By BTN_THEM_DIEN_DAN_CONFIRM =
    	    By.xpath("//button[@type='submit']//span[normalize-space()='Thêm']");
    
    private By tabDienDanThaoLuan = 
    		By.xpath("//*[@id=\"v-main-app\"]/div/div/div/div[1]/div/div[2]/div/div[4]");
    
    private By BTN_OKMEETING = By.xpath("/html/body/div[2]/div/div[6]/button[1]");
    
    // ================= UTIL =================

    private void scrollToElement(WebElement el) {
        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollIntoView({block:'center'});", el);
    }

    private void jsClick(WebElement el) {
        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].click();", el);
    }
    
    public void clickThemDienDan() {
        wait.until(ExpectedConditions.elementToBeClickable(BTN_THEM_DIEN_DAN)).click();
    }
    
    public void enterTieuDeDienDan(String text) {

        WebElement input = wait.until(ExpectedConditions.visibilityOfElementLocated(INPUT_TEN_DIEN_DAN));

        input.click();        
        input.clear();        
        input.sendKeys(text);

        input.sendKeys(Keys.TAB);
    }
    
    public void enterMoTaDienDan(String text) {

        WebElement textarea = wait.until(ExpectedConditions.visibilityOfElementLocated(INPUT_MO_TA_DIEN_DAN));

        textarea.click();
        textarea.clear();
        textarea.sendKeys(text);

        textarea.sendKeys(Keys.TAB);
    }
    
    public void uploadAnhDienDan(String path) throws Exception {

        wait.until(ExpectedConditions.elementToBeClickable(BTN_CHON_ANH)).click();

        Robot robot = new Robot();
        robot.delay(2000);

        StringSelection selection = new StringSelection(path);
        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(selection, null);

        robot.keyPress(KeyEvent.VK_CONTROL);
        robot.keyPress(KeyEvent.VK_V);

        robot.keyRelease(KeyEvent.VK_V);
        robot.keyRelease(KeyEvent.VK_CONTROL);

        robot.delay(1000);

        robot.keyPress(KeyEvent.VK_ENTER);
        robot.keyRelease(KeyEvent.VK_ENTER);
    }
    
    public void clickThemDienDanConfirm() {

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));

        By btn = By.xpath("//div[contains(@class,'v-dialog')]//button[@type='submit']");

        WebElement button = wait.until(ExpectedConditions.elementToBeClickable(btn));

        // scroll nếu cần
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", button);

        button.click();
    }
    
    public void clickDienDanThaoLuanTab() {
        driver.findElement(tabDienDanThaoLuan).click();
    }
    
    public void clickVideoConferenceTab() {
        driver.findElement(BTN_VIDEO_CONFERENCE).click();
    }

    public void clickThemVideoConference() {
        driver.findElement(BTN_THEM_VIDEO_CONFERENCE).click();
    }

    public void nhapTenMeeting(String ten) {
        driver.findElement(INPUT_TEN_MEETING).sendKeys(ten);
    }

    public void nhapMoTaMeeting(String mota) {
        driver.findElement(TEXTAREA_MO_TA_MEETING).sendKeys(mota);
    }

    public void nhapLinkMeeting(String link) {
        driver.findElement(INPUT_LINK_MEETING).sendKeys(link);
    }

    public void clickThemMeeting() {
        driver.findElement(BTN_THEM_VIDEO_MEETING).click();
    }
    
    public void clickOKMeeTing() {
    	driver.findElement(BTN_OKMEETING).click();
    }
    
    public void clickThongTinMonHocTab() {
        wait.until(ExpectedConditions.elementToBeClickable(TAB_THONG_TIN_MON_HOC)).click();
    }

    public void clickCapNhatKhoaHoc() {
        wait.until(ExpectedConditions.elementToBeClickable(BTN_CAP_NHAT_KHOA_HOC)).click();
    }
    
    // ===== NHAP MO TA KHOA HOC =====
    
    public void enterMoTaKhoaHoc(String moTa) {

        WebElement element = wait.until(
            ExpectedConditions.elementToBeClickable(INPUT_MO_TA)
        );

        element.click();

        // clear
        element.sendKeys(Keys.CONTROL + "a");
        element.sendKeys(Keys.DELETE);

        // nhập lại
        element.sendKeys(moTa);
    }
    
    public void enterTenKhoaHocCapNhat(String value) {

        WebElement input = wait.until(
            ExpectedConditions.elementToBeClickable(INPUT_CAP_NHAT_KHOA_HOC)
        );

        input.click();
        input.sendKeys(Keys.CONTROL + "a");
        input.sendKeys(Keys.DELETE);
        input.sendKeys(value);

        input.sendKeys(Keys.TAB); //QUAN TRỌNG
    }

    // ĐÃ SỬA: Bọc lại hàm uploadAnhBia để dùng lại code có sẵn (throws Exception)
    public void uploadAnhBiaKhoaHoc(String filePath) throws Exception {
        uploadAnhBia(filePath);
    }
    
    // CHƯƠNG HỌC 
 // ===== MỞ CHƯƠNG =====
    public void moChuongCuoi() {
        WebElement el = wait.until(ExpectedConditions.elementToBeClickable(BTN_OPEN_CHUONG_LAST));
        jsClick(el);
    }

    // ===== NHẬP CHƯƠNG =====
    public void nhapChuong(String title, String body) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(INPUT_CHUONG_TITLE)).sendKeys(title);
        wait.until(ExpectedConditions.visibilityOfElementLocated(INPUT_CHUONG_BODY)).sendKeys(body);
    }

    // ===== THÊM BÀI HỌC =====
    public void themBaiHoc() {
        wait.until(ExpectedConditions.elementToBeClickable(BTN_THEM_BAI_HOC)).click();
    }

    // ===== MỞ BÀI HỌC =====
    public void moBaiHocCuoi() {
        WebElement el = wait.until(ExpectedConditions.elementToBeClickable(BTN_OPEN_BAI_HOC_LAST));
        jsClick(el);
    }

    // ===== NHẬP BÀI HỌC =====
    public void nhapBaiHoc(String title, String body) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(INPUT_LESSON_TITLE)).sendKeys(title);
        wait.until(ExpectedConditions.visibilityOfElementLocated(INPUT_LESSON_BODY)).sendKeys(body);
    }
    
 // ===== XÓA CHƯƠNG =====
    private By BTN_XOA_CHUONG_LAST =
        By.xpath("(//button[.//i[contains(@class,'mdi-close')]])[last()]");
    
    public void moChuongTheoTen(String title){
        WebElement el = wait.until(
            ExpectedConditions.elementToBeClickable(getChuongByTitle(title))
        );
        el.click();
    }
    
    public By BTN_XOA_CHUONG = By.xpath("//button[.//span[contains(text(),'Xoá chương học')]]");
    private By BTN_TAI_LAI_DU_LIEU = By.xpath("//*[@id=\"v-main-app\"]/div/div/div/div[2]/div/div[2]/div/button[1]/span");
    
    public void clickTaiLaiDuLieuChuong(){
    	WebElement btnTaiLai = wait.until(ExpectedConditions.elementToBeClickable(BTN_TAI_LAI_DU_LIEU));
    	btnTaiLai.click();
    }

    public void clickXoaChuong(){
        WebElement btn = wait.until(ExpectedConditions.elementToBeClickable(BTN_XOA_CHUONG));
        btn.click();
    }
    
    private By FIRST_CHUONG =
    	    By.xpath("(//div[contains(@class,'v-expansion-panel')])[1]//button[contains(@class,'v-expansion-panel-header')]");

    public void moChuongDauTien() {
    	    WebElement el = wait.until(ExpectedConditions.elementToBeClickable(FIRST_CHUONG));
    	    el.click();
    	}

    public void confirmXoa() {
    	    WebElement ok = wait.until(ExpectedConditions.elementToBeClickable(BTN_OK_POPUP));
    	    ok.click();
    	}
    
    public boolean isConChuong() {
        List<WebElement> list = driver.findElements(
            By.xpath("//div[contains(@class,'v-expansion-panel')]//button[contains(@class,'v-expansion-panel-header')]")
        );

        return list.size() > 0;
    }
    
    // ===== So sánh danh sách học viên thực tế và kết quả mong đợi từ json  =====
    public List<String> getDanhSachMaHocVien() {
        List<String> list = new ArrayList<>();

        List<WebElement> elements = driver.findElements(
            By.xpath("//table//tbody/tr/td[1]")
        );

        for(WebElement e : elements){
            list.add(e.getText().trim());
        }

        return list;
    }
    
 // ===== So sánh diễn đàn thực tế và kết quả mong đợi từ json  =====
    public boolean isDienDanExist(String title) {
        try {
            return driver.findElement(
                By.xpath("//table//td[contains(text(),'" + title + "')]")
            ).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }
    
    public List<String> getDanhSachTenDienDan() {

        List<String> list = new ArrayList<>();

        List<WebElement> elements = driver.findElements(
            By.xpath("//table//tbody/tr/td[1]")
        );

        for(WebElement e : elements){
            String text = e.getText().trim();

            // 🔥 lọc dữ liệu rác
            if(text != null && !text.isEmpty() && !text.equals("-")){
                list.add(text);
            }
        }

        return list;
    }
    
    public List<String> getDanhSachTenMeeting() {

        List<String> list = new ArrayList<>();

        List<WebElement> elements = driver.findElements(
            By.xpath("//table//tbody/tr/td[1]")
        );

        for(WebElement e : elements){
            String text = e.getText().trim();

            if(text != null && !text.isEmpty() && !text.equals("-")){
                list.add(text);
            }
        }

        return list;
    }
    
    // ===== nút comfirm khi nhập nội dung môn học xong sẽ hiển thị popup để chuyển trang  =====
    public void clickConfirmPopupChuyenTrang() {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));

            WebElement btn = wait.until(
                ExpectedConditions.elementToBeClickable(
                    By.xpath("//div[contains(@class,'swal2-popup')]//button[contains(@class,'swal2-confirm')]")
                )
            );

            btn.click();

        } catch (Exception e) {
            System.out.println("Khong co popup chuyen trang");
        }
    }
    
    // thêm video 
    public boolean isMeetingExist(String tenMeeting, String link) {
        try {
            return driver.findElement(
                By.xpath("//table//tr[td[contains(text(),'" + tenMeeting + "')] and td[contains(text(),'" + link + "')]]")
            ).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }
    
    public String getTenKhoaHoc() {
        WebElement input = wait.until(
            ExpectedConditions.visibilityOfElementLocated(
                By.name("name")
            )
        );

        return input.getAttribute("value");
    }
    
    public String getMoTaKhoaHoc() {
        WebElement textarea = new WebDriverWait(driver, Duration.ofSeconds(5))
            .until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//textarea")
            ));

        return textarea.getAttribute("value");
    }
    
    public void xoaChuongCuoi() {
        WebElement btn = wait.until(ExpectedConditions.elementToBeClickable(BTN_XOA_CHUONG_LAST));
        jsClick(btn);
    }
}