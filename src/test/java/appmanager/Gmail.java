package appmanager;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;

import java.util.concurrent.TimeUnit;

public class Gmail extends Helper implements Details {

    public Gmail(WebDriver browser) {
        super(browser);
    }

    public void gmailSignIn() throws InterruptedException {
        goTo("https://mail.google.com/");
        sendKeys(By.cssSelector("input#identifierId"), googleLogin);
        click(By.xpath("//*[@id=\"identifierNext\"]//*[@class=\"RveJvd snByac\"]"));
        getDriver().manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        Thread.sleep(2000);
        sendKeys(By.cssSelector("input[name=\"password\"]"), googlePassword + "\n");
        getDriver().manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);
        Assert.assertTrue(elementExists(By.xpath("//*[contains(text(),'Smal Yurii')]")), "Fail");
        System.out.println("Login Ok");
    }

    public void createGmailLetter() throws InterruptedException {
        getDriver().manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        clickIfAvail(By.xpath("//*[@class=\"Ls77Lb aZ6\"]//*[@class=\"aic\"]//*[@role=\"button\"]"));
        getDriver().manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        sendKeys(By.xpath("//*[@class=\"wO nr l1\"]//*[@name=\"to\"]"), tempMail);
        getDriver().manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        writeSavedLetter(By.xpath("//*[@style=\"display: block;\"]//*[@aria-label=\"Message Body\"]"));
        Thread.sleep(2000);
        System.out.println("Letter created!");
    }

    public void sendGmailLetter() throws InterruptedException {
        click(By.xpath("//*[@class=\"dC\"]//*[contains(text(),'Send')]"));
        Thread.sleep(5000);
        click(By.xpath("//*[@data-tooltip=\"Sent\"]//*[contains(text(),'Sent')]"));
        Assert.assertTrue(
                elementExists(By.xpath("//*[@data-hovercard-id=\"" + tempMail + "\"]")), "Fail");
        System.out.println("Letter Sent ");
    }
}
