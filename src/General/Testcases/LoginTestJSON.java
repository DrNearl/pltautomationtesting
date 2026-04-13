package General.Testcases;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.*;

import Login.LoginPage;
import io.github.bonigarcia.wdm.WebDriverManager;
import Utils.DocFileJSON;

public class LoginTestJSON {

    WebDriver driver;
    LoginPage loginPage;

    @BeforeClass
    public void setup() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().window().maximize();

        loginPage = new LoginPage(driver);
        loginPage.openLoginPage();
    }

    @Test
    public void loginWithJSON() {

        JSONArray data = DocFileJSON.docDuLieuJSON("resources/loginData.json");

        for (Object obj : data) {
            JSONObject user = (JSONObject) obj;

            String username = (String) user.get("username");
            String password = (String) user.get("password");

            loginPage.login(username, password);

            // nếu cần test nhiều account thì nên viết thêm hàm logout ở đây
        }
    }

    @AfterClass
    public void teardown() {
//        driver.quit();
    }
}