package User.Pages;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class UserDashboardPage {
    protected WebDriver driver;

    public UserDashboardPage(WebDriver driver) {
        this.driver = driver;
    }

    public By MENU_KHOA_HOC = By.xpath("//div[contains(text(), 'Khoá học của tôi') or contains(text(), 'Khóa học của tôi')]");
    public By MENU_DANG_XUAT = By.xpath("//div[contains(text(), 'Đăng xuất')]");
    public By BTN_LOGOUT_OK = By.xpath("//div[contains(@class, 'v-dialog--active')]//button[.//span[contains(text(), 'OK') or contains(text(), 'Ok')]]");
    public By COURSE = By.xpath("//div[contains(text(), 'Test Nhóm 03')]");
    public By TAB_DIEN_DAN = By.xpath("//div[@role='tab' and contains(.,'Diễn đàn')]");
    public By INPUT_DIEN_DAN = By.xpath("//input[@placeholder='Trả lời diễn đàn']");
    public By BTN_SEND = By.xpath("//button[.//i[contains(@class,'mdi-send')]]");
    public By TAB_VIDEO = By.xpath("//div[@role='tab' and contains(.,'Video conference')]");
    public By BTN_THICH_LATEST = By.xpath("(//div[contains(text(), 'Thích')] | //span[contains(text(), 'Thích')])[last()]");
    public By BTN_THU_HOI_LATEST = By.xpath("(//div[contains(text(), 'Thu hồi')] | //span[contains(text(), 'Thu hồi')])[last()]");
    public By BTN_CONFIRM_OK = By.xpath("//button[.//span[contains(text(), 'OK')] or contains(text(), 'OK')]");
    public By BTN_JOIN_MEET = By.xpath("//*[@id=\"v-main-app\"]/div/div/div[3]/div/div[3]/div/div/div/div[2]/a/span");
    
    public void toggleMyCourses() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement menuKhoaHoc = wait.until(ExpectedConditions.elementToBeClickable(MENU_KHOA_HOC));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", menuKhoaHoc);
        try { Thread.sleep(1500); } catch (InterruptedException e) {}
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", menuKhoaHoc);
        try { Thread.sleep(1000); } catch (InterruptedException e) {}
    }

    public void logout() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement menuDangXuat = wait.until(ExpectedConditions.presenceOfElementLocated(MENU_DANG_XUAT));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", menuDangXuat);
        try { Thread.sleep(500); } catch (InterruptedException e) {}
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", menuDangXuat);
        WebElement btnOk = wait.until(ExpectedConditions.elementToBeClickable(BTN_LOGOUT_OK));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", btnOk);
        
        try { Thread.sleep(2000); } catch (InterruptedException e) {}
    }
    
    public void clickCourse() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(COURSE));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
    }

    public void clickChapterAndAllLessons(JSONObject chapterData) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        
        String chapterName = (String) chapterData.get("ChapterName");
        JSONArray lessons = (JSONArray) chapterData.get("Lessons");

        By chapterLocator = By.xpath("//button[contains(@class, 'v-expansion-panel-header') and contains(., '" + chapterName + "')]");
        WebElement currentHeader = wait.until(ExpectedConditions.elementToBeClickable(chapterLocator));

        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", currentHeader);
        try { Thread.sleep(1000); } catch (InterruptedException e) {}
        
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", currentHeader);
        try { Thread.sleep(2000); } catch (InterruptedException e) {}

        for (int i = 0; i < lessons.size(); i++) {
            String lessonName = (String) lessons.get(i);
            
            By lessonLocator = By.xpath("//div[contains(@class, 'v-expansion-panel-content')]//div[contains(@class, 'white--text') and contains(., '" + lessonName + "')]");
            
            WebElement currentLesson = wait.until(ExpectedConditions.presenceOfElementLocated(lessonLocator));

            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", currentLesson);
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", currentLesson);
            try { Thread.sleep(1500); } catch (InterruptedException e) {}
            
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", currentLesson);
            try { Thread.sleep(500); } catch (InterruptedException e) {}
        }

        currentHeader = wait.until(ExpectedConditions.elementToBeClickable(chapterLocator));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", currentHeader);
        try { Thread.sleep(1000); } catch (InterruptedException e) {}
    }

    public void clickForumTab() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement tab = wait.until(ExpectedConditions.elementToBeClickable(TAB_DIEN_DAN));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", tab);
    }

    public void typeForumAndSend(String text) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        
        WebElement input = wait.until(ExpectedConditions.presenceOfElementLocated(INPUT_DIEN_DAN));
        
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", input);
        try { Thread.sleep(500); } catch (InterruptedException e) {}
        
        wait.until(ExpectedConditions.elementToBeClickable(input)).sendKeys(text);
        
        WebElement sendBtn = wait.until(ExpectedConditions.presenceOfElementLocated(BTN_SEND));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", sendBtn);
    }

    public void clickVideoTab() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement tab = wait.until(ExpectedConditions.elementToBeClickable(TAB_VIDEO));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", tab);
    }
    
    public void clickThichLatestPost() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement btnThich = wait.until(ExpectedConditions.presenceOfElementLocated(BTN_THICH_LATEST));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", btnThich);
        try { Thread.sleep(500); } catch (InterruptedException e) {}
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", btnThich);
    }

    public void clickThuHoiLatestPost() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement btnThuHoi = wait.until(ExpectedConditions.presenceOfElementLocated(BTN_THU_HOI_LATEST));  
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", btnThuHoi);
        try { Thread.sleep(500); } catch (InterruptedException e) {}
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", btnThuHoi);
        WebElement btnOk = wait.until(ExpectedConditions.presenceOfElementLocated(BTN_CONFIRM_OK));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", btnOk);
    }

    public void joinGoogleMeet() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement joinBtn = wait.until(ExpectedConditions.elementToBeClickable(BTN_JOIN_MEET));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", joinBtn);
    }
}