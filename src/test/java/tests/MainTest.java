package tests;

import appmanager.Helper;
import org.testng.annotations.Test;

import java.io.IOException;

public class MainTest extends TestBase {

    //Make random links for letter
    @Test
    public void makeLetter() {
        appMan.getHelper().makeRandomLetter();
    }

    //Open GetNada and save time mail
    @Test(dependsOnMethods = {"makeLetter"})
    public void makeTimeMail() throws InterruptedException {
        appMan.getHelper().getNadaMail();
    }

    //Gmail login
    @Test(dependsOnMethods = {"makeTimeMail"})
    public static void loginToGmail() throws InterruptedException {
        appMan.getGmail().gmailSignIn();
    }

    //Create letter
    @Test(dependsOnMethods = {"loginToGmail"})
    public void createLetter() throws InterruptedException {
        appMan.getGmail().createGmailLetter();
    }

    //Send letter
    @Test(dependsOnMethods = {"createLetter"})
    public void sendLetter() throws InterruptedException {
        appMan.getGmail().sendGmailLetter();
    }

    //Check time mail
    @Test(dependsOnMethods = {"sendLetter"})
    public void waitLetterOnTimeMail() throws InterruptedException {
        appMan.getHelper().waitLetter();
    }

    //Check that letter correct
    @Test(dependsOnMethods = {"waitLetterOnTimeMail"})
    public void checkLetterOnTimeMail() throws InterruptedException {
        appMan.getHelper().checkLinks(Helper.letter);
    }

    //Make Screens
    @Test(dependsOnMethods = {"sendLetter"})
    public void makeScreen() throws InterruptedException {
        appMan.getHelper().makeScreen(Helper.letter, "Screen");
    }
}
