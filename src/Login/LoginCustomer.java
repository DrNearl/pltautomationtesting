package Login;


import org.testng.annotations.Test;
import org.testng.annotations.BeforeClass;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import Admin.Pages.AdminDashboardPage;
import io.github.bonigarcia.wdm.WebDriverManager;

public class LoginCustomer {

    WebDriver driver;
    LoginPage loginPage;
    AdminDashboardPage adminDashboardPage;

    @BeforeClass
    public void setup() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        loginPage = new LoginPage(driver);
    }

    @Test
    public void Login_Customer() throws InterruptedException {

        loginPage.openLoginPage();
        loginPage.login("test.pltsolutions1@gmail.com", "plt@intern_051224");
        
        Thread.sleep(5000);
        System.out.println(driver.getPageSource());

        adminDashboardPage = new AdminDashboardPage(driver);

        Assert.assertTrue(adminDashboardPage.isDashboardDisplayed());
    }

	@AfterClass
    public void tearDown() {
        driver.quit();
    }
}
