package appmanager;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.BrowserType;

import java.util.concurrent.TimeUnit;

public class ApplicationManager {

    private WebDriver browser;
    private Helper helper;
    private String browserType;
    private Gmail gmail;

    public ApplicationManager(String browserType) {
        this.browserType = browserType;
    }

    public void init() {
        if (browserType.equals(BrowserType.CHROME)) {
            browser = new ChromeDriver();
        } else if (browserType.equals(BrowserType.FIREFOX)) {
            browser = new FirefoxDriver();
        } else if (browserType.equals(BrowserType.IE)) {
            new InternetExplorerDriver();
        }
        browser.manage().window().maximize();
        browser.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        helper = new Helper(browser);
        gmail = new Gmail(browser);
    }

    public void stop() {
        browser.quit();
    }

    public Helper getHelper() {
        return helper;
    }

    public Gmail getGmail() {
        return gmail;
    }
}