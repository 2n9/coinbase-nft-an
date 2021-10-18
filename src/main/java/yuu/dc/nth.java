package yuu.dc;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import javax.mail.*;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeUtility;
import javax.mail.search.FlagTerm;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class nth extends Thread implements Runnable {

    List<String> mails;
    String mailUrl;

    public nth(String std, String mailUrl) throws IOException {
        mails = new ArrayList<>();
        this.mailUrl = mailUrl;
        File f = new File(std);
        BufferedReader br = new BufferedReader(new FileReader(f));
        String line = br.readLine();
        while (line != null) {
            mails.add(line);
            line = br.readLine();
        }
        br.close();
    }

    @Override
    public void run() {
        try {
            WebDriver driver = new ChromeDriver();

            int c = 0;
            for (String mail : mails) {
                c++;
                driver.get("https://coinbase.com/nft/announce/DUMMY");

                Thread.sleep(5000);

                List<WebElement> elements = driver.findElements(By.tagName("button"));

                for (WebElement element : elements) {
                    if(element.getAttribute("class").equals("sc-AxjAm kpdUcq")){
                        element.click();
                        Thread.sleep(1000);
                        break;
                    }
                }

                elements = driver.findElements(By.tagName("input"));
                for (WebElement element : elements) {
                    if (element.getAttribute("id").equals("waitlist_email")) {
                        element.click();
                        element.sendKeys(mail);
                        Thread.sleep(1000);
                    }

                    if (element.getAttribute("id").equals("wantsEmails")) {
                        element.click();
                        Thread.sleep(1000);
                    }
                }

                elements = driver.findElements(By.tagName("button"));
                for (WebElement element : elements) {
                    if (element.getAttribute("class").equals("cds-interactable-i13lggcw cds-interactableBackground-i1tfznye cds-transparentChildren-t13p63z cds-transparent-t1iirkex cds-standard-sy7al66 cds-button-b1psflw0 cds-focusRing-f1r6m9tt cds-scaledDownState-s1v6pvyx cds-primaryForeground-progmjq cds-button-bxm6m07 cds-2-_176ib6b cds-2-_n1dzqr cds-3-_1s99az2 cds-3-_1f9iorr")) {
                        element.click();
                        System.out.println("[#" + this.getId() + "] " + mail + " register ( " + c + " / " + mails.size() + " )");
                    }
                }

                Thread.sleep(30000);

                String html = nth.getMails(mailUrl, 993, mail, "DUMMY");
                System.out.println("[#" + this.getId() + "] " + mail + " connect mail server ( " + c + " / " + mails.size() + " )");
                if (html == null) {
                    while (html == null){
                        Thread.sleep(10000);
                        html = nth.getMails(mailUrl, 993, mail, "DUMMY");
                        System.out.println("[#" + this.getId() + "] " + mail + " retrying connect mail server ( " + c + " / " + mails.size() + " )");
                    }
                }
                String verifyUrl = nth.getVerify(html);
                System.out.println("[#" + this.getId() + "] " + mail + " challenge url verify ( " + c + " / " + mails.size() + " )");
                driver.get(verifyUrl);
                Thread.sleep(20000);
                System.out.println("[#" + this.getId() + "] " + mail + " verified ( " + c + " / " + mails.size() + " )");

                Thread.sleep(30000);
            }

            driver.close();
        } catch (Exception exp) {
            // catch all
        }
    }

    public static String getVerify(String html) throws MessagingException, UnsupportedEncodingException {
        Pattern pattern = Pattern.compile("(http://|https://){1}[\\w\\.\\-/:\\#\\?\\=\\&\\;\\%\\~\\+]+('>Click here to verify your email.)", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(html);
        String url = null;
        while (matcher.find()) {
            url = matcher.group().replaceAll("'>Click here to verify your email.", "");
        }
        return url;
    }

    public static String getMails(String url, int port, String user, String pass) throws MessagingException, IOException {
        Properties props = System.getProperties();

        Session session = Session.getInstance(props, null);

        Store imap4 = session.getStore("imaps");

        imap4.connect(user.split("@")[1].split("\\.")[0] + "DUMMY", port, user, pass);

        Folder folder = imap4.getFolder("INBOX");
        UIDFolder uf = (UIDFolder) folder;
        folder.open(Folder.HOLDS_FOLDERS);

        Message[] msgs = folder.getMessages();
        for (Message msg : msgs) {

            Long messageId = uf.getUID(msg);
            String subjectText = MimeUtility.decodeText(msg.getSubject());
            if(subjectText.equalsIgnoreCase("Please verify your email to join the waitlist")) {
                Message msg1 = uf.getMessageByUID(messageId);
                Part part = msg1;
                String bodyText = (String) part.getContent();
                folder.close();
                imap4.close();
                return bodyText;
            }

        }

        return null;
    }
}
