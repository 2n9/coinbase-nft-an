package yuu.dc;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import javax.mail.MessagingException;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) throws IOException {
        System.setProperty("webdriver.chrome.driver", "./exe/chromedriver.exe");

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(4000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                //alive
            }
        }).start();

        List<nth> nth = new ArrayList<>();
        nth.add(new nth("test.txt", "dummy"));
        System.out.println("Start " + nth.size() + "s threads");
        nth.forEach(Thread::start);

    }

}
