package appmanager;

import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.openqa.selenium.*;
import org.testng.Assert;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;


public class Helper {

    private String tag = "a";
    public ArrayList<String> arrTagsFirst = new ArrayList<>();
    public static ArrayList<String> letter;
    public static String currentUrl;
    public static String tempMail;
    public String[] form = {".jpg", ".mp4", ".jpeg", ".gif", ".png"};
    public int end = 0;
    public String randomCat = getUrlFromAnswer(getAnswer("https://aws.random.cat/meow"));
    public String randomDog = getUrlFromAnswer(getAnswer("https://random.dog/woof.json"));
    public String randomFox = getUrlFromAnswer(getAnswer("https://randomfox.ca/floof/"));


    private WebDriver browser;

    public Helper(WebDriver browser) {
        this.browser = browser;
    }

    public boolean findElement(By selector) {
        browser.findElement(selector);
        return true;
    }

    public void click(By selector) {
        browser.findElement(selector).click();
    }

    public void sendKeys(By selector, String text) {
        browser.findElement(selector).sendKeys(text);
    }

    public boolean elementExists(By selector) {
        return browser.findElements(selector).size() != 0;
    }

    public boolean elementDisplayed(By selector) {
        return browser.findElement(selector).isDisplayed();
    }

    public void waiting(int i) {
        browser.manage().timeouts().implicitlyWait(i, TimeUnit.SECONDS);
    }

    public void goTo(String url) {
        browser.get(url);
    }

    public void findElements(By locator) {
        browser.findElements(locator);
    }

    public void elementIsClickable(By selector) {
        browser.findElement(selector).isEnabled();
    }

    public String getText(By selector) {
        String text = browser.findElement(selector).getText();
        return text;
    }

    public List<String> getProductsList(By selector) {
        List<String> ar = new ArrayList<String>();
        List<WebElement> elements = browser.findElements(selector);
        for (WebElement element : elements) {
            String name = element.getText();
            ar.add(name);
        }
        return ar;
    }

    public String fileReader(String path) throws IOException {
        FileReader fr = new FileReader(path);
        Scanner scanner = new Scanner(fr);
        int i = 1;
        for (; scanner.hasNext(); i++) {
            String url = scanner.nextLine();
            return url;
        }
        return scanner.nextLine();
    }

    public int getResponse(String link) throws IOException {
        URL url = new URL(link);
        HttpURLConnection openConnection = (HttpURLConnection) url.openConnection();
        openConnection.connect();
        int rCode = openConnection.getResponseCode();
        return rCode;
    }

    public long loadTime(String url) {
        long start = System.currentTimeMillis();
        browser.get(url);
        long finish = System.currentTimeMillis();
        long totalTime = finish - start;
        return totalTime;
    }

    public String getAnswer(String link) {
        StringBuilder answer = new StringBuilder();
        String line;
        try {
            URL url = new URL(link);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setUseCaches(false);

            int code = connection.getResponseCode();

            if (code == HttpURLConnection.HTTP_OK) {
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8));
                while ((line = reader.readLine()) != null) {
                    answer.append(line);
                }
                reader.close();
                return answer.toString();
            }
            connection.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return String.valueOf(answer);
    }

    public String getUrlFromAnswer(String loc) {
        int begin = loc.contains("http:") ? loc.indexOf("http:") : loc.indexOf("https:");

        for (String s : form) {
            if (loc.toLowerCase().contains(s)) {
                end = loc.toLowerCase().indexOf(s) + s.length();
                break;
            }
        }
        return loc.substring(begin, end)
                .replace("\\", "");
    }

    public void createLetter(String... text) {
        letter = new ArrayList<>(Arrays.asList(text));
    }

    public WebDriver getDriver() {
        return browser;
    }

    public void writeSavedLetter(By locator) {
        for (String s : letter) {
            browser.findElement(locator).sendKeys(s + "\n");
        }
    }

    public void clickIfAvail(By selector) throws InterruptedException {
        if (!browser.findElement(selector).isEnabled()) {
            Thread.sleep(5000);
            browser.findElement(selector).click();
        } else {
            browser.findElement(selector).click();
        }
    }

    private void waitElementAndClick(By locator) throws InterruptedException {
        int count = 0;
        while (!elementExists(locator)) {
            Thread.sleep(5000);
            browser.navigate().refresh();
            count++;
            System.out.println("Try: " + count);
            if (count >= 10) {
                break;
            }
        }
        Assert.assertTrue(findElement(locator));
    }

    public void checkLinks(ArrayList<String> urls) throws InterruptedException {
        int count = 0;
        WebElement iFrame = browser.findElement(By.id("idIframe"));
        browser.switchTo().frame(iFrame);
        for (String s : urls) {
            if (getText(By.xpath("//*[@dir=\"ltr\"]")).contains(s)) {
                count++;
                System.out.println(s + " - Ok");
            } else
                System.out.println(s + " NOt Ok");
        }
        Assert.assertEquals(count, letter.size(), "Something went wrong!");
        System.out.println("Letter Ok!");
    }

    public void makeRandomLetter() {
        createLetter(randomDog, randomCat, randomFox);
        Assert.assertNotNull(letter);
        System.out.println(letter);
    }

    public void getNadaMail() throws InterruptedException {
        goTo("https://getnada.com");
        Thread.sleep(1000);
        tempMail = getText(By.xpath("//*[@id=\"app\"]//*[@class=\"address what_to_copy\"]"));
        Assert.assertNotNull(tempMail, "Fail");
        System.out.println(tempMail);
    }

    public String getCurrentUrl() {
        return currentUrl = browser.getCurrentUrl();
    }

    public void parseHtml(String url) throws IOException {
        Document document = pagePars(url);
        checkPageTags(arrTagsFirst, tag, document);
        int count = 0;
        for (int a = 0; a < letter.size(); a++) {

            for (int i = 0; i < arrTagsFirst.size(); i++) {

                if (arrTagsFirst.get(i).contains(letter.get(a))) {
                    System.out.println(url + " contains letter with sent link " + letter.get(a));

                } else {
                    System.out.println("Not find");
                }
            }
        }
        Assert.assertEquals(count, letter.size(), "Not equal!");
    }

    private static void checkPageTags(ArrayList<String> arrTagsFirst, String el, Document document) {
        for (Element paragraph : document.getElementsByTag(el)) {
            arrTagsFirst.add(paragraph.getElementsByTag(el).toString());
        }
    }

    private static Document pagePars(String url) throws IOException {
        return Jsoup.connect(url).timeout(20000).get();
    }

    public void waitLetter() throws InterruptedException {
        goTo("https://getnada.com");
        waitElementAndClick(By.xpath("//*[@class=\"msg_item\"]//*[@data-letters=\"SY\"]"));
        Assert.assertTrue(elementExists(By.xpath("//*[@class=\"msg_item\"]//*[@data-letters=\"SY\"]")));
        click(By.xpath("//*[@class=\"msg_item\"]//*[@data-letters=\"SY\"]"));
        System.out.println("Mail find!");
        Thread.sleep(5000);
    }

    public void makeScreen(ArrayList<String> urls, String name) throws InterruptedException {
        int count = 1;
        for (String url : urls) {
            try {
                goTo(url);
                Thread.sleep(2000);
                File scrFile = ((TakesScreenshot) browser).getScreenshotAs(OutputType.FILE);
                FileUtils.copyFile(scrFile, new File("Screens\\screenshot." + name + count + ".png"));
                count++;
            } catch (IOException e) {
                System.out.println("Something went wrong");
            }
            System.out.println("Screen created. Open Screen folder in project for watch!");
        }
    }
}