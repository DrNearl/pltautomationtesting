package Admin.Pages;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.JavascriptExecutor;
import java.time.Duration;

public class HocVienPage {

	private WebDriver driver;

	// ===== Button =====
	private By BTN_THEM_MOI = By.xpath("//header//button[1]");
	private By BTN_LUU_THEM = By.xpath("/html/body/div/div/div[3]/div/div/form/div[2]/button[2]/span");
	private By BTN_SUA_CHINH = By.xpath("//*[@id=\"app\"]/div[4]/div/div/form/div[2]/button[2]/span");
	private By BTN_HUY = By.xpath("//button[@type='button' and .//span[text()='Huỷ']]");

	// ===== Input text =====
	private By TXT_HO_TEN = By.name("full_name");
	private By TXT_MA_HOC_VIEN = By.name("student_code");
	private By TXT_EMAIL = By.name("email");
	private By TXT_SO_DIEN_THOAI = By.name("phone");
	private By TXT_DIA_CHI = By.name("address");
	private By TXT_NGAY_SINH = By.name("dob");

	// ===== Radio giới tính =====
	private By RADIO_NAM = By.xpath("//label[text()='Nam']");
	private By RADIO_NU = By.xpath("//label[text()='Nữ']");
	private By RADIO_KHAC = By.xpath("//label[text()='Khác']");

	private By BTN_OK = By.xpath("/html/body/div[2]/div/div[6]/button[1]");

	// button xoá học viên dòng đầu tiên
	private By BTN_XOA_HOC_VIEN = By
			.xpath("//*[@id='v-main-app']/div/div/div/div[1]/div[1]/table/tbody/tr[1]/td[10]/button[2]");

	// nút xác nhận xoá
	private By BTN_XOA = By.xpath("//div[contains(@class, 'v-dialog--active')]//button[.//span[contains(text(), 'Xoá') or contains(text(), 'Xóa')]]");

	// button sửa học viên dòng đầu tiên
	private By BTN_SUA_HOC_VIEN = By
			.xpath("//*[@id='v-main-app']/div/div/div/div[1]/div[1]/table/tbody/tr[1]/td[10]/button[1]");

	// ô tìm kiếm học viên
	private By TXT_TIM_KIEM = By.xpath("//*[@id='v-main-app']/div/div/div/div[1]/header/div/div[3]//input");

	// So sánh học viên sau khi nhập được xuất hiện ở ô đầu tiên
	private By searchBox = By.xpath("//*[@id='v-main-app']//header//input[@type='text']");
	By maHocVien = By.xpath("//tbody/tr[1]/td[1]");
	By ho = By.xpath("//tbody/tr[1]/td[2]");
	By ten = By.xpath("//tbody/tr[1]/td[3]");
	By email = By.xpath("//tbody/tr[1]/td[5]");
	By ngaySinh = By.xpath("//tbody/tr[1]/td[6]");
	By diaChi = By.xpath("//tbody/tr[1]/td[8]/div");

	private By TOAST_THONG_BAO = By.id("swal2-title");

	public HocVienPage(WebDriver driver) {
		this.driver = driver;
	}

	private void clearAndType(By locator, String value) {
		WebElement element = driver.findElement(locator);
		element.click();
		element.sendKeys(Keys.CONTROL + "a");
		element.sendKeys(Keys.DELETE);
		element.sendKeys(value);
	}

	// ===== Actions =====
	public void clickThemMoi() {
		driver.findElement(BTN_THEM_MOI).click();
	}

	public void enterHoTen(String value) {
		clearAndType(TXT_HO_TEN, value);
	}

	public void enterMaHocVien(String value) {
		clearAndType(TXT_MA_HOC_VIEN, value);
	}

	public void enterEmail(String value) {
		clearAndType(TXT_EMAIL, value);
	}

	public void enterSoDienThoai(String value) {
		clearAndType(TXT_SO_DIEN_THOAI, value);
	}

	public void enterDiaChi(String value) {
		clearAndType(TXT_DIA_CHI, value);
	}

	public void enterNgaySinh(String yyyyMMdd) {
		clearAndType(TXT_NGAY_SINH, yyyyMMdd);
	}

	public void selectGioiTinhNam() {
		driver.findElement(RADIO_NAM).click();
	}

	public void selectGioiTinhNu() {
		driver.findElement(RADIO_NU).click();
	}

	public void selectGioiTinhKhac() {
		driver.findElement(RADIO_KHAC).click();
	}

	public void clickLuuThem() {
	    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
	    WebElement btnLuu = wait.until(ExpectedConditions.elementToBeClickable(BTN_LUU_THEM));
	    ((JavascriptExecutor) driver).executeScript("arguments[0].click();", btnLuu);
	}

	public void clickOK() {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		wait.until(ExpectedConditions.elementToBeClickable(BTN_OK));
		driver.findElement(BTN_OK).click();
	}

	public void clickXoaHocVien() {
		driver.findElement(BTN_XOA_HOC_VIEN).click();
	}

	public void confirmXoa() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement btnXoa = wait.until(ExpectedConditions.presenceOfElementLocated(BTN_XOA));  
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", btnXoa);
    }

	public void clickSuaHocVien() {
		driver.findElement(BTN_SUA_HOC_VIEN).click();
	}

	public void searchHocVien(String keyword) {
		clearAndType(TXT_TIM_KIEM, keyword);
		driver.findElement(TXT_TIM_KIEM).sendKeys(Keys.ENTER);
	}

    // ==== Hàm mới: Xóa trống ô tìm kiếm và Enter để reset bảng ====
    public void clearSearchBox() {
        WebElement element = driver.findElement(TXT_TIM_KIEM);
        element.click();
        element.sendKeys(Keys.CONTROL + "a");
        element.sendKeys(Keys.DELETE);
        element.sendKeys(Keys.ENTER);
        try { Thread.sleep(500); } catch (InterruptedException e) {}
    }

	// Nhập vào cột tìm học viên
	By tableRows = By.xpath("//tbody/tr");

	public boolean isSearchResultDisplayed() {
		return driver.findElements(tableRows).size() > 0;
	}

	// Hiển thị trang học của admin
	public boolean isHocVienPageDisplayed() {
		return driver.getCurrentUrl().contains("/quan-tri-vien/hoc-vien");
	}

	public String getHoTen() {
		String ho = driver.findElement(this.ho).getText();
		String ten = driver.findElement(this.ten).getText();
		return ho + " " + ten;
	}

	public String getMaHocVien() {
		return driver.findElement(maHocVien).getText();
	}

	public String getEmail() {
		return driver.findElement(email).getText();
	}

	public String getNgaySinh() {
		return driver.findElement(ngaySinh).getText();
	}

	public String getDiaChi() {
		return driver.findElement(diaChi).getText();
	}

	public String getThongBaoThanhCong() {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
		return wait.until(ExpectedConditions.visibilityOfElementLocated(TOAST_THONG_BAO)).getText();
	}

	public void clickThem() {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		wait.until(ExpectedConditions.elementToBeClickable(BTN_LUU_THEM)).click();
	}

	public void clickSuaChinh() {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		wait.until(ExpectedConditions.elementToBeClickable(BTN_SUA_CHINH)).click();
	}

}