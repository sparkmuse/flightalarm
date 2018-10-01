package con.github.sparkmuse.flightalarm.service;

import con.github.sparkmuse.flightalarm.ChromeDriverHelper;
import con.github.sparkmuse.flightalarm.entity.Proxy;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.stereotype.Service;
import sun.plugin2.message.helper.ProxyHelper;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProxyService {

    private static final String URL = "https://www.proxynova.com/proxy-server-list/country-de/";

    private ChromeDriver driver;

    public Optional<Proxy> getAGoodAddress() {

        driver = ChromeDriverHelper.getDriver();

        driver.get(URL);
        Document document = Jsoup.parse(driver.getPageSource());

        Elements rows = document.getElementsByAttribute("data-proxy-id");

        List<Proxy> proxies = new ArrayList<>();
        for (Element element : rows) {

            Elements cells = element.getElementsByTag("td");
            Proxy p = new Proxy(
                    cells.get(0).text(),
                    cells.get(1).text(),
                    cells.get(6).text(),
                    new Integer(cells.get(3).text().replace(" ms", "")));
            proxies.add(p);
        }

        driver.close();
        driver.quit();

        List<Proxy> idealProxies = proxies
                .stream()
                .filter(e -> !e.getAnonymity().equalsIgnoreCase("Transparent"))
                .sorted(Comparator.comparingInt(Proxy::getSpeed))
                .collect(Collectors.toList());

        Proxy found = null;
        for (Proxy proxy : idealProxies) {
            if (isValidServer(proxy)) {
                found = proxy;
                break;
            }
        }

        if (found == null) {
            return Optional.empty();
        }

        return Optional.of(found);
    }


    private Boolean isValidServer(Proxy proxy) {

        ChromeDriver driver = ChromeDriverHelper.getDriver(proxy);
        driver.get("https://www.otto.de/");

        WebElement searchBox = null;
        try {
            searchBox = driver.findElement(By.cssSelector("div[id='searchAndIconWrp']"));
        } catch (NoSuchElementException ex) {
            //does not have captcha
        }

        driver.close();
        driver.quit();

        return (searchBox != null);
    }
}