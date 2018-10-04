package con.github.sparkmuse.flightalarm.service;

import con.github.sparkmuse.flightalarm.ChromeDriverHelper;
import con.github.sparkmuse.flightalarm.config.ProxyConfig;
import con.github.sparkmuse.flightalarm.entity.Proxy;
import lombok.RequiredArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProxyService {

    private final ProxyConfig config;
    private ChromeDriver driver;

    public Optional<Proxy> getAGoodAddress() {

        driver = ChromeDriverHelper.getDriver();

        driver.get(config.getProxyUrl());
        Document document = Jsoup.parse(driver.getPageSource());

        Elements rows = document.getElementsByAttribute(config.getRowCss());

        Optional<Proxy> proxy = rows
                .stream()
                .map(this::getProxy)
                .filter(this::isAceptedType)
                .sorted(Comparator.comparingInt(Proxy::getSpeed))
                .filter(this::isValidServer)
                .findFirst();

        driver.close();
        driver.quit();

        return proxy;
    }

    private boolean isAceptedType(Proxy p) {
        return !p.getAnonymity().equalsIgnoreCase(config.getExclusionProxyType());
    }

    private Proxy getProxy(Element element) {
        Elements cells = element.getElementsByTag("td");
        return new Proxy(
                cells.get(0).text(),
                cells.get(1).text(),
                cells.get(6).text(),
                new Integer(cells.get(3).text().replace(" ms", "")));
    }

    private Boolean isValidServer(Proxy proxy) {

        ChromeDriver driver = ChromeDriverHelper.getDriver(proxy);
        driver.get(config.getValidServerTestUrl());

        WebElement searchBox = null;
        try {
            searchBox = driver.findElement(By.cssSelector(config.getValidServerSearchCss()));
        } catch (NoSuchElementException ex) {
            //does not have captcha
        }

        driver.close();
        driver.quit();

        return (searchBox != null);
    }
}