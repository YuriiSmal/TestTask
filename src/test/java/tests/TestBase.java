package tests;

import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import appmanager.ApplicationManager;

import javax.swing.*;

public class TestBase {

    public static final ApplicationManager appMan = new ApplicationManager("chrome");

    @BeforeSuite
    public void setBrowser() {
        appMan.init();
    }

    @AfterSuite
    public void closeBrowser() throws InterruptedException {
        appMan.stop();
        JOptionPane.showMessageDialog(null, "Test already done");
    }
}
