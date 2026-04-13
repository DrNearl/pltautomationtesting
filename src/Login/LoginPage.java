package Login;

import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;

public class LoginPage {
	private WebDriver driver;
	
	private By USERNAME_INPUT = By.id("input-10");
	private By PASSWORD_INPUT = By.id("input-13");
	private By SIGNIN_BUTTON = By.xpath("//*[@id=\"app\"]/div/main/div/div[1]/div/div[3]/form/div[2]/button");
	
	public LoginPage(WebDriver driver) {
        this.driver = driver;
	}
	
    public void openLoginPage() {
        driver.get("https://elearning.plt.pro.vn/");
    }
    
    public void enterUsername(String username) {
        driver.findElement(USERNAME_INPUT).sendKeys(username);
    }

    public void enterPassword(String password) {
        driver.findElement(PASSWORD_INPUT).sendKeys(password);
    }

    public void clickLogin() {
        driver.findElement(SIGNIN_BUTTON).click();
    }

    public void login(String username, String password) {
        enterUsername(username);
        enterPassword(password);
        clickLogin();
    }
    
}
