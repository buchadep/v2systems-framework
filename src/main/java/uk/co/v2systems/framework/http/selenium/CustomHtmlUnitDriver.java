package uk.co.v2systems.framework.http.selenium;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
/**
 * Created by PBU10 on 14/10/2015.
 */

public class CustomHtmlUnitDriver {
    HtmlUnitDriver driver;
    String url;

    public CustomHtmlUnitDriver(){
        driver=new HtmlUnitDriver();
    }
    public void setUrl(String url) {
        this.url = url;
    }
    public void openUrl(){
        driver.get(url);
        System.out.println(driver.getTitle());
    }

    public void getPageSource(){
        System.out.println(driver.getPageSource());
    }

    public void destory(){
        driver.close();
    }

}