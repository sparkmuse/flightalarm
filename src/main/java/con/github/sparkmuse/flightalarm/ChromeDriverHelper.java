package con.github.sparkmuse.flightalarm;

import con.github.sparkmuse.flightalarm.entity.Proxy;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class ChromeDriverHelper {

    public static ChromeDriver getDriver(Proxy proxy) {
        return ChromeDriverHelper.getDriver(proxy.getAddress());
    }

    public static ChromeDriver getDriver(String fullHostAddress) {
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--proxy-server=" + fullHostAddress);
        options.addArguments("--incognito", "--window-size=1920,1080");
        return  new ChromeDriver(options);
    }

    public static ChromeDriver getDriver() {
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--incognito", "--window-size=1920,1080");
        return new ChromeDriver(options);
    }
}