package General.Testcases;

import General.Pages.CoursePage;
import Login.LoginPage;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import org.testng.annotations.*;
import org.testng.asserts.SoftAssert;

import java.util.*;

public class Compare {

    WebDriver driver;

    LoginPage loginPage;
    CoursePage coursePage;

    String LOGIN_URL = "https://elearning.plt.pro.vn/login";
    String USERNAME = "test1.pltsolutions@gmail.com";
    String PASSWORD = "plt@intern_051224";

    Map<String, List<String>> expectedCourseData = new LinkedHashMap<>();

    @BeforeClass
    public void setup() throws Exception {

        System.setProperty("webdriver.chrome.driver", "c:\\chromedriver.exe");

        driver = new ChromeDriver();
        driver.manage().window().maximize();

        loginPage = new LoginPage(driver);
        coursePage = new CoursePage(driver);

        expectedCourseData.put("Chương 1: HTML cơ bản",
                Arrays.asList(
                        "Bài số 1: ổng quan về Website và Internet",
                        "Bài số 2: Giới thiệu HTML",
                        "Bài số 3: Các thẻ văn bản và hình ảnh",
                        "Bài số 4: Danh sách và bảng",
                        "Bài số 5: Form và nhập liệu"
                ));

        expectedCourseData.put("Chương 2: CSS và thiết kế giao diện",
                Arrays.asList(
                        "Bài số 1: Giới thiệu CSS",
                        "Bài số 2: Màu sắc và font chữ",
                        "Bài số 3: Bố cục với Flexbox",
                        "Bài số 4: Responsive với Media Query"
                ));

        loginPage.openLoginPage();
        loginPage.login(USERNAME, PASSWORD);
    }

    @Test
    public void testCourseStructure() throws Exception {

        SoftAssert softAssert = new SoftAssert();

        Thread.sleep(3000);

        coursePage.openHomePage();

        Thread.sleep(2000);

        String courseName = "Lập trình Web cơ bản";

        coursePage.openCourse(courseName);

        for (String expectedChapterName : expectedCourseData.keySet()) {

            String chapterShortName = expectedChapterName.split(":")[0];

            WebElement chapterElement;

            try {
                chapterElement = coursePage.findChapter(chapterShortName);
            } catch (Exception e) {
                continue;
            }

            coursePage.openChapter(chapterElement);

            List<String> expectedLessons = expectedCourseData.get(expectedChapterName);

            for (String lessonName : expectedLessons) {

                String lessonKey = lessonName.split(":")[0];

                try {

                    WebElement lessonEl = coursePage.findLesson(lessonKey);

                    if (lessonEl.isDisplayed()) {

                        String actualText = lessonEl.getText();

                        softAssert.assertTrue(actualText.contains(lessonKey));

                    } else {

                        chapterElement.click();
                        Thread.sleep(1000);
                    }

                } catch (Exception e) {

                    softAssert.fail("Thiếu bài học: " + lessonName + " trong chương " + expectedChapterName);
                }
            }
        }

        softAssert.assertAll();
    }

    @AfterClass
    public void tearDown() {
        if (driver != null)
            driver.quit();
    }
}
