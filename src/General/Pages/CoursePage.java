package General.Pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class CoursePage {

    WebDriver driver;
    WebDriverWait wait;

    public CoursePage(WebDriver driver) {
        this.driver = driver;
        wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    public void openHomePage() {
        driver.get("https://elearning.plt.pro.vn/trang-chu");
    }

    public void openCourse(String courseName) throws InterruptedException {

        WebElement courseTitle = wait.until(ExpectedConditions.presenceOfElementLocated(
                By.xpath("//*[contains(text(), '" + courseName + "')]")
        ));

        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", courseTitle);
        Thread.sleep(1000);

        try {
            courseTitle.click();
        } catch (Exception ex) {
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", courseTitle);
        }

        Thread.sleep(3000);
    }

    public WebElement findChapter(String chapterShortName) {
        return driver.findElement(By.xpath("//*[contains(text(), '" + chapterShortName + "')]"));
    }

    public void openChapter(WebElement chapterElement) throws InterruptedException {

        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", chapterElement);

        Thread.sleep(500);

        try {
            chapterElement.click();
        } catch (Exception ex) {
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", chapterElement);
        }

        Thread.sleep(2000);
    }

    public WebElement findLesson(String lessonKey) {
        return driver.findElement(By.xpath("//*[contains(text(), '" + lessonKey + "')]"));
    }
}
